package MVP.Interfaces.Survey;

public interface SurveyInteractor {
    void findAndLoadSurvey(String id, OnSurveyFinishListener listener);
    void getNextQuestion(OnSurveyFinishListener listener);
    void createNewSurveyEvidence(int surveyId, double latitude, double longitude, double altitude, OnSurveyFinishListener listener);
}
