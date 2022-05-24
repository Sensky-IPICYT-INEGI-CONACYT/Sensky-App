package MVP.Presenters;

import MVP.Interactors.NewEvidenceInteractorImpl;
import MVP.Interactors.SurveyInteractorImpl;
import MVP.Interfaces.NewEvidence.NewEvidenceInteractor;
import MVP.Interfaces.Survey.OnSurveyFinishListener;
import MVP.Interfaces.Survey.SurveyInteractor;
import MVP.Interfaces.Survey.SurveyPresenter;
import MVP.Interfaces.Survey.SurveyView;
import SenSkyCore.Surveys.SurveyControl.Question;
import android.content.Context;

public class SurveyPresenterImpl implements SurveyPresenter, OnSurveyFinishListener {

    private SurveyView surveyView;
    private SurveyInteractor surveyInteractor;
    private NewEvidenceInteractor newEvidenceInteractor;

    public SurveyPresenterImpl(SurveyView surveyView, Context context){
        this.surveyView = surveyView;
        this.surveyInteractor = new SurveyInteractorImpl(context);
        this.newEvidenceInteractor = new NewEvidenceInteractorImpl(context);

    }


    //Mandan a ejecutar las tareas al interactor
    @Override
    public void loadSurvey(String id) {
        if(surveyView != null){
            surveyInteractor.findAndLoadSurvey(id, this);
        }
    }


    @Override
    public void nextQuestion() {
        if(surveyView != null){
            surveyInteractor.getNextQuestion(this);
        }
    }

    @Override
    public void saveSurvey(int idSurvey, double latitude, double longitude, double altitude) {
        if(surveyView != null){
            surveyInteractor.createNewSurveyEvidence(idSurvey, latitude, longitude, altitude,this);
        }
    }





    //Regresan los valores a la vista de la encuesta que se aplica
    @Override
    public void returnCurrentQuestion(Question currentQuestion) {
        if(surveyView != null)
        {
            surveyView.currentQuestion(currentQuestion);
        }
    }

    @Override
    public void returnNameOfSurvey(String nameSurvey) {
         if(surveyView != null){
             surveyView.nameSurvey(nameSurvey);
         }
    }

    @Override
    public void endOfSurvey(boolean bnd) {
        if(surveyView != null){
            surveyView.endSurvey(bnd);
        }
    }

    @Override
    public void questionMandatoryNotAnswered() {
        if(surveyView != null){
            surveyView.requiredResponse();
        }
    }

    @Override
    public void errorReadingSurvey() {
        if(surveyView != null){
            surveyView.Error();
        }
       
    }

    @Override
    public void errorSavingSurvey() {
        if(surveyView != null){
            surveyView.Error();
        }
    }

    @Override
    public void surveySaved() {
        if(surveyView != null){
            surveyView.surveySaved();
        }
    }
}
