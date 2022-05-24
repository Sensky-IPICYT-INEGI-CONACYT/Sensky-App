package MVP.Interactors;

import AppTools.DirectoryManager;
import MVP.Interfaces.Configuration.ConfigurationInteractor;
import MVP.Interfaces.Configuration.OnConfigurationFinishListener;
import android.content.Context;

public class ConfigurationInteractorImpl implements ConfigurationInteractor {


    Context context;

    public ConfigurationInteractorImpl(Context context){
        this.context = context;
    }


    @Override
    public void createDefaultAppDirectories(OnConfigurationFinishListener listener) {
        DirectoryManager.createDirectories();
        listener.statusDirectoryProcess(true);
    }
}
