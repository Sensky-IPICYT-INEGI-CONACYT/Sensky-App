package SenSkyCore.Avatars;

import java.util.List;

public interface AvatarsDAO {
    public List<Avatar> getAllAvatars();
    public Avatar getAvatar(int index);
    public void createAvatar(String ID, String name,String imagePath);
    public void activateAvatar(int index);
    public Avatar getCurrenAvatar();
}
