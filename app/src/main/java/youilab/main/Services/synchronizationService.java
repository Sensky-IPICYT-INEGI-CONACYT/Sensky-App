package youilab.main.Services;

import SenSkyCore.Evidences.Evidence;
import SenSkyCore.Evidences.EvidenceDAO;
import SenSkyCore.Evidences.EvidenceDAOImpl;
import SenSkyCore.Surveys.SurveyControl.Tag;
import SenSkyCore.WebServices.UploadEvidence;
import android.app.AlertDialog;
import android.app.IntentService;

import android.app.ProgressDialog;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import youilab.main.R;

import static AppTools.DirectoryManager.SENSKY_EVIDENCE_TYPE_PHOTO;
import static AppTools.DirectoryManager.SENSKY_EVIDENCE_TYPE_SURVEY;


public class synchronizationService extends IntentService {

    public static final String ACTION_RUN_ISERVICE = "yil.sensky.action.RUN_INTENT_SERVICE";
    private static final String NOTIFICATION_CHANNEL_ID = "yil.sensky.notification";
    public static final String RECEIVER_SYNCRONIZATION_SERVICE = "GET_SYNC_STATUS_END";
    public static final String RECEIVER_SYNCRONIZATION_SERVICE_STATUS = "STATUS";

    public static final String ACTION_SYNCRONITATION_FINISHED = "100";


    private EvidenceDAO evidenceDAO;

    public synchronizationService() {
        super("synchronizationService");
    }


    protected void onHandleIntent(@Nullable Intent paramIntent)
    {
        if ((paramIntent != null) && (ACTION_RUN_ISERVICE.equals(paramIntent.getAction())))
        {
            evidenceDAO = new EvidenceDAOImpl(getApplicationContext());//dao de el manejador de evidencias

            //creación del cuadro de dialogo de captura de llave
            int total = evidenceDAO.getAllPendingEvidences().size();
            int count =0;
            if(total>0) {
                for (Evidence pendingEvidence : evidenceDAO.getAllPendingEvidences()//se obtienen las evidencias pendientes por sincronizar
                ) {

                    sendEvidenceStatusToActivity("0");


                    String pathFile = "";
                    if (pendingEvidence.getEvidenceType() == SENSKY_EVIDENCE_TYPE_PHOTO)//si es una fotografia se obtiene la direccion de la imagen
                        pathFile = pendingEvidence.getFileImagePath();
                    else //caso contraio se obtiene la dirección de la encuesta
                        pathFile = pendingEvidence.getFileSurveyPath();

                    //se ejecuta el httprequest para la subida de evidencias y se obtiene el resultado de esta acción
                    String result = new UploadEvidence().UploadEvidence(pathFile, createJSONEvidence(pendingEvidence).toString(), pendingEvidence.getEvidenceType());

                    if (result.compareTo("SUCCESS") == 0) {//si devuelve SUCCESS se registra que esta evidencia se subio con exito a la plataforma
                        evidenceDAO.setStatusEvidenceSyncronized(pendingEvidence.getID());
                    }

                    count++;
                    int percent = (count / total) * 100;

                    sendEvidenceStatusToActivity(String.valueOf(percent));
                }
            }

            sendEvidenceStatusToActivity(ACTION_SYNCRONITATION_FINISHED);

        }
    }



    public void onStart(@Nullable Intent paramIntent, int paramInt)
    {
        super.onStart(paramIntent, paramInt);
    }

    /**
     *     crea el json con la información de la evidencia a subir
     * @param evidence objeto evidencia para pasar a json
     */
    private JSONObject createJSONEvidence(Evidence evidence)
    {
        JSONObject localJSONObject1 = new JSONObject();
        try
        {
            JSONArray tagsList = new JSONArray();

            for (Tag tag:evidence.getTagList()
                 ) {
                tagsList.put(tag.getValue());
            }
            localJSONObject1.put("avatar",evidence.getAvatarId());
            localJSONObject1.put("workshop-id", evidence.getTrainingId());
            localJSONObject1.put("timestamp", evidence.getTimeStamp());
            localJSONObject1.put("latitude", evidence.getLatitude());
            localJSONObject1.put("longitude", evidence.getLongitude());
            localJSONObject1.put("altitude", evidence.getAltitude());
            localJSONObject1.put("comment", evidence.getComment());

            if (evidence.getEvidenceType() == SENSKY_EVIDENCE_TYPE_PHOTO)
                localJSONObject1.put("tags", tagsList);
            else
                localJSONObject1.put("survey-id",String.valueOf(evidence.getSurveyId()));

            return localJSONObject1;
        }
        catch (JSONException localJSONException)
        {
            localJSONException.printStackTrace();
        }
        return localJSONObject1;
    }


    /**
     * Envio de broadcast del estado en que una evidencia se ha subido al servidor
     * @param status resultado de la sincronización
     */
    private void sendEvidenceStatusToActivity(String status)
    {
        Intent sendInfo = new Intent();
        sendInfo.setAction(RECEIVER_SYNCRONIZATION_SERVICE);
        sendInfo.putExtra( RECEIVER_SYNCRONIZATION_SERVICE_STATUS,status);

        sendBroadcast(sendInfo);

    }

    public void onDestroy()
    {
        super.onDestroy();
    }


}
