package youilab.main;

import MVP.Interfaces.LocationView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Date;

public class GPS {
    static final int COARSE_LOCATION_PERMISSION = 2;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    static final int FINE_LOCATION_PERMISSION = 1;
    private static final long MIN_DISPLACEMENT_IN_METERS = 0;//metros minimos de desplazamiento del usuario para actualizar la ubicación
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;//cada 10 segundos es la actualización de la ubicación
    private Context context;
    private Activity activityCompat;
    private Location mCurrentLocation = null;
    private FusedLocationProviderClient mFusedLocationClient;
    private String mLastUpdateTime;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Boolean mRequestingLocationUpdates;
    private SettingsClient mSettingsClient;
    private LocationView locationView=null;

    /**
     * Constructor en caso de que se pida desde un servicio en segundo plano
     * @param paramContext contexto del servicio
     * @param activityCompat
     */
    public GPS(Context paramContext, Activity activityCompat)
    {
        this.context = paramContext;
        this.activityCompat = activityCompat;

        init();
    }

    /**
     * Constructor en caso de que se pida desde una actividad con una implementación de la clase LocationView
     * @param paramContext
     * @param activityCompat
     * @param locationView
     */
    public GPS(Context paramContext, Activity activityCompat, LocationView locationView)
    {
        this.context = paramContext;
        this.activityCompat = activityCompat;
        this.locationView = locationView;
        init();
    }

    /**
     * Inicializar el servicio de localización
     */
    private void init()
    {
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context);
        this.mSettingsClient = LocationServices.getSettingsClient(this.context);

        this.mLocationCallback = new LocationCallback()
        {
            public void onLocationResult(LocationResult locationResult)
            {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                locationView.currentLocation(mCurrentLocation);
            }
        };
        this.mRequestingLocationUpdates =false;

        //configuración del servicio de localización
        this.mLocationRequest = new LocationRequest();
        this.mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        this.mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.mLocationRequest.setSmallestDisplacement(MIN_DISPLACEMENT_IN_METERS);

        LocationSettingsRequest.Builder localBuilder = new LocationSettingsRequest.Builder();
        localBuilder.addLocationRequest(this.mLocationRequest);
        this.mLocationSettingsRequest = localBuilder.build();
    }


    /**
     * Consulta de tu localización más reciente
     * @return Location type of current location
     */
    public Location getCurrentLocation()
    {
        if (this.mCurrentLocation != null)
        {
            Location localLocation = new Location(Context.LOCATION_SERVICE);
            localLocation.setLatitude(mCurrentLocation.getLatitude());
            localLocation.setLongitude(mCurrentLocation.getLongitude());
            localLocation.setAccuracy(mCurrentLocation.getAccuracy());
            localLocation.setAltitude(mCurrentLocation.getAltitude());
            this.mCurrentLocation = localLocation;
            return this.mCurrentLocation;
        }else
            return null;
    }

    /**
     *Inicia la obtención de la localización gps
     */
    public void startGPSLocation() {
        this.mRequestingLocationUpdates = true;
        startLocationUpdates();
    }
    /**
     * Starting location updates
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//en caso de que sea version m en adelante el sistema del dispositivo
                            if (activityCompat.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && activityCompat.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                //requestPermissions();
                                Log.i("PERMISOS","Se pide permiso");
                                return;
                            }
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());

                        if(locationView!=null&&mCurrentLocation!=null)
                            locationView.currentLocation(mCurrentLocation);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();

                        e.printStackTrace();
                        switch (statusCode)
                        {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try{
                                    //Show the dialog by calling startResolutionForResult(), and check the
                                    //result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(activityCompat,REQUEST_CHECK_SETTINGS);
                                }catch (IntentSender.SendIntentException loc)
                                {
                                    loc.printStackTrace();
                                }
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in settings";
                        }
                    }
                });
    }

    /**
     *Detiene la obtención de datos gps si esta activo
     */
    public void stopGPSLocation() {
        if (this.mRequestingLocationUpdates) {
            this.mRequestingLocationUpdates = false;
            stopLocationUpdates();
        }
    }
    /**
     * Removing location updates
     */
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }





}
