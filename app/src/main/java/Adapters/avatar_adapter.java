package Adapters;

import MVP.Interfaces.NewAvatar.AvatarsPresenter;

import SenSkyCore.Avatars.Avatar;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import youilab.main.R;

import java.util.List;

public class avatar_adapter extends RecyclerView.Adapter<avatar_adapter.avatarViewHolder> {

    private Activity activity;
    private int itemActivated = -1;
    private List<Avatar> avatarList;

    private AvatarsPresenter presenter;


    /**
     * Constructor del adaptador pata la lista de avatares
     * @param avatarList lista de avatars
     * @param paramActivity actividad donde se muestra la lista
     * @param presenter presentador (MVP) que maneja las acciones en torno de los avatares
     */
    public avatar_adapter(List<Avatar> avatarList, Activity paramActivity, AvatarsPresenter presenter)
    {
        this.avatarList = avatarList;
        this.activity = paramActivity;
        this.presenter = presenter;
        notifyDataSetChanged();//se notifica que se cambia un conjunto de datos
    }


    /**
     * Se prepara el manejador de la vista cuando se crea para cada uno
     * @param paramViewGroup
     * @param paramInt
     * @return manejador del la vista del item
     */
    @NonNull
    public avatarViewHolder onCreateViewHolder(@NonNull ViewGroup paramViewGroup, int paramInt)
    {
        return new avatarViewHolder(LayoutInflater.from(paramViewGroup.getContext()), paramViewGroup, paramInt);
    }

    /**
     * Modifica en la vista los colores y textos cuando un item esta seleccionado
     * @param profileViewHolder Manejador del item que no esta seleccionado
     */
    private void selectedItem(avatarViewHolder  profileViewHolder)
    {
        int j,k;
        if (Build.VERSION.SDK_INT <= 23)
        {
            j = this.activity.getResources().getColor(R.color.blue_light);
            k = this.activity.getResources().getColor(R.color.white);
        }
        else
        {
            j = this.activity.getResources().getColor(R.color.blue_light, null);
            k = this.activity.getResources().getColor(R.color.white,null);
        }
        profileViewHolder.avatarItem.setActived(1);//especifica en el objeto del item que esta seleccionado
        profileViewHolder.avatar.setTextColor(k);
        profileViewHolder.cv.setCardBackgroundColor(j);
    }

    /**
     * Modifica en la vista los colores y textos cuando un item no esta seleccionado
     * @param profileViewHolder Manejador del item que no esta seleccionado
     */
    private void unSelectedItem(avatarViewHolder profileViewHolder)
    {
        int j,k;
        if (Build.VERSION.SDK_INT <= 23)
        {
            j = this.activity.getResources().getColor(R.color.white);
            k = this.activity.getResources().getColor(R.color.greyDark);
        }
        else
        {
            j = this.activity.getResources().getColor(R.color.white, null);
            k = this.activity.getResources().getColor(R.color.greyDark,null);
        }

        profileViewHolder.avatarItem.setActived(0);//especifica en el objeto del item que no esta seleccionado
        profileViewHolder.avatar.setTextColor(k);
        profileViewHolder.cv.setCardBackgroundColor(j);
    }

    /**
     * Vincular las acciones que tendra cada item de la lista
     * @param profileViewHolder manejador del item
     * @param i valor del index del item
     */
    @Override
    public void onBindViewHolder(@NonNull final avatarViewHolder profileViewHolder, final int i) {

        profileViewHolder.avatarItem = avatarList.get(i);//item de la lista a vincular
        profileViewHolder.avatar.setText(profileViewHolder.avatarItem.getName()); //al textview del avatar se pone el nombre correspondiente

        Uri imgProfile= Uri.parse(profileViewHolder.avatarItem.getImagePath());//se obtiene el Uri de la imagen del avatar
        Glide.with(activity).load(imgProfile).into(profileViewHolder.img);//con Glide se carga la imagen en el ImageView



        if( this.itemActivated==-1) { //si nungun avatar a sido seleccionado

            if(avatarList.get(i).getActived()==1) { //si el avatar esta selecccionado por defecto
                this.itemActivated = i;//se establece el index del item seleccionado por defecto
                selectItem(profileViewHolder,i);//se notifica como el item seleccionado
            }else
                unSelectedItem(profileViewHolder);//si no esta por defecto no se resalta en la pantalla
        }else {//significa que algun item ha sido seleccionado por el usuario
            if (this.itemActivated == i) //si el item seleccionado tiene el mismo index que el de este item se refresca en la pantalla que vez que se mueve la lista
                selectedItem(profileViewHolder); //se muestra en pantalla como el item seleecionado
            else
                unSelectedItem(profileViewHolder);//se muestra como item no seleccionado
        }



        //evento de click en le item individual de cada avatar
        profileViewHolder.cv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {

                presenter.isActiveAvatar(profileViewHolder.avatarItem.getIndex());//habilta en la base de datos que avatar ha sido seleccionado
                selectItem(profileViewHolder,i);//se notifica que un item a sido seleccionado
            }
        });
    }

    /**
     * Notificar que un item a sido seleccionado de la lista por el usuario
     * @param paramViewHolder manejador del item
     * @param i valor del index del item seleccionado por el usuario
     */
    private void selectItem(avatarViewHolder paramViewHolder , int i){

        if (itemActivated != i) { //si el item no estaba seleccionado se notifica
            notifyItemChanged(itemActivated);
            itemActivated = i;
        }
        selectedItem(paramViewHolder); // se resalta en la pantalla el item seleccionado
    }

    /**
     * Devuelve el numero de items en la lista que se quieres mostrar
     * @return numero de items en la lista
     */
    @Override
    public int getItemCount() {
        return this.avatarList.size();
    }



    /**
     * Para poder realizar alguna acción cuando nuestro item de lista sale de la pantalla (como borrar una imagen para ahorrar memoria)
     * @param paramAvatarViewHolder
     */
    public void onViewDetachedFromWindow(@NonNull avatarViewHolder paramAvatarViewHolder)
    {
        super.onViewDetachedFromWindow(paramAvatarViewHolder);
        paramAvatarViewHolder.itemView.clearAnimation();
    }


    /**
     * Manejador de la vista para poder declarar los componentes que tiene nuestro item
     */
    static class avatarViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView avatar;
        ImageView img;
        Avatar avatarItem;

        public avatarViewHolder(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, int paramInt) {
            super(paramLayoutInflater.inflate(R.layout.item_avatar,paramViewGroup,false));

             cv = (CardView)this.itemView.findViewById(R.id.cvItemAvatar);
             avatar = (TextView)this.itemView.findViewById(R.id.itemAvatarName);
             img = (ImageView) this.itemView.findViewById(R.id.imgAvatar);
        }
    }
}
