package MVP.Interfaces.Trainings;

public interface TrainingsInteractor {
    void runTrainingService(String key, OnTrainingsFinishListener listener);
    void getAllTrainings(OnTrainingsFinishListener listener);
    void getActivedTrainings(OnTrainingsFinishListener listener);
    void getSoonTrainings(OnTrainingsFinishListener listener);
    void setActiveTraining(String keyConfirm, OnTrainingsFinishListener listener);
}
