package youilab.main.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import youilab.main.Services.SenSkyIntentService;

/**
 * Recibidor broadcast para cuando se inicia el dispositivo ya que haya sido
 * reiniciado o apagado, se encargara de lanzar el servicio que vigila el estado de la
 * conexión de internet
 */
public class SenSkyBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Intent serv = new Intent(context, SenSkyIntentService.class); //serv de tipo Intent
            ContextCompat.startForegroundService(context,serv);//este se lanza como un startforegrundservice ya que estará siempre activo
            //estará ligado a una notificación esta notificación debe lanzarse en menos de 5 segundos de iniciar ya que si no falla el enlace
        }
    }



}
