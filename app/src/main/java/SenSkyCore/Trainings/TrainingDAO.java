package SenSkyCore.Trainings;

import SenSkyCore.Avatars.Avatar;

import java.util.List;

public interface TrainingDAO {
    public List<Training> getAllTrainings(String avatarID);
    public List<Training> getActivatedTrainings(String avatarID);
    public List<Training> getSoonTrainings(String avatarID);
    public Training getActivatedTraining(String avatarID);
    public void createTraining(String ID, String name, long timeStampRunTraining, String emailTeam, String avatarID);
    public void activateTraining(String key, String avatarId);
}
