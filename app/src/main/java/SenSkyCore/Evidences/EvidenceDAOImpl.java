package SenSkyCore.Evidences;

import SenSkyCore.DataBase.DataBaseCore.DataBaseCoreImpl;
import SenSkyCore.Surveys.SurveyControl.Tag;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class EvidenceDAOImpl implements EvidenceDAO{

    //Lista de evidencias registrados en el dispositivo
    private List<Evidence> evidenceList;

    //necesario para realizar queries en la base de datos
    private Context context;

    //Manejador de la base de datos
    private DataBaseCoreImpl dataBaseCore;

    public EvidenceDAOImpl(Context context){
        this.context= context;
        this.dataBaseCore = new DataBaseCoreImpl(context);
    }

    /**
     * Regresa todas las evidencias en el dispositivo
     * @return lista de objetos evidencias
     */
    @Override
    public List<Evidence> getAllEvidences() {
        return createEvidencesListObjects(dataBaseCore.getAllEvidences());
    }

    /**
     * Regresa todas las evidencias que estan pendientes de sincronizar en el dispositivo
     * @return lista de objetos evidencias
     */
    @Override
    public List<Evidence> getAllPendingEvidences() {
        return createEvidencesListObjects(dataBaseCore.getAllPendingEvidences());
    }

    /**
     * Regresa todas las evidencias de una capacitación en concreto en la que un avatar esta participando
     * @param avatarId para identificar la información de que avatar se busca
     * @param trainingId para ver que capacitación esta siendo usada por un avatar
     * @return lista de todos los objetos evidencias de una capacitación
     */
    @Override
    public List<Evidence> getAllTrainingEvidences(String avatarId, String trainingId) {
        return createEvidencesListObjects(dataBaseCore.getAllTrainingEvidences(avatarId,trainingId));
    }

    /**
     * Creación de una nueva evidencia (fotografia o encuesta) por el usuario
     * @param id id de la evidencia
     * @param tagList lista de tags de la evidencia (disponibles para fotos)
     * @param avatarId avatar creador de la evidencia
     * @param latitude coordenada en decimales de la latitud
     * @param longitude coordenana en decimales de la longitud
     * @param altitude coordenada en decimales de la altitud (no es muy usada pero por si se usa)
     * @param timestamp estampa de tiempo en la que la evidencia es creada en  milisegundos
     * @param trainingId capacitación en la que se creo la evidencia
     * @param surveyId id de la encuesta que fue contestada (varias por capacitación), esto solo si la evidencia es una encuesta
     * @param comment texto escrito por el usuario sobre la evidencia (solo para fotografías)
     * @param fileSurvey dirección en el dispositivo del archivo de la evidencia (encuesta)
     * @param fileImage dirección en el dispositivo del archivo de la evidencia (fotografía)
     * @param type tipo de evidencia que se creará (foto o encuesta)
     */
    @Override
    public void createNewEvidence(String id, List<Tag> tagList, String avatarId, double latitude, double longitude, double altitude, Long timestamp, String trainingId, int surveyId, String comment, String fileSurvey, String fileImage, int type) {
        //registro de la nueva evidencia
        dataBaseCore.createNewEvidence(id, avatarId,String.valueOf(latitude),String.valueOf(longitude), String.valueOf(altitude),String.valueOf(timestamp),trainingId,surveyId,comment,fileSurvey, fileImage,type);
        dataBaseCore.closeDataBaseWritable();
        for (Tag tag: tagList
             ) {
            if(tag.getSelected()) {//si alguna de la etiquetas fueron seleccionadas se registran
                dataBaseCore.createNewTag(id, tag.getValue());//registro de etiqueta con el id de la evidencia a la que pertenecen
                dataBaseCore.closeDataBaseWritable();
            }
        }
    }

    /**
     * Cambiar el estado de sincronizacion de la evidencia cuando se sube al servidor
     * @param evidenceId identificardor de la evidencia
     */
    @Override
    public void setStatusEvidenceSyncronized(String evidenceId) {
        dataBaseCore.updateSynchronizedEvidenceStatus(evidenceId);
        dataBaseCore.closeDataBaseWritable();
    }

    /**
     * Creación de una colección de evidencias
     * @param cursor colección de datos en formato de SQLite a pasar a objetos
     * @return lista de objetos de evidencias
     */
    private List<Evidence> createEvidencesListObjects(Cursor cursor){
        evidenceList = new ArrayList<>();
        while (cursor.moveToNext()) { // cada renglon con datos de una evidencia se pasa a objeto
            evidenceList.add(createEvidenceObject(cursor)); //se agrega a la lista un nuevo objeto evidencia
        }
        cursor.close();//se cierra el cursor de consulta
        dataBaseCore.closeDataBaseReadable();//cierra la conexión a la base de datos
        return evidenceList;
    }

    /**
     * Creación de un objeto evidencia a partir de un cursor de base de datos
     * @param cursor contiene la información de una evidencia
     * @return objeto evidencia con la información correspodiente
     */
    private Evidence createEvidenceObject(Cursor cursor){
        Evidence evidence = new Evidence();
        evidence.setID(cursor.getString(0));
        evidence.setAvatarId(cursor.getString(1));
        evidence.setLatitude(Double.valueOf(cursor.getString(2)));
        evidence.setLongitude(Double.valueOf(cursor.getString(3)));
        evidence.setAltitude(Double.valueOf(cursor.getString(4)));
        evidence.setTimeStamp(Long.valueOf(cursor.getString(5)));
        evidence.setTrainingId(cursor.getString(6));
        evidence.setSurveyId(cursor.getInt(7));
        evidence.setFileSurveyPath(cursor.getString(8));
        evidence.setFileImagePath(cursor.getString(9));
        evidence.setEvidenceType(cursor.getInt(10));
        evidence.setComment(cursor.getString(11));
        evidence.setStatusSync(cursor.getInt(12));
        evidence.setTagList(getTags(evidence.getID()));
        return evidence;
    }

    /**
     * Creación de un conjunto de objetos de etiquetas pertenecientes a una evidencia
     * @param id identificador de la evidencia a la que pertenecen las eqtiquetas
     * @return lista de objetos etiquetas de una evidencia
     */
    private List<Tag> getTags(String id){
        List<Tag> tagList = new ArrayList<>();
        Cursor cursorTags = dataBaseCore.getTagsOfEvidence(id); //obtención de etiquetas de la base de datos
        while (cursorTags.moveToNext()) {//cada etiquera se pasa a un objeto Tag
            Tag tag = new Tag();
            tag.setValue(cursorTags.getString(1));
            tagList.add(tag);
        }

        return tagList;
    }
}
