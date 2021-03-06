package com.androidapp.isagip;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidapp.isagip.model.Feedback;
import com.androidapp.isagip.model.Operation;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.edit_comment)
    EditText editComment;
    @BindView(R.id.button_send_feedback)
    Button buttonSendFeedback;
    Unbinder unbinder;
    @BindView(R.id.seekBar_feedback_food)
    SeekBar seekBarFeedbackFood;
    @BindView(R.id.seekBar_feedback_clothes)
    SeekBar seekBarFeedbackClothes;
    @BindView(R.id.seekBar_feedback_medicine)
    SeekBar seekBarFeedbackMedicine;
    @BindView(R.id.seekBar_feedback_other)
    SeekBar seekBarFeedbackOther;
    @BindView(R.id.spinner_operation)
    Spinner spinnerOperation;
    private DatabaseReference myRef;
    private DatabaseReference mDatabase;
    private DatabaseReference myRef1;
    private DatabaseReference mDatabase1;
    private DatabaseReference myRef2;
    private DatabaseReference mDatabase2;
    private DatabaseReference myRef3;
    private DatabaseReference mDatabase3;
    private String food = "true", clothes = "true", medicine = "true", others = "true", othersRate = "true", comment = "true";
    Feedback model;
    Operation model1;
    UserStatus model2;
    Request model3;
    private ChildEventListener ref;
    private ChildEventListener ref1;
    private ChildEventListener ref2;
    private ChildEventListener ref3;
    double latitude;
    double longitude;
    List<Address> addresses;
    Location location;
    private String operationId;
    private double a = 0.0;
    private String transactionId;

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
        requestFineLocation();
        requestCoarseLocation();
        latitude = this.getArguments().getDouble("lat");
        longitude = this.getArguments().getDouble("long");
        checkFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    food = String.valueOf(seekBarFeedbackFood.getProgress());
                    seekBarFeedbackFood.setVisibility(View.VISIBLE);
                } else {
                    food = "false";
                    seekBarFeedbackFood.setVisibility(View.GONE);
                }
            }
        });
        checkClothes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    clothes = String.valueOf(seekBarFeedbackClothes.getProgress());
                    seekBarFeedbackClothes.setVisibility(View.VISIBLE);
                } else {
                    clothes = "false";
                    seekBarFeedbackClothes.setVisibility(View.GONE);
                }
            }
        });
        checkMedicine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    medicine = String.valueOf(seekBarFeedbackMedicine.getProgress());
                    seekBarFeedbackMedicine.setVisibility(View.VISIBLE);
                } else {
                    medicine = "false";
                    seekBarFeedbackMedicine.setVisibility(View.GONE);
                }
            }
        });
        checkOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editOther.setVisibility(View.VISIBLE);
                    seekBarFeedbackOther.setVisibility(View.VISIBLE);
                    others = editOther.getText().toString();
                    othersRate = String.valueOf(seekBarFeedbackOther.getProgress());
                } else {
                    editOther.setVisibility(View.GONE);
                    seekBarFeedbackOther.setVisibility(View.GONE);
                    others = "false";
                    othersRate = "false";
                }
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("feedback");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model = dataSnapshot.getValue(Feedback.class);
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
        final List<String> areas = new ArrayList<String>();
        myRef1 = database1.getReference("operations");
        mDatabase1 = FirebaseDatabase.getInstance().getReference();
        ref1 = myRef1.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model1 = dataSnapshot.getValue(Operation.class);
                        Location loc = new Location("");
                        loc.setLatitude(model1.getLatitude());
                        loc.setLongitude(model1.getLongitude());

                        Location loc1 = new Location("");
                        loc1.setLatitude(latitude);
                        loc1.setLongitude(longitude);

                        areas.add(model1.getTitle());
                        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, areas);
                        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerOperation.setAdapter(areasAdapter);

                        if (a < loc.distanceTo(loc1)) {
                            a = loc.distanceTo(loc1);
                            location = loc;
                            operationId = String.valueOf(model1.getId());
                        }
                        if (model1.getLatitude() == null) {
                            Toast.makeText(getContext(), "No Operations", Toast.LENGTH_SHORT).show();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.content_main, new NewsFragment());
                            transaction.commit();
                        }

                        if (model1.getLatitude() == 0) {
                            Toast.makeText(getContext(), "No Operations", Toast.LENGTH_SHORT).show();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.content_main, new NewsFragment());
                            transaction.commit();
                        }
                        if (model1.getArea() == null) {
                            Toast.makeText(getContext(), "No Operations", Toast.LENGTH_SHORT).show();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.content_main, new NewsFragment());
                            transaction.commit();
                        }
                        if (model1.getArea() == "") {
                            Toast.makeText(getContext(), "No Operations", Toast.LENGTH_SHORT).show();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.content_main, new NewsFragment());
                            transaction.commit();
                        }
                    } catch (Exception ex) {
                        Log.e("RAWR", ex.getMessage());
                    }
                } else {
                    Toast.makeText(getContext(), "No Operations", Toast.LENGTH_SHORT).show();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.content_main, new NewsFragment());
                    transaction.commit();
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


        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        myRef2 = database2.getReference("userStatus");
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        ref2 = myRef2.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model2 = dataSnapshot.getValue(UserStatus.class);
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model2.getId())) {
                            if (model2.getStatus().equals("sent")) {
                                buttonSendFeedback.setEnabled(false);
                                buttonSendFeedback.setText("Feedback Unavailable");
                            } else {
                                buttonSendFeedback.setEnabled(true);
                            }
                            transactionId = model2.getTransactionId();
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

        final FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        myRef3 = database3.getReference("request");
        mDatabase3 = FirebaseDatabase.getInstance().getReference();
        ref3 = myRef3.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model3 = dataSnapshot.getValue(Request.class);


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_send_feedback)
    public void onViewClicked() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String comment = editComment.getText().toString();
        if (comment.isEmpty() || comment.equals(null)) {
            comment = "false";
        }
        String others = editOther.getText().toString();
        if (others.isEmpty() || others.equals(null)) {
            others = "false";
        }
        Feedback feedback = new Feedback(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                currentDateTimeString,
                FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                food,
                clothes,
                medicine,
                others,
                othersRate,
                "",
                comment,
                "requested",
                operationId,
                "");

        if (isNetworkAvailable()) {
            if (!isFieldsEmpty()) {
                mDatabase.child("feedback").child(transactionId).setValue(feedback);
                Toast.makeText(getContext(), "Feedback Sent!", Toast.LENGTH_SHORT).show();

                buttonSendFeedback.setEnabled(false);

                DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("request").child(model2.getTransactionId()).child("status");
                data.setValue("sent");

                DatabaseReference data1 = FirebaseDatabase.getInstance().getReference().child("userStatus").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status");
                data1.setValue("sent");

                DatabaseReference data2 = FirebaseDatabase.getInstance().getReference().child("request").child(model2.getTransactionId()).child("operationId");
                data2.setValue(operationId);
            }
        } else {
            Toast.makeText(getContext(), "Please Turn on Wifi/Mobile Network.", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isFieldsEmpty() {
        return food.equals("false") && clothes.equals("false") && medicine.equals("false") && others.equals("false") && comment.equals("false");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public void getNearestOperation() {

    }
}
