package MVP.Interactors;

import AppTools.DirectoryManager;
import MVP.Interfaces.Survey.OnSurveyFinishListener;
import MVP.Interfaces.Survey.SurveyInteractor;
import SenSkyCore.Avatars.AvatarsDAO;
import SenSkyCore.Avatars.AvatarsDAOImpl;
import SenSkyCore.Evidences.EvidenceDAO;
import SenSkyCore.Evidences.EvidenceDAOImpl;
import SenSkyCore.Surveys.Survey;
import SenSkyCore.Surveys.SurveyControl.ParseSurveyV3;
import SenSkyCore.Surveys.SurveyControl.Question;
import SenSkyCore.Surveys.SurveyControl.SurveyHandler;
import SenSkyCore.Surveys.SurveyControl.Tag;
import SenSkyCore.Surveys.SurveyDAO;
import SenSkyCore.Surveys.SurveyDAOImpl;
import SenSkyCore.Trainings.TrainingDAO;
import SenSkyCore.Trainings.TrainingDAOImpl;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import static AppTools.DirectoryManager.SENSKY_EVIDENCE_TYPE_SURVEY;

public class SurveyInteractorImpl implements SurveyInteractor {

   private SurveyDAO surveyDAO;
    TrainingDAO trainingDAO;
    AvatarsDAO avatarsDAO;
    EvidenceDAO evidenceDAO;
   private SurveyHandler surveyHandler;

    public SurveyInteractorImpl(Context context){
        this.surveyDAO = new SurveyDAOImpl(context);
        trainingDAO = new TrainingDAOImpl(context);
        avatarsDAO = new AvatarsDAOImpl(context);
        evidenceDAO = new EvidenceDAOImpl(context);
    }

    @Override
    public void findAndLoadSurvey(String id, OnSurveyFinishListener listener) {

        Survey survey = surveyDAO.getSurveyById(id);
        ParseSurveyV3 parseSurveyV3 = new ParseSurveyV3();
        if(parseSurveyV3.validateFile(survey.getLocalPath()))
        {
            surveyHandler = new SurveyHandler(parseSurveyV3.getSurvey(survey.getLocalPath()));
            listener.returnNameOfSurvey(survey.getTitle());//Envio el nombre de la encuesta cargada
            listener.returnCurrentQuestion(surveyHandler.getCurrentQuestion());//Envio la primera pregunta de la encuesta
        }
        else
          listener.errorReadingSurvey();
    }


    @Override
    public void getNextQuestion(OnSurveyFinishListener listener) {
        Question currentQuestion= surveyHandler.getCurrentQuestion();
       if(currentQuestion.getMandatory()==1){
           if(currentQuestion.getQuestionAnswered())
               nextQuestion(listener);
           else
               listener.questionMandatoryNotAnswered();
       }else
       {
           nextQuestion(listener);
       }
    }

    @Override
    public void createNewSurveyEvidence(int id, double latitude, double longitude, double altitude, OnSurveyFinishListener listener) {


        String evidenceId= "SURVEY_"+System.currentTimeMillis();
        String idAvatar= avatarsDAO.getCurrenAvatar().getID();
        String surveyFilePath = DirectoryManager.getDirectoryToSurveys(idAvatar,trainingDAO.getActivatedTraining(idAvatar).getKey()) +evidenceId+".xml";
            SurveyHandler.createFileSurvey(surveyFilePath, surveyHandler.getSurvey().getSections(), "2",surveyHandler.getSurvey().getId());

            File surveyFile = new File(Environment.getExternalStorageDirectory(), surveyFilePath);
            if (surveyFile.exists())
            {

                String avatarId = avatarsDAO.getCurrenAvatar().getID();
                String trainingId= trainingDAO.getActivatedTraining(avatarId).getKey();
                evidenceDAO.createNewEvidence(evidenceId,new ArrayList<Tag>(),avatarId,latitude,longitude,altitude,System.currentTimeMillis(),trainingId,id,"",surveyFile.getAbsolutePath(),"",SENSKY_EVIDENCE_TYPE_SURVEY);
                listener.surveySaved();

            }else
                listener.errorSavingSurvey();

    }


    private void nextQuestion(OnSurveyFinishListener listener){
        Question nextQuestion= surveyHandler.nextQuestion();
        if(nextQuestion!=null) {
            listener.endOfSurvey(false);
            listener.returnCurrentQuestion(nextQuestion);
        }
        else
            listener.endOfSurvey(true);
    }



}
