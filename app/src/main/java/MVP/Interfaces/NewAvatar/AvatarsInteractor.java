package MVP.Interfaces.NewAvatar;

public interface AvatarsInteractor {
    void createNewAvatar(String name, String imagePath, OnAvatarsFinishListener listener);
    void setActiveAvatar(int index, OnAvatarsFinishListener listener);
    void getAllAvatars(OnAvatarsFinishListener listener);
    void getActiveAvatar(OnAvatarsFinishListener listener);
}
