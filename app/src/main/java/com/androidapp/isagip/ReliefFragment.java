package com.androidapp.isagip;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidapp.isagip.model.Feedback;
import com.androidapp.isagip.model.Request;
import com.androidapp.isagip.model.UserStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReliefFragment extends Fragment {

    @BindView(R.id.check_food)
    CheckBox checkFood;
    @BindView(R.id.card_food)
    CardView cardFood;
    @BindView(R.id.check_clothes)
    CheckBox checkClothes;
    @BindView(R.id.card_clothes)
    CardView cardClothes;
    @BindView(R.id.check_medicine)
    CheckBox checkMedicine;
    @BindView(R.id.card_medicine)
    CardView cardMedicine;
    @BindView(R.id.check_other)
    CheckBox checkOther;
    @BindView(R.id.edit_other)
    EditText editOther;
    @BindView(R.id.card_other)
    CardView cardOther;
    @BindView(R.id.edit_name1)
    EditText editName1;
    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.edit_name2)
    EditText editName2;
    @BindView(R.id.spinner2)
    Spinner spinner2;
    @BindView(R.id.edit_name3)
    EditText editName3;
    @BindView(R.id.spinner3)
    Spinner spinner3;
    @BindView(R.id.edit_name4)
    EditText editName4;
    @BindView(R.id.spinner4)
    Spinner spinner4;
    @BindView(R.id.edit_name5)
    EditText editName5;
    @BindView(R.id.spinner5)
    Spinner spinner5;
    @BindView(R.id.button_send)
    Button buttonSend;
    Unbinder unbinder;
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;
    private DatabaseReference mDatabase1;
    private DatabaseReference myRef1;
    double latitude;
    double longitude;
    List<Address> addresses;
    private String food = "false";
    private String clothes = "false";
    private String medicine = "false";
    private String other = "false";
    Request model;
    UserStatus model1;
    private ChildEventListener ref;
    private ChildEventListener ref1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relief, container, false);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("requests");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        unbinder = ButterKnife.bind(this, view);


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
                    other = "true";
                    editOther.setVisibility(View.VISIBLE);
                } else {
                    other = "false";
                    editOther.setVisibility(View.GONE);
                }
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("request");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model = dataSnapshot.getValue(Request.class);
                    } catch (Exception ex) {
                        Log.e("RAWR", ex.getMessage());
                    }
                }
            }

            // This function is called each time a child item is removed.
            public void onChildRemoved(DataSnapshot dataSnapshot) {
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

        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        myRef1 = database1.getReference("userStatus");
        mDatabase1 = FirebaseDatabase.getInstance().getReference();
        ref1 = myRef1.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model1 = dataSnapshot.getValue(UserStatus.class);
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model1.getId())) {
                            if (model1.getStatus().equals("sent")) {
                                buttonSend.setEnabled(true);
                            } else {
                                buttonSend.setEnabled(false);
                            }
                        }
                    } catch (Exception ex) {
                        Log.e("RAWR", ex.getMessage());
                    }
                }
            }

            // This function is called each time a child item is removed.
            public void onChildRemoved(DataSnapshot dataSnapshot) {
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
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    public void requestCoarseLocation() {
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void openInternetDialog() {
        new AlertDialog.Builder(getContext(), R.style.MyAlertDialogStyle)
                .setTitle("Attention")
                .setMessage("No Internet Connection Detected.")
                .setNeutralButton("Open Wi-Fi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        WifiManager wifi = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(true);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Open Mobile Data", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_send)
    public void onViewClicked() {
        String name1, name2, name3, name4, name5;
        String gender1, gender2, gender3, gender4, gender5;
        if (editName1.getText().toString().isEmpty()) {
            gender1 = "n/a";
            name1 = "n/a";
        } else {
            gender1 = spinner1.getSelectedItem().toString();
            name1 = editName1.getText().toString().trim();
        }
        if (editName2.getText().toString().isEmpty()) {
            gender2 = "n/a";
            name2 = "n/a";
        } else {
            gender2 = spinner2.getSelectedItem().toString();
            name2 = editName2.getText().toString().trim();
        }
        if (editName3.getText().toString().isEmpty()) {
            gender3 = "n/a";
            name3 = "n/a";
        } else {
            gender3 = spinner3.getSelectedItem().toString();
            name3 = editName3.getText().toString().trim();
        }
        if (editName4.getText().toString().isEmpty()) {
            gender4 = "n/a";
            name4 = "n/a";
        } else {
            gender4 = spinner4.getSelectedItem().toString();
            name4 = editName4.getText().toString().trim();
        }
        if (editName5.getText().toString().isEmpty()) {
            gender5 = "n/a";
            name5 = "n/a";
        } else {
            gender5 = spinner5.getSelectedItem().toString();
            name5 = editName5.getText().toString().trim();
        }
        String other = editOther.getText().toString();
        if (other.isEmpty()) {
            other = "false";
        }
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Request request = new Request(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                currentDateTimeString,
                String.valueOf(longitude),
                String.valueOf(latitude),
                addresses.get(0).getCountryName() + ", " + addresses.get(0).getAddressLine(0) + "  " + addresses.get(0).getLocality(),
                food,
                clothes,
                medicine,
                name1,
                gender1,
                name2,
                gender2,
                name3,
                gender3,
                name4,
                gender4,
                name5,
                gender5,
                other,
                "requested",
                "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyy", Locale.US);
        String format = simpleDateFormat.format(new Date());
        String nano = String.valueOf(System.nanoTime());
        UserStatus userStatus = new UserStatus(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                format,
                nano,
                "requested");
        if (isNetworkAvailable()) {
            mDatabase.child("request").child(nano).setValue(request);
            mDatabase.child("userStatus").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userStatus);
            Toast.makeText(getContext(), "Request Successful", Toast.LENGTH_SHORT).show();
            buttonSend.setEnabled(false);

        } else {
            Toast.makeText(getContext(), "Please Turn on Wifi/Mobile Network.", Toast.LENGTH_LONG).show();
        }
    }
}
