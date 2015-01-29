package com.example.stockexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() throws RuntimeException {
        LatLng USA = new LatLng(39.50, -96.75);
        addMarkersToMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(USA, 3));
    }

    private void addMarkersToMap() {
        Intent intent = getIntent();
        ArrayList companyList = intent.getParcelableArrayListExtra("CompanyList");
        ArrayList<Marker> mMarkers = new ArrayList<Marker>();
        mMap.clear();

        for (int i = 0; i < companyList.size(); i++) {
            MyCompany company = (MyCompany) companyList.get(i);
            String symbol = company.getCompanySymbol();
            switch (symbol) {
                case "AAPL":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.331789, -122.029620)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.331789, -122.029620)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.331789, -122.029620)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                case "CVX":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.758251, -121.958097)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.758251, -121.958097)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.758251, -121.958097)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                case "GOOG":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.422441, -122.084286)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.422441, -122.084286)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.422441, -122.084286)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                case "MMM":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(44.952414, -92.994121)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(44.952414, -92.994121)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(44.952414, -92.994121)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                case "PG":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(39.102772, -84.505778)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(39.102772, -84.505778)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(39.102772, -84.505778)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                case "AA":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(40.759649, -73.972918)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(40.759649, -73.972918)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(40.759649, -73.972918)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                case "LL":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.413458, -76.810874)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.413458, -76.810874)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.413458, -76.810874)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                case "YHOO":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.418820, -122.025712)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.418820, -122.025712)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(37.418820, -122.025712)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                case "MSFT":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(47.639583, -122.128381)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(47.639583, -122.128381)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(47.639583, -122.128381)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                case "SNE":
                    if (company.getCompanyStock().charAt(0) == '+')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(35.628397, 139.746562)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    else if (company.getCompanyStock().charAt(0) == '-')
                        mMap.addMarker(new MarkerOptions().position(new LatLng(35.628397, 139.746562)).title(company.getCompanyName() + " HQ"));
                    else
                        mMap.addMarker(new MarkerOptions().position(new LatLng(35.628397, 139.746562)).title(company.getCompanyName() + " HQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    break;
                default:
                    System.out.println("ERROR");
                    break;
            }
        }
    }


}
