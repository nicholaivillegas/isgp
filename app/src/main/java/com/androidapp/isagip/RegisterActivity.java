package com.androidapp.isagip;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidapp.isagip.adapter.MemberListAdapter;
import com.androidapp.isagip.databinding.ActivityRegisterBinding;
import com.androidapp.isagip.databinding.DialogAddMemberBinding;
import com.androidapp.isagip.databinding.DialogDatePickerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, MemberListAdapter.iMemberList {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private ActivityRegisterBinding binder;
    private Dialog dialog;
    private Dialog dialog2;
    private MemberListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_register);
        //setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.button_signin);
        btnRegister = (Button) findViewById(R.id.button_register);
        inputEmail = (EditText) findViewById(R.id.edit_email);
        inputPassword = (EditText) findViewById(R.id.edit_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        binder.imgbuttonAddMember.setOnClickListener(this);
        binder.editBirthday.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        initDialogAdd();
        initDialogDate();

        adapter = new MemberListAdapter(this);
        binder.recyclerviewMemberList.setAdapter(adapter);
        binder.recyclerviewMemberList.setLayoutManager(new LinearLayoutManager(this));
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public void initDialogAdd() {
        dialog = new Dialog(this);
        final DialogAddMemberBinding dialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.dialog_add_member,
                (ViewGroup) binder.getRoot(),
                false
        );
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
        dialogBinding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogBinding.editMemberName.getText().length() > 0) {
                    adapter.addName(dialogBinding.editMemberName.getText().toString());
                    dialogBinding.editMemberName.setText("");
                    dialog.hide();
                }
            }
        });
        dialog.setTitle("Add Member");
    }

    public void initDialogDate() {
        final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        final Calendar bdate = Calendar.getInstance();
        if (binder.editBirthday.getText().length() > 0){
            try {
                bdate.setTime(format.parse(binder.editBirthday.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        dialog2 = new Dialog(this);
        final DialogDatePickerBinding dialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.dialog_date_picker,
                (ViewGroup) binder.getRoot(),
                false
        );
        dialog2.setContentView(dialogBinding.getRoot());
        dialog2.setTitle("Date picker");
        dialogBinding.datepickerBirthday.updateDate(
                bdate.get(Calendar.YEAR),
                bdate.get(Calendar.MONTH),
                bdate.get(Calendar.DAY_OF_MONTH)
        );
        dialogBinding.buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bdate.set(Calendar.YEAR,dialogBinding.datepickerBirthday.getYear());
                bdate.set(Calendar.MONTH,dialogBinding.datepickerBirthday.getMonth());
                bdate.set(Calendar.DAY_OF_MONTH,dialogBinding.datepickerBirthday.getDayOfMonth());
                binder.editBirthday.setText(format.format(bdate.getTime()));
                dialog2.hide();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_signin:
                finish();
                break;
            case R.id.button_register:
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    break;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed. " + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    finish();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            }
                        });
                break;
            case R.id.imgbutton_add_member:
                binder.linearlayoutRegisterContainer.clearFocus();
                dialog.show();

                break;
            case R.id.edit_birthday:
                binder.linearlayoutRegisterContainer.clearFocus();
                dialog2.show();
                break;
        }
    }

    @Override
    public void onItemChange() {
        if (adapter.getItemCount() > 0){
            binder.tvLabel.setText("Members : ".concat(String.valueOf(adapter.getItemCount())));
            binder.textviewEmptyList.setVisibility(View.GONE);
        } else {
            binder.tvLabel.setText("Member");
            binder.textviewEmptyList.setVisibility(View.VISIBLE);
        }
    }
}
