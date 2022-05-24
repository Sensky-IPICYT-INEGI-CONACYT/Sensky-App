package MVP.Presenters;

import MVP.Interactors.SplashInteractorImpl;
import MVP.Interfaces.Splash.OnSplashFinishListener;
import MVP.Interfaces.Splash.SplashInteractor;
import MVP.Interfaces.Splash.SplashPresenter;
import MVP.Interfaces.Splash.SplashView;
import android.content.Context;

public class SplashPresenterImpl implements SplashPresenter, OnSplashFinishListener {

    private SplashView view = null;
    private SplashInteractor  splashInteractor= null;

    public SplashPresenterImpl(SplashView view, Context context){
        this.view = view;
        splashInteractor = new SplashInteractorImpl(context);
    }

    @Override
    public void getAvatarDefaultStatus() {
        splashInteractor.existAvatarAccount(this);
    }

    @Override
    public void existDefaultAvatar() {
        if(view!= null){
            view.hasAvatarActived(true);
        }
    }

    @Override
    public void notExistDefaultAvatar() {
        if(view!= null){
            view.hasAvatarActived(false);
        }
    }

}
