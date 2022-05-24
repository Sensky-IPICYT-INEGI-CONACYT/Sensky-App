package youilab.main;

import AppTools.PermissionsAplication;
import MVP.Interfaces.Configuration.ConfigurationPresenter;
import MVP.Interfaces.Configuration.ConfigurationView;
import MVP.Presenters.ConfigurationPresenterImpl;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import static AppTools.PermissionsAplication.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

public class permissionFiles extends AppCompatActivity implements ConfigurationView {

    boolean bndWriteESPermission=false;

    CardView cvFiles;
    private ConfigurationPresenter configurationPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_files);



        cvFiles = (CardView) findViewById(R.id.cvFiles);
        cvFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionsAplication.accessWriteESPermission(permissionFiles.this)) {
                    cvFiles.setCardBackgroundColor(getResources().getColor(R.color.green));
                    bndWriteESPermission = true;
                }

                checkBnds();
            }
        });

        configurationPresenter = new ConfigurationPresenterImpl(this,getApplicationContext());

    }


    /**
     * Resulatdo de si el usuario dio o no el permiso de escritura y lectura
     * @param paramInt codigo que regresa el permiso
     * @param permissions arreglo de permisos
     * @param paramArrayOfInt
     */
    @Override
    public void onRequestPermissionsResult(int paramInt, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        if (paramInt == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if ((paramArrayOfInt.length > 0) && (paramArrayOfInt[0] == PackageManager.PERMISSION_GRANTED)) {
                bndWriteESPermission = true;
                cvFiles.setCardBackgroundColor(getResources().getColor(R.color.green));
            }
        } else {
            return;
        }

        checkBnds();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //cambiar color de texto del status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    /**
     *Si el permiso de escritura lectura es dado se crean los directorios raiz
     */
    private void checkBnds() {
        if (bndWriteESPermission )
            configurationPresenter.createDirectories();
    }


    /**
     * Si los directorios raiz se crearon se abre la actividad de permiso de localización
     * @param status resultado de la creación de los directorios
     */
    @Override
    public void DirectoryProcessStatus(boolean status) {
        if(status) {
            Intent intentToFirstAvatar = new Intent(this, PermissionLocation.class);
            startActivity(intentToFirstAvatar);
            finish();
        }else
            Toast.makeText(this, "No se crearon los directorios",Toast.LENGTH_SHORT).show();
    }

}
