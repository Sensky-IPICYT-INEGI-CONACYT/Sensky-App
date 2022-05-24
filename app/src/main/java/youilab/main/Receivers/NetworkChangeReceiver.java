package youilab.main.Receivers;

import AppTools.NetworkStatus;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import youilab.main.Services.synchronizationService;

/**
 * Recibidor broadcast para el cambio de conexiones de red,
 * en este caso en el servicio llamada SenSkyIntentService que siemore esta activo vigilando el estado de la red
 * para ver si esta conectado al wifi y subir las evidencias pendientes
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    /**
     * Recibe los cambios en las conexiones del dispositivo
     * @param context Contexto en el que se esta usando el bradcast
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        try
        {
            int bndConection = NetworkStatus.getStatus(context); //se obtiene el estado de la conexion de internet
            if (bndConection == NetworkStatus.WIFI_INTERNET_ACCCESS) {//si se tiene acceso a internet por Wifi se lanza el servicio de sincronización

                Intent myService = new Intent(context, synchronizationService.class);//intent al servicio que sincroniza las evidencias
                myService.setAction(synchronizationService.ACTION_RUN_ISERVICE);//valor para que el servicio reconosca que accion va a realizar
                context.startService(myService);//se inicia el servicio
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
