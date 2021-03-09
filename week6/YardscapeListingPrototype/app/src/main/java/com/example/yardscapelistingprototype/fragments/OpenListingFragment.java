package com.example.yardscapelistingprototype.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.yardscapelistingprototype.Listing;
import com.example.yardscapelistingprototype.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpenListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenListingFragment extends Fragment {

    public TextView listing_title, listing_day, listing_time, listing_desc;
    private Listing sourceListing;

    // Listing that is bundled on click
    private Bundle listingBundle;

    public OpenListingFragment(Bundle bundle) {
        sourceListing = bundle.getParcelable("openListing");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bundle Parameter 1.
     * @return A new instance of fragment OpenListingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpenListingFragment newInstance(Bundle bundle) {
        OpenListingFragment fragment = new OpenListingFragment(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sourceListing = listingBundle.getParcelable("openListing");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.open_listing, container, false);

        listing_title = (TextView) v.findViewById(R.id.open_listing_title);
        listing_day = (TextView) v.findViewById(R.id.open_listing_date);
        listing_time = (TextView) v.findViewById(R.id.open_listing_time);
        listing_desc = (TextView) v.findViewById(R.id.open_listing_desc);

        // Sets Textview values according to listing
        listing_title.setText(sourceListing.getListing_title());
        listing_day.setText(sourceListing.getListing_date());
        listing_time.setText(sourceListing.getListing_time());
        listing_desc.setText(sourceListing.getListing_description());

        // Inflate the layout for this fragment
        return v;
    }
}