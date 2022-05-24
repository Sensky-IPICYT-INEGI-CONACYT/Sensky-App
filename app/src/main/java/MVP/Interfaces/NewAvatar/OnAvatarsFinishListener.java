package MVP.Interfaces.NewAvatar;

import SenSkyCore.Avatars.Avatar;

import java.util.List;

public interface OnAvatarsFinishListener {
    void returnAvatarList(List<Avatar> avatarList);
    void returnAvatarActive(Avatar activeAvatar);
    void returnNewAvatarSuccess();
    void returnNewAvatarFailed(String message);
}
