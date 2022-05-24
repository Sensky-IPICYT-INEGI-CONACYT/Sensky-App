package MVP.Interactors;

import MVP.Interfaces.Trainings.OnTrainingsFinishListener;
import MVP.Interfaces.Trainings.TrainingsInteractor;
import SenSkyCore.Avatars.AvatarsDAO;
import SenSkyCore.Avatars.AvatarsDAOImpl;
import SenSkyCore.Trainings.Training;
import SenSkyCore.Trainings.TrainingDAO;
import SenSkyCore.Trainings.TrainingDAOImpl;
import SenSkyCore.WebServices.TrainingServices;
import android.content.Context;

import java.util.List;

public class TrainingsInteractorImpl implements TrainingsInteractor {

    TrainingDAO trainingDAO;
    AvatarsDAO avatarsDAO;
    Context context;

    public TrainingsInteractorImpl(Context context){
        this.context= context;
        trainingDAO = new TrainingDAOImpl(context);
        avatarsDAO = new AvatarsDAOImpl(context);
    }

    @Override
    public void runTrainingService(String key, OnTrainingsFinishListener listener) {
        boolean bndExistTraining= false;
        List<Training> trainingList = trainingDAO.getAllTrainings(avatarsDAO.getCurrenAvatar().getID());
        //Se verifica que la llave de la capacitación no exista para poder correr el servicio de consulta
        for (Training training: trainingList
        ) {
            if(training.getKey().compareTo(key)==0)
            {
                bndExistTraining=true;//existe la capacitacion en el dispositivo
                break;//se rompe el ciclo
            }
        }

        if(!bndExistTraining) {
            //aqui correr el servicio de consulta de la capacitación
            new TrainingServices(key,context, listener);
        }else
            listener.NewTrainingCreatedFailed("Ya existe una capacitación con ese código");

    }

    @Override
    public void getAllTrainings(OnTrainingsFinishListener listener) {
        listener.TrainingList(trainingDAO.getAllTrainings(avatarsDAO.getCurrenAvatar().getID()));
    }

    @Override
    public void getActivedTrainings(OnTrainingsFinishListener listener) {
        listener.TrainingList(trainingDAO.getActivatedTrainings(avatarsDAO.getCurrenAvatar().getID()));
    }

    @Override
    public void getSoonTrainings(OnTrainingsFinishListener listener) {
        listener.TrainingList(trainingDAO.getSoonTrainings(avatarsDAO.getCurrenAvatar().getID()));

    }

    @Override
    public void setActiveTraining(String keyConfirm, OnTrainingsFinishListener listener) {
            trainingDAO.activateTraining(keyConfirm,avatarsDAO.getCurrenAvatar().getID());//se cambia la capacitación activa
            listener.activateTrainingSuccessful();//el cambio fue exitoso
    }
}
