package com.appers.ayvaz.thetravelingsalesman;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.dialog.DeleteAlertDialogFragment;
import com.appers.ayvaz.thetravelingsalesman.dialog.PickOrTakePhotoFragment;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.utils.PictureUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientEditFragment extends Fragment {
    public static final String EXTRA_PICK_OR_CAPTURE = "pick_or_capture";
    // parameter arguments, choose names that match
    private static final String ARG_CLIENT_ID = "client_id";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_DELETE = 2;
    private static final int REQUEST_PICK_PHOTO = 3;
    private static final int REQUEST_PICK_OR_CAPTURE = 4;
    private static final String DIALOG_DELETE = "DialogDelete";
    private static final String DIALOG_PHOTO = "DialogPhoto";
    @Bind(R.id.firstName)
    TextView mFirstName;
    @Bind(R.id.lastName)
    TextView mLastName;
    @Bind(R.id.client_phone_first)
    TextView mEmail;
    @Bind(R.id.company)
    TextView mCompany;
    @Bind(R.id.mobile)
    TextView mPhoneFirst;
    @Bind(R.id.secondPhone)
    TextView mPhoneSecond;
    @Bind(R.id.note)
    TextView mNote;
    @Bind(R.id.address)
    TextView mAddress;
    @Bind(R.id.imageView)
    ImageView mImageView;
    @Bind(R.id.deletePhoto)
    ImageButton mDelPhoto;
    private UUID mClientId;
    private Client mClient;
    private File mPhotoFile, mPhotoTmp;
    private MenuItem mStar, mDelete;


    public ClientEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClientEditFragment.
     */
    public static ClientEditFragment newInstance(UUID clientId) {
        ClientEditFragment fragment = new ClientEditFragment();
        if (clientId != null) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_CLIENT_ID, clientId);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = (UUID) getArguments().get(ARG_CLIENT_ID);
            mClient = ClientManager.get(getActivity()).getClient(mClientId);
        } else {
            mClient = new Client();
        }

        mPhotoFile = ClientManager.get(getActivity()).getPhotoFile(mClient, false);
        mPhotoTmp = ClientManager.get(getActivity()).getPhotoFile(mClient, true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("............", "create view");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_edit, container, false);
        ButterKnife.bind(this, view);
//        mStar = mMenu.findItem(R.id.action_star);

        updateUI();
        updatePhotoView();
        mPhoneFirst.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mPhoneSecond.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhoto();
            }
        });
        mDelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DeleteAlertDialogFragment dialog = DeleteAlertDialogFragment.newInstance("One photo");
                dialog.setTargetFragment(ClientEditFragment.this, REQUEST_DELETE);
                dialog.show(manager, DIALOG_DELETE);

            }
        });
        return view;
    }


    private void updatePhotoView() {
        if ((mPhotoFile == null || !mPhotoFile.exists()) && (mPhotoTmp == null || !mPhotoTmp.exists())) {
            mImageView.setImageResource(R.drawable.avatar);
            mDelPhoto.setVisibility(View.INVISIBLE);
        } else {
            File file = (mPhotoTmp != null && mPhotoTmp.exists()) ? mPhotoTmp : mPhotoFile;
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    file.getPath(), getActivity());
            mImageView.setImageBitmap(bitmap);
            mDelPhoto.setVisibility(View.VISIBLE);
        }
    }

    private void setPhoto() {
        FragmentManager manager = getFragmentManager();
        PickOrTakePhotoFragment dialog = new PickOrTakePhotoFragment();
        dialog.setTargetFragment(ClientEditFragment.this, REQUEST_PICK_OR_CAPTURE);
        dialog.show(manager, DIALOG_PHOTO);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mPhotoTmp != null && takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Uri uri = Uri.fromFile(mPhotoTmp);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void takePictureFromGallery() {
        startActivityForResult(
                Intent.createChooser(
                        new Intent(Intent.ACTION_GET_CONTENT)
                                .setType("image/*"), "Choose an image"),
                REQUEST_PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PICK_OR_CAPTURE) {
            int res = data.getIntExtra(EXTRA_PICK_OR_CAPTURE, 1);
            if (res == PickOrTakePhotoFragment.CAPTURE) {
                dispatchTakePictureIntent();
            } else {
                takePictureFromGallery();
            }
            return;
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            updatePhotoView();
            return;
        }

        if (requestCode == REQUEST_PICK_PHOTO) {
            if (data != null) {

                try(InputStream from = getActivity().getContentResolver().openInputStream(data.getData())) {
                    PictureUtils.copyFile(from, mPhotoTmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                updatePhotoView();

            }
            return;
        }

        if (requestCode == REQUEST_DELETE) {
            deletePic(mPhotoTmp);
            deletePic(mPhotoFile);
            updatePhotoView();
        }





/**
 * following code moved to ClientActivity
 * */
        /*
        if (requestCode == REQUEST_DELETE) {
            if (ClientManager.get(getContext()).delete(mClientId)) {
                Toast.makeText(getActivity(), "Client deleted", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
        */
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_client_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
//        mStar = menu.findItem(R.id.action_star);
//        mDelete = menu.findItem(R.id.action_delete);
//        updateActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                AlertDialog.Builder cancelBuilder = new AlertDialog.Builder(getContext())
                        .setMessage("Change will be discarded.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null);
                cancelBuilder.create().show();
                return true;
            case R.id.action_done:
                updateClient();

                if (!checkValid()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                            .setMessage("Must have a name or phone number or email address")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.create().show();
                    return true;
                }

                ClientManager content = ClientManager.get(getActivity());

                if (mClientId != null) {
                    content.updateClient(mClient);

                    getActivity().setResult(Activity.RESULT_OK);
                } else {


                    mClientId = mClient.getId();
                    content.addClient(mClient);
                }

                savePhoto();
                getActivity().finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void savePhoto() {
        if (mPhotoTmp != null && mPhotoTmp.exists()) {
            if ((!mPhotoFile.exists() || mPhotoFile.delete()) && mPhotoTmp.renameTo(mPhotoFile)) {
                return;
            }

            Toast.makeText(getContext(), "Photo not saved", Toast.LENGTH_LONG).show();

        }
    }

    private boolean checkValid() {
        return !(isEmpty(mClient.getFirstName()) && isEmpty(mClient.getLastName())
                && isEmpty(mClient.getFirstPhone()) && isEmpty(mClient.getSecondPhone())
                && isEmpty(mClient.getEmail()));
    }

    private boolean isEmpty(String s) {
        return (s == null) || s.equals("");
    }

    private void updateClient() {
        mClient.setFirstName(mFirstName.getText().toString());
        mClient.setLastName(mLastName.getText().toString());
        mClient.setFirstPhone(mPhoneFirst.getText().toString());
        mClient.setSecondPhone(mPhoneSecond.getText().toString());
        mClient.setEmail(mEmail.getText().toString());
        mClient.setNote(mNote.getText().toString());
        mClient.setAddress(mAddress.getText().toString());
        mClient.setCompany(mCompany.getText().toString());

        // // TODO: 028 12/28 set other attributes
    }

    private void updateUI() {
        if (mClientId == null) {
            return;
        }

        mFirstName.setText(mClient.getFirstName());
        mLastName.setText(mClient.getLastName());
        mCompany.setText(mClient.getCompany());
        mPhoneFirst.setText(mClient.getFirstPhone());
        mNote.setText(mClient.getNote());
        mAddress.setText(mClient.getAddress());
        mEmail.setText(mClient.getEmail());
        mPhoneSecond.setText(mClient.getSecondPhone());
        // // TODO: 007 01/07 set other attributes

    }

    @Override
    public void onStop() {
        super.onStop();
        deletePic(mPhotoTmp);
    }



    private void deletePic(File fileName) {
        if (fileName != null && fileName.exists()) {
            fileName.delete();

        }
    }

}
