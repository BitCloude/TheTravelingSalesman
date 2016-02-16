package com.appers.ayvaz.thetravelingsalesman.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.dialog.DeleteAlertDialogFragment;
import com.appers.ayvaz.thetravelingsalesman.dialog.PickOrTakePhotoFragment;
import com.appers.ayvaz.thetravelingsalesman.utils.PictureUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "photo_file";
    private static final String ARG_PARAM2 = "photo_file_temp";
    private static final String ARG_PARAM3 = "load_or_add";
    public static final String EXTRA_PICK_OR_CAPTURE = "pick_or_capture";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_DELETE = 2;
    private static final int REQUEST_PICK_PHOTO = 3;
    private static final int REQUEST_PICK_OR_CAPTURE = 4;
    private static final String DIALOG_DELETE = "DialogDelete";
    private static final String DIALOG_PHOTO = "DialogPhoto";
    private File mPhotoFile, mPhotoTmp;
    private boolean load;
    private ImageView imageView;
    private ImageButton mDelPhoto;

    // TODO: Rename and change types of parameters
    private File mParam1;
    private File mParam2;
    private  boolean mParam3;

    private OnFragmentInteractionListener mListener;

    public PhotoViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param2 Parameter 3.
     * @return A new instance of fragment PhotoViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoViewFragment newInstance(File param1, File param2, boolean param3) {
        PhotoViewFragment fragment = new PhotoViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3,param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (File) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = (File) getArguments().getSerializable(ARG_PARAM2);
            mParam3 = getArguments().getBoolean(ARG_PARAM3);
        }
        setRetainInstance(true);
        mPhotoFile = mParam1;
        mPhotoTmp = mParam2;
        load = mParam3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photo_view, container, false);
        imageView = (ImageView) v.findViewById(R.id.fragment_photo_view);
        mDelPhoto = (ImageButton) v.findViewById(R.id.fragment_photo_delete);
        mDelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DeleteAlertDialogFragment dialog = DeleteAlertDialogFragment.newInstance("One photo");
                dialog.setTargetFragment(PhotoViewFragment.this, REQUEST_DELETE);
                dialog.show(manager, DIALOG_DELETE);

            }
        });
        if(load){
            updatePhotoView();
        }else
            setPhoto();
        return v;
    }

    private void updatePhotoView() {
        if ((mPhotoFile == null || !mPhotoFile.exists()) && (mPhotoTmp == null || !mPhotoTmp.exists())) {
            imageView.setImageDrawable(null);
            imageView.setVisibility(View.INVISIBLE);

            mDelPhoto.setVisibility(View.INVISIBLE);
        } else {
            File file = (mPhotoTmp != null && mPhotoTmp.exists()) ? mPhotoTmp : mPhotoFile;
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    file.getPath(), getActivity());
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            mDelPhoto.setVisibility(View.VISIBLE);
        }
        if (mListener != null) {
            Log.i("......", "sending to activity: tmp:  " + mPhotoTmp.getAbsolutePath());
            Log.i("......", "sending to activity: file:  " + mPhotoFile.getAbsolutePath());
            mListener.onFragmentInteraction(mPhotoFile, mPhotoTmp);
        }
    }

    private void setPhoto() {
        FragmentManager manager = getFragmentManager();
        PickOrTakePhotoFragment dialog = new PickOrTakePhotoFragment();
        dialog.setTargetFragment(PhotoViewFragment.this, REQUEST_PICK_OR_CAPTURE);
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

                try (InputStream from = getActivity().getContentResolver().openInputStream(data.getData())) {
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


    }

    private void savePhoto() {
        if (mPhotoTmp != null && mPhotoTmp.exists()) {
            if ((!mPhotoFile.exists() || mPhotoFile.delete()) && mPhotoTmp.renameTo(mPhotoFile)) {
                return;
            }

            Toast.makeText(getContext(), "Photo not saved", Toast.LENGTH_LONG).show();

        }
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

    /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(File PhotoFile, File PhotoTmp);
    }
}
