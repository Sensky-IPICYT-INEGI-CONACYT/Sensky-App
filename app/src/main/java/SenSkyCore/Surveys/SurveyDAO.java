package SenSkyCore.Surveys;

import SenSkyCore.Avatars.Avatar;

import java.util.List;

public interface SurveyDAO {
    public List<Survey> getAllSurveysByTraining(String keyTraining);
    public Survey getSurveyById(String id);
    public void createSurvey(int ID, String keyTraining, String title,String addressPath, String localPath);
}
