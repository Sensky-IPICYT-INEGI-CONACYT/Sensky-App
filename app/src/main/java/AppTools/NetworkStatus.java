package AppTools;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkStatus {

	public final static int WITHOUT_INTERNET_ACCCESS = 1;
	public final static int WIFI_INTERNET_ACCCESS = 2;
	public final static int MOBILE_INTERNET_ACCCESS = 3;


	public static int getStatus(Context actividad) {
		/*
		 * 1-sin acceso a internet
		 * 2-vía datos móviles
		 * 3-vía WIFI
		 * 4 sólo WIFI
		 * 5 wifi y datos moviles
		 */
		ConnectivityManager manager = (ConnectivityManager) actividad.getSystemService(Context.CONNECTIVITY_SERVICE);
		int bnd = WITHOUT_INTERNET_ACCCESS;


		boolean ismobile=false;
		try {
			ismobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
		}catch (Exception e){
			ismobile=false;
		}

		boolean isWifi = manager.getNetworkInfo(
		                        ConnectivityManager.TYPE_WIFI)
		                        .isConnected();

        if (!ismobile && !isWifi)
        	bnd=WITHOUT_INTERNET_ACCCESS;
		else if(isWifi)
			bnd=WIFI_INTERNET_ACCCESS;
        else if(ismobile)
			bnd=MOBILE_INTERNET_ACCCESS;

	return bnd;
	}
}
