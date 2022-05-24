package MVP.Presenters;

import MVP.Interactors.ConfigurationInteractorImpl;
import MVP.Interfaces.Configuration.ConfigurationInteractor;
import MVP.Interfaces.Configuration.ConfigurationPresenter;
import MVP.Interfaces.Configuration.ConfigurationView;
import MVP.Interfaces.Configuration.OnConfigurationFinishListener;
import android.content.Context;

public class ConfigurationPresenterImpl implements ConfigurationPresenter, OnConfigurationFinishListener {

    private ConfigurationView view;
    private ConfigurationInteractor configurationInteractor;

    public ConfigurationPresenterImpl(ConfigurationView view, Context context){
        this.view= view;
        this.configurationInteractor = new ConfigurationInteractorImpl(context);
    }


    @Override
    public void createDirectories() {
        this.configurationInteractor.createDefaultAppDirectories(this);
    }



    @Override
    public void statusDirectoryProcess(boolean statusProcess) {
        if(view != null){
            if(statusProcess)
                view.DirectoryProcessStatus(true);
            else
                view.DirectoryProcessStatus(false);
        }
    }
}
