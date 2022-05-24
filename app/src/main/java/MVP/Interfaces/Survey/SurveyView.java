package MVP.Interfaces.Survey;

import SenSkyCore.Surveys.SurveyControl.Question;

public interface SurveyView {
    void currentQuestion(Question currentQuestion);
    void nameSurvey(String name);
    void endSurvey(boolean bnd);
    void surveySaved();
    void Error();
    void requiredResponse();
    void surveyNotExist();
}
