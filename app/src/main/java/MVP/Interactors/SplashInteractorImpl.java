package MVP.Interactors;

import MVP.Interfaces.Splash.OnSplashFinishListener;
import MVP.Interfaces.Splash.SplashInteractor;
import SenSkyCore.Avatars.AvatarsDAO;
import SenSkyCore.Avatars.AvatarsDAOImpl;
import android.content.Context;

public class SplashInteractorImpl implements SplashInteractor {

    AvatarsDAO avatarsDAO;
    Context context;


    public SplashInteractorImpl( Context context){
        this.context= context;
        avatarsDAO= new AvatarsDAOImpl(context);
    }


    @Override
    public void existAvatarAccount(OnSplashFinishListener listener) {
        if(avatarsDAO.getAllAvatars().size()>0)
            listener.existDefaultAvatar();
        else
            listener.notExistDefaultAvatar();
    }
}
