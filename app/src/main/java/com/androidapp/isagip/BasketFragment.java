package com.androidapp.isagip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidapp.isagip.model.AffectedArea;
import com.androidapp.isagip.model.Feedback;
import com.androidapp.isagip.model.Request;
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

    @BindView(R.id.check_food_expected)
    CheckBox checkFoodExpected;
    @BindView(R.id.card_food_expected)
    CardView cardFoodExpected;
    @BindView(R.id.check_clothes_expected)
    CheckBox checkClothesExpected;
    @BindView(R.id.card_clothes_expected)
    CardView cardClothesExpected;
    @BindView(R.id.check_medicine_expected)
    CheckBox checkMedicineExpected;
    @BindView(R.id.card_medicine_expected)
    CardView cardMedicineExpected;
    @BindView(R.id.check_other_expected)
    CheckBox checkOtherExpected;
    @BindView(R.id.edit_other_expected)
    EditText editOtherExpected;
    @BindView(R.id.card_other_expected)
    CardView cardOtherExpected;
    @BindView(R.id.check_food_received)
    CheckBox checkFoodReceived;
    @BindView(R.id.text_food_received_date)
    TextView textFoodReceivedDate;
    @BindView(R.id.card_food_received)
    CardView cardFoodReceived;
    @BindView(R.id.check_clothes_received)
    CheckBox checkClothesReceived;
    @BindView(R.id.text_clothes_received_date)
    TextView textClothesReceivedDate;
    @BindView(R.id.card_clothes_received)
    CardView cardClothesReceived;
    @BindView(R.id.check_medicine_received)
    CheckBox checkMedicineReceived;
    @BindView(R.id.text_medicine_received_date)
    TextView textMedicineReceivedDate;
    @BindView(R.id.card_medicine_received)
    CardView cardMedicineReceived;
    @BindView(R.id.check_other_received)
    CheckBox checkOtherReceived;
    @BindView(R.id.text_other_received_date)
    TextView textOtherReceivedDate;
    @BindView(R.id.card_other)
    CardView cardOther;
    Unbinder unbinder;
    private DatabaseReference myRef;
    private DatabaseReference mDatabase;
    private ChildEventListener ref;
    private List<AffectedArea> mArea;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket1, container, false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("feedback");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        Feedback model = dataSnapshot.getValue(Feedback.class);
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getId())) {
                            if (model.getFood().equals("true")) {
                                cardFoodExpected.setVisibility(View.VISIBLE);
                                cardFoodReceived.setVisibility(View.GONE);
                            } else {
                                cardFoodExpected.setVisibility(View.GONE);
                                cardFoodReceived.setVisibility(View.VISIBLE);
                            }
                            if (model.getClothes().equals("true")) {
                                cardClothesExpected.setVisibility(View.VISIBLE);
                                cardClothesReceived.setVisibility(View.GONE);
                            } else {
                                cardClothesExpected.setVisibility(View.GONE);
                                cardClothesReceived.setVisibility(View.VISIBLE);
                            }
                            if (model.getMedicine().equals("true")) {
                                cardMedicineExpected.setVisibility(View.VISIBLE);
                                cardMedicineReceived.setVisibility(View.GONE);
                            } else {
                                cardMedicineExpected.setVisibility(View.GONE);
                                cardMedicineReceived.setVisibility(View.VISIBLE);
                            }
                            if (model.getOthers().equals("true")) {
                                cardOtherExpected.setVisibility(View.VISIBLE);
                                cardOther.setVisibility(View.GONE);
                            } else {
                                cardOtherExpected.setVisibility(View.GONE);
                                cardOther.setVisibility(View.VISIBLE);
                                textOtherReceivedDate.setText(model.getTimestamp());
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