package com.example.yardscapelistingprototype.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.yardscapelistingprototype.Listing;
import com.example.yardscapelistingprototype.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment {
    Button createButton;
    EditText create_title;
    EditText create_desc;
    EditText timeView;
    EditText dateView;

    Listing inputListing = new Listing();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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
        Button uploadButton = (Button) view.findViewById(R.id.uploadImageButton);
        ImageView imagePreview = (ImageView) view.findViewById(R.id.imagePreview);

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
}