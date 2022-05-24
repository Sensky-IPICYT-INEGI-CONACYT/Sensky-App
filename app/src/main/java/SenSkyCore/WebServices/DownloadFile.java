package SenSkyCore.WebServices;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class DownloadFile {
    private String name;
    private String tmpFileName;
    private String urlFile;
    private File folder;

    public final static String NO_FILE="";

    /**
     * Metodo conel cual se inicia la descarga del archivo
     * @param nameFile nombre del archivo final con el que se registrará
     * @param id id de la encuesta con el que se nombrara el archivo temporal
     * @param urlFile url de donde se descargará el archivo
     * @param folder directorio donde se almacenará el archivo
     */
    protected DownloadFile(String nameFile, String id, String urlFile, File folder){
        this.name = nameFile;//nombre del archivo final
        this.tmpFileName = "tmpSurvey"+id+".xml";//nombre del archivo temporal

        this.folder=folder;//carpeta destino en el dispositivo
        this.urlFile=urlFile;//url de descarga

        try{
            getFile();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**Se crean los archivos (final y temporal) y se ejecuta la descarga del archivo en una tarea asincrona para que no interfiera con las tareas del servicio
     *
     * @throws IOException
     */
    private void getFile() throws IOException {

        File newFile = new File(folder,name);
        File tmpFile = new File(folder,tmpFileName);

            if (name.compareTo(NO_FILE) != 0) {
                try {
                    if (!folder.exists())
                        folder.mkdir();

                    downloadFileAsyncTask downloadFile = new downloadFileAsyncTask(new URL(urlFile), newFile,tmpFile);
                    downloadFile.execute();
                    //   downloadFile(new URL(urlFile), newFile);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

    }


    /**
     * Tarea asincrona para la descarga del archivo sin interrumpir el hilo principal de la aplicación
     */
    private class downloadFileAsyncTask extends AsyncTask {

        File newFile;
        File tmpFile;
        URL urlFile;

        public downloadFileAsyncTask(URL url, File newFile, File tmpFile)
        {
            this.newFile = newFile;
            this.tmpFile = tmpFile;
            this.urlFile = url;
        }

        /**
         * Acciones de descarga del archivo
         * @param arg0
         * @return
         */
        @Override
        protected Object doInBackground(Object... arg0) {
            //manejo del archivo temporal
            try {
                if (!tmpFile.exists()) {//si no existe crea uno nuevo
                    tmpFile.createNewFile();
                    FileUtils.copyURLToFile(urlFile,tmpFile);

                } else {//si existe borra y crea uno nuevo
                    tmpFile.delete();
                    tmpFile.createNewFile();
                    FileUtils.copyURLToFile(urlFile,tmpFile);
                }
            }catch (IOException io)
            {
                io.printStackTrace();
            }
            return null;
        }

        /**
         * acciones con el resultado de la descarga
         * @param o
         */
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            //verificar que el archivo temporal existe y no es un archivo vacio
            if(tmpFile.exists() && tmpFile.length()>0){
                if(!newFile.exists()){//si no existe se crea uno nuevo
                    try{
                        newFile.createNewFile();
                    }catch (IOException io)
                    {
                        io.printStackTrace();
                    }
                }
                tmpFile.renameTo(newFile);//el archivo temporal es pasado al nuevo
                tmpFile.delete();//el archivo temporal es borrado
            }

        }
    }
}
