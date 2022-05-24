package MVP.Presenters;

import MVP.Interactors.NewEvidenceInteractorImpl;
import MVP.Interfaces.NewEvidence.OnNewEvidenceFinishListener;
import MVP.Interfaces.NewEvidence.NewEvidenceInteractor;
import MVP.Interfaces.NewEvidence.NewEvidencePresenter;
import MVP.Interfaces.NewEvidence.PhotoEvidenceView;
import SenSkyCore.Surveys.SurveyControl.Tag;
import android.content.Context;

import java.util.List;

public class NewEvidencePresenterImpl implements NewEvidencePresenter, OnNewEvidenceFinishListener {

    NewEvidenceInteractor newEvidenceInteractor;
    PhotoEvidenceView photoEvidenceView;

    public NewEvidencePresenterImpl(PhotoEvidenceView photoEvidenceView, Context context){
        this.photoEvidenceView = photoEvidenceView;
        this.newEvidenceInteractor = new NewEvidenceInteractorImpl(context);
    }

    @Override
    public void savePhotoEvidence(String comment, List<Tag> tagList, String imagePath, double latitude, double longitude, double altitude) {
        newEvidenceInteractor.createNewPhotoEvidence(comment,tagList, imagePath,latitude,longitude,altitude,this);
    }

    @Override
    public void photoEvidenceSuccess() {
        if(photoEvidenceView != null){
            photoEvidenceView.photoEvidenceSaved(true);
        }
    }

    @Override
    public void photoEvidenceFailed() {
        if(photoEvidenceView != null){
            photoEvidenceView.photoEvidenceSaved(false);
        }
    }
}
