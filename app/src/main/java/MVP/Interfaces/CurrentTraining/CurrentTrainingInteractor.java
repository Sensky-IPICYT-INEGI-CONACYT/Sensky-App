package MVP.Interfaces.CurrentTraining;

public interface CurrentTrainingInteractor {
    void getCurrentTraining(OnCurrentTriningFinishListener listener);
    void getTrainingEvidences(OnCurrentTriningFinishListener listener);
    void existPendingEvidences(OnCurrentTriningFinishListener listener);
    void getTrainingSurveys(OnCurrentTriningFinishListener listener);
}
