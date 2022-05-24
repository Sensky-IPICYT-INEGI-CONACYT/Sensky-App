package MVP.Interfaces.CurrentTraining;

import SenSkyCore.Evidences.Evidence;
import SenSkyCore.Surveys.Survey;
import SenSkyCore.Trainings.Training;

import java.util.List;

public interface OnCurrentTriningFinishListener {
    void returnCurrentTraining(Training training);
    void returnSurveysToTraining(List<Survey> surveys);
    void returnEvidencesOfCurrentEvidences(List<Evidence> evidences);
    void returnStatuspendingEvidences(Boolean existPendingEvidences);
}
