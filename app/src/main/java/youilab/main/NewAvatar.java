package youilab.main;


import MVP.Interfaces.NewAvatar.AvatarsPresenter;
import MVP.Interfaces.NewAvatar.NewAvatarView;
import MVP.Presenters.AvatarsPresenterImpl;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;

import java.util.regex.Pattern;

public class NewAvatar extends AppCompatActivity implements NewAvatarView {

    final static int PICK_IMAGE =0;
    private EditText txtProfileName;
    private CardView cvNewProfile;
    private CardView cvLoadImage;
    private ImageView imgProfile;

    private String UriImgProfile=null;

    private AvatarsPresenter avatarsPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_avatar);


        txtProfileName = (EditText) findViewById(R.id.txtAvatarName);

        txtProfileName.addTextChangedListener(new TextWatcher() {
            boolean bndAceptabletext=true;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bndAceptabletext= Pattern.matches("^[a-zA-Z0-9]*$", charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!bndAceptabletext || editable.length()>10)
                    editable.delete(editable.length()-1,editable.length());
            }
        });
        imgProfile = (ImageView) findViewById(R.id.imgNewProfile);



        cvNewProfile = (CardView) findViewById(R.id.btnSaveProfile);
        cvNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtProfileName.getText().toString().compareTo("")!=0 && UriImgProfile != null)
                {
                    avatarsPresenter.NewAvatar(txtProfileName.getText().toString(),UriImgProfile);
                }else
                {
                    Toast.makeText(NewAvatar.this,"Datos faltantes",Toast.LENGTH_LONG).show();
                }
            }
        });

        cvLoadImage = (CardView) findViewById(R.id.cvAvatarImage);
        cvLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String action;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    action = Intent.ACTION_OPEN_DOCUMENT;
                } else {
                    action = Intent.ACTION_PICK;
                }
               // Intent intent = new Intent(action, uri);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(action);
                startActivityForResult(Intent.createChooser(intent, "Selecciona foto de perfil"), PICK_IMAGE);
            }
        });

        avatarsPresenter = new AvatarsPresenterImpl(this, getApplicationContext());
    }


    /**
     * REsultado de escoger una imagen de la galeria del dispositivo
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            Log.i("imagen code", String.valueOf(resultCode));
            if (resultCode == -1) {
                Uri selectedImage = data.getData();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)//pedir el permisos para leer archivos internos en el dispositivo desde la version kitkat
                    getApplicationContext().getContentResolver().takePersistableUriPermission(selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                UriImgProfile = data.getDataString();
                Glide.with(this).load(selectedImage).into(imgProfile);//se muestra la imagen selecionada
                //TODO: action
            }
        }
    }

    /**
     * Resulatdo de si el usuario dio o no el permiso de buscar imagenes en el dispositivo
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Intent.FLAG_GRANT_READ_URI_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //reload my activity with permission granted or use the features that required the permission

            } else {
                Toast.makeText(getApplicationContext(), "Sin permiso", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * Abre la actividad principal y termina la pantalla del nuevo avatar
     */
    @Override
    public void GotoHome() {
        Intent intentToHome = new Intent(this,NavigationActivity.class);
        startActivity(intentToHome);
        finish();
    }


}
