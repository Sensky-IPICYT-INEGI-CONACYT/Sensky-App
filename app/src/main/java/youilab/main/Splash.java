package youilab.main;

import MVP.Interfaces.Splash.SplashPresenter;
import MVP.Interfaces.Splash.SplashView;
import MVP.Presenters.SplashPresenterImpl;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity implements SplashView {

    private ImageView iv;
    SplashPresenter splashPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv= (ImageView) findViewById(R.id.splashLogo);

        Animation anim_splash = AnimationUtils.loadAnimation(this,R.anim.splash_transition);
        iv.startAnimation(anim_splash);//inicia la animación
        //instancia al presentador de splash
        splashPresenter = new SplashPresenterImpl(this,getApplicationContext());

    }


    @Override
    protected void onResume() {
        super.onResume();
        //se pospone un segundo la consulta del estado de la sesión para mostrar la animación
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashPresenter.getAvatarDefaultStatus();//se consulta si existe al menos un avatar
            }
        },1000);
    }


    /**
     * Si hay un avartar en uso se muestra la pantalla principal o la de los permisos de configuración
     * @param status valor de si existe un avatar en uso
     */
    @Override
    public void hasAvatarActived(boolean status) {
        Intent intentFirstAvatar = new Intent(this, permissionFiles.class);
        Intent intentMainScreen = new Intent(this, NavigationActivity.class);
        if(status)
            startActivity(intentMainScreen);//Ya se tiene un avatar y se pasa a la pantalla principal
        else
            startActivity(intentFirstAvatar);//Se deben hacer las comfiguraciones de inicio

        finish();//Termina esta Actividad
    }
}
