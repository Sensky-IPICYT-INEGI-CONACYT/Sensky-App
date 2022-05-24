package youilab.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import youilab.main.Services.SenSkyIntentService;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    /**
     * Se ejecuta por primera vez el intentService que vigila la conexión a internet, este mismo servicio se lanza cuando se reinicia el dispositivo
     */
    @Override
    protected void onResume() {
        super.onResume();
        Intent serv = new Intent(getApplicationContext(), SenSkyIntentService.class); //serv de tipo Intent
        ContextCompat.startForegroundService(this,serv);
    }
}