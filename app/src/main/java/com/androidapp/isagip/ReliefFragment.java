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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
    LinearLayout linearFoodCannedGoods, linearFoodRice, linearFoodNoodles, linearClothesInfant, linearClothesYoung, linearClothesAdult, linearMedicineFever, linearMedicineColds, linearMedicineCough;
    TextView txtFoodCannedGoods, txtFoodRice, txtFoodNoodles, txtClothesInfant, txtClothesYoung, txtClothesAdult, txtMedicineFever, txtMedicineColds, txtMedicineCough;
    SeekBar seekFoodCannedGoods, seekFoodRice, seekFoodNoodles, seekClothesInfant, seekClothesYoung, seekClothesAdult, seekMedicineFever, seekMedicineColds, seekMedicineCough, seekOthers;
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

        editOthers = (EditText) view.findViewById(R.id.edit_others);

        linearFoodCannedGoods = (LinearLayout) view.findViewById(R.id.linear_canned_goods);
        linearFoodRice = (LinearLayout) view.findViewById(R.id.linear_rice);
        linearFoodNoodles = (LinearLayout) view.findViewById(R.id.linear_noodles);
        txtFoodCannedGoods = (TextView) view.findViewById(R.id.text_canned_goods);
        txtFoodNoodles = (TextView) view.findViewById(R.id.text_noodles);
        txtFoodRice = (TextView) view.findViewById(R.id.text_rice);
        linearMedicineFever = (LinearLayout) view.findViewById(R.id.linear_fever);
        linearMedicineColds = (LinearLayout) view.findViewById(R.id.linear_colds);
        linearMedicineCough = (LinearLayout) view.findViewById(R.id.linear_cough);
        txtMedicineFever = (TextView) view.findViewById(R.id.text_fever);
        txtMedicineColds = (TextView) view.findViewById(R.id.text_colds);
        txtMedicineCough = (TextView) view.findViewById(R.id.text_cough);
        linearClothesInfant = (LinearLayout) view.findViewById(R.id.linear_infant);
        linearClothesYoung = (LinearLayout) view.findViewById(R.id.linear_young);
        linearClothesAdult = (LinearLayout) view.findViewById(R.id.linear_adult);
        txtClothesInfant = (TextView) view.findViewById(R.id.text_infant);
        txtClothesYoung = (TextView) view.findViewById(R.id.text_young);
        txtClothesAdult = (TextView) view.findViewById(R.id.text_adult);
        seekFoodCannedGoods = (SeekBar) view.findViewById(R.id.seekbar_canned_goods);
        seekFoodRice = (SeekBar) view.findViewById(R.id.seekbar_rice);
        seekFoodNoodles = (SeekBar) view.findViewById(R.id.seekbar_noodles);
        seekClothesInfant = (SeekBar) view.findViewById(R.id.seekbar_infant_clothes);
        seekClothesYoung = (SeekBar) view.findViewById(R.id.seekbar_young_clothes);
        seekClothesAdult = (SeekBar) view.findViewById(R.id.seekbar_adult_clothes);
        seekMedicineColds = (SeekBar) view.findViewById(R.id.seekbar_colds_medicine);
        seekMedicineFever = (SeekBar) view.findViewById(R.id.seekbar_fever_medicine);
        seekMedicineCough = (SeekBar) view.findViewById(R.id.seekbar_cough_medicine);
        seekOthers = (SeekBar) view.findViewById(R.id.seekbar_others);
        checkFood = (CheckBox) view.findViewById(R.id.check_food);
        checkClothes = (CheckBox) view.findViewById(R.id.check_clothes);
        checkMedicine = (CheckBox) view.findViewById(R.id.check_medicine);
        checkOthers = (CheckBox) view.findViewById(R.id.check_others);
        buttonSend = (Button) view.findViewById(R.id.button_send);
        buttonShare = (Button) view.findViewById(R.id.button_share);

        seekFoodCannedGoods.setEnabled(false);
        seekFoodNoodles.setEnabled(false);
        seekFoodRice.setEnabled(false);
        seekClothesInfant.setEnabled(false);
        seekClothesYoung.setEnabled(false);
        seekClothesAdult.setEnabled(false);
        seekMedicineFever.setEnabled(false);
        seekMedicineColds.setEnabled(false);
        seekMedicineCough.setEnabled(false);
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
                    seekFoodCannedGoods.setEnabled(false);
                    seekFoodNoodles.setEnabled(false);
                    seekFoodRice.setEnabled(false);

//                    seekFoodCannedGoods.setVisibility(View.GONE);
//                    seekFoodNoodles.setVisibility(View.GONE);
//                    seekFoodRice.setVisibility(View.GONE);
//                    linearFoodCannedGoods.setVisibility(View.GONE);
//                    linearFoodRice.setVisibility(View.GONE);
//                    linearFoodNoodles.setVisibility(View.GONE);
//                    txtFoodCannedGoods.setVisibility(View.GONE);
//                    txtFoodNoodles.setVisibility(View.GONE);
//                    txtFoodRice.setVisibility(View.GONE);
                } else {
                    seekFoodCannedGoods.setEnabled(true);
                    seekFoodNoodles.setEnabled(true);
                    seekFoodRice.setEnabled(true);

//                    seekFoodCannedGoods.setVisibility(View.VISIBLE);
//                    seekFoodNoodles.setVisibility(View.VISIBLE);
//                    seekFoodRice.setVisibility(View.VISIBLE);
//                    linearFoodCannedGoods.setVisibility(View.VISIBLE);
//                    linearFoodRice.setVisibility(View.VISIBLE);
//                    linearFoodNoodles.setVisibility(View.VISIBLE);
//                    txtFoodCannedGoods.setVisibility(View.VISIBLE);
//                    txtFoodNoodles.setVisibility(View.VISIBLE);
//                    txtFoodRice.setVisibility(View.VISIBLE);
                }
            }
        });
        checkClothes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    seekClothesInfant.setEnabled(false);
                    seekClothesYoung.setEnabled(false);
                    seekClothesAdult.setEnabled(false);

//                    seekClothesInfant.setVisibility(View.GONE);
//                    seekClothesYoung.setVisibility(View.GONE);
//                    seekClothesAdult.setVisibility(View.GONE);
//                    linearClothesInfant.setVisibility(View.GONE);
//                    linearClothesYoung.setVisibility(View.GONE);
//                    linearClothesAdult.setVisibility(View.GONE);
//                    txtClothesInfant.setVisibility(View.GONE);
//                    txtClothesYoung.setVisibility(View.GONE);
//                    txtClothesAdult.setVisibility(View.GONE);
                } else {
                    seekClothesInfant.setEnabled(true);
                    seekClothesYoung.setEnabled(true);
                    seekClothesAdult.setEnabled(true);

//                    seekClothesInfant.setVisibility(View.VISIBLE);
//                    seekClothesYoung.setVisibility(View.VISIBLE);
//                    seekClothesAdult.setVisibility(View.VISIBLE);
//                    linearClothesInfant.setVisibility(View.VISIBLE);
//                    linearClothesYoung.setVisibility(View.VISIBLE);
//                    linearClothesAdult.setVisibility(View.VISIBLE);
//                    txtClothesInfant.setVisibility(View.VISIBLE);
//                    txtClothesYoung.setVisibility(View.VISIBLE);
//                    txtClothesAdult.setVisibility(View.VISIBLE);
                }
            }
        });
        checkMedicine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    seekMedicineFever.setEnabled(false);
                    seekMedicineCough.setEnabled(false);
                    seekMedicineColds.setEnabled(false);

//                    seekMedicineFever.setVisibility(View.GONE);
//                    seekMedicineCough.setVisibility(View.GONE);
//                    seekMedicineColds.setVisibility(View.GONE);
//                    linearMedicineFever.setVisibility(View.GONE);
//                    linearMedicineColds.setVisibility(View.GONE);
//                    linearMedicineCough.setVisibility(View.GONE);
//                    txtMedicineFever.setVisibility(View.GONE);
//                    txtMedicineColds.setVisibility(View.GONE);
//                    txtMedicineCough.setVisibility(View.GONE);
                } else {
                    seekMedicineFever.setEnabled(true);
                    seekMedicineCough.setEnabled(true);
                    seekMedicineColds.setEnabled(true);

//                    seekMedicineFever.setVisibility(View.VISIBLE);
//                    seekMedicineCough.setVisibility(View.VISIBLE);
//                    seekMedicineColds.setVisibility(View.VISIBLE);
//                    linearMedicineFever.setVisibility(View.VISIBLE);
//                    linearMedicineColds.setVisibility(View.VISIBLE);
//                    linearMedicineCough.setVisibility(View.VISIBLE);
//                    txtMedicineFever.setVisibility(View.VISIBLE);
//                    txtMedicineColds.setVisibility(View.VISIBLE);
//                    txtMedicineCough.setVisibility(View.VISIBLE);
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
        requestFineLocation();
        requestCoarseLocation();
        latitude = this.getArguments().getDouble("lat");
        longitude = this.getArguments().getDouble("long");
        getLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send:

                boolean isFood = checkFood.isChecked();
                String foodCannedGoodsRate = String.valueOf(seekFoodCannedGoods.getProgress());
                String foodRiceRate = String.valueOf(seekFoodRice.getProgress());
                String foodNoodlesRate = String.valueOf(seekFoodNoodles.getProgress());
                boolean isClothes = checkClothes.isChecked();
                String clothesInfantRate = String.valueOf(seekClothesInfant.getProgress());
                String clothesYoungRate = String.valueOf(seekClothesYoung.getProgress());
                String clothesAdultRate = String.valueOf(seekClothesAdult.getProgress());
                boolean isMedicine = checkMedicine.isChecked();
                String medicineFeverRate = String.valueOf(seekMedicineFever.getProgress());
                String medicineColdsRate = String.valueOf(seekMedicineColds.getProgress());
                String medicineCoughRate = String.valueOf(seekMedicineCough.getProgress());
                boolean isOthers = checkOthers.isChecked();
                String others = editOthers.getText().toString();
                String othersRate = String.valueOf(seekOthers.getProgress());
                if (!isFood) {
                    foodCannedGoodsRate = "not requested";
                    foodRiceRate = "not requested";
                    foodNoodlesRate = "not requested";
                }
                if (!isClothes) {
                    clothesInfantRate = "not requested";
                    clothesYoungRate = "not requested";
                    clothesAdultRate = "not requested";
                }
                if (!isMedicine) {
                    medicineFeverRate = "not requested";
                    medicineColdsRate = "not requested";
                    medicineCoughRate = "not requested";
                }
                if (!isOthers) {
                    othersRate = "not requested";
                } else {
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Request request = new Request(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                            "",
                            currentDateTimeString,
                            addresses.get(0).getCountryName() + ", " + addresses.get(0).getAddressLine(0) + "  " + addresses.get(0).getLocality(),
                            foodCannedGoodsRate,
                            foodRiceRate,
                            foodNoodlesRate,
                            clothesInfantRate,
                            clothesYoungRate,
                            clothesAdultRate,
                            medicineFeverRate,
                            medicineColdsRate,
                            medicineCoughRate,
                            others + ": " + othersRate);
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
