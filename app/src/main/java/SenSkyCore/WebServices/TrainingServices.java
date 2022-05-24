package SenSkyCore.WebServices;

import AppTools.DirectoryManager;
import MVP.Interfaces.Trainings.OnTrainingsFinishListener;
import SenSkyCore.Avatars.AvatarsDAO;
import SenSkyCore.Avatars.AvatarsDAOImpl;
import SenSkyCore.Surveys.SurveyDAO;
import SenSkyCore.Surveys.SurveyDAOImpl;
import SenSkyCore.Trainings.TrainingDAO;
import SenSkyCore.Trainings.TrainingDAOImpl;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static AppTools.DirectoryManager.SENSKY_DIRECTORY_TRAINING_SURVEYS;
import static AppTools.DirectoryManager.rootDirectory;

public class TrainingServices {

    private static final String TRAINING_SERVICE = "http://youilab.ipicyt.edu.mx:8080/sensky/api/v1/workshop/";
    private static final String SURVEYS_STORE = "http://youilab.ipicyt.edu.mx/sensky_store/";

    static String key;
    static Context context;
    static OnTrainingsFinishListener listener;

    /**
     * Servicio de consulta de información de capacitaciones
     * @param key llave unica de la capacitación
     * @param context
     * @param listener
     */
    public TrainingServices(String key, Context context, OnTrainingsFinishListener listener) {
        this.key = key;
        this.listener= listener;
        this.context = context;
        ChallengesServiceHandler getChallenges = new ChallengesServiceHandler();
        getChallenges.execute();
    }

    public static class ChallengesServiceHandler extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return   startGetTrainingService();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("Request", s);
            switch (s){
                case "0":
                    listener.NewTrainingCreatedFailed("Error en la comunicación con el servidor");
                    break;
                case "1":
                    listener.NewTrainingCreatedFailed("Error: 401 SC_UNAUTHORIZED");
                    break;
                case "2":
                    listener.NewTrainingCreatedFailed("Error: Revisa tu conexión a internet e intenta más tarde");
                    break;
                default:
                    if(!s.contains("WORKSHOP_NOT_FOUND")){
                        try {
                            registerTraining(new JSONObject(s));
                        }catch (JSONException e){
                            e.printStackTrace();
                            listener.NewTrainingCreatedFailed("ERROR: INTENTA MÁS TARDE");
                        }
                    }
                    else
                        listener.NewTrainingCreatedFailed("Capacitación no encontrada");
                    break;
            }
        }


        /**
         * Configuración y ejecución del http request para la consulta de una capacitación
         * @return
         */
        private String startGetTrainingService(){
            try {
                DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
                HttpGet localHttpPost = new HttpGet(TRAINING_SERVICE + key);
                HttpResponse response = localDefaultHttpClient.execute(localHttpPost);
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                    Log.i("ERROR",response.getStatusLine().toString());
                    return "0";//problemas internos con el servidor
                }

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String result = null;
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        result = EntityUtils.toString(entity);
                        return result;
                    }
                }else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                    Log.i("Error",response.getStatusLine().toString());
                    return "1";//"401 SC_UNAUTHORIZED";
                }

                Log.i("Error",response.getStatusLine().toString());
                return "2";
            }catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
            return "2";
        }

        /**
         * Registro de la capacitacón consultada cuando se regresan datos validos
         * @param jsonObjectTraining objeto json con los datos de la capacitación
         * @throws JSONException
         */
        private void registerTraining(JSONObject jsonObjectTraining) throws JSONException {
            TrainingDAO trainingDAO = new TrainingDAOImpl(context);

            String avatarId =new AvatarsDAOImpl(context).getCurrenAvatar().getID();

            //registro de la capacitación
            trainingDAO.createTraining(
                    jsonObjectTraining.getString("key"),
                    jsonObjectTraining.getString("place-name"),
                    jsonObjectTraining.getLong("init-timestamp"),
                    jsonObjectTraining.getString("team-key"),
                    avatarId
            );//registro en la base de datos de la capacitación

            DirectoryManager.createTrainingDirectories(avatarId,jsonObjectTraining.getString("key"));//creacion de los diretorios de la capacitación

            registerSurveys(jsonObjectTraining.getString("key"),new JSONArray(jsonObjectTraining.getString("surveys")));//registro de las encuestas de la capacitación y descarga de los archivos
            listener.NewTrainingCreated(trainingDAO.getAllTrainings(avatarId));//devolver la lista nueva de capacitaciones a la vista

        }

        /**
         * registrar las encuestas obtenidas del servicio de consulta
         * @param jsonArraySurveys arreglo json con la información de las encuestas
         * @return retorna un bool si se realizo con exito
         * @throws JSONException para atrapr cualquier error con el json obtenido
         */
        private boolean registerSurveys(String keyTraining, JSONArray jsonArraySurveys) throws JSONException {
            SurveyDAO surveyDAO = new SurveyDAOImpl(context);
            AvatarsDAO avatarsDAO = new AvatarsDAOImpl(context);


            for (int i = 0; i < jsonArraySurveys.length(); i++) {

                JSONObject jsonSurveyObject = new JSONObject(jsonArraySurveys.getString(i));

                String nameFile = "survey"+jsonSurveyObject.getString("key")+".xml";
                //download survey file from server
                new DownloadFile(nameFile,jsonSurveyObject.getString("key"),SURVEYS_STORE+jsonSurveyObject.getString("path"),new File(Environment.getExternalStorageDirectory(),rootDirectory+avatarsDAO.getCurrenAvatar().getID()+SENSKY_DIRECTORY_TRAINING_SURVEYS+"T"+keyTraining));

                //registrar la encuesta en la base de datos una vez descargado el archivo
                surveyDAO.createSurvey(Integer.valueOf(jsonSurveyObject.getString("key")),
                        keyTraining,
                        jsonSurveyObject.getString("title"),
                        jsonSurveyObject.getString("path"),
                        rootDirectory+avatarsDAO.getCurrenAvatar().getID()+SENSKY_DIRECTORY_TRAINING_SURVEYS+"T"+keyTraining+ "/" +nameFile
                );
            }
            return true;
        }
    }
}
