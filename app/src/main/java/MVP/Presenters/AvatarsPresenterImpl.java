package MVP.Presenters;

import MVP.Interactors.AvatarsInteractorImpl;
import MVP.Interfaces.NewAvatar.*;
import SenSkyCore.Avatars.Avatar;
import android.content.Context;

import java.util.List;

public class AvatarsPresenterImpl implements AvatarsPresenter, OnAvatarsFinishListener {

    private AvatarsView view = null;
    private NewAvatarView newAvatarView = null;
    private AvatarsInteractor avatarsInteractor = null;

    public AvatarsPresenterImpl(AvatarsView view, Context context){
        this.view=view;
        this.avatarsInteractor = new AvatarsInteractorImpl(context);
    }

    public AvatarsPresenterImpl(NewAvatarView view, Context context){
        this.newAvatarView=view;
        this.avatarsInteractor = new AvatarsInteractorImpl(context);
    }

    @Override
    public void getAvatarList() {
        avatarsInteractor.getAllAvatars(this);
    }

    @Override
    public void getCurrentAvatar() {
        avatarsInteractor.getActiveAvatar(this);
    }

    @Override
    public void NewAvatar(String name, String imagePath) {
        avatarsInteractor.createNewAvatar(name, imagePath,this);
    }

    @Override
    public void isActiveAvatar(int indice) {
        avatarsInteractor.setActiveAvatar(indice, this);
    }



    @Override
    public void returnAvatarList(List<Avatar> avatarList) {
        if(view!= null){
            view.AvatarList(avatarList);
        }
    }

    @Override
    public void returnAvatarActive(Avatar activeAvatar) {
        if(view!= null){
            view.CurrentAvatar(activeAvatar);
        }


    }

    @Override
    public void returnNewAvatarSuccess() {
        if(newAvatarView!= null){
            newAvatarView.GotoHome();
        }

    }

    @Override
    public void returnNewAvatarFailed(String message) {
        if(view!= null){
            view.Error(message);
        }
    }
}
