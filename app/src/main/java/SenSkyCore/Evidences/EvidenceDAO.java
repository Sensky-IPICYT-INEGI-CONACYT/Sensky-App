package SenSkyCore.Evidences;

import SenSkyCore.Surveys.SurveyControl.Tag;

import java.util.List;

public interface EvidenceDAO {
   public  List<Evidence> getAllEvidences();
     public List<Evidence> getAllPendingEvidences();
     public List<Evidence> getAllTrainingEvidences(String avatarId, String trainingId);
     public void createNewEvidence(String id, List<Tag> tagList, String avatarId, double latitude, double longitude, double altitude, Long timestamp, String trainingId, int surveyId, String comment, String fileSurvey, String fileImage, int type);
     public void setStatusEvidenceSyncronized(String evidenceId);
}
