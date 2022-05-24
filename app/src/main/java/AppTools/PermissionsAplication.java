package AppTools;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class PermissionsAplication {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static boolean bndLocation=false;
    private static boolean bndWriteES = false;
    private static boolean bndCamera = false;


    private static void checkPermission(Activity paramActivity, String paramString, int paramInt)
    {
        if (ContextCompat.checkSelfPermission(paramActivity,paramString) != PackageManager.PERMISSION_GRANTED)
        {
           /* if (ActivityCompat.shouldShowRequestPermissionRationale(paramActivity, paramString)) {

            }else
            {*/
                ActivityCompat.requestPermissions(paramActivity, new String[] { paramString }, paramInt);
            //}
        }
        else
        {
            if (paramInt == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
                bndWriteES = true;
            }
            if (paramInt == MY_PERMISSIONS_REQUEST_LOCATION) {
                bndLocation = true;
            }

            if (paramInt == MY_PERMISSIONS_REQUEST_CAMERA) {
                bndCamera = true;
            }
        }

        return;
    }


    /**
     * metodo para verificar el permiso de ubicación
     * @param activity actividad de donde se esta llamando al metodo
     * @return estado del permiso
     */
    public static boolean accesFineLocationPermission(Activity activity)
    {
        bndLocation = false;
        checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION,MY_PERMISSIONS_REQUEST_LOCATION);

        return bndLocation;
    }

    /**
     * metodo para verificar el permiso de escritura de archivos
     * @param activity actividad de donde se esta llamando al metodo
     * @return estado del permiso
     */
    public static boolean accessWriteESPermission(Activity activity)
    {
        bndWriteES = false;
        checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        return bndWriteES;
    }


    /**
     * metodo para verificar el permiso de camara
     * @param activity actividad de donde se esta llamando al metodo
     * @return estado del permiso
     */
    public static boolean accessCameraPermission(Activity activity)
    {
        bndCamera = false;
        checkPermission(activity, Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA);
        return bndCamera;
    }


}
