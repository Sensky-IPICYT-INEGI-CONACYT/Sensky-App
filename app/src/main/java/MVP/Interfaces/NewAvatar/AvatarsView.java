package MVP.Interfaces.NewAvatar;

import SenSkyCore.Avatars.Avatar;

import java.util.List;

public interface AvatarsView {
    void AvatarList(List<Avatar> avatarList);
    void CurrentAvatar(Avatar avatar);
    void Error(String message);
}
