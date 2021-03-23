package com.example.yardscapelistingprototype.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.yardscapelistingprototype.Listing;
import com.example.yardscapelistingprototype.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment {
    Button createButton, uploadButton;
    EditText create_title;
    EditText create_desc;
    EditText timeView;
    EditText dateView;
    ImageView imagePreview;

    Listing inputListing = new Listing();

    // Firebase Information for Image Uploading
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private Uri filePath;


    public CreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance() {
        CreateFragment fragment = new CreateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        create_title = view.findViewById(R.id.create_name);
        create_desc = view.findViewById(R.id.create_description);
        timeView = view.findViewById(R.id.create_time);
        dateView = view.findViewById(R.id.create_date);
        uploadButton = (Button) view.findViewById(R.id.uploadImageButton);
        imagePreview = (ImageView) view.findViewById(R.id.imagePreview);

        // Calls the datePickerFragment when the date input box is clicked on
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getChildFragmentManager(), "date picker");
            }
        });

        // Calls the TimePickerFragment when the time input box is clicked on
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getChildFragmentManager(), "time picker");
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        createButton = (Button) view.findViewById(R.id.createListingButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldCheck() == true) {
                    createListing();
                    databaseReference = firebaseDatabase.getReference().child("listing");
                    databaseReference.setValue(inputListing);
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    // Opens the DatePickerDialog and updates the input once the user picks the date
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        EditText dateEdit = (EditText) view.findViewById(R.id.create_date);
        dateEdit.setText(currentDateString, TextView.BufferType.EDITABLE);
    }

    // Opens the TimePickerDialog and updates the input once the user picks the time
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EditText timeEdit = (EditText) view.findViewById(R.id.create_time);
        timeEdit.setText(hourOfDay + ":" + minute, TextView.BufferType.EDITABLE);
    }

    // If any of the fields are empty, then the user is prompted to complete the fields
    public boolean fieldCheck() {
        boolean isFilled = true;
        if(create_title.getText().toString().trim().equalsIgnoreCase("")){
            create_title.setError("Please enter a title for your listing");
            isFilled = false;
        }

        if(timeView.getText().toString().trim().equalsIgnoreCase("")){
            timeView.setError("Please enter a time for your listing");
            isFilled = false;
        }

        if(dateView.getText().toString().trim().equalsIgnoreCase("")){
            dateView.setError("Please enter a date for your listing");
            isFilled = false;
        }

        return isFilled;
    }

    private void createListing() {
        String input_title = create_title.getText().toString();
        String input_desc = create_desc.getText().toString();
        String dateString = dateView.getText().toString();
        String timeString = timeView.getText().toString();

        inputListing.setTitle(input_title);
        inputListing.setDescription(input_desc);
        inputListing.setListing_date(dateString);
        inputListing.setListing_time(timeString);
        if(inputListing.getListing_image_path() == null) {inputListing.setDefaultImage();}
    }

    // Prompts the user for an image when the upload image button is clicked
    private void selectImage() {
        // Asks for permission to access storage before user uploads image
        // This prevents a bug where the image would not upload due to the
        // verifyStoragePermissions(CreateListing.this);

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the image is uploaded properly and the resultcode is OK
        // The imagePreview is updated and the image is uploaded
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() == null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), filePath);
                imagePreview.setImageBitmap(bitmap);
                uploadImage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(requireContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("listings/" + UUID.randomUUID().toString());

            // Attempts to create and then compress the uploaded image
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            // adding listeners on upload
            // or failure of image
            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // Image uploaded successfully
            // Dismiss dialog
            progressDialog.dismiss();
            Toast.makeText(requireActivity(),"Image Uploaded!!",Toast.LENGTH_SHORT).show();
            }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                // Error, Image not uploaded
                progressDialog.dismiss();
                Toast.makeText(requireActivity(),"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(
                UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+ (int)progress + "%");
                }
            });
        }
    }
}