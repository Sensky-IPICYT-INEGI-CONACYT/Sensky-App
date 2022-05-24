package SenSkyCore.Surveys;

public class Survey {
    private int id;
    private String title;
    private String addressPath;
    private String localPath;

    protected void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    protected void setAddressPath(String addressPath) {
        this.addressPath = addressPath;
    }

    public String getAddressPath() {
        return addressPath;
    }

    protected void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
