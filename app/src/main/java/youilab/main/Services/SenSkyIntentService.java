package youilab.main.Services;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.ConnectivityManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.multidex.BuildConfig;

import youilab.main.R;
import youilab.main.Receivers.NetworkChangeReceiver;


import static youilab.main.App.CHANNEL_ID;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SenSkyIntentService extends IntentService  {
    // TODO: Rename actions, choose action names that describe tasks that this




    private PowerManager.WakeLock wakeLock;//manejador de bateria del dispositivo



    private final String TRANSITION_ACTION_RECEIVER =
            BuildConfig.APPLICATION_ID + "TRANSITION_ACTION_RECEIVER";

    private BroadcastReceiver mNetworkReceiver;




    Notification notification;

    public SenSkyIntentService() {
        super("TriviralIntentService");
        setIntentRedelivery(true);//recuperar el servicio si este termina abruptamente
    }




    @Override
    public void onCreate() {
        super.onCreate();

        //configuracion paraevitar que el dispositvo entre en modo de suspension y el sistema vaya terminar el servicio persistente de la app
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"TriVi:Wakelock");
        wakeLock.acquire();



        setMainNotification("Espera un momento, localizando..");

        //instancia del receiver que captara los cambios en las conexiones del disporitivo
        mNetworkReceiver = new NetworkChangeReceiver();

        //registro del recividor del estado de conexión de internet
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }



    /**
     * muestra una notificación dependiendo de la versión de android del dispositivo, esta notificación se ligará al servicio
     * @param message mensaje a mostrar en el dispositivo
     */
    private void setMainNotification(String message)
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
        {
            notification = new Notification.Builder(this,CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_sensky)
                    .addAction(R.mipmap.ic_sensky,"Ocultar esta notificación", persistentNoticationDisabled(this))
                    .build();

            //despues de construir la notificación esta debe lanzarce dentro de los primero 5 segundos de ejecutar el servicio para ligar el servicio con la notifiación
            startForeground(1, notification);

        }else{
            NotificationCompat.Builder localBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_sensky)
                    .addAction(R.mipmap.ic_sensky,"Ocultar esta notificación", persistentNoticationDisabled(this));
            startForeground(1, localBuilder.build());

        }
    }



//se agrega la opcion de boton en la notificaión para poder ocultarla y no este siempre presente en la statusbar
    private PendingIntent persistentNoticationDisabled(Context context){
        Intent i = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
                .putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);

        return pendingIntent;
    }

    /**
     * ciclo infinito para que no muera el servicio
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        for (;;){
            SystemClock.sleep(5000);
        }
    }



    @Override
    public void onDestroy() {
        wakeLock.release();

        //si el serviio termina cerramos el receiver registrado
        try {
            if(mNetworkReceiver != null)
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


}
