package Adapters;


import SenSkyCore.Surveys.SurveyControl.Tag;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import youilab.main.R;

import java.util.ArrayList;
import java.util.List;


public class tag_adapter extends RecyclerView.Adapter<tag_adapter.tagViewHolder> {

    private Activity activity;
    private int checkedPosition = -1;
    private List<Tag> listTags;
    public static final int TAG_SELECTABLE = 1;
    public static final int TAG_NOT_SELECTABLE =0;

    private int TAG_TYPE = 1;

    /**
     * Adaptador para las etiquetas que se muestran al tomar una fotografia y poder asiganrle etiquetas
     * @param tagList lista de etiquetas
     * @param typeTag tipo de etiqueta (para seleccioanr o solo para ver)
     * @param paramActivity actividad que llama al adaptador
     */
    public tag_adapter(List<Tag> tagList,int typeTag, Activity paramActivity)
    {
        this.listTags = tagList;
        this.activity = paramActivity;
        this.TAG_TYPE = typeTag;
        notifyDataSetChanged();//se notifica que se cambia un conjunto de datos
    }

    /**
     * Modifica en la vista los colores y textos cuando un item esta seleccionado
     * @param paramtagViewHolder Manejador del item que no esta seleccionado
     */
    private void selectedItem(tagViewHolder paramtagViewHolder)
    {
        int i;
        int j;
        if (Build.VERSION.SDK_INT <= 23)
        {
            i = this.activity.getResources().getColor(R.color.white);
            j = this.activity.getResources().getColor(R.color.colorPrimary);
        }
        else
        {
            i = this.activity.getResources().getColor(R.color.white, null);
            j = this.activity.getResources().getColor(R.color.colorPrimary, null);
        }
        paramtagViewHolder.myOption.setSelected(true);//especifica en el objeto del item que esta seleccionado
        paramtagViewHolder.backColor.setBackgroundColor(j);
        paramtagViewHolder.tagValue.setTextColor(i);
    }

    /**
     * Modifica en la vista los colores y textos cuando un item no esta seleccionado
     * @param paramtagViewHolder Manejador del item que no esta seleccionado
     */
    private void unSelectedItem(tagViewHolder paramtagViewHolder)
    {
        int i;
        int j;
        if (Build.VERSION.SDK_INT <= 23)
        {
            i = this.activity.getResources().getColor(R.color.greyDark);
            j = this.activity.getResources().getColor(R.color.white);
        }
        else
        {
            i = this.activity.getResources().getColor(R.color.greyDark, null);
            j = this.activity.getResources().getColor(R.color.white, null);
        }
        paramtagViewHolder.myOption.setSelected(false);// la etiqueta no ha sido seleccionada
        paramtagViewHolder.backColor.setBackgroundColor(j);
        paramtagViewHolder.tagValue.setTextColor(i);
    }

    /**
     * Se prepara el manejador de la vista cuando se crea para cada uno
     * @param paramViewGroup
     * @param paramInt
     * @return manejador del la vista del item
     */
    @NonNull
    public tagViewHolder onCreateViewHolder(@NonNull ViewGroup paramViewGroup, int paramInt)
    {
        return new tagViewHolder(LayoutInflater.from(paramViewGroup.getContext()), paramViewGroup, paramInt);
    }


    /**
     * Vincular las acciones que tendra cada item de la lista
     * @param multioptionViewHolder manejador del item
     * @param i valor del index del item
     */
    @Override
    public void onBindViewHolder(@NonNull final tagViewHolder multioptionViewHolder, final int i) {
        multioptionViewHolder.myOption = ((Tag)this.listTags.get(i));
        multioptionViewHolder.tagValue.setText(multioptionViewHolder.myOption.getValue());


        if(TAG_TYPE == TAG_SELECTABLE) {//si la etiqueta es para seleccionar la opción

            if (!multioptionViewHolder.myOption.getSelected()) {// si  no esta seleccionada
                unSelectedItem(multioptionViewHolder);//se muetra con los colores de una etiqueta no seleccionada
            } else if (multioptionViewHolder.myOption.getSelected()) { //si esta selecionada
                selectedItem(multioptionViewHolder);//se muestra como seleccionada
            }


            //se asgian el evento de click a una eqiqueta de seleccion
            multioptionViewHolder.cv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    if (multioptionViewHolder.myOption.getSelected()) {
                        unSelectedItem(multioptionViewHolder);
                    } else {
                        selectedItem(multioptionViewHolder);

                    }
                }
            });
        }
    }


    /**
     * Devuelve el numero de items en la lista que se quieres mostrar
     * @return numero de items en la lista
     */
    @Override
    public int getItemCount() {
        return this.listTags.size();
    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView paramRecyclerView)
    {
        super.onAttachedToRecyclerView(paramRecyclerView);
    }

    public void onViewDetachedFromWindow(@NonNull tagViewHolder paramtagViewHolder)
    {
        super.onViewDetachedFromWindow(paramtagViewHolder);
        paramtagViewHolder.itemView.clearAnimation();
    }

    /**
     * Manejador de la vista para poder declarar los componentes que tiene nuestro item
     */
    static class tagViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout backColor;
        CardView cv;
        Tag myOption;
        TextView tagValue;

        public tagViewHolder(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, int paramInt) {
            super(paramLayoutInflater.inflate(R.layout.item_tag,paramViewGroup,false));

             backColor = (RelativeLayout)this.itemView.findViewById(R.id.itemTagBackground);
             cv = (CardView)this.itemView.findViewById(R.id.cvItemTag);
             tagValue = (TextView)this.itemView.findViewById(R.id.itemTagText);
        }
    }
}
