package com.appers.ayvaz.thetravelingsalesman;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.Expense;
import com.appers.ayvaz.thetravelingsalesman.models.ExpenseContent;
import com.appers.ayvaz.thetravelingsalesman.models.ExpenseReport;
import com.appers.ayvaz.thetravelingsalesman.models.Trip;
import com.appers.ayvaz.thetravelingsalesman.models.TripContent;
import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;
import com.appers.ayvaz.thetravelingsalesman.utils.MyCsvWriter;
import com.appers.ayvaz.thetravelingsalesman.utils.ReportExportUtils;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.xml.transform.Result;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ReportExpenseFragment extends Fragment {

    private static final String DEBUG_TAG = "ReportExpenseFragment";
    private static final int REQUEST_CLIENT = 0;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.titleHeader)
    View mTitleHeader;
    @Bind(R.id.dateHeader)
    View mDateHeader;
    @Bind(R.id.titleSorted)
    ImageView mTitleSorted;
    @Bind(R.id.dateSorted)
    ImageView mDateSorted;

    @Bind(R.id.progressBarContainer)
    FrameLayout mProgressBarContainer;
    @Bind(R.id.startDateButton)
    Button mStartButton;
    @Bind(R.id.endDateButton)
    Button mEndButton;
    @Bind(R.id.applyButton)
    Button mApplyButton;
    @Bind(R.id.client_name)
    Button mClientName;
    @Bind(R.id.tripSpinner)
    Spinner mTripSpinner;
    @Bind(R.id.clearClient)
    ImageButton mClearClient;
    @Bind(R.id.amount)
    TextView mAmount;
//    @Bind(R.id.hotelSum) TextView mHotelSum;
//    @Bind(R.id.restaurantSum) TextView mRestaurantSum;
//    @Bind(R.id.giftSum) TextView mGiftSum;
//    @Bind(R.id.otherSum) TextView mOtherSum;
//    @Bind(R.id.cabSum) TextView mCabSum;

    private int UNSORTED_ICON = R.drawable.ic_dark_sortable;
    private int ASC_ICON = R.drawable.ic_dark_sorted_asc;
    private int DESC_ICON = R.drawable.ic_dark_sorted_desc;
    private int[] sortable_icons = {UNSORTED_ICON, ASC_ICON, DESC_ICON};
    private Calendar mStartDateSet, mEndDateSet, mStartDate, mEndDate;
    private Client mClient;
    private Trip mTrip;
    private long lastEventId;
    private ImageView[] mHeaderIcons;
    private ExpenseReport mTotal;

    private ExpenseReportAdapter mAdapter;
    private ArrayAdapter<Trip> mTripAdapter;

    // for sorting
    private int[] orders = new int[2];

    public ReportExpenseFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartDate = Calendar.getInstance();
        mStartDate.add(Calendar.MONTH, -2);
        mEndDate = Calendar.getInstance();
        mEndDate.add(Calendar.MONTH, 2);

        mStartDateSet = Calendar.getInstance();
        mEndDateSet = Calendar.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_expense, container, false);
        Log.i(DEBUG_TAG, "view");
        ButterKnife.bind(this, view);
        mHeaderIcons = new ImageView[]{mTitleSorted, mDateSorted};
        Log.i(DEBUG_TAG, "after bind");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));

        setHasOptionsMenu(true);
        mClearClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClient = null;
                updateUI();
            }
        });

        updateUI();

        mDateHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orders[0] < 2) {
                    mAdapter.sortByDate(true);
                    orders[0] = 2;
                    orders[1] = 0;
                } else {
                    mAdapter.sortByDate(false);
                    orders[0] = 1;
                    orders[1] = 0;
                }

                updateIcon();
            }
        });

//        mTitleSorted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (orders[1] < 2) {
//                    mAdapter.sortByTitle(true);
//                    orders[1] = 2;
//                    orders[0] = 0;
//                } else {
//                    mAdapter.sortByTitle(false);
//                    orders[1] = 1;
//                    orders[0] = 0;
//                }
//
//                updateIcon();
//
//            }
//        });


        mStartButton.setOnClickListener(new PickDateButtonListener());
        mEndButton.setOnClickListener(new PickDateButtonListener());

        mApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStartDate.setTime(mStartDateSet.getTime());
                mEndDate.setTime(mEndDateSet.getTime());
                new GetExpenseReport().execute(mStartDate, mEndDate);

            }
        });


        mClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), ClientPickActivity.class),
                        REQUEST_CLIENT);
            }
        });


        return view;
    }

    private void showStartTime() {
        mStartButton.setText(DateTimeHelper.formatDate(mStartDateSet.getTime()));
    }

    private void showEndTime() {
        mEndButton.setText(DateTimeHelper.formatDate(mEndDateSet.getTime()));
    }

    private void updateIcon() {

        for (int i = 0; i < mHeaderIcons.length; i++) {
            mHeaderIcons[i].setImageResource(sortable_icons[orders[i]]);
        }
    }

    private void updateUI() {

        if (mClient != null) {
            mClientName.setText(mClient.toString());
            mClearClient.setVisibility(View.VISIBLE);
        } else {
            mClientName.setText(getString(R.string.select_a, "client"));
            mClearClient.setVisibility(View.INVISIBLE);
        }

        showStartTime();
        showEndTime();


        new GetExpenseReport().execute(mStartDate, mEndDate);
        new GetTrips().execute(mClient);


    }

    @Override
    public void onResume() {
        super.onResume();

        //reset button time
        mStartDateSet.setTime(mStartDate.getTime());
        mEndDateSet.setTime(mEndDate.getTime());

        // set client
        if (mClient != null) {
            mClient = ClientManager.get(getActivity()).getClient(mClient.getId());
        }

        updateUI();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CLIENT) {
            UUID uuid = UUID.fromString(data.getStringExtra(ClientPickActivity.EXTRA_CLIENT_ID));
            mClient = ClientManager.get(getContext()).getClient(uuid);
            updateUI();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_report, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                mAdapter.saveReport(mClient);
                return true;

            case R.id.action_send:
//                saveAndShare();
                selectFile();
                return true;

            case R.id.action_open_folder:
                ReportExportUtils.openReportFolder(getActivity());
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void selectFile() {
        final File dir = ReportExportUtils.getReportDir(getActivity());
        if (dir == null) {
            return;
        }
        final String[] fileList = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.startsWith(ReportExportUtils.EXPENSE_PREFIX);
            }
        });

        Log.i(DEBUG_TAG, fileList.length + " files found");


        new AlertDialog.Builder(getActivity()).setTitle(R.string.select_report)
                .setItems(fileList,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ReportExportUtils.shareFile(getActivity(), new File(dir, fileList[which]));
                            }
                        })
                .create().show();
    }

    private void setupSpinner(List<Trip> trips) {
        Trip dummyTrip = new Trip();
        dummyTrip.setDescription(getString(R.string.select_a, "trip"));
        trips.add(0, dummyTrip);

        if (mTripAdapter == null) {
            mTripAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                    trips);
            mTripSpinner.setAdapter(mTripAdapter);
            mTripSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        mTrip = (Trip) parent.getItemAtPosition(position);
                        Log.i(DEBUG_TAG, "selected trip: " + mTrip);
                    } else {
                        mTrip = null;
                    }

                    new GetExpenseReport().execute(mStartDate, mEndDate);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } else {
            mTripAdapter.clear();
            mTripAdapter.addAll(trips);
//            mTripAdapter.notifyDataSetChanged();
        }
    }

    private List<Trip> loadTrip(Client client) {
        TripContent tripContent = TripContent.get(getActivity());
        List<Trip> list = client == null ? tripContent.getTrips()
                : tripContent.getClientTrips(client.getId());

        return list;

    }

    private List<ExpenseReport> getExpenseReport(Client client, Calendar start, Calendar end) {
        TripContent tripContent = TripContent.get(getActivity());
        List<ExpenseReport> reports = new ArrayList<>();

        // last row of the table
        mTotal = new ExpenseReport();
        mTotal.setInfo(ExpenseReport.TOTAL);
        Calendar dummyDate = Calendar.getInstance();
        dummyDate.setTimeInMillis(Long.MAX_VALUE);
        mTotal.setDate(dummyDate);


        if (mTrip == null) {
            // 1. add trip related expenses
            List<Trip> trips = client == null ? tripContent.getTrips(start, end)
                    : tripContent.getClientTrips(client.getId());

            Log.i(DEBUG_TAG, "trips found: " + trips.size());
            for (Trip t : trips) {

                List<Expense> expenses = ExpenseContent.get(getActivity())
                        .getTripExpenses(t.getId(), start, end);

                Log.i(DEBUG_TAG, "Expense found: " + expenses.size());

                if (expenses.size() == 0) {
                    continue;
                }

                ExpenseReport r = new ExpenseReport();
                if (client == null) {
                    r.setClient(ClientManager.get(getActivity()).getClient(t.getClient_id()));
                } else {
                    r.setClient(client);
                }

                r.setInfo(t.toString());
                r.setDate(t.getDate_from());
                setTripReport(expenses, r);
                reports.add(r);
            }

            // 2. add expenses that don't belong to any trip
            List<Expense> orphanExpenses = ExpenseContent.get(getActivity())
                    .getOrphanExpense(start, end);

            Log.i(DEBUG_TAG, "Orphans: " + orphanExpenses.size());

            for (Expense e : orphanExpenses) {
                reports.add(getReportFromExpense(e));
            }

        } else {

            // show seperated expenses of a trip
            List<Expense> expenses = ExpenseContent.get(getActivity())
                    .getTripExpenses(mTrip.getId(), start, end);
            for (Expense expense : expenses) {
                reports.add(getReportFromExpense(expense));
            }


        }

        if (reports.size() != 0) {
            reports.add(mTotal);
        }


        return reports;
    }

    private void setTripReport(List<Expense> expenses, ExpenseReport r) {

        for (Expense expense : expenses) {
            String type = expense.getType();
            double amount = Double.parseDouble(expense.getAmount());
            Log.i(DEBUG_TAG, "Amount: " + amount);
            switch (type) {
                case Expense.TYPE_RESTAURANT:
                    r.addRestaurant(amount);
                    mTotal.addRestaurant(amount);
                    break;
                case Expense.TYPE_OTHER:
                    r.addOther(amount);
                    mTotal.addOther(amount);
                    break;
                case Expense.TYPE_GIFT:
                    r.addGifts(amount);
                    mTotal.addGifts(amount);
                    break;
                case Expense.TYPE_HOTEL:
                    r.addHotel(amount);
                    mTotal.addHotel(amount);
                    break;
                case Expense.TYPE_TRAVEL:
                    r.addCabs(amount);
                    mTotal.addCabs(amount);
                    break;
                default:
                    Log.i(DEBUG_TAG, "expense type: " + type + " not recognized");
                    break;
            }
        }

    }

    private ExpenseReport getReportFromExpense(Expense expense) {
        ExpenseReport r;
            String type = expense.getType();
            double amount = Double.parseDouble(expense.getAmount());
            r = new ExpenseReport();
            r.setInfo(expense.getDescription());
            r.setDate(expense.getDate_from());
//                r.setToDate(expense.getDate_to());

            Log.i(DEBUG_TAG, "Amount: " + amount);
            switch (type) {
                case Expense.TYPE_RESTAURANT:
                    r.setRestaurant(amount);
                    mTotal.addRestaurant(amount);
                    break;
                case Expense.TYPE_OTHER:
                    r.setOther(amount);
                    mTotal.addOther(amount);
                    break;
                case Expense.TYPE_GIFT:
                    r.setGifts(amount);
                    mTotal.addGifts(amount);
                    break;
                case Expense.TYPE_HOTEL:
                    r.setHotel(amount);
                    mTotal.addHotel(amount);
                    break;
                case Expense.TYPE_TRAVEL:
                    r.setCabs(amount);
                    mTotal.addCabs(amount);
                    break;
                default:
                    Log.i(DEBUG_TAG, "expense type: " + type + " not recognized");
                    break;
            }

          return r;

    }

    private void clearOrders() {
        for (int i = 0; i < orders.length; i++) {
            orders[i] = 0;
        }

        updateIcon();
    }

    private class PickDateButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            DatePickerDialog.OnDateSetListener onDateSetListener;
            Calendar date;

            if (v.getId() == R.id.startDateButton) {
                onDateSetListener = new OnStartDateSetListener();
                date = mStartDateSet;
            } else {
                onDateSetListener = new OnEndDateSetListener();
                date = mEndDateSet;
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener,
                    date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();

        }
    }

    private class OnStartDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mStartDateSet.set(year, monthOfYear, dayOfMonth);
            if (!mStartDateSet.before(mEndDateSet)) {
                mEndDateSet.set(mStartDateSet.get(Calendar.YEAR), mStartDateSet.get(Calendar.MONTH),
                        mStartDateSet.get(Calendar.DAY_OF_MONTH));
                showEndTime();
            }
            showStartTime();
        }
    }

    private class OnEndDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mEndDateSet.set(year, monthOfYear, dayOfMonth);
            if (!mStartDateSet.before(mEndDateSet)) {
                mStartDateSet.set(mEndDateSet.get(Calendar.YEAR), mEndDateSet.get(Calendar.MONTH),
                        mEndDateSet.get(Calendar.DAY_OF_MONTH));
                showStartTime();
            }

            showEndTime();

        }
    }

   /* private void bindTotal(ExpenseReport sum) {
        mHotelSum.setText(ReportExportUtils.formatMoney(sum.getHotel()));
        mRestaurantSum.setText(ReportExportUtils.formatMoney(sum.getRestaurant()));
        mCabSum.setText(ReportExportUtils.formatMoney(sum.getCabs()));
        mGiftSum.setText(ReportExportUtils.formatMoney(sum.getGifts()));
        mOtherSum.setText(ReportExportUtils.formatMoney(sum.getOther()));
    }
*/

    private class GetTrips extends AsyncTask<Client, Void, List<Trip>> {
        @Override
        protected List<Trip> doInBackground(Client... params) {
            if (params.length != 1) {
                return new ArrayList<>();
            }

            return loadTrip(params[0]);
        }

        @Override
        protected void onPostExecute(List<Trip> trips) {
            setupSpinner(trips);
        }
    }

    private class GetExpenseReport extends AsyncTask<Calendar, Void, List<ExpenseReport>> {

        @Override
        protected List<ExpenseReport> doInBackground(Calendar... params) {
            /*try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            Calendar start = params[0];
            Calendar end = params[1];

            return getExpenseReport(mClient, start, end);

        }

        @Override
        protected void onPreExecute() {
            mProgressBarContainer.setVisibility(View.VISIBLE);
//            mRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(List<ExpenseReport> expenses) {
            if (mAdapter == null) {
                mAdapter = new ExpenseReportAdapter(expenses, getActivity());
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setData(expenses);
                mAdapter.notifyDataSetChanged();
            }

//            bindTotal(mTotal);
            mProgressBarContainer.setVisibility(View.GONE);
            if (mAdapter.getItemCount() > 1) {
                double total = mTotal.getTotal();
                mAmount.setText(ReportExportUtils.formatMoney(total));
            }

            clearOrders();
//            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


}

class ExpenseReportAdapter extends RecyclerView.Adapter<ExpenseReportAdapter.ViewHolder> {
    private static final String DEBUG_TAG = "ExpenseReportAdapter";
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_TOTAL = 1;
    List<ExpenseReport> mExpenses;
    Context mContext;
    private Comparator<ExpenseReport> dateComparatorDesc = new Comparator<ExpenseReport>() {
        @Override
        public int compare(ExpenseReport lhs, ExpenseReport rhs) {
            Calendar total = Calendar.getInstance();
            total.setTimeInMillis(Long.MAX_VALUE);
            if (lhs.getDate().equals(total) && rhs.getDate().equals(total)) {
                return 0;
            }

            if (lhs.getDate().equals(total)) {
                return 1;
            }

            if (rhs.getDate().equals(total)) {
                return -1;
            }


            return rhs.getDate().compareTo(lhs.getDate());
        }
    };
    private Comparator<ExpenseReport> dateComparator = new Comparator<ExpenseReport>() {
        @Override
        public int compare(ExpenseReport lhs, ExpenseReport rhs) {
            return lhs.getDate().compareTo(rhs.getDate());
        }
    };
    private Comparator<ExpenseReport> titleComparator = new Comparator<ExpenseReport>() {
        @Override
        public int compare(ExpenseReport lhs, ExpenseReport rhs) {

            return lhs.getInfo().compareTo(rhs.getInfo());
        }
    };

    public ExpenseReportAdapter(List<ExpenseReport> expenses, Context context) {
        mExpenses = expenses;
        mContext = context;
    }

    public void sortByDate(boolean order) {
        if (!order) {
            Collections.sort(mExpenses, dateComparator);

        } else {
            Collections.sort(mExpenses, dateComparatorDesc);

        }

        notifyDataSetChanged();
    }

    public void sortByTitle(boolean order) {
        Collections.sort(mExpenses, order ? titleComparator : Collections.reverseOrder(titleComparator));
        notifyDataSetChanged();
    }

    public String saveReport(Client client) {
        if (mExpenses.size() == 0) {
            Toast.makeText(mContext, R.string.report_nothing_to_save, Toast.LENGTH_SHORT).show();
            return "";
        }

        final File file = ReportExportUtils.getFileNameByTime(mContext,
                ReportExportUtils.TYPE_EXPENSE, client);

        if (file == null) {
            Log.i(DEBUG_TAG, "FILE NULL");
            return "";
        }

        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(mContext.getString(R.string.save_report, file.getPath()))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new GenerateExpenseReport().execute(file);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        alertDialog.show();

        return file.getPath();

    }

    public void setData(List<ExpenseReport> data) {
        mExpenses = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        int layoutRes = viewType == TYPE_NORMAL ? R.layout.view_report_expense_row
//                : R.layout.view_report_expense_row_last;
        int layoutRes = R.layout.view_report_expense_row;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mExpenses.size() - 1) {
            return TYPE_TOTAL;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExpenseReport current = mExpenses.get(position);
        holder.bindView(current);

        if (position == getItemCount() - 1) {
            holder.showYear(-1);
            return;
        }

        int currentYear = current.getDate().get(Calendar.YEAR);
        if (position == 0 || currentYear != mExpenses.get(position - 1)
                .getDate().get(Calendar.YEAR)) {
            holder.showYear(currentYear);
        } else {
            holder.showYear(-1);
        }
    }

    @Override
    public int getItemCount() {
        return mExpenses.size();
    }

    public String getReport(Client mClient) {
        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.tripTitle)
        TextView tripTitle;
        @Bind(R.id.hotel)
        TextView hotel;
        @Bind(R.id.restaurant)
        TextView restaurant;
        @Bind(R.id.cab)
        TextView cab;
        @Bind(R.id.gifts)
        TextView gifts;
        @Bind(R.id.other)
        TextView other;

        @Bind(R.id.yearChanged)
        View yearChanged;
        @Bind(R.id.year)
        TextView year;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bindView(ExpenseReport row) {
            if (getAdapterPosition() != getItemCount() - 1) {
                date.setText(DateTimeHelper.formatShortDate(row.getDate().getTime()));
            }


            tripTitle.setText(row.getInfo());
            hotel.setText(ReportExportUtils.formatMoney(row.getHotel()));
            restaurant.setText(ReportExportUtils.formatMoney(row.getRestaurant()));
            cab.setText(ReportExportUtils.formatMoney(row.getCabs()));
            gifts.setText(ReportExportUtils.formatMoney(row.getGifts()));
            other.setText(ReportExportUtils.formatMoney(row.getOther()));


        }

        public void showYear(int y) {
            if (y <= 0) {
                yearChanged.setVisibility(View.GONE);
            } else {
                year.setText(Integer.toString(y));
                yearChanged.setVisibility(View.VISIBLE);
            }
        }
    }

    private class GenerateExpenseReport extends AsyncTask<File, Void, Boolean> {

        File mFile;
        int mId;
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager;




        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(File... params) {
            if (params.length != 1) {
                return null;
            }

            mFile = params[0];
            File parentDir = mFile.getParentFile();
            if (!parentDir.exists()) {
                if (!parentDir.mkdir()) {
                    return false;
                }
            }

/** 0. name | 1. date | 2. title | 3. hotel | 4. resturant | 5. cab | 6. gift | 7. other */
            try (MyCsvWriter writer = new MyCsvWriter(mFile)) {

                String[] header = {
                        mContext.getString(R.string.client_name),
                        mContext.getString(R.string.start_time),
                        mContext.getString(R.string.trip_title),
                        mContext.getString(R.string.hotel),
                        mContext.getString(R.string.restaurant),
                        mContext.getString(R.string.cab),
                        mContext.getString(R.string.gifts),
                        mContext.getString(R.string.other)
                };

                writer.addRow(header);

                String[] row = new String[header.length];

                for (int i = 0; i < mExpenses.size(); i++) {
//                    publishProgress(i);

                    ExpenseReport report = mExpenses.get(i);
                    if (report.getClient() != null) {
                        row[0] = report.getClient().toString();
                    } else {
                        row[0] = "";
                    }

                    if (report.getDate().getTimeInMillis() != Long.MAX_VALUE) {
                        row[1] = DateTimeHelper.formatDateForExport(report.getDate().getTime());
                    } else {
                        row[1] = "";
                    }

                    row[2] = report.getInfo();
                    row[3] = ReportExportUtils.formatMoneyNumberOnly(report.getHotel());
                    row[4] = ReportExportUtils.formatMoneyNumberOnly(report.getRestaurant());
                    row[5] = ReportExportUtils.formatMoneyNumberOnly(report.getCabs());
                    row[6] = ReportExportUtils.formatMoneyNumberOnly(report.getGifts());
                    row[7] = ReportExportUtils.formatMoneyNumberOnly(report.getOther());

                    writer.addRow(row);
                }

                writer.flush();
                writer.close();

                Thread.sleep(1000);
                return true;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
//            Toast.makeText(mContext, result ? R.string.message_finish_export : R.string.message_export_failed,
//                    Toast.LENGTH_SHORT).show();

            mNotificationManager = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(mContext);

            if (result) {
                mBuilder.setSmallIcon(R.drawable.ic_save)
                        .setContentTitle(mContext.getString(R.string.message_finish_export))
                        .setContentText(mFile.getPath())
                        .setContentInfo("Open the report")
                        .setAutoCancel(true);

                Intent resultIntent = ReportExportUtils.getOpenIntent(mFile, mContext);
                if (resultIntent == null) {
                    return;
                }

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
                stackBuilder.addParentStack(ReportExpenseActivity.class);
                stackBuilder.addNextIntent(resultIntent);

                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
                mNotificationManager.notify(mId, mBuilder.build());
            } else {
                Toast.makeText(mContext, R.string.message_export_failed, Toast.LENGTH_SHORT)
                        .show();
            }


        }
    }


}
