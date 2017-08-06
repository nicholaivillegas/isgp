package com.androidapp.isagip;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidapp.isagip.model.Request;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.text_food)
    TextView textFood;
    @BindView(R.id.text_clothes)
    TextView textClothes;
    @BindView(R.id.text_medicine)
    TextView textMedicine;
    @BindView(R.id.text_other)
    TextView textOther;
    @BindView(R.id.text_total)
    TextView textTotal;
    @BindView(R.id.cardView)
    CardView cardView;
    private GoogleMap mMap;
    private DatabaseReference myRef;
    private DatabaseReference mDatabase;
    private ChildEventListener ref;
    private double foodCounter = 0;
    private double clothesCounter = 0;
    private double medicineCounter = 0;
    private double otherCounter = 0;
    private int counter = 0;

    private double totalFood = 0;
    private double totalClothes = 0;
    private double totalMedicine = 0;
    private double totalOther = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);


        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(12.8797, 121.7740));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(4);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

// ...
        // real time database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Todo: change to chats after testing
        myRef = database.getReference("request");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        Request model = dataSnapshot.getValue(Request.class);
                        LatLng marker = new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()));
                        mMap.addMarker(new MarkerOptions().position(marker).title(model.getLocation()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                        if (model.getFood().equals("true")) {
                            foodCounter++;
                        }
                        if (model.getClothes().equals("true")) {
                            clothesCounter++;
                        }
                        if (model.getMedicine().equals("true")) {
                            medicineCounter++;
                        }
                        if (model.getOthers().equals("true")) {
                            otherCounter++;
                        }
                        counter++;
                        computePercentage();
                        Toast.makeText(MapsActivity.this, "SOMEONE NEEDS HELP!", Toast.LENGTH_SHORT).show();
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void computePercentage() {
        totalFood = (foodCounter / counter) * 100;
        totalClothes = (clothesCounter / counter) * 100;
        totalMedicine = (medicineCounter / counter) * 100;
        totalOther = (otherCounter / counter) * 100;

        textFood.setText(totalFood + "%");
        textClothes.setText(totalClothes + "%");
        textMedicine.setText(totalMedicine + "%");
        textOther.setText(totalOther + "%");
        textTotal.setText(counter + "");
    }

    @OnClick({R.id.text_food, R.id.text_clothes, R.id.text_medicine, R.id.text_other, R.id.text_total})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_food:
                Toast.makeText(this, "Food", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_clothes:
                Toast.makeText(this, "Clothes", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_medicine:
                Toast.makeText(this, "Medicine", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_other:
                Toast.makeText(this, "Others", Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_total:
                Toast.makeText(this, "Total Requests", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
