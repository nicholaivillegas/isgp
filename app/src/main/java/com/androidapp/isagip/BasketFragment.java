package com.androidapp.isagip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidapp.isagip.model.AffectedArea;
import com.androidapp.isagip.model.Feedback;
import com.androidapp.isagip.model.Request;
import com.androidapp.isagip.model.UserStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BasketFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.card_food_received)
    CardView cardFoodReceived;
    @BindView(R.id.card_clothes_received)
    CardView cardClothesReceived;
    @BindView(R.id.card_medicine_received)
    CardView cardMedicineReceived;
    @BindView(R.id.card_other_received)
    CardView cardOtherReceived;
    @BindView(R.id.card_food_expected)
    CardView cardFoodExpected;
    @BindView(R.id.card_clothes_expected)
    CardView cardClothesExpected;
    @BindView(R.id.card_medicine_expected)
    CardView cardMedicineExpected;
    @BindView(R.id.card_other_expected)
    CardView cardOtherExpected;
    @BindView(R.id.check_food_received)
    TextView checkFoodReceived;
    @BindView(R.id.check_food_received_date)
    TextView checkFoodReceivedDate;
    @BindView(R.id.check_clothes_received)
    TextView checkClothesReceived;
    @BindView(R.id.check_clothes_received_date)
    TextView checkClothesReceivedDate;
    @BindView(R.id.check_medicine_received)
    TextView checkMedicineReceived;
    @BindView(R.id.check_medicine_received_date)
    TextView checkMedicineReceivedDate;
    @BindView(R.id.check_other_received)
    TextView checkOtherReceived;
    @BindView(R.id.check_other_received_date)
    TextView checkOtherReceivedDate;
    @BindView(R.id.check_food_expected)
    TextView checkFoodExpected;
    @BindView(R.id.check_food_expected_date)
    TextView checkFoodExpectedDate;
    @BindView(R.id.check_clothes_expected)
    TextView checkClothesExpected;
    @BindView(R.id.check_clothes_expected_date)
    TextView checkClothesExpectedDate;
    @BindView(R.id.check_medicine_expected)
    TextView checkMedicineExpected;
    @BindView(R.id.check_medicine_expected_date)
    TextView checkMedicineExpectedDate;
    @BindView(R.id.check_other_expected)
    TextView checkOtherExpected;
    @BindView(R.id.check_other_expected_date)
    TextView checkOtherExpectedDate;
    private DatabaseReference myRef;
    private DatabaseReference mDatabase;
    private ChildEventListener ref;

    private DatabaseReference myRef1;
    private DatabaseReference mDatabase1;
    private ChildEventListener ref1;

    private DatabaseReference myRef2;
    private DatabaseReference mDatabase2;
    private ChildEventListener ref2;

    Feedback model;
    UserStatus model1;
    Request model2;

    private List<AffectedArea> mArea;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket1, container, false);
        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        myRef1 = database1.getReference("userStatus");
        mDatabase1 = FirebaseDatabase.getInstance().getReference();
        ref1 = myRef1.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model1 = dataSnapshot.getValue(UserStatus.class);
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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("feedback");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model = dataSnapshot.getValue(Feedback.class);
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model1.getId())) {
                            if (model.getId().equals(model1.getId())) {
                                if (model.getFood().equals("true")) {
                                    checkFoodExpectedDate.setText(model.getTimestamp());
                                    cardFoodExpected.setVisibility(View.VISIBLE);
                                }
                                if (model.getClothes().equals("true")) {
                                    checkClothesExpectedDate.setText(model.getTimestamp());
                                    cardClothesExpected.setVisibility(View.VISIBLE);
                                }
                                if (model.getMedicine().equals("true")) {
                                    checkMedicineExpectedDate.setText(model.getTimestamp());
                                    cardMedicineExpected.setVisibility(View.VISIBLE);
                                }
                                if (!model.getOthers().equals("false")) {
                                    checkOtherExpectedDate.setText(model.getTimestamp());
                                    cardOtherExpected.setVisibility(View.VISIBLE);
                                }


                                if (model2.getStatus().equals("requested")) {
                                    if (model2.getFood().equals("true")) {
                                        checkFoodReceivedDate.setText(model2.getDate());
                                        cardFoodReceived.setVisibility(View.VISIBLE);
                                    }
                                    if (model2.getClothes().equals("true")) {
                                        checkClothesReceivedDate.setText(model2.getDate());

                                        cardClothesReceived.setVisibility(View.VISIBLE);
                                    }
                                    if (model2.getMedicine().equals("true")) {
                                        checkMedicineReceivedDate.setText(model2.getDate());
                                        cardMedicineReceived.setVisibility(View.VISIBLE);
                                    }
                                    if (!model2.getOthers().equals("false")) {
                                        checkOtherReceivedDate.setText(model2.getDate());
                                        cardOtherReceived.setVisibility(View.VISIBLE);
                                    }
                                }
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

        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        myRef2 = database2.getReference("request");
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        ref2 = myRef2.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model2 = dataSnapshot.getValue(Request.class);
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model1.getId())) {
                            if (model2.getId().equals(model1.getId())) {
                                if (model2.getStatus().equals("requested")) {
                                    if (model2.getFood().equals("true")) {
                                        checkFoodReceivedDate.setText(model2.getDate());
                                        cardFoodReceived.setVisibility(View.VISIBLE);
                                    }
                                    if (model2.getClothes().equals("true")) {
                                        checkClothesReceivedDate.setText(model2.getDate());

                                        cardClothesReceived.setVisibility(View.VISIBLE);
                                    }
                                    if (model2.getMedicine().equals("true")) {
                                        checkMedicineReceivedDate.setText(model2.getDate());
                                        cardMedicineReceived.setVisibility(View.VISIBLE);
                                    }
                                    if (!model2.getOthers().equals("false")) {
                                        checkOtherReceivedDate.setText(model2.getDate());
                                        cardOtherReceived.setVisibility(View.VISIBLE);
                                    }


                                    if (model.getFood().equals("true")) {
                                        checkFoodExpectedDate.setText(model.getTimestamp());
                                        cardFoodExpected.setVisibility(View.VISIBLE);
                                    }
                                    if (model.getClothes().equals("true")) {
                                        checkClothesExpectedDate.setText(model.getTimestamp());
                                        cardClothesExpected.setVisibility(View.VISIBLE);
                                    }
                                    if (model.getMedicine().equals("true")) {
                                        checkMedicineExpectedDate.setText(model.getTimestamp());
                                        cardMedicineExpected.setVisibility(View.VISIBLE);
                                    }
                                    if (!model.getOthers().equals("false")) {
                                        checkOtherExpectedDate.setText(model.getTimestamp());
                                        cardOtherExpected.setVisibility(View.VISIBLE);
                                    }
                                }
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


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}