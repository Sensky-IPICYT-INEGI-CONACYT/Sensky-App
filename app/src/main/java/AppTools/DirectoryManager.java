package AppTools;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DirectoryManager {

    //valores para los tipos de evidencias
    public static final int SENSKY_EVIDENCE_TYPE_PHOTO = 1;
    public static final int SENSKY_EVIDENCE_TYPE_SURVEY = 3;


    public static final String SENSKY_PHOTO_FORMAT = ".jpeg";



    //constantes con los nombres de los directorios que se crean en el dispositivo
    public final static String rootDirectory ="SenSky/";
    public final static String SENSKY_DIRECTORY_TRAINING_SURVEYS = "/SurveysTraining/";
    public final static String SENSKY_DIRECTORY_EVIDENCES = "/Evidences/";
    public final static String SENSKY_DIRECTORY_PHOTOS = "/photos/";
    public final static String SENSKY_DIRECTORY_SURVEYS = "/surveys/";


    /*
    manda crear el directorio raiz
     */
    public static void createDirectories()
    {
        createDirectory(rootDirectory);//crecion del directorio raiz
    }

    /**
     * Creación de los directorios de un avatar
     * @param idAvatar nombre del avatar para crear el directorio
     */
    public static void createAvatarDirectories(String idAvatar){
        createDirectory(rootDirectory+idAvatar);//directorio del avatar
        createDirectory(rootDirectory+idAvatar+SENSKY_DIRECTORY_TRAINING_SURVEYS);//directorio de encuestas de capacitaciones
        createDirectory(rootDirectory+idAvatar+SENSKY_DIRECTORY_EVIDENCES);//directorio de los archivos de evidencias que el usuario creará
    }

    /**
     * Creacion de los directorias para cada capacitacion de cada avatar
     * @param idAvatar nopmbre del avatar que participa en una capacitacion
     * @param keyTraining id de la capacitación en la que el usuario participará
     */
    public static void createTrainingDirectories(String idAvatar, String keyTraining){

        keyTraining = "T"+keyTraining;
        createDirectory(rootDirectory+idAvatar+SENSKY_DIRECTORY_TRAINING_SURVEYS+keyTraining);
        createDirectory(rootDirectory+idAvatar+SENSKY_DIRECTORY_EVIDENCES+keyTraining);
        createDirectory(rootDirectory+idAvatar+SENSKY_DIRECTORY_EVIDENCES+keyTraining+SENSKY_DIRECTORY_PHOTOS);
        createDirectory(rootDirectory+idAvatar+SENSKY_DIRECTORY_EVIDENCES+keyTraining+SENSKY_DIRECTORY_SURVEYS);

    }

    /**
     * Obtención del directorio donde se alojan las fotos que el usaurio ha tomado
     * @param idAvatar nombre del aavatar
     * @param keyTraining is de la capacitación
     * @return cadena de caracteres del la ruta del diretorio
     */
    public static String getDirectoryToPhotos(String idAvatar, String keyTraining){
        return rootDirectory+idAvatar+SENSKY_DIRECTORY_EVIDENCES+"T"+keyTraining+SENSKY_DIRECTORY_PHOTOS;
    }

    /**
     * Obtención del directorio donde se alojan las encuestas que el usaurio ha tomado
     * @param idAvatar nombre del aavatar
     * @param keyTraining id de la capacitación
     * @return cadena de caracteres del la ruta del diretorio
     */
    public static String getDirectoryToSurveys(String idAvatar, String keyTraining){
        return rootDirectory+idAvatar+SENSKY_DIRECTORY_EVIDENCES+"T"+keyTraining+SENSKY_DIRECTORY_SURVEYS;
    }

    /*
    Crea un directorio en el dispositivolet
     */
    public static boolean createDirectory(String name)
    {

        boolean bnd = false;
        File folder = new File(Environment.getExternalStorageDirectory(), name);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                bnd = false;
                Log.i("Error", "Promebla al crear directorio "+ name);
            } else {
                bnd = true;
                Log.i("Direcrtorio", "Secreo el directorio");
            }

        }else
            bnd = true;

        return bnd;
    }
}
