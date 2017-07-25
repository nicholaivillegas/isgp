package com.androidapp.isagip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.AlteredCharSequence;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidapp.isagip.model.AffectedArea;
import com.androidapp.isagip.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 100;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;

    private TextView mStatusText;
    private TextView mDetailText;
    private TextView mBirthdayLabel;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    //    private EditText mFirstName;
//    private EditText mLastName;
    private EditText mName;
    private EditText mEmail;
    private EditText mFatherName;
    private EditText mMotherName;
    private EditText mChildName1;
    private EditText mChildName2;
    private EditText mChildName3;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton;
    private Button mSubmit;

    private DatePicker mDatepicker;

    private DatabaseReference mDatabase;

    private PhoneAuthCredential mCredential;

    private String mMobileNumber;
    private String childName;

    private int familyCounter = 0;

    private DatabaseReference myRef;
    private ChildEventListener ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!isNetworkAvailable()) {
            openInternetDialog();
        }
        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // Assign views
        mPhoneNumberViews = (ViewGroup) findViewById(R.id.phone_auth_fields);
        mSignedInViews = (ViewGroup) findViewById(R.id.signed_in_buttons);

        mStatusText = (TextView) findViewById(R.id.status);
        mDetailText = (TextView) findViewById(R.id.detail);
        mBirthdayLabel = (TextView) findViewById(R.id.text_birthday_label);

        mPhoneNumberField = (EditText) findViewById(R.id.edit_phone_number);
        mVerificationField = (EditText) findViewById(R.id.edit_verification_code);
        mName = (EditText) findViewById(R.id.edit_name);
//        mFirstName = (EditText) findViewById(R.id.edit_first_name);
//        mLastName = (EditText) findViewById(R.id.edit_last_name);
        mEmail = (EditText) findViewById(R.id.edit_email);
        mFatherName = (EditText) findViewById(R.id.edit_father_name);
        mMotherName = (EditText) findViewById(R.id.edit_mother_name);
        mChildName1 = (EditText) findViewById(R.id.edit_child_name1);
        mChildName2 = (EditText) findViewById(R.id.edit_child_name2);
        mChildName3 = (EditText) findViewById(R.id.edit_child_name3);

        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        mResendButton = (Button) findViewById(R.id.button_resend);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mSubmit = (Button) findViewById(R.id.buttom_submit);

        mDatepicker = (DatePicker) findViewById(R.id.datepicker);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        showPhoneRegistrationFields();

        //add permission checking
        if (checkWriteExternalPermission()) {
            TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            mPhoneNumberField.setText(mPhoneNumber);
        } else {
            Toast.makeText(this, "Enable Permissions", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                mCredential = credential;
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                //saveProfile()
                signInWithPhoneAuthCredential(mCredential);
                Toast.makeText(LoginActivity.this, "PHONE NUMBER VERIFIED!", Toast.LENGTH_SHORT).show();
                hidePhoneRegistrationFields();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }


    private boolean checkWriteExternalPermission() {

        String permission = "android.permission.READ_PHONE_STATE";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void hidePhoneRegistrationFields() {
        mMobileNumber = mPhoneNumberField.getText().toString();
        mPhoneNumberField.setVisibility(View.GONE);
        mStartButton.setVisibility(View.GONE);
        mResendButton.setVisibility(View.GONE);
        mVerifyButton.setVisibility(View.GONE);
        mSignOutButton.setVisibility(View.GONE);
        mVerificationField.setVisibility(View.GONE);

//        mFirstName.setVisibility(View.VISIBLE);
//        mLastName.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mEmail.setVisibility(View.VISIBLE);
        mBirthdayLabel.setVisibility(View.VISIBLE);
        mDatepicker.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.VISIBLE);
        mFatherName.setVisibility(View.VISIBLE);
        mMotherName.setVisibility(View.VISIBLE);
        mChildName1.setVisibility(View.VISIBLE);
        mChildName2.setVisibility(View.VISIBLE);
        mChildName3.setVisibility(View.VISIBLE);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        User model = dataSnapshot.getValue(User.class);
                        if (model.getNumber().equals(mMobileNumber)) {
                            Toast.makeText(LoginActivity.this, model.getNumber(), Toast.LENGTH_LONG).show();

//                            mFirstName.setVisibility(View.GONE);
//                            mLastName.setVisibility(View.GONE);
//                            mName.setVisibility(View.VISIBLE);

                            mName.setText(model.getName());
                            mEmail.setText(model.getEmail());
                            mFatherName.setText(model.getFather());
                            mMotherName.setText(model.getMother());
                        }
                    } catch (Exception ex) {
                        Log.e("RAWR", ex.getMessage());
                    }
                }
            }

            // This function is called each time a child item is removed.
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                AffectedArea model = dataSnapshot.getValue(AffectedArea.class);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG:", "Failed to read value.", error.toException());
            }
        });
    }

    private void showPhoneRegistrationFields() {
        mPhoneNumberField.setVisibility(View.VISIBLE);
        mStartButton.setVisibility(View.VISIBLE);
        mResendButton.setVisibility(View.VISIBLE);
        mVerifyButton.setVisibility(View.VISIBLE);
        mSignOutButton.setVisibility(View.VISIBLE);
        mVerificationField.setVisibility(View.VISIBLE);

//        mFirstName.setVisibility(View.GONE);
//        mLastName.setVisibility(View.GONE);
        mEmail.setVisibility(View.GONE);
        mBirthdayLabel.setVisibility(View.GONE);
        mDatepicker.setVisibility(View.GONE);
        mSubmit.setVisibility(View.GONE);
        mFatherName.setVisibility(View.GONE);
        mMotherName.setVisibility(View.GONE);
        mChildName1.setVisibility(View.GONE);
        mChildName2.setVisibility(View.GONE);
        mChildName3.setVisibility(View.GONE);
    }

    public String countFamily() {
        if (!mFatherName.getText().toString().isEmpty()) {
            familyCounter++;
        }
        if (!mMotherName.getText().toString().isEmpty()) {
            familyCounter++;
        }
        if (!mChildName1.getText().toString().isEmpty()) {
            familyCounter++;
        }
        if (!mChildName2.getText().toString().isEmpty()) {
            familyCounter++;
        }
        if (!mChildName3.getText().toString().isEmpty()) {
            familyCounter++;
        }
        return String.valueOf(familyCounter);
    }

    //save profile to database
    private void saveProfile() {
        String name = mName.getText().toString();
        User user = new User(mMobileNumber,
                name,
                mEmail.getText().toString(),
                mMobileNumber,
                makeDate(),
                "n/a",
                "n/a",
                "user",
                "active",
                mFatherName.getText().toString(),
                mMotherName.getText().toString(),
                childName(),
                countFamily());
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(mEmail.getText().toString())
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    public String childName() {
//        mChildName1.getText().toString() + "," + mChildName2.getText().toString() + "," + mChildName3.getText().toString()
        if (!mChildName1.getText().toString().isEmpty()) {
            childName = mChildName1.getText().toString().trim();
            mChildName2.setVisibility(View.VISIBLE);
            if (!mChildName2.getText().toString().isEmpty()) {
                childName = mChildName1.getText().toString().trim() + "," + mChildName2.getText().toString().trim();
                mChildName3.setVisibility(View.VISIBLE);
                if (!mChildName3.getText().toString().isEmpty()) {
                    childName = mChildName1.getText().toString().trim() + "," + mChildName2.getText().toString().trim() + "," + mChildName3.getText().toString().trim();
                }
            }
        } else {
            childName = "n/a";
        }
        return childName;
    }

    public String makeDate() {
        return String.valueOf(mDatepicker.getMonth()) + "-" + String.valueOf(mDatepicker.getDayOfMonth()) + "-" + String.valueOf(mDatepicker.getYear());
    }

    private boolean isValidAge() {
        Date currentDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);

        int age = calendar.get(Calendar.YEAR) - mDatepicker.getYear();
        return age > 17;
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            //saveProfile()
                            Toast.makeText(LoginActivity.this, "PHONE NUMBER IS VERIFIED", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            startActivity(i);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
//            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews(mStartButton, mPhoneNumberField);
                disableViews(mVerifyButton, mResendButton, mVerificationField);
                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                disableViews(mStartButton);
                mDetailText.setText("Verification Code Sent");
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                mDetailText.setText("Verification Failed");
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                mDetailText.setText("Verification Success");

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText("Instant Validation");
                    }
                }
                hidePhoneRegistrationFields();
                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                mDetailText.setText("Sign in Failed");
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                hidePhoneRegistrationFields();
                break;
        }

        if (user == null) {
            // Signed out
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            mSignedInViews.setVisibility(View.GONE);

            mStatusText.setText("Signed Out");
        } else {
            // Signed in
//            mPhoneNumberViews.setVisibility(View.GONE);
//            mSignedInViews.setVisibility(View.VISIBLE);

            enableViews(mPhoneNumberField, mVerificationField);
            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);

            mStatusText.setText("Signed In");
            mDetailText.setText(getString(R.string.firebaseui_status_fmt, user.getUid()));
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                if (!validatePhoneNumber()) {
                    return;
                }

                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                break;
            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.button_resend:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;
            case R.id.buttom_submit:
                if (isValidAge()) {
                    if (emailValidator(mEmail.getText().toString())) {
                        saveProfile();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    } else Toast.makeText(this, "INVALID EMAIL", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(this, "INVALID AGE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void openInternetDialog() {
        new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setTitle("Attention")
                .setMessage("No Internet Connection Detected.")
                .setNeutralButton("Open Wi-Fi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(true);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton("Open Mobile Data", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable()) {
            openInternetDialog();
        }
    }

    @Override
    public void onBackPressed() {

    }
}