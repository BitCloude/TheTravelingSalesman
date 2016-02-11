package com.appers.ayvaz.thetravelingsalesman.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
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
import com.appers.ayvaz.thetravelingsalesman.utils.DbBitmapUtility;
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
    private static final String ARG_PARAM1 = "image_byte_array";
    private static final String ARG_PARAM2 = "boolean_add_photo";
    public static final String EXTRA_PICK_OR_CAPTURE = "pick_or_capture";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_DELETE = 2;
    private static final int REQUEST_PICK_PHOTO = 3;
    private static final int REQUEST_PICK_OR_CAPTURE = 4;
    private static final String DIALOG_DELETE = "DialogDelete";
    private static final String DIALOG_PHOTO = "DialogPhoto";
    private File mPhotoFile, mPhotoTmp;
    private ImageView imageView;
    private ImageButton mDelPhoto;
    private Bitmap image;
    private Bitmap imageTemp;
    private File tempFile;

    // TODO: Rename and change types of parameters
    private byte[] mParam1;
    private boolean mParam2;

    private OnFragmentInteractionListener mListener;

    public PhotoViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PhotoViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoViewFragment newInstance(byte[] param1, boolean param2) {
        PhotoViewFragment fragment = new PhotoViewFragment();
        Bundle args = new Bundle();
        args.putByteArray(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getByteArray(ARG_PARAM1);
            mParam2 = getArguments().getBoolean(ARG_PARAM2);
        }

            image = DbBitmapUtility.getImage(mParam1);

        String filename = "TEMP_IMG.jpg";
        File externalFilesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            tempFile = null;
        }
        else {
            tempFile = new File(externalFilesDir, filename);
            Log.i("......", "Path: " + externalFilesDir.getPath());
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photo_view, container, false);
        Toast.makeText(getActivity(), "onCreate", Toast.LENGTH_SHORT).show();
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
        if(mParam2){
        setPhoto();}
        else
        updatePhotoView();

        return v;
    }

    private void updatePhotoView() {
       if(image == null) {
            imageView.setImageDrawable(null);
            imageView.setVisibility(View.INVISIBLE);

            mDelPhoto.setVisibility(View.INVISIBLE);
        } else {
            imageView.setImageBitmap(image);
            imageView.setVisibility(View.VISIBLE);
            mDelPhoto.setVisibility(View.VISIBLE);
        }
        if (mListener != null) {
            mListener.onFragmentInteraction(DbBitmapUtility.getBytes(image));
        }
    }
    private void setImage(){
        if(tempFile.exists()){

            image = BitmapFactory.decodeFile(tempFile.getAbsolutePath());


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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            Uri uri = Uri.fromFile(tempFile);
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
            setImage();
            updatePhotoView();
            return;
        }

        if (requestCode == REQUEST_PICK_PHOTO) {
            if (data != null) {

                try (InputStream from = getActivity().getContentResolver().openInputStream(data.getData())) {
                    PictureUtils.copyFile(from, tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setImage();
                updatePhotoView();

            }
            return;
        }

        if (requestCode == REQUEST_DELETE) {
            deletePic(tempFile);
            image = null;
            updatePhotoView();
        }


    }

   /* private void savePhoto() {
        if (mPhotoTmp != null && mPhotoTmp.exists()) {
            if ((!mPhotoFile.exists() || mPhotoFile.delete()) && mPhotoTmp.renameTo(mPhotoFile)) {
                return;
            }

            Toast.makeText(getContext(), "Photo not saved", Toast.LENGTH_LONG).show();

        }
    }*/

    @Override
    public void onStop() {
        super.onStop();
        deletePic(tempFile);
        image = null;
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
        void onFragmentInteraction(byte[] image);
    }
}

        /*BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inDither = true;
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        image = BitmapFactory.decodeByteArray(mParam1, 0, mParam1.length, opt);}*/