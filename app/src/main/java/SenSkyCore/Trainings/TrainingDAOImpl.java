package SenSkyCore.Trainings;

import SenSkyCore.Avatars.Avatar;
import SenSkyCore.DataBase.DataBaseCore.DataBaseCoreImpl;
import SenSkyCore.Surveys.Survey;
import android.content.Context;
import android.database.Cursor;
import android.net.http.DelegatingSSLSession;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrainingDAOImpl implements TrainingDAO{

    //Lista de capacitaciones registradas en el dispositivo
    private List<Training> trainingList;

    //necesario para realizar queries en la base de datos
    private Context context;

    //Manejador de la base de datos
    private DataBaseCoreImpl dataBaseCore;

    public TrainingDAOImpl(Context context){
        this.trainingList = new ArrayList<>();
        this.dataBaseCore = new DataBaseCoreImpl(context);
    }

    /**
     * obtencíon de las capacitaciones de un avatar
     * @param avatarID identificador del avatar
     * @return lista de objetos de capacitaciones del avatar
     */
    @Override
    public List<Training> getAllTrainings(String avatarID) {
        return createTrainingListObjects(dataBaseCore.getAllTrainings(avatarID));
    }

    /**
     * obtener las capacitaciones en las que ya se puede participar (la fecha ya llegó)
     * @param avatarID identificador del avatar
     * @return lista de objetos de capacitaciones
     */
    @Override
    public List<Training> getActivatedTrainings(String avatarID) {
        List<Training> trainingList = new ArrayList<>();

        for (Training training: createTrainingListObjects(dataBaseCore.getAllTrainings(avatarID))
             ) {

            if( System.currentTimeMillis() > training.getTimeStampRunTraining())
                trainingList.add(training);

        }
        return trainingList;
    }

    /**
     * obtener las capacitaciones que estan en el dipositivo pero que aun no puedes participar
     * @param avatarID identificador del avatar activo
     * @return lista de capacitaciones
     */
    @Override
    public List<Training> getSoonTrainings(String avatarID) {
        List<Training> trainingList = new ArrayList<>();

        for (Training training: createTrainingListObjects(dataBaseCore.getAllTrainings(avatarID))
        ) {

            if( System.currentTimeMillis()<training.getTimeStampRunTraining())
                trainingList.add(training);

        }
        return trainingList;
    }

    /**
     * obtener la información de la capacitación que se esta usando actualmente
     * @param avatarID identificador del avatar
     * @return objeto de la capacitación activa
     */
    @Override
    public Training getActivatedTraining(String avatarID) {
        Training training;
        Cursor cursorTraining = dataBaseCore.getCursorActivatedTraining(avatarID);
        if (cursorTraining.getCount() > 0){
            cursorTraining.moveToFirst();
            training = createTrainingObject(cursorTraining);
        }else
        training= null;
        cursorTraining.close();//cierra el cursor de consulta
        dataBaseCore.closeDataBaseReadable();//cierra la conexión de base de datos
        return training;
    }

    /**
     * creación de una nueva capacitacion en el dispositivo
     * @param key identificador
     * @param namePlace nombre del lugar de la capacitación
     * @param timeStampRunTraining fecha y hora de inicio
     * @param emailTeam email del organizador
     * @param avatarId identificador del avatar que registro la capacitación
     */
    @Override
    public void createTraining(String key, String namePlace, long timeStampRunTraining, String emailTeam,String avatarId) {

        dataBaseCore.createNewTraining(key,namePlace,emailTeam,String.valueOf(timeStampRunTraining),avatarId);
        dataBaseCore.closeDataBaseWritable();
    }

    /**
     * Señalar una capacitación como la activa actual
     * @param key identificador de la capacitación
     * @param avatarID identificador del avatar
     */
    @Override
    public void activateTraining(String key,String avatarID) {
        dataBaseCore.activateTraining(key,avatarID);
        dataBaseCore.closeDataBaseWritable();
    }



    /**
     * Se iteran los registros de cada training y se obtiene la lista de trainings
     * @param cursor
     * @return
     */
    private List<Training> createTrainingListObjects(Cursor cursor){
        trainingList = new ArrayList<>();
        while (cursor.moveToNext()) {
            trainingList.add(createTrainingObject(cursor));
        }
        cursor.close();//se cierra el cursor de consulta
        dataBaseCore.closeDataBaseReadable();//cierra la conexión a la base de datos
        return trainingList;
    }

    /**
     * Del cursor se extrae la informacionn y se crea un objeto Training
     * @param cursor
     * @return
     */
    private Training createTrainingObject(Cursor cursor){
        Training training = new Training();
        training.setKey(cursor.getString(0));
        training.setPlaceName(cursor.getString(1));
        training.setEmailTeam(cursor.getString(2));
        training.setTimeStampRunTraining(cursor.getLong(3));
        training.setStatus(cursor.getInt(4));
        return training;
    }
}
