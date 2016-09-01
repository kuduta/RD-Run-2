package rd.nakorn.rdrun;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;


public class ServiceActivity extends FragmentActivity implements OnMapReadyCallback {

    //Explicit
    private GoogleMap mMap;
    private String idString, avataString, nameString, surnameString;
    private ImageView imageView;
    private TextView nameTextView, surnameTextView;
    private int[] avataInts;
    private double userLatADouble = 13.8067437, userLngADouble = 100.5747341;
    private LocationManager locationManager;  //service find location
    private Criteria criteria; // 3D location


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_service);

        //bind widget

        imageView = (ImageView) findViewById(R.id.imageView7);
        nameTextView = (TextView) findViewById(R.id.textView8);
        surnameTextView = (TextView) findViewById(R.id.textView9);

        //show text

        nameTextView.setText(nameString);
        surnameTextView.setText(surnameString);

        //get value from Intent
        idString = getIntent().getStringExtra("id");
        avataString = getIntent().getStringExtra("Avata");
        nameString = getIntent().getStringExtra("Name");
        surnameString = getIntent().getStringExtra("Surname");

        //Setup location XXXXX Context pipe
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);


        //show avata
        MyConstant myConstant = new MyConstant();
        avataInts = myConstant.getAvataInts();
        imageView.setImageResource(avataInts[Integer.parseInt(avataString)]);




        //show text




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    } // main Method

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.removeUpdates(locationListener);
        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER); //find location by ISP

        if (networkLocation != null) {

            userLngADouble = networkLocation.getLongitude();
            userLatADouble = networkLocation.getLatitude();

        }

        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);  //find location by GPS
        if (gpsLocation != null) {

            userLngADouble = networkLocation.getLongitude();
            userLatADouble = networkLocation.getLatitude();
        }




    } // onResume

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.removeUpdates(locationListener);


    }

    public Location myFindLocation(String strProvider) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {

            // Setting LocationManager 1000 is time , 10 is distance
            locationManager.requestLocationUpdates(strProvider,1000, 10,locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {

            Log.d("1SepV1", "Cannot find location");

        }

        return location;
    }



    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            userLatADouble = location.getLatitude();
            userLngADouble = location.getLongitude();


        }// onLocationChanged

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //setup center of map
        LatLng latLng = new LatLng(userLatADouble,userLngADouble);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));


        //Loop
        myLoop();

    }//onMap

    private void myLoop() {

        Log.d("1SepV2", "Lat ==>  " + userLatADouble);
        Log.d("1SepV2", "Lng ==>  " + userLngADouble);

        // Post Delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                myLoop();

            }
        },1000);

    }//myloop
}//main class
