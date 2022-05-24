package MVP.Presenters;

import MVP.Interactors.TrainingsInteractorImpl;
import MVP.Interfaces.Trainings.*;
import SenSkyCore.Trainings.Training;
import android.content.Context;

import java.util.List;

public class TrainingsPresenterImpl implements TrainingPresenter, OnTrainingsFinishListener {


    TrainingsInteractor trainingsInteractor;
    TrainingsView trainingsView;
    TrainingActivateView trainingActivateView;


    public TrainingsPresenterImpl(TrainingsView view, Context context)
    {
        this.trainingsView = view;
        trainingsInteractor = new TrainingsInteractorImpl(context);
    }

    public TrainingsPresenterImpl(TrainingActivateView trainingActivateView, Context context){
        this.trainingActivateView = trainingActivateView;
        trainingsInteractor = new TrainingsInteractorImpl(context);
    }



    @Override
    public void TrainingList(List<Training> trainingList) {
        if(trainingsView != null)
        {
            trainingsView.TrainingsList(trainingList);

        }


    }

    @Override
    public void NewTrainingCreated(List<Training> trainingList) {
        if(trainingsView != null)
        {
            trainingsView.trainingSaved();
            trainingsView.TrainingsList(trainingList);
        }


    }

    @Override
    public void NewTrainingCreatedFailed(String error) {
        if(trainingsView != null)
        {
            trainingsView.Error(error);
        }


    }

    @Override
    public void activateTrainingSuccessful() {
        if(trainingActivateView != null)
        {
            trainingActivateView.activatedTraining(true);
        }
    }

    @Override
    public void activateTrainingFailed() {
        if(trainingActivateView != null)
        {
            trainingActivateView.activatedTraining(false);
        }
    }





    @Override
    public void GetAllTrainings() {
        if(trainingsView != null) {
            trainingsInteractor.getAllTrainings(this);
        }


    }


    @Override
    public void GetActiveTrainings() {
        if(trainingsView != null) {

            trainingsInteractor.getActivedTrainings(this);
        }
    }

    @Override
    public void GetSoonTrainings() {
        if(trainingsView != null) {
            trainingsInteractor.getSoonTrainings(this);
        }
    }

    @Override
    public void NewTraining(String key) {
        trainingsInteractor.runTrainingService(key, this);
    }

    @Override
    public void ActivateTraining(String keyConfirm) {
        trainingsInteractor.setActiveTraining( keyConfirm, this);
    }
}
