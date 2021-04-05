/**
 *  Matric Number - s1920031
 */
package com.example.ocallaghan_james_s1920031;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class eq_gps_map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq_gps_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
    * DISCLOSURE THIS IS PARTIAL GOOGLE MAPS IMPORT ONLY PART
     * ADD WAS ABILITY TO PLACE MARKERS AND HAVE THEM COLOUR CODED
    */

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng eq_marker = new LatLng(ApplicationClass.s_lat, ApplicationClass.s_lng);
        MarkerOptions newOp = new MarkerOptions().position(eq_marker).title("Marker for EQ location");
        newOp.icon(BitmapDescriptorFactory.defaultMarker(ApplicationClass.s_magChange));
        mMap.addMarker(newOp);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eq_marker));
    }
    public final GoogleMap getmMap() {
        return mMap;
    }
}