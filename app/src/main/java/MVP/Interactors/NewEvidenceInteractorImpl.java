package MVP.Interactors;

import MVP.Interfaces.NewEvidence.OnNewEvidenceFinishListener;
import MVP.Interfaces.NewEvidence.NewEvidenceInteractor;
import SenSkyCore.Avatars.AvatarsDAO;
import SenSkyCore.Avatars.AvatarsDAOImpl;
import SenSkyCore.Evidences.EvidenceDAO;
import SenSkyCore.Evidences.EvidenceDAOImpl;
import SenSkyCore.Surveys.SurveyControl.Tag;
import SenSkyCore.Trainings.TrainingDAO;
import SenSkyCore.Trainings.TrainingDAOImpl;
import android.content.Context;

import java.util.List;

import static AppTools.DirectoryManager.SENSKY_EVIDENCE_TYPE_PHOTO;

public class NewEvidenceInteractorImpl implements NewEvidenceInteractor {
    TrainingDAO trainingDAO;
    AvatarsDAO avatarsDAO;
    EvidenceDAO evidenceDAO;
    Context context;

    public NewEvidenceInteractorImpl(Context context){
        this.context = context;
        trainingDAO = new TrainingDAOImpl(context);
        avatarsDAO = new AvatarsDAOImpl(context);
        evidenceDAO = new EvidenceDAOImpl(context);
    }




    @Override
    public void createNewPhotoEvidence(String comment, List<Tag> tagList, String imagePath, double latitude, double longitude, double altitude, OnNewEvidenceFinishListener listener) {
        String avatarId = avatarsDAO.getCurrenAvatar().getID();
        String trainingId= trainingDAO.getActivatedTraining(avatarId).getKey();
        evidenceDAO.createNewEvidence("PHOTO_"+trainingId+System.currentTimeMillis(),tagList, avatarId,latitude,longitude,altitude,System.currentTimeMillis(),trainingId,0,comment,"",imagePath,SENSKY_EVIDENCE_TYPE_PHOTO);
        listener.photoEvidenceSuccess();
    }
}
