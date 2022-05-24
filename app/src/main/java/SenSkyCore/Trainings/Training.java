package SenSkyCore.Trainings;

import SenSkyCore.Surveys.SurveyControl.Survey;

import java.util.List;

public class Training {
    private String key;
    private String placeName;
    private long timeStampRunTraining;
    private String emailTeam;
    private int status;
    private List<Survey> surveyList;

    protected void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    protected void setEmailTeam(String emailTeam) {
        this.emailTeam = emailTeam;
    }

    public String getEmailTeam() {
        return emailTeam;
    }

    protected void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceName() {
        return placeName;
    }

    protected void setTimeStampRunTraining(Long timeStampRunTraining) {
        this.timeStampRunTraining = timeStampRunTraining;
    }

    public long getTimeStampRunTraining() {
        return timeStampRunTraining;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public List<Survey> getSurveyList() {
        return surveyList;
    }

    protected void setSurveyList(List<Survey> surveyList) {
        this.surveyList = surveyList;
    }
}
