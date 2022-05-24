package youilab.main;

import AppTools.PermissionsAplication;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import static AppTools.PermissionsAplication.MY_PERMISSIONS_REQUEST_LOCATION;

public class PermissionLocation extends AppCompatActivity {

     boolean bndLocationPermission=false;

    CardView cvLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_location);


        cvLocation = findViewById(R.id.cvLocation);
        cvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionsAplication.accesFineLocationPermission(PermissionLocation.this)) {
                    cvLocation.setCardBackgroundColor(getResources().getColor(R.color.green));
                    bndLocationPermission = true;
                }
                checkManufacturer();
            }
        });




    }


    /**
     * Resulatdo de si el usuario dio o no el permiso de localización
     * @param paramInt codigo que regresa el permiso
     * @param permissions arreglo de permisos
     * @param paramArrayOfInt
     */
    @Override
    public void onRequestPermissionsResult(int paramInt, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        if (paramInt == MY_PERMISSIONS_REQUEST_LOCATION) {
            if ((paramArrayOfInt.length > 0) && (paramArrayOfInt[0] == PackageManager.PERMISSION_GRANTED)) {
                bndLocationPermission = true;
                cvLocation.setCardBackgroundColor(getResources().getColor(R.color.green));
            }
        } else {
            return;
        }

        if(bndLocationPermission)//si el permiso de localización esta aceptado se checa la lista de marca
        {
            checkManufacturer();
        }
    }

    /**
     * Cambio dek color del status bar
     */

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }


    /**
     *Verificar si el permiso de localización es dado y si la marca del dispositivo necesita alguna configuración adicional
     */
    private void checkManufacturer() {

        if(bndLocationPermission)//si el permiso de localización esta aceptado se checa la lista de marca
        {
            String telefonoMarca = Build.MANUFACTURER;//se obtiene la marca del dispositivo

            if (checkManufacturerExceptions(telefonoMarca))//si esta entre la lista se pasa a la configuración adicional
            {
                Intent intentToFirstAvatar = new Intent(this, ManufacturerException.class);
                startActivity(intentToFirstAvatar);
                finish();
            } else {//si no esta en la lista se para a registrar el prime avatar

                Intent intentToFirstAvatar = new Intent(this, NewAvatar.class);
                startActivity(intentToFirstAvatar);
                finish();
            }

        }
    }

    /**
     * Comparación de la marca del dispositivo con la lista de dispositivos que necesitan una configuración extra
     * @param marca string con la marque del dispositivo
     * @return el valor de si pertenece a alguna de las marcas con configuraciónes adicionales
     */
    private boolean checkManufacturerExceptions(String marca){
        boolean bndManufacturer = false;

        if ("huawei".equalsIgnoreCase(marca)) {
            bndManufacturer = true;
        } else if ("xiaomi".equalsIgnoreCase(marca)) {
            bndManufacturer = true;
        } else if ("oppo".equalsIgnoreCase(marca)) {
            bndManufacturer = true;
        } else if ("vivo".equalsIgnoreCase(marca)) {
            bndManufacturer = true;
        } else if ("Letv".equalsIgnoreCase(marca)) {
            bndManufacturer = true;
        } else if ("Honor".equalsIgnoreCase(marca)) {
            bndManufacturer = true;
        }

        return bndManufacturer;
    }

}
