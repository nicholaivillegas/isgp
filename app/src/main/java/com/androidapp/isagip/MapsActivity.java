package com.androidapp.isagip;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.androidapp.isagip.model.AffectedArea;
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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference myRef;
    private DatabaseReference mDatabase;
    private ChildEventListener ref;
    private List<AffectedArea> mArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        myRef = database.getReference("affected_areas");
//        myRef = database.getReference(selectedStation);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = myRef.addChildEventListener(new ChildEventListener() {

            // This function is called once for each child that exists
            // when the listener is added. Then it is called
            // each time a new child is added.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        AffectedArea model = dataSnapshot.getValue(AffectedArea.class);
                        LatLng marker = new LatLng(model.getLatitude(), model.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(marker).title(model.getLocation()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                        Toast.makeText(MapsActivity.this, model.getLatitude().toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
