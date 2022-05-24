package MVP.Interfaces.Trainings;

import SenSkyCore.Trainings.Training;

import java.util.List;

public interface TrainingsView {
    void TrainingsList(List<Training> trainingList);
    void Error(String error);
    void trainingSaved();

}
