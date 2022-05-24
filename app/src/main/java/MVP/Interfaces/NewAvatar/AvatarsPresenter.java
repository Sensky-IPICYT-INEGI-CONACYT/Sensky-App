package MVP.Interfaces.NewAvatar;

public interface AvatarsPresenter {
    void getAvatarList();
    void getCurrentAvatar();
    void NewAvatar(String name, String imagePath);
    void isActiveAvatar(int indice);

}
