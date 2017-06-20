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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidapp.isagip.adapter.MemberListAdapter;
import com.androidapp.isagip.databinding.ActivityRegisterBinding;
import com.androidapp.isagip.databinding.DialogAddMemberBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, MemberListAdapter.iMemberList {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private ActivityRegisterBinding binder;
    private Dialog dialog;
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
        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        initDialogAdd();

        adapter = new MemberListAdapter(this);
        binder.recyclerviewMemberList.setAdapter(adapter);
        binder.recyclerviewMemberList.setLayoutManager(new LinearLayoutManager(this));

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
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    finish();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            }
                        });
                break;
            case R.id.imgbutton_add_member:

                dialog.show();
                binder.linearlayoutRegisterContainer.clearFocus();
                break;
        }
    }

    @Override
    public void onItemChange() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (adapter.getItemCount() > 0){
            binder.tvLabel.setText("Members : ".concat(String.valueOf(adapter.getItemCount())));
            binder.textviewEmptyList.setVisibility(View.GONE);
        } else {
            binder.tvLabel.setText("Member");
            binder.textviewEmptyList.setVisibility(View.VISIBLE);
        }
    }
}
