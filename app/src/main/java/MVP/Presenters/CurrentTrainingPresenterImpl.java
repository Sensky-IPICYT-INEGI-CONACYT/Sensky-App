package MVP.Presenters;

import MVP.Interactors.CurrentTrainingInteractorImpl;
import MVP.Interfaces.CurrentTraining.CurrentTrainingInteractor;
import MVP.Interfaces.CurrentTraining.CurrentTrainingPresenter;
import MVP.Interfaces.CurrentTraining.CurrentTrainingView;
import MVP.Interfaces.CurrentTraining.OnCurrentTriningFinishListener;
import SenSkyCore.Evidences.Evidence;
import SenSkyCore.Surveys.Survey;
import SenSkyCore.Trainings.Training;
import android.content.Context;

import java.util.List;

public class CurrentTrainingPresenterImpl implements CurrentTrainingPresenter, OnCurrentTriningFinishListener {

    CurrentTrainingView currentTrainingView;
    CurrentTrainingInteractor currentTrainingInteractor;

    public CurrentTrainingPresenterImpl(CurrentTrainingView currentTrainingView,Context context){
        this.currentTrainingView = currentTrainingView;
        currentTrainingInteractor = new CurrentTrainingInteractorImpl(context);
    }

    @Override
    public void getCurrentTraining() {
        currentTrainingInteractor.getCurrentTraining(this);
    }

    @Override
    public void getEvidencesOfCurrentTraining() {
        currentTrainingInteractor.getTrainingEvidences(this);
    }

    @Override
    public void getSurveysOfCurrentTraining() {
        currentTrainingInteractor.getTrainingSurveys(this);
    }

    @Override
    public void existPendingEvidencesStatus() {
        currentTrainingInteractor.existPendingEvidences(this);
    }

    @Override
    public void returnCurrentTraining(Training training) {
        if(currentTrainingView != null){
            currentTrainingView.currentTraining(training);
        }
    }

    @Override
    public void returnSurveysToTraining(List<Survey> surveys) {
        if(currentTrainingView != null){
            currentTrainingView.surveysToTraining(surveys);
        }
    }

    @Override
    public void returnEvidencesOfCurrentEvidences(List<Evidence> evidences) {
        if(currentTrainingView != null){
            currentTrainingView.evidencesOfTraining(evidences);
        }
    }

    @Override
    public void returnStatuspendingEvidences(Boolean existPendingEvidences) {
        if(currentTrainingView != null) {
            if(existPendingEvidences)
                currentTrainingView.existpendingEvidences();
            else
                currentTrainingView.notExistPendingEvidences();
        }
    }
}
