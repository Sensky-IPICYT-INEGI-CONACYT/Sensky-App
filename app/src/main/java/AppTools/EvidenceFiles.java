package AppTools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import java.io.File;

import static AppTools.DirectoryManager.SENSKY_EVIDENCE_TYPE_PHOTO;
import static AppTools.DirectoryManager.SENSKY_PHOTO_FORMAT;

public class EvidenceFiles {

    /**
     *
     * @param type tipo de archivo que se busca
     * @param pathChallenge ruta relativa del archivo
     * @param nameFile nombre del archivo
     * @return uri del archivo que se busca
     */
    public static Uri getOutputMediaFileUri (int type, String pathChallenge, String nameFile){
        return Uri.fromFile(getOutputMediaFile(type,pathChallenge,nameFile));
    }

    private static File getOutputMediaFile(int type, String path, String nameFile){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(path),"");
        if(!mediaStorageDir.exists() && (!mediaStorageDir.mkdir()))
            return null;

        //Crear un archivo ya sea imagen o video

        File mediaFile;
        if (type == SENSKY_EVIDENCE_TYPE_PHOTO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    +nameFile+ SENSKY_PHOTO_FORMAT);
        } else
            return null;

        return mediaFile;
    }

    /**
     * Preparar el intent que abre la aplicación de fotografia con las configuraciones correspondientes
     * @param paramIntent intent que llamara la plaicacion de fotografía
     * @param context contexto donde se usará el intent
     * @param fileUri dirección donde se guardara la fotografia
     * @return
     */
    public static Intent cameraIntent(Intent paramIntent, Context context, Uri fileUri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
        {
            paramIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }
        else
        {
            File file = new File(fileUri.getPath());
            Uri photoUri = FileProvider.getUriForFile(context.getApplicationContext(),
                    context.getApplicationContext().getPackageName()+".fileproviderRipples",
                    file);
            paramIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }
        paramIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return paramIntent;
    }
}
