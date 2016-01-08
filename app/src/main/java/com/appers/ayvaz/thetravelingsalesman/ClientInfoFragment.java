package com.appers.ayvaz.thetravelingsalesman;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.dialog.PickOrTakePhotoFragment;
import com.appers.ayvaz.thetravelingsalesman.Model.Client;
import com.appers.ayvaz.thetravelingsalesman.Model.ClientContent;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientInfoFragment extends Fragment {
    // parameter arguments, choose names that match
    private static final String ARG_CLIENT_ID = "client_id";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_DELETE = 2;
    private static final int REQUEST_PICK_PHOTO = 3;
    private static final int REQUEST_PICK_OR_CAPTURE = 4;
    public static final String EXTRA_PICK_OR_CAPTURE = "pick_or_capture";
    private static final String DIALOG_DELETE = "DialogDelete";
    private static final String DIALOG_PHOTO = "DialogPhoto";

    private UUID mClientId;
    private Client mClient;
    private ImageView mImageView;
    private MenuItem mStar, mDelete;
    @Bind(R.id.firstName) TextView mFirstName;
    @Bind(R.id.lastName) TextView mLastName;
    @Bind(R.id.email) TextView mEmail;
    @Bind(R.id.company) TextView mCompany;
    @Bind(R.id.mobile) TextView mPhoneFirst;
    @Bind(R.id.note) TextView mNote;
    @Bind(R.id.address) TextView mAddress;



    public ClientInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ClientEditFragment.
     */
    public static ClientInfoFragment newInstance(UUID clientId) {
        ClientInfoFragment fragment = new ClientInfoFragment();
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
            mClient = ClientContent.get(getActivity()).getClient(mClientId);
        } else {
            mClient = new Client();
        }
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

        mImageView = (ImageView) view.findViewById(R.id.imageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhoto();
            }
        });
        return view;
    }



    private void setPhoto() {
        FragmentManager manager = getFragmentManager();
        PickOrTakePhotoFragment dialog = new PickOrTakePhotoFragment();
        dialog.setTargetFragment(ClientInfoFragment.this, REQUEST_PICK_OR_CAPTURE);
        dialog.show(manager, DIALOG_PHOTO);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void takePictureFromGallery()
    {
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
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }

        // // TODO: 007 01/07 Process and save image
/**
* following code moved to ClientActivity
* */
        /*
        if (requestCode == REQUEST_DELETE) {
            if (ClientContent.get(getContext()).delete(mClientId)) {
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
            case R.id.action_done :
                ClientContent content = ClientContent.get(getActivity());
                updateClient();
                if (mClientId != null) {
                    content.updateClient(mClient);
                    getActivity().setResult(Activity.RESULT_OK);
                } else {
                    mClientId = mClient.getId();
                    content.addClient(mClient);
                }
                getActivity().finish();
                return true;

           /* case R.id.action_star:
                if (mClientId != null) {
                    mClient.setStared(!mClient.isStared());
                    updateActionBar();
                } else {
                    Toast.makeText(getActivity(), "You need to save this client first", Toast.LENGTH_LONG)
                            .show();
                }
                return true;

            case R.id.action_delete:
                FragmentManager manager = getFragmentManager();
                DeleteAlertDialogFragment dialog = DeleteAlertDialogFragment.newInstance("client");
                dialog.setTargetFragment(ClientEditFragment.this, REQUEST_DELETE);
                dialog.show(manager, DIALOG_DELETE);
                return true;

            */
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void updateClient() {
        mClient.setFirstName(mFirstName.getText().toString());
        mClient.setLastName(mLastName.getText().toString());
        mClient.setFirstPhone(mPhoneFirst.getText().toString());
        mClient.setEmail(mEmail.getText().toString());
        mClient.setNote(mNote.getText().toString());
        mClient.setAddress(mAddress.getText().toString());
        mClient.setCompany(mCompany.getText().toString());
        // // TODO: 028 12/28 set other attributes
    }

    private void updateUI() {
        mFirstName.setText(mClient.getFirstName());
        mLastName.setText(mClient.getLastName());
        mCompany.setText(mClient.getCompany());
        mPhoneFirst.setText(mClient.getFirstPhone());
        mNote.setText(mClient.getNote());
        mAddress.setText(mClient.getAddress());
        mEmail.setText(mClient.getEmail());
        // // TODO: 007 01/07 set other attributes

    }


    private void updateActionBar() {
        mStar.setIcon(mClient.isStared() ? R.drawable.ic_star_yellow_500_24dp :
                R.drawable.ic_star_outline_white_24dp);
        mDelete.setVisible(mClientId != null);
    }


}
