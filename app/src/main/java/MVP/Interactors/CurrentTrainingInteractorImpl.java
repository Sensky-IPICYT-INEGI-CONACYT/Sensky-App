package MVP.Interactors;

import MVP.Interfaces.CurrentTraining.CurrentTrainingInteractor;
import MVP.Interfaces.CurrentTraining.OnCurrentTriningFinishListener;
import SenSkyCore.Avatars.AvatarsDAO;
import SenSkyCore.Avatars.AvatarsDAOImpl;
import SenSkyCore.Evidences.EvidenceDAO;
import SenSkyCore.Evidences.EvidenceDAOImpl;
import SenSkyCore.Surveys.Survey;
import SenSkyCore.Surveys.SurveyDAO;
import SenSkyCore.Surveys.SurveyDAOImpl;
import SenSkyCore.Trainings.Training;
import SenSkyCore.Trainings.TrainingDAO;
import SenSkyCore.Trainings.TrainingDAOImpl;
import android.content.Context;

import java.util.ArrayList;

public class CurrentTrainingInteractorImpl implements CurrentTrainingInteractor {

    TrainingDAO trainingDAO;
    AvatarsDAO avatarsDAO;
    EvidenceDAO evidenceDAO;
    SurveyDAO surveyDAO;
    Context context;

    public CurrentTrainingInteractorImpl(Context context){
        this.context = context;
        trainingDAO = new TrainingDAOImpl(context);
        avatarsDAO = new AvatarsDAOImpl(context);
        evidenceDAO = new EvidenceDAOImpl(context);
        surveyDAO = new SurveyDAOImpl(context);
    }


    @Override
    public void getCurrentTraining(OnCurrentTriningFinishListener listener) {
        Training currentTraining = trainingDAO.getActivatedTraining(avatarsDAO.getCurrenAvatar().getID());
        if(currentTraining!=null) {
            listener.returnCurrentTraining(currentTraining);
        }else
        {
            listener.returnCurrentTraining(null);
        }
    }

    @Override
    public void getTrainingEvidences(OnCurrentTriningFinishListener listener) {
        Training currentTraining = trainingDAO.getActivatedTraining(avatarsDAO.getCurrenAvatar().getID());
        if (currentTraining != null)
        listener.returnEvidencesOfCurrentEvidences(evidenceDAO.getAllTrainingEvidences(avatarsDAO.getCurrenAvatar().getID(),currentTraining.getKey()));
    }

    @Override
    public void existPendingEvidences(OnCurrentTriningFinishListener listener) {
        listener.returnStatuspendingEvidences(evidenceDAO.getAllPendingEvidences().size() != 0);
    }

    @Override
    public void getTrainingSurveys(OnCurrentTriningFinishListener listener) {
        Training currentTraining = trainingDAO.getActivatedTraining(avatarsDAO.getCurrenAvatar().getID());
        if(currentTraining!=null) {
            listener.returnSurveysToTraining(surveyDAO.getAllSurveysByTraining(currentTraining.getKey()));
        }else
        {
            listener.returnSurveysToTraining(new ArrayList<Survey>());
        }
    }
}
