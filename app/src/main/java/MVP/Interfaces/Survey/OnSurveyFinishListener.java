package MVP.Interfaces.Survey;

import SenSkyCore.Surveys.SurveyControl.Question;

public interface OnSurveyFinishListener {
    void returnCurrentQuestion(Question currentQuestion);
    void returnNameOfSurvey(String nameSurvey);
    void endOfSurvey(boolean bnd);
    void questionMandatoryNotAnswered();
    void errorReadingSurvey();
    void errorSavingSurvey();
    void surveySaved();
}
