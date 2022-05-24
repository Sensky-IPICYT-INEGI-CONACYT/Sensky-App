package SenSkyCore.Surveys;

import SenSkyCore.Avatars.Avatar;
import SenSkyCore.DataBase.DataBaseCore.DataBaseCoreImpl;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class SurveyDAOImpl implements SurveyDAO{
    //Lista de encuestas registrados en el dispositivo
    private List<Survey> surveyList;

    //necesario para realizar queries en la base de datos
    private Context context;

    //Manejador de la base de datos
    private DataBaseCoreImpl dataBaseCore;


    public SurveyDAOImpl(Context context)
    {
        this.surveyList = new ArrayList<>();
        this.dataBaseCore = new DataBaseCoreImpl(context);
    }

    /**
     * Obtención de las encuestas por el id de capacitación
     * @param keyTraining identificardor de la capacitación
     * @return lista de encuestas pertenecientes a una capacitación
     */
    @Override
    public List<Survey> getAllSurveysByTraining(String keyTraining) {
        return createSurveysListObjects(dataBaseCore.getAllSurveysByTraining(keyTraining));
    }

    /**
     * Obtención de una enciesta por el id
     * @param id identificador de la encuesta a obtener
     * @return objeto encuesta con la información de la misma
     */
    @Override
    public Survey getSurveyById(String id) {
        Cursor cursorSurvey = dataBaseCore.getSurveyById(id);
        cursorSurvey.moveToFirst();
        Survey currentSurvey = createSurveyObject(cursorSurvey);
        cursorSurvey.close();//cierra el cursor de consulta
        dataBaseCore.closeDataBaseReadable();//cierra la conexión de base de datos
        return currentSurvey;
    }

    /**
     * Registro de encuesta que puede ser contestada por el usuario
     * @param ID identificador de la encuesta
     * @param keyTraining identificador de la capacitación a la que pertenece
     * @param title titulo de la encuesta
     * @param addressPath dirección de internet donde esta alojada
     * @param localPath dirección local donde se guarda en el dispositivo del usuario
     */
    @Override
    public void createSurvey(int ID, String keyTraining, String title, String addressPath, String localPath) {
        dataBaseCore.createNewSurvey(ID, keyTraining, title, addressPath, localPath);
        dataBaseCore.closeDataBaseWritable();
    }
    
    /**
     * Se iteran los registros de cada encuesta y se obtiene la lista de encuestas
     * @param cursor contiene los registros de encuestas
     * @return regresa la lista de objetos encuestas
     */
    private List<Survey> createSurveysListObjects(Cursor cursor){
        surveyList = new ArrayList<>();
        while (cursor.moveToNext()) {
            surveyList.add(createSurveyObject(cursor));
        }
        cursor.close();//se cierra el cursor de consulta
        dataBaseCore.closeDataBaseReadable();//cierra la conexión a la base de datos
        return surveyList;
    }

    /**
     * Del cursor se extrae la informacionn y se crea un objeto encuesta
     * @param cursor contiene información de una encuesta
     * @return regresa el objeto encuesta
     */
    private Survey createSurveyObject(Cursor cursor){
        Survey survey = new Survey();
        survey.setId(cursor.getInt(0));
        survey.setTitle(cursor.getString(2));
        survey.setAddressPath(cursor.getString(3));
        survey.setLocalPath(cursor.getString(4));
        return survey;
    }
}
