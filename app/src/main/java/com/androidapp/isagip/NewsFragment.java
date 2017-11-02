package com.androidapp.isagip;

import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidapp.isagip.model.AffectedArea;
import com.androidapp.isagip.model.Operation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DatabaseReference databaseReference;
    private List<Operation> operations;
    AffectedArea model2;
    private DatabaseReference mDatabase2;
    private DatabaseReference myRef2;
    private ChildEventListener ref2;

    double latitude;
    double longitude;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        operations = new ArrayList<Operation>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        getAllOperations(dataSnapshot);

                    } catch (Exception ex) {
                        Log.e("RAWR", ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                getAllOperations(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                taskDeletion(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        myRef2 = database2.getReference("affected_areas");
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        ref2 = myRef2.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        model2 = dataSnapshot.getValue(AffectedArea.class);
                    } catch (Exception ex) {
                        Log.e("RAWR", ex.getMessage());
                    }
                } else {
                    Toast.makeText(getContext(), "No Affected Areas", Toast.LENGTH_SHORT).show();
                    android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
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
        return view;
    }

    private void getAllOperations(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            Operation operation = singleSnapshot.getValue(Operation.class);
            if (operation.getStatus().equals("active")) {
                if (checkDistance(currentLatitude, currentLongitude) < 5) {
                    operations.add(operation);
                    recyclerViewAdapter = new RecyclerViewAdapter(this.getContext(), operations);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());
                    mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                    mBuilder.setContentTitle("ISagip News");
                    mBuilder.setContentText("There are operations on-going!");
                    NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

//                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                mBuilder.setSound(alarmSound);
//                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
//                mBuilder.setVibrate(pattern);

                    // notificationID allows you to update the notification later on.
                    mNotificationManager.notify(1, mBuilder.build());
                } else {
                    Toast.makeText(getContext(), "No Nearby Calamity", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public double checkDistance(double latitude, double longitude) {

        Location startPoint = new Location("locationA");
        startPoint.setLatitude(latitude);
        startPoint.setLongitude(longitude);

        Location endPoint = new Location("locationA");
        endPoint.setLatitude(model2.getLatitude());
        endPoint.setLongitude(model2.getLongitude());

        double distance = startPoint.distanceTo(endPoint) / 1000;
        return distance;
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
//        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }


}
