package youilab.main;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Build;
import youilab.main.Receivers.SenSkyBootReceiver;

public class App extends Application {
    public static final String CHANNEL_ID = "SenSkyNotification";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        enableReceptor();
    }

    private void enableReceptor(){
        ComponentName receiver = new ComponentName(getApplicationContext(), SenSkyBootReceiver.class);
        PackageManager pm = getApplicationContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channelMain = new NotificationChannel(CHANNEL_ID,"SenSky", NotificationManager.IMPORTANCE_LOW);
            channelMain.setDescription("Monitoreo");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelMain);
        }
    }
}
