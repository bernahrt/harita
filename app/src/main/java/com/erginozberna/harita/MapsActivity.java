package com.erginozberna.harita;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.erginozberna.harita.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.util.logging.Logger.global;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    double latitude;
    double longitude;
    LocationListener locationListener;
    Marker markerL;
    LatLng userLastLocation;  // Son konum
    LatLng latLng; // Anlık konum
    LatLng location; // Kasisler
    ArrayList<LatLng> locations = new ArrayList<>();
    ArrayList<Double> distance = new ArrayList<>();
    MediaPlayer sound;
    ArrayList<Double> border = new ArrayList<>();
    double a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {  // Harita hazır olduğunda yapılacak işlemler.
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);


        // Gerçek kasislerin konumu

        // Bahçecik-Yenicuma-Arafilboyu-Esentepe
        locations.add(new LatLng(40.997825, 39.745689));

        locations.add(new LatLng(40.999733, 39.736747));

        locations.add(new LatLng(40.997406, 39.722136));

        locations.add(new LatLng(40.996619, 39.721983));

        locations.add(new LatLng(40.993722, 39.721633));

        locations.add(new LatLng(40.991261, 39.721061));

        locations.add(new LatLng(40.994404, 39.720036));

        locations.add(new LatLng(40.996300, 39.715781));

        locations.add(new LatLng(40.995544, 39.713067));

        locations.add(new LatLng(40.993733, 39.710960));

        // Ktü Giriş
        locations.add(new LatLng(40.998032, 39.764234));

        locations.add(new LatLng(40.997852, 39.766635));

        locations.add(new LatLng(40.997299, 39.768506));

        locations.add(new LatLng(40.994939, 39.767573));

        locations.add(new LatLng(40.995783, 39.767427));

        locations.add(new LatLng(40.997534, 39.769611));

        locations.add(new LatLng(40.997429, 39.771231));

        locations.add(new LatLng(40.997413, 39.771725));

        locations.add(new LatLng(40.995646, 39.773672));

        locations.add(new LatLng(40.994664, 39.774668));

        locations.add(new LatLng(40.993874, 39.775471));

        // Ktü Çıkış
        locations.add(new LatLng(40.993277, 39.776024));

        locations.add(new LatLng(40.993953, 39.775556));

        locations.add(new LatLng(40.995727, 39.773720));

        locations.add(new LatLng(40.997220, 39.772257));

        locations.add(new LatLng(40.997534, 39.771730));

        locations.add(new LatLng(40.997587, 39.770499));

        locations.add(new LatLng(40.997642, 39.769650));

        locations.add(new LatLng(40.997228, 39.768362));

        locations.add(new LatLng(40.995781, 39.767413));

        locations.add(new LatLng(40.994856, 39.766923));

        locations.add(new LatLng(40.997938, 39.766670));

        locations.add(new LatLng(40.998136, 39.764456));

        for (LatLng location : locations) {
            markerL = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }

        for (LatLng location : locations) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(location);
            circleOptions.radius(50);
            circleOptions.strokeColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
            circleOptions.fillColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
            circleOptions.strokeWidth(6);
            mMap.addCircle(circleOptions);
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker markerL) {

                return null;
            }

            @Override
            public View getInfoContents(Marker markerL) {

                View v = getLayoutInflater().inflate(R.layout.info_window, null);
                ImageView r = (ImageView) v.findViewById(R.id.imageView);
                r.setImageResource(R.drawable.kasis);

                return v;
            }
        });

        markerL.showInfoWindow();


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                latLng = new LatLng(location.getLatitude(),location.getLongitude());

                // Mesafe hesabı

               /* for (LatLng k : locations) {
                   double[] distance ={SphericalUtil.computeDistanceBetween(latLng,k)};
                    System.out.println(distance[0]);
                }

*/              // Anlık konumun kasislere olan uzaklığı
               for (LatLng k :locations){
                   distance.add(SphericalUtil.computeDistanceBetween(latLng,k));
                   //System.out.println(distance.get(0));System.out.println(distance.size());
               }

               /*  for (int m =0; m<distance.size(); m++) {
                    a =distance.get(m);
                }
                if (a<=10){
                    sound = MediaPlayer.create(MapsActivity.this,R.raw.sound);
                    sound.start();
                    markerL.showInfoWindow();
                }*/



               for (int m =0; m<distance.size(); m++) {
                    if (distance.get(m)<=50){
                    sound = MediaPlayer.create(MapsActivity.this,R.raw.sound);
                    sound.start();

                    markerL.showInfoWindow();

                    }
                }

                /*  10 'lardan oluşan bir dizi. Distance boyutunda oluşturuldu.
               for (int i =0 ; i<=distance.size(); i++){
                    border.add(10.0);
                }
                System.out.println(border);*/

            }

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

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { //Eğer izin yoksa
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //izin iste
            } else { // Eğer zaten izin verilmişse yani mesela uygulamayı 2. sefer açımızda olacaklar
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //System.out.println("lastLocation: " + lastLocation);
                latitude = lastLocation.getLatitude();
                longitude =lastLocation.getLongitude();
                userLastLocation = new LatLng(latitude,longitude);
                mMap.setMyLocationEnabled(true); // Anlık konum alma,mavi icon gözüküyor

               // mMap.addMarker(new MarkerOptions().title("Your Location").position(userLastLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation, 15));

            }
        } else { // Eğer 23 den küçükse bunu yap.İzin sormaz.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng userLastLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation, 15));
        }

    }

    @Override // Kullanıcı ilk defa izin verdiğinde yapılacaklar. Uygulamayı ilk defa çalıştırdığımızda devreye girer.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0) {
            if (requestCode == 1) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    latitude = lastLocation.getLatitude();
                    longitude =lastLocation.getLongitude();
                    userLastLocation = new LatLng(latitude,longitude);
                    mMap.setMyLocationEnabled(true);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation, 15));
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}