package MVP.Interfaces.Trainings;

import SenSkyCore.Trainings.Training;

import java.util.List;

public interface OnTrainingsFinishListener {
    void TrainingList(List<Training> trainingList);
    void NewTrainingCreated(List<Training> trainingList);
    void NewTrainingCreatedFailed(String error);
    void activateTrainingSuccessful();
    void activateTrainingFailed();
}
