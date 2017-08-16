package com.androidapp.isagip;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidapp.isagip.model.Feedback;
import com.androidapp.isagip.model.Operation;
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
    @BindView(R.id.spinner)
    Spinner spinner;
    private GoogleMap mMap;
    private DatabaseReference myRef;
    private DatabaseReference mDatabase;
    private ChildEventListener ref;
    private DatabaseReference myRef1;
    private DatabaseReference mDatabase1;
    private ChildEventListener ref1;
    private double foodCounter = 0;
    private double clothesCounter = 0;
    private double medicineCounter = 0;
    private double otherCounter = 0;
    private int counter = 0;

    private double totalFood = 0;
    private double totalClothes = 0;
    private double totalMedicine = 0;
    private double totalOther = 0;

    Operation model;
    FirebaseDatabase database;

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


        // real time database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("request");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        Request model = dataSnapshot.getValue(Request.class);
                        if (model.getStatus().equals("requested")) {
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
                            if (!model.getOthers().equals("false")) {
                                otherCounter++;
                            }
                            counter++;
                            computePercentage();
                            Toast.makeText(MapsActivity.this, "SOMEONE NEEDS HELP!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Log.e("RAWR", ex.getMessage());
                    }
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(6.0f);
                    mMap.animateCamera(zoom);
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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(MapsActivity.this, "All", Toast.LENGTH_SHORT).show();
                    getAllOperations();
                } else if (position == 1) {
                    Toast.makeText(MapsActivity.this, "Fire", Toast.LENGTH_SHORT).show();
                    focusToOperation("Fire");
                } else if (position == 2) {
                    Toast.makeText(MapsActivity.this, "Typhoon", Toast.LENGTH_SHORT).show();
                    focusToOperation("Typhoon");
                } else if (position == 3) {
                    Toast.makeText(MapsActivity.this, "Earthquake", Toast.LENGTH_SHORT).show();
                    focusToOperation("Earthquake");
                } else if (position == 4) {
                    Toast.makeText(MapsActivity.this, "Others", Toast.LENGTH_SHORT).show();
                    focusToOperation("Others");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getAllOperations();

            }
        });
    }

    public void focusToOperation(final String operation) {
        myRef1 = database.getReference("operations");
        mDatabase1 = FirebaseDatabase.getInstance().getReference();
        ref1 = myRef1.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model = dataSnapshot.getValue(Operation.class);
                        if (model.getTitle().equalsIgnoreCase(operation) && model.getStatus().equals("active")) {
                            Location loc = new Location("");
                            loc.setLatitude(model.getLatitude());
                            loc.setLongitude(model.getLongitude());
                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(model.getLatitude(), model.getLongitude()));
                            mMap.moveCamera(center);
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(8.0f);
                            mMap.animateCamera(zoom);
                        } else {
                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(12.879721, 121.774017));
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(5.0f);

                            mMap.moveCamera(center);
                            mMap.animateCamera(zoom);
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

    public void getAllOperations() {
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(12.879721, 121.774017));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(5.0f);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        myRef1 = database.getReference("operations");
        mDatabase1 = FirebaseDatabase.getInstance().getReference();
        ref1 = myRef1.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        if (model.getStatus().equals("active")) {
                            model = dataSnapshot.getValue(Operation.class);
                            Location loc = new Location("");
                            loc.setLatitude(model.getLatitude());
                            loc.setLongitude(model.getLongitude());
                        }
                    } catch (Exception ex) {
                        Log.e("RAWR", ex.getMessage());
                    }
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(12.879721, 121.774017));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(5.0f);

                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
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

        textFood.setText(String.format("%.2f", totalFood) + "%");
        textClothes.setText(String.format("%.2f", totalClothes) + "%");
        textMedicine.setText(String.format("%.2f", totalMedicine) + "%");
        textOther.setText(String.format("%.2f", totalOther) + "%");
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
