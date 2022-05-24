package MVP.Interfaces.Trainings;

public interface TrainingPresenter {
    void GetAllTrainings();
    void GetActiveTrainings();
    void GetSoonTrainings();
    void NewTraining(String key);
    void ActivateTraining(String keyConfirm);
}
