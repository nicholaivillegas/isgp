package com.androidapp.isagip;

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
import android.support.annotation.IdRes;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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
    @BindView(R.id.button_send)
    Button buttonSend;
    Unbinder unbinder;
    @BindView(R.id.radio_family_1)
    RadioButton radioFamily1;
    @BindView(R.id.radio_family_10)
    RadioButton radioFamily10;
    @BindView(R.id.radioGroup_family)
    RadioGroup radioGroupFamily;
    @BindView(R.id.card_family_size)
    CardView cardFamilySize;
    @BindView(R.id.edit_name_official)
    EditText editNameOfficial;
    @BindView(R.id.edit_number_official)
    EditText editNumberOfficial;
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
    private String familySize = "small";


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
        radioGroupFamily.check(radioFamily1.getId());
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

        radioGroupFamily.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == radioFamily1.getId()) {
                    familySize = "small";
                } else if (checkedId == radioFamily10.getId()) {
                    familySize = "big";
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
                                Toast.makeText(getContext(), "You're not yet allowed to send another request wait for further announcement", Toast.LENGTH_LONG).show();
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
        String name = editNameOfficial.getText().toString(), number = editNumberOfficial.getText().toString();
        String other = editOther.getText().toString();
        if (other.isEmpty()) {
            other = "false";
        }

        if (name.isEmpty()) {
            name = "n/a";
        }
        if (number.isEmpty()) {
            number = "n/a";
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
                familySize,
                name,
                number,
                other,
                "requested",
                "",
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyy", Locale.US);
        String format = simpleDateFormat.format(new Date());
        String nano = String.valueOf(System.nanoTime());
        UserStatus userStatus = new UserStatus(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                format,
                nano,
                "requested");
        if (isNetworkAvailable()) {
            if (!isFieldsEmpty()) {
                mDatabase.child("request").child(nano).setValue(request);
                mDatabase.child("userStatus").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userStatus);
                Toast.makeText(getContext(), "Request Successful", Toast.LENGTH_SHORT).show();
                buttonSend.setEnabled(false);
            } else {
                Toast.makeText(getContext(), "Please check one", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(getContext(), "Please Turn on Wifi/Mobile Network.", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isFieldsEmpty() {
        return food.equals("false") && clothes.equals("false") && medicine.equals("false") && other.equals("false");
    }
}
