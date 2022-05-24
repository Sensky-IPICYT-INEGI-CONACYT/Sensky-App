package MVP.Interfaces.NewEvidence;

import SenSkyCore.Surveys.SurveyControl.Tag;

import java.util.List;

public interface NewEvidenceInteractor {
    void createNewPhotoEvidence(String comment, List<Tag> tagList, String imagePath, double latitude, double longitude, double altitude, OnNewEvidenceFinishListener listener);
}
