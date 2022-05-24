package MVP.Interfaces.CurrentTraining;

import SenSkyCore.Evidences.Evidence;
import SenSkyCore.Surveys.Survey;
import SenSkyCore.Trainings.Training;

import java.util.List;

public interface CurrentTrainingView {
    void currentTraining(Training training);
    void surveysToTraining(List<Survey> surveys);
    void evidencesOfTraining(List<Evidence> evidences);
    void existpendingEvidences();
    void notExistPendingEvidences();
}
