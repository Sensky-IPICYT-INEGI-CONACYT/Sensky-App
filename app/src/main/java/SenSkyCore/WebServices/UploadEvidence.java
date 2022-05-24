package SenSkyCore.WebServices;


import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;

import static AppTools.DirectoryManager.SENSKY_EVIDENCE_TYPE_SURVEY;

public class UploadEvidence {

    static HttpClient httpclient;

    /**
     * Httprequest para subir una evidencia con sus datos de cabecera y sus archivos (encuesta o fotografía)
     * @param FilePath dirección local del archivo de la evidencia
     * @param JSONEvidence objeto json que contiene información de la evidencia (comentario, timestamp, etiquetas, localización gps, tipo de evidcenica, id de usuario, id de capacitación)
     * @return
     */
    public String UploadEvidence(String FilePath, String JSONEvidence,int typeEvidence)
    {
        httpclient = new DefaultHttpClient();
        String str1="";
        try
        {

            String UPLOAD_SERVICE = "http://youilab.ipicyt.edu.mx:8080/sensky/api/v1/evidences/append";
            HttpPost localHttpPost = new HttpPost(UPLOAD_SERVICE);


            MultipartEntityBuilder localMultipartEntityBuilder = MultipartEntityBuilder.create();
            localHttpPost.setHeader("charset", "utf-8");//el conjunto de caracteres se especifica en utf-8
            localMultipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            localMultipartEntityBuilder.addBinaryBody("raw-data", convertToBase64(JSONEvidence).getBytes(Charset.forName("UTF-8")));//el json con datos de la evidencia se envian en una cadena en base 64, no en texto plano
            File localFile = new File(FilePath);//conseguir el archivo de la evidencia
            if (localFile.exists()) { //si existe el archivo se inicia el proceso de subida
                if(typeEvidence == SENSKY_EVIDENCE_TYPE_SURVEY)//sies una encuesta se incluye con la etiqueta file
                localMultipartEntityBuilder.addPart("file", new FileBody(localFile));
                else
                    localMultipartEntityBuilder.addPart("photo", new FileBody(localFile));//en caso de que sea una fotografía se incluye con la etiqueta Photo
            } else {
                System.out.println("El archivo no existe");
            }
            localHttpPost.setEntity(localMultipartEntityBuilder.build());
            String str2 = EntityUtils.toString(httpclient.execute(localHttpPost).getEntity());
            str1 = str2;

        }
        catch (Exception localException)
        {

            str1="ERROR";
            localException.printStackTrace();
        }
        return str1;
    }

    /**
     * Conversor de la informacion (objeto json) a base 64
     * @param jsonEvidence json con los datos de texto de la evidencia
     * @return String en base 64 con los datos de la evidencia
     */
    private String convertToBase64(String jsonEvidence)
    {
        byte[] bytesEncoded = Base64.encodeBase64(jsonEvidence.getBytes());
        return new String(bytesEncoded);
    }

}
