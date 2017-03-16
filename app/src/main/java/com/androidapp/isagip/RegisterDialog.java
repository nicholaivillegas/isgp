package com.androidapp.isagip;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.androidapp.isagip.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nico on 3/15/2017.
 */

public class RegisterDialog extends DialogFragment implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private DatabaseReference myRef;
    FirebaseDatabase database;
    EditText editTextNumber;
    DatePicker datePicker;
    Button buttonSubmit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_register, container, false);
        editTextNumber = (EditText) view.findViewById(R.id.edit_contact_number);
        datePicker = (DatePicker) view.findViewById(R.id.datepicker);
        buttonSubmit = (Button) view.findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setTitle("Additional Information: ");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        editTextNumber.getText().toString(), makeDate(), "user");
                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                dismiss();
                break;
        }
    }

    public String makeDate() {
        return String.valueOf(datePicker.getMonth()) + "-" + String.valueOf(datePicker.getDayOfMonth()) + "-" + String.valueOf(datePicker.getYear());
    }
}