package MVP.Interfaces.Survey;

public interface SurveyPresenter {
    void loadSurvey(String id);
    void nextQuestion();
    void saveSurvey(int idSurvey, double latitude, double longitude, double altitude);
}
