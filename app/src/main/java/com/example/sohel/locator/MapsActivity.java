package com.example.sohel.locator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sohel.locator.Model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener,OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mMapView;
    private Marker marker;
    private DatabaseReference mDatabase;
    private List<User> userList;
    private String firstName;
    private String lastName;
    private String email;
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        userList = new ArrayList<>();

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
        final ArrayList<String> statesArrayList= new ArrayList<>();

        MapsInitializer.initialize(getApplicationContext());
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    String postKey = postSnapshot.getKey();
                    user.setPostKey(postKey);
                    statesArrayList.add(postKey);
                    userList.add(user);
                }
//                for (int i = 0; i < userList.size(); i++) {
//                    Log.d("name", userList.get(i).getFirstName());
//                    Log.d("email", userList.get(i).getEmail());
//
//                }
                for (int i = 0; i < userList.size(); i++) {


                    LatLng latLng = new LatLng(userList.get(i).getLatitude(), userList.get(i).getLongitude());
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title(userList.get(i).getFirstName() + " " + userList.get(i).getLastName()).snippet(userList.get(i).getPostKey()));

                    if(i==userList.size()-1)
                    {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(userList.get(i-1).getLatitude(), userList.get(i-1).getLongitude()))
                                .zoom(4)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }

                    marker.showInfoWindow();
                    final int finalI = i;
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

//                            final String firstName = userList.get(finalI).getFirstName();
//                            final String lastName = userList.get(finalI).getLastName();
//                            final String email = userList.get(finalI).getEmail();
//                            final String postKey = statesArrayList.get(finalI);
                            String postKey = marker.getSnippet();
                            alertDialogBuilder(postKey);
                            Toast.makeText(MapsActivity.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void alertDialogBuilder(String postKey) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("User Info");

        LayoutInflater inflater = LayoutInflater.from(this);
        View infoWindow = inflater.inflate(R.layout.layout_infowindow,null);

        final EditText edtFirstName = infoWindow.findViewById(R.id.edtFirstName);
        final EditText edtLastName = infoWindow.findViewById(R.id.edtLastName);
        final EditText edtEmail = infoWindow.findViewById(R.id.edtEmail);
        final EditText edtlatitude = infoWindow.findViewById(R.id.edtLatitude);
        final EditText edtlongitude = infoWindow.findViewById(R.id.edtLongitude);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(postKey);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                firstName = user.getFirstName();
                lastName = user.getLastName();
                email = user.getEmail();
                latitude = user.getLatitude();
                longitude = user.getLongitude();

                    edtFirstName.setText(firstName);
                    edtLastName.setText(lastName);
                    edtEmail.setText(email);
                    edtlatitude.setText(latitude.toString());
                    edtlongitude.setText(longitude.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dialog.setView(infoWindow);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                firstName = edtFirstName.getText().toString();
                lastName = edtLastName.getText().toString();
                email = edtEmail.getText().toString();
                latitude = Double.parseDouble(edtlatitude.getText().toString());
                longitude = Double.parseDouble(edtlongitude.getText().toString());

                User newUser = new User(firstName,lastName,email,latitude,longitude);
                databaseReference.setValue(newUser);
                Toast.makeText(MapsActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                mMap.clear();
                startActivity(new Intent(MapsActivity.this, MapsActivity.class));
                onMapReady(mMap);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.removeValue();
                Toast.makeText(MapsActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                mMap.clear();
                startActivity(new Intent(MapsActivity.this, MapsActivity.class));
                onMapReady(mMap);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
