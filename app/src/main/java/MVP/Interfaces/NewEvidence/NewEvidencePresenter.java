package MVP.Interfaces.NewEvidence;

import SenSkyCore.Surveys.SurveyControl.Tag;

import java.util.List;

public interface NewEvidencePresenter {
    void savePhotoEvidence(String comment, List<Tag> tagList, String imagePath, double latitude, double longitude, double altitude);

}
