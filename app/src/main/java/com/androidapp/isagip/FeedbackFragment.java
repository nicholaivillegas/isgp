package com.androidapp.isagip;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.androidapp.isagip.model.Feedback;
import com.androidapp.isagip.model.Request;
import com.androidapp.isagip.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FeedbackFragment extends Fragment {


    @BindView(R.id.check_food)
    CheckBox checkFood;
    @BindView(R.id.check_clothes)
    CheckBox checkClothes;
    @BindView(R.id.check_medicine)
    CheckBox checkMedicine;
    @BindView(R.id.check_other)
    CheckBox checkOther;
    @BindView(R.id.edit_other)
    EditText editOther;
    @BindView(R.id.seekBar_feedback)
    SeekBar seekBarFeedback;
    @BindView(R.id.edit_comment)
    EditText editComment;
    @BindView(R.id.button_send_feedback)
    Button buttonSendFeedback;
    Unbinder unbinder;
    private DatabaseReference myRef;
    private DatabaseReference mDatabase;
    private String food, clothes, medicine, others, comment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        unbinder = ButterKnife.bind(this, view);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("feedback");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    food = "true";
                } else {
                    food = "false";
                }
            }
        });
        checkClothes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    clothes = "true";
                } else {
                    clothes = "false";
                }
            }
        });
        checkMedicine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    medicine = "true";
                } else {
                    medicine = "false";
                }
            }
        });
        checkOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editOther.setVisibility(View.VISIBLE);
                    others = editOther.getText().toString();
                } else {
                    editOther.setVisibility(View.GONE);
                    others = "false";
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_send_feedback)
    public void onViewClicked() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Feedback feedback = new Feedback(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                currentDateTimeString,
                FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                food,
                clothes,
                medicine,
                others,
                String.valueOf(seekBarFeedback.getProgress()),
                editComment.getText().toString());

        if (isNetworkAvailable()) {
            mDatabase.child("feedback").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(feedback);
            Toast.makeText(getContext(), "Feedback Sent!", Toast.LENGTH_SHORT).show();
            buttonSendFeedback.setEnabled(false);
        } else {
            Toast.makeText(getContext(), "Please Turn on Wifi/Mobile Network.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
