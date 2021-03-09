package com.example.yardscapelistingprototype;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.yardscapelistingprototype.fragments.DatePickerFragment;
import com.example.yardscapelistingprototype.fragments.TimePickerFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;

public class CreateListing extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String KEY_User_Document1 = "doc1";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String Document_img1 = "";
    Button createButton, uploadButton;
    ImageView imagePreview;
    Listing inputListing = new Listing();
    EditText create_title;
    EditText create_desc;
    EditText timeView;
    EditText dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        create_title = findViewById(R.id.create_name);
        create_desc = findViewById(R.id.create_description);
        timeView = findViewById(R.id.create_time);
        dateView = findViewById(R.id.create_date);

        // Sets up the action bar to allow the user the go back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create a Listing");

        uploadButton = (Button) findViewById(R.id.uploadImageButton);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        
        imagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        // Calls the datePickerFragment when the date input box is clicked on
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        // Calls the TimePickerFragment when the time input box is clicked on
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        //

        // Creates the listing on button click
        createButton = (Button) findViewById(R.id.createListingButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Makes sure all necessary fields are filled in
                if(fieldCheck() == true) {
                    listing_set();
                    Intent createdIntent = new Intent();
                    createdIntent.putExtra("new_listing", inputListing);

                    setResult(RESULT_OK, createdIntent);
                    finish();
                }
                else {
                    Toast.makeText(CreateListing.this, "Please fill all values to create the listing", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Opens the DatePickerDialog and updates the input once the user picks the date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());

        EditText dateEdit = (EditText) findViewById(R.id.create_date);
        dateEdit.setText(currentDateString, TextView.BufferType.EDITABLE);
    }

    // Opens the TimePickerDialog and updates the input once the user picks the time
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EditText timeEdit = (EditText) findViewById(R.id.create_time);
        timeEdit.setText(hourOfDay + ":" + minute, TextView.BufferType.EDITABLE);
    }

    // Function to parse data from EditText into a listing variable
    public void listing_set() {
        String input_title = create_title.getText().toString();
        String input_desc = create_desc.getText().toString();


/*      Date input_date = new Date(01, 01, 2021);
        Date input_time = new Date(1200);*/

        String dateString = dateView.getText().toString();
/*        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        // Parses Date Input Value into a proper Date variable
        try {
            input_date = simpleDateFormat.parse((dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        String timeString = timeView.getText().toString();
/*        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        // Parses Time Input Value into a proper Date variable
        try {
            input_time = timeFormat.parse(timeString);
        } catch(ParseException e) {
            e.printStackTrace();
        }*/

        inputListing.setTitle(input_title);
        inputListing.setDescription(input_desc);
        inputListing.setListing_date(dateString);
        inputListing.setListing_time(timeString);
        if(inputListing.getListing_image_path() == null) {inputListing.setDefaultImage();}
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

    // Prompts the user for an image when the upload image button is clicked
    private void selectImage() {
        // Asks for permission to access storage before user uploads image
        // This prevents a bug where the image would not upload due to the
        verifyStoragePermissions(CreateListing.this);

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateListing.this);
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

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    InputStream is = new URL(f.getAbsolutePath()).openStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    imagePreview.setImageBitmap(bitmap);
                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    inputListing.setListing_image_path(path);
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                inputListing.setListing_image_path(picturePath);
                imagePreview.setImageBitmap(thumbnail);
            }
        }
    }
}