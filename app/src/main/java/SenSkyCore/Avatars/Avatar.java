package SenSkyCore.Avatars;

public class Avatar{
    private String ID;
    private String name;
    private String ImagePath;
    private int Index;
    private String SerialFlow;
    private int Actived;


    public String getID() {
        return ID;
    }

    protected void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    protected void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getIndex() {
        return Index;
    }

    protected void setIndex(int index) {
        Index = index;
    }

    public String getSerialFlow() {
        return SerialFlow;
    }

    protected void setSerialFlow(String serialFlow) {
        SerialFlow = serialFlow;
    }

    public int getActived() {
        return Actived;
    }

    public void setActived(int actived) {
        Actived = actived;
    }
}
