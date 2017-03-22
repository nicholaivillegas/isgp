package com.androidapp.isagip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.text.DateFormat;
import java.util.Date;

public class ReliefFragment extends Fragment implements View.OnClickListener {

    EditText editOthers;
    SeekBar seekFood, seekClothes, seekMedicine, seekOthers;
    CheckBox checkFood, checkClothes, checkMedicine, checkOthers;
    Button buttonSend, buttonShare;
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relief, container, false);

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
                    Request request = new Request(FirebaseAuth.getInstance().getCurrentUser().getEmail(), currentDateTimeString, "n/a", "n/a", foodRate, waterRate, medicineRate, others + ": " + othersRate);
                    mDatabase.child("request").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(request);
                }
                break;

            case R.id.button_share:
                break;
        }
    }


}
