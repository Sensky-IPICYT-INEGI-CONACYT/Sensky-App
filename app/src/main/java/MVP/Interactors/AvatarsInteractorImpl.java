package MVP.Interactors;

import AppTools.DirectoryManager;
import MVP.Interfaces.NewAvatar.AvatarsInteractor;
import MVP.Interfaces.NewAvatar.OnAvatarsFinishListener;
import SenSkyCore.Avatars.AvatarsDAO;
import SenSkyCore.Avatars.AvatarsDAOImpl;
import android.content.Context;

public class AvatarsInteractorImpl implements AvatarsInteractor {
    AvatarsDAO avatarsDAO;
    Context context;

    public AvatarsInteractorImpl(Context context){
        this.context= context;
        avatarsDAO = new AvatarsDAOImpl(context);
    }


    @Override
    public void createNewAvatar(String name, String imagePath, OnAvatarsFinishListener listener) {
        String idAvatar="ID_"+name+"_"+System.currentTimeMillis();
        avatarsDAO.createAvatar(idAvatar,name,imagePath);
        DirectoryManager.createAvatarDirectories(idAvatar);
        listener.returnNewAvatarSuccess();
    }

    @Override
    public void setActiveAvatar(int index, OnAvatarsFinishListener listener) {
        avatarsDAO.activateAvatar(index);
        listener.returnAvatarActive(avatarsDAO.getAvatar(index));
    }

    @Override
    public void getAllAvatars(OnAvatarsFinishListener listener) {
        listener.returnAvatarList( avatarsDAO.getAllAvatars());
    }


    @Override
    public void getActiveAvatar(OnAvatarsFinishListener listener) {
        listener.returnAvatarActive(avatarsDAO.getCurrenAvatar());
    }
}
