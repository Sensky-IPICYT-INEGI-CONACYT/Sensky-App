package SenSkyCore.DataBase.DataBaseCore;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseCoreImpl extends SQLiteOpenHelper {
    private SQLiteDatabase dbW;
    private SQLiteDatabase dbR;
    public DataBaseCoreImpl( Context context) {
        super(context, "senskydb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE AVATARS(" +
                "INDEX_AVATAR INTEGER  PRIMARY KEY AUTOINCREMENT," +
                "ID_AVATAR VARCHAR(80)," +
                "AVATAR VARCHAR(20)," +
                "IMG_PATH VARCHAR(120)," +
                "SERIAL_NUMBER_FLOW VARCHAR(30)," +
                "SESSION_STATUS INTEGER);");

        db.execSQL("CREATE TABLE SURVEYS(" +
                "ID INTEGER PRIMARY KEY," +
                "ID_TRAINING VARCHAR(10),"+
                "TITLE VARCHAR(120)," +
                "PATH VARCHAR(120)," +
                "LOCAL_PATH VARCHAR(120));");


        db.execSQL("CREATE TABLE TRAININGS(" +
                "KEY_TRAINING VARCHAR(10)," +
                "NAME_PLACE VARCHAR(200)," +
                "EMAIL_TEAM VARCHAR(70)," +
                "TIMESTAMP_RUN VARCHAR(50), " +
                "STATUS INTEGER," +
                "ID_AVATAR VARCHAR(80)," +
                "UNIQUE (KEY_TRAINING, ID_AVATAR));");

        db.execSQL("CREATE TABLE EVIDENCES(" +
                "ID VARCHAR(100) UNIQUE, " +
                "ID_AVATAR  varchar(80), " +
                "LATITUDE varchar(30), " +
                "LONGITUDE VARCHAR(30), " +
                "ALTITUDE VARCHAR(30), " +
                "TIMESTAMP VARCHAR(50)," +
                "TRAINING_ID VARCHAR(20)," +
                "SURVEY_ID INTEGER, " +
                "FILE_SURVEY VARCHAR(150)," +
                "FILE_IMAGE VARCHAR(150)," +
                "TYPE INTEGER, " +
                "COMMENT VARCHAR(255)," +
                "STATUS_SYNC INTEGER);");


        db.execSQL("CREATE TABLE TAGS(" +
                "ID_EVIDENCE VARCHAR(100)," +
                "TAG VARCHAR(70)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion){
            case 4:

            break;
        }
    }

    public void closeDataBaseReadable()
    {
        dbR.close();
    }

    public void closeDataBaseWritable(){
        dbW.close();
    }

    ///////////////////////////////////////////*Queries con tabla avatars*///////////////////////////////////////////////////////////

    /**
     * Regresa los registros con la información de los avatares registrados
     * @return Se regresa el cursos tal cual con la lista de registros de todos los avatares existentes para su uso (debe ser cerrado una vez usado)
     */
    public Cursor getCursorAllAvatars(){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM AVATARS";//obtengo todos los avartars registrados

        return dbR.rawQuery(myQuery, null);
    }

    /**
     * Devuelve el cursor con información del avatar del indice indicado
     * @param index indice perteneciente al vatar seleccionado
     * @return Se regresa el cursos tal cual para su uso (debe ser cerrado una vez usado)
     */
    public Cursor getCursorAvatar(int index){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM AVATARS WHERE INDEX_AVATAR = "+ index;//obtengo todos los avartars registrados

        return dbR.rawQuery(myQuery, null);
    }


    /**
     * Crea un nuevo registro de avatar
     * @param id id unico con el que se registra en la plataforma web
     * @param name nombre del avatar
     * @param imgPath Ruta a la imagen que se seleccionó
     */
    public void createAvatar(String id, String name,String serialFlow,String imgPath){
        dbW = this.getWritableDatabase();
        dbW.execSQL("UPDATE AVATARS SET SESSION_STATUS = '0'");
        dbW.execSQL("INSERT INTO AVATARS (ID_AVATAR,AVATAR,IMG_PATH,SERIAL_NUMBER_FLOW,SESSION_STATUS) VALUES ('"+id+"','"+name+"','"+imgPath+"','"+serialFlow+"',1);");

    }

    /**
     * Cambia el estado del avatar a 1 -> activo
     * @param index indice perteneciente al vatar seleccionado
     */
    public void activateAvatar(int index){
        dbW = this.getWritableDatabase();
        dbW.execSQL("UPDATE AVATARS SET SESSION_STATUS = 0");
        dbW.execSQL("UPDATE AVATARS SET SESSION_STATUS = 1 where INDEX_AVATAR = "+index);
    }

    /**
     * Obtiene la informacion del avatar activo
     */
    public Cursor getActivatedAvatar(){
        dbR = this.getWritableDatabase();
        String myQuery = "SELECT * FROM AVATARS WHERE SESSION_STATUS = 1";//obtengo todos los avartars registrados

        return dbR.rawQuery(myQuery, null);
    }


    ///////////////////////////////////////////*Queries con tabla surveys*///////////////////////////////////////////////////////////

    /*
    * Obtiene los registros de las encuestas diponible para su aplicación a los usuarios
    */
    public Cursor getAllSurveysByTraining(String keyTraining){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM SURVEYS WHERE ID_TRAINING = '"+keyTraining+"'";//obtengo todos los avartars registrados

        return dbR.rawQuery(myQuery, null);
    }

    /**
     * Esta información solo se obtiene en la primera configuracíon de la aplicación
     * @param ID número de encuesta
     * @param title título general de la encuesta
     * @param relativePath dirección del servidor en la que se encuentra
     * @param localPath ruta del archivo de encuesta en el dispositivo
     */
    public void createNewSurvey(int ID, String keyTraining, String title, String relativePath, String localPath){
        dbW = this.getWritableDatabase();
        dbW.execSQL("INSERT OR IGNORE INTO SURVEYS (ID, ID_TRAINING, TITLE,PATH,LOCAL_PATH) VALUES ("+ID+",'"+keyTraining+"','"+title+"','"+relativePath+"', '"+localPath+"');");
    }

    /**
     * Se consulta la información local de una encuesta en especifico
     * @param id identificador de la encuesta a aplicar
     * @return Cursor con los datos de la encuesta
     */
    public Cursor getSurveyById(String id){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM SURVEYS WHERE ID = "+Integer.valueOf(id);

        return dbR.rawQuery(myQuery, null);
    }


    ////////////////////////////////////////*TABLA DE CAPACITACIONES*/////////////////////////////////

    /**
     *
     * @param avatarID
     * @return
     */
    public Cursor getAllTrainings(String avatarID){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM TRAININGS where ID_AVATAR = '"+avatarID+"' ORDER BY TIMESTAMP_RUN asc";//obtengo todos los capacitciones registradas

        return dbR.rawQuery(myQuery, null);
    }

    public void createNewTraining(String key, String namePlace, String email, String ts_run, String avatarId){
        dbW = this.getWritableDatabase();
        dbW.execSQL("UPDATE TRAININGS SET STATUS = 0 where ID_AVATAR = '"+ avatarId +"'");
        dbW.execSQL("INSERT OR IGNORE INTO TRAININGS (KEY_TRAINING,NAME_PLACE,EMAIL_TEAM,TIMESTAMP_RUN, STATUS, ID_AVATAR) VALUES ('"+key+"','"+namePlace+"','"+email+"','"+ts_run+"', 1,'"+avatarId+"');");
    }

    public void activateTraining(String key,String avatarID){
        dbW = this.getWritableDatabase();
        dbW.execSQL("UPDATE TRAININGS SET STATUS = 0 where ID_AVATAR = '"+ avatarID +"'");
        dbW.execSQL("UPDATE TRAININGS SET STATUS = 1 where KEY_TRAINING = '"+key+"' AND ID_AVATAR = '"+ avatarID +"'");
    }

    public Cursor getCursorActivatedTraining(String avatarId){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM TRAININGS WHERE STATUS = 1 AND ID_AVATAR = '"+ avatarId +"'";

        return dbR.rawQuery(myQuery, null);
    }

    ////////////////////////////////////////*TABLA DE EVIDENCIAS*/////////////////////////////////
    public void createNewEvidence(String id, String avatarId, String latitude, String longitude, String altitude, String timestamp, String trainingId, int surveyId, String comment, String fileSurvey, String fileImage, int type){
        dbW = this.getWritableDatabase();
        dbW.execSQL("INSERT OR IGNORE INTO EVIDENCES (ID, ID_AVATAR, LATITUDE, LONGITUDE, ALTITUDE, TIMESTAMP, TRAINING_ID, SURVEY_ID, FILE_SURVEY, FILE_IMAGE, TYPE, COMMENT, STATUS_SYNC) VALUES ('"+id+"','"+avatarId+"','"+latitude+"','"+longitude+"','"+altitude+"', '"+timestamp+"', '"+trainingId+"', "+surveyId+",  '"+fileSurvey+"','"+fileImage+"', "+type+",'"+comment+"', 0);");
    }

    public Cursor getAllEvidences(){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM EVIDENCES";//obtengo todos los capacitciones registradas

        return dbR.rawQuery(myQuery, null);
    }

    public Cursor getAllPendingEvidences(){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM EVIDENCES WHERE STATUS_SYNC = 0";//obtengo todos los capacitciones registradas

        return dbR.rawQuery(myQuery, null);
    }

    public Cursor getAllTrainingEvidences(String avatarId, String trainingId){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM EVIDENCES WHERE ID_AVATAR = '"+ avatarId +"' AND TRAINING_ID = '"+ trainingId +"' ORDER BY TIMESTAMP DESC";

        return dbR.rawQuery(myQuery, null);
    }

    public void updateSynchronizedEvidenceStatus(String evidenceId){
        dbW = this.getWritableDatabase();
        dbW.execSQL("UPDATE EVIDENCES SET STATUS_SYNC = 1 where ID = '"+ evidenceId +"'");

    }

    //////////////////////////////*TABLA DE ETIQUETAS*////////////////////////////

    public void createNewTag(String idEvidence, String tag){
        dbW = this.getWritableDatabase();
        dbW.execSQL("INSERT OR IGNORE INTO TAGS (ID_EVIDENCE, TAG) VALUES ('"+idEvidence+"','"+tag+"');");
    }

    public Cursor getTagsOfEvidence(String idEvidence){
        this.dbR = getReadableDatabase();

        String myQuery = "SELECT * FROM TAGS WHERE ID_EVIDENCE = '"+idEvidence+"'";

        return dbR.rawQuery(myQuery, null);
    }
}
