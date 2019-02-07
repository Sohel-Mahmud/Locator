package com.example.sohel.locator.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.sohel.locator.Model.User;
import com.example.sohel.locator.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowAdapter(Context ctx)
    {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.map_custom_infowindow,null);

        TextView firstName = view.findViewById(R.id.txtfirstname);
        TextView lastName = view.findViewById(R.id.txtLastName);
        TextView email = view.findViewById(R.id.txtemail);
        TextView postkey = view.findViewById(R.id.txtpostkey);

        User userData = (User)marker.getTag();
        firstName.setText(userData.getFirstName());
        lastName.setText(userData.getLastName());
        email.setText(userData.getEmail());
        postkey.setText(userData.getPostKey());
        return view;
    }
}
