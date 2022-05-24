package SenSkyCore.Evidences;

import SenSkyCore.Surveys.SurveyControl.Tag;

import java.util.List;

public class Evidence {
    private String ID;
    private String avatarId;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Long timeStamp;
    private String trainingId;
    private int surveyId;
    private String comment;
    private String fileImagePath;
    private String fileSurveyPath;
    private int evidenceType;
    private int statusSync;

    private List<Tag> tagList;

    protected void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public String getFileSurveyPath() {
        return fileSurveyPath;
    }

    protected void setFileSurveyPath(String fileSurveyPath) {
        this.fileSurveyPath = fileSurveyPath;
    }



    protected void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    protected void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    protected void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    protected void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatarId() {
        return avatarId;
    }

    protected void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getTrainingId() {
        return trainingId;
    }

    protected void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int getSurveyId() {
        return surveyId;
    }

    protected void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    protected void setEvidenceType(int evidenceType) {
        this.evidenceType = evidenceType;
    }

    public int getEvidenceType() {
        return evidenceType;
    }

    protected void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    protected void setFileImagePath(String filePath) {
        this.fileImagePath = filePath;
    }

    public String getFileImagePath() {
        return fileImagePath;
    }

    protected void setStatusSync(int statusSync) {
        this.statusSync = statusSync;
    }

    public int getStatusSync() {
        return statusSync;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Tag> getTagList() {
        return tagList;
    }
}
