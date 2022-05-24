package SenSkyCore.Avatars;

import SenSkyCore.DataBase.DataBaseCore.DataBaseCoreImpl;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class AvatarsDAOImpl implements AvatarsDAO{

    //Lista de avatars registrados en el dispositivo
    private List<Avatar> avatarList;

    //necesario para realizar queries en la base de datos
    private Context context;

    //Manejador de la base de datos
    private DataBaseCoreImpl dataBaseCore;

    /**
     * Constructor de la implementación que recibe el contexto de la aplicación, se inicializa la lista de avatares y la instancia al manejador de la base de datos
     * @param context
     */
    public AvatarsDAOImpl(Context context)
    {
        this.avatarList = new ArrayList<>();//inicialización de lista de avatares
        this.dataBaseCore = new DataBaseCoreImpl(context);//manejador de base de datos
    }

    /**
     * duvuelve una lista con todos los avatres en el dispositivo
     * @return
     */
    @Override
    public List<Avatar> getAllAvatars() {

        return createAvatarListObjects(dataBaseCore.getCursorAllAvatars());
    }

    /**
     * obtiene al infomación de un determinado avatar
     * @param index indice de registro en el dispositivo
     * @return
     */
    @Override
    public Avatar getAvatar(int index) {
        Cursor cursorAvatar = dataBaseCore.getCursorAvatar(index);
        cursorAvatar.moveToFirst();
        Avatar avatar = createAvatarObject(cursorAvatar);
        cursorAvatar.close();//cierra el cursor de consulta
        dataBaseCore.closeDataBaseReadable();//cierra la conexión de base de datos
        return avatar;
    }

    /**
     * registro de un nuevo avatar en la base de datos
     * @param ID
     * @param name
     * @param imagePath
     */
    @Override
    public void createAvatar(String ID, String name, String imagePath) {
        dataBaseCore.createAvatar(ID,name,"",imagePath);
        dataBaseCore.closeDataBaseWritable();
    }

    /**
     * Al seleccionar un avatar este es señalado como el que esta siendo usado
     * @param index
     */
    @Override
    public void activateAvatar(int index) {
        dataBaseCore.activateAvatar(index);
        dataBaseCore.closeDataBaseWritable();
    }

    /**
     * Obtiene la informacion del avatr que esta en uso
     * @return objeto avatar con información
     */
    @Override
    public Avatar getCurrenAvatar() {
        Cursor cursorAvatar = dataBaseCore.getActivatedAvatar();
        cursorAvatar.moveToFirst();
        Avatar currentAvatar = createAvatarObject(cursorAvatar);
        cursorAvatar.close();//cierra el cursor de consulta
        dataBaseCore.closeDataBaseReadable();//cierra la conexión de base de datos
        return currentAvatar;
    }


    /**
     * Se iteran los registros de cada avatr y se obtiene la lista de avatares
     * @param cursor
     * @return
     */
    private List<Avatar> createAvatarListObjects(Cursor cursor){
        avatarList = new ArrayList<>();
        while (cursor.moveToNext()) {
            avatarList.add(createAvatarObject(cursor));
        }
        cursor.close();//se cierra el cursor de consulta
        dataBaseCore.closeDataBaseReadable();//cierra la conexión a la base de datos
        return avatarList;
    }

    /**
     * Del cursor se extrae la informacionn y se crea un objeto Avatar
     * @param cursor
     * @return
     */
    private Avatar createAvatarObject(Cursor cursor){
        Avatar avatar = new Avatar();
        avatar.setIndex(cursor.getInt(0));
        avatar.setID(cursor.getString(1));
        avatar.setName(cursor.getString(2));
        avatar.setImagePath(cursor.getString(3));
        avatar.setSerialFlow(cursor.getString(4));
        avatar.setActived(cursor.getInt(5));

        return avatar;
    }
}
