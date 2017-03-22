package com.androidapp.isagip;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;

import com.androidapp.isagip.model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReliefFragment extends Fragment implements View.OnClickListener {

    EditText editOthers;
    SeekBar seekFood, seekClothes, seekMedicine, seekOthers;
    CheckBox checkFood, checkClothes, checkMedicine, checkOthers;
    Button buttonSend, buttonShare;
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;
    double latitude;
    double longitude;
    List<Address> addresses;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relief, container, false);
        requestFineLocation();
        requestCoarseLocation();
        editOthers = (EditText) view.findViewById(R.id.edit_others);
        seekFood = (SeekBar) view.findViewById(R.id.seekbar_food);
        seekClothes = (SeekBar) view.findViewById(R.id.seekbar_clothes);
        seekMedicine = (SeekBar) view.findViewById(R.id.seekbar_medicine);
        seekOthers = (SeekBar) view.findViewById(R.id.seekbar_others);
        checkFood = (CheckBox) view.findViewById(R.id.check_food);
        checkClothes = (CheckBox) view.findViewById(R.id.check_clothes);
        checkMedicine = (CheckBox) view.findViewById(R.id.check_medicine);
        checkOthers = (CheckBox) view.findViewById(R.id.check_others);
        buttonSend = (Button) view.findViewById(R.id.button_send);
        buttonShare = (Button) view.findViewById(R.id.button_share);

        seekFood.setEnabled(false);
        seekClothes.setEnabled(false);
        seekMedicine.setEnabled(false);
        seekOthers.setEnabled(false);
        editOthers.setEnabled(false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("requests");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonSend.setOnClickListener(this);
        buttonShare.setOnClickListener(this);

        checkFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    seekFood.setEnabled(false);
                } else {
                    seekFood.setEnabled(true);
                }
            }
        });
        checkClothes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    seekClothes.setEnabled(false);
                } else {
                    seekClothes.setEnabled(true);
                }
            }
        });
        checkMedicine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    seekMedicine.setEnabled(false);
                } else {
                    seekMedicine.setEnabled(true);
                }
            }
        });
        checkOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    seekOthers.setEnabled(false);
                    editOthers.setEnabled(false);
                } else {
                    seekOthers.setEnabled(true);
                    editOthers.setEnabled(true);
                }
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        latitude = this.getArguments().getDouble("lat");
        longitude = this.getArguments().getDouble("long");
        getLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send:

                boolean isFood = checkFood.isChecked();
                String foodRate = String.valueOf(seekFood.getProgress());
                boolean isClothes = checkClothes.isChecked();
                String waterRate = String.valueOf(seekClothes.getProgress());
                boolean isMedicine = checkMedicine.isChecked();
                String medicineRate = String.valueOf(seekMedicine.getProgress());
                boolean isOthers = checkOthers.isChecked();
                String others = editOthers.getText().toString();
                String othersRate = String.valueOf(seekOthers.getProgress());
                if (!isFood) {
                    foodRate = "not requested";
                }
                if (!isClothes) {
                    waterRate = "not requested";
                }
                if (!isMedicine) {
                    medicineRate = "not requested";
                }
                if (!isOthers) {
                    othersRate = "not requested";
                } else {

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Request request = new Request(FirebaseAuth.getInstance().getCurrentUser().getEmail(), currentDateTimeString, addresses.get(0).getAddressLine(0), foodRate, waterRate, medicineRate, others + ": " + othersRate);
                    mDatabase.child("request").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(request);
                }
                break;

            case R.id.button_share:
                break;
        }
    }

    public void getLocation() {
        Geocoder geocoder;

        geocoder = new Geocoder(this.getContext(), Locale.getDefault());


        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void requestFineLocation() {
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    public void requestCoarseLocation() {
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }
}
