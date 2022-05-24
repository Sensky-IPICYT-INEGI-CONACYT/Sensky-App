package Adapters;


import SenSkyCore.Surveys.SurveyControl.Question;
import SenSkyCore.Surveys.SurveyControl.multipleOption;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import youilab.main.R;

import java.util.List;

public class lickert_group_adapter extends RecyclerView.Adapter<lickert_group_adapter.multioptionViewHolder> {

    private Activity activity;
    private int checkedPosition = -1;
    private boolean bndAnswered=true;
    private List<multipleOption> listOptions;
    private Question questionAdapter;

    private String note="";

    /**
     * Adaptador de las opciones de una pregunta lickert de varias opciones
     * @param paramQuestion objeto pregunta de varias opciones lickert
     * @param paramActivity actividad donde se muestra la lista
     */
    public lickert_group_adapter(Question paramQuestion, Activity paramActivity)
    {
        this.listOptions = paramQuestion.getListMultipleOptions();
        this.activity = paramActivity;
        this.note = paramQuestion.getNote();
        this.questionAdapter = paramQuestion;
        notifyDataSetChanged();//se notifica que se cambia un conjunto de datos
    }

    /**
     * Modifica en la vista los colores y textos cuando un item esta seleccionado
     * @param paramtagViewHolder Manejador del item que no esta seleccionado
     */
    private void selectedItem(multioptionViewHolder paramtagViewHolder)
    {
        bndAnswered=true;
        paramtagViewHolder.myOption.setSelected(true);//especifica en el objeto del item que esta seleccionado
        paramtagViewHolder.valueSelected.setText(paramtagViewHolder.myOption.getValue()); //se muestra la respuesta seleccionada en pantalla
        questionAdapter.setAnswer(createAnswer(listOptions)); //se crea en formato el conjunto de respuestas que se van creando
        if(bndAnswered)
            questionAdapter.setQuestionAnswered(true);
    }

    /**
     * Modifica en la vista los colores y textos cuando un item no esta seleccionado
     * @param paramtagViewHolder Manejador del item que no esta seleccionado
     */
    private void unselectedItem(multioptionViewHolder paramtagViewHolder)
    {
        paramtagViewHolder.myOption.setSelected(false);
        paramtagViewHolder.valueSelected.setText("Vacío");
    }

    /**
     * Se crea una respuesta compuesta de los valores seleccioandos para cada opción de la pregunta lickert
     * @param listOptions lista de opciones de la pregunta con sus respuestas
     * @return string con el conjunto de respuestas de las opciones
     */
    private String createAnswer(List<multipleOption> listOptions)
    {
        StringBuilder answer= new StringBuilder();
        for (multipleOption option:listOptions
             ) {
            if(option.getSelected())// si la opción tiene una respuesta seleccionada
            {
                answer.append(option.getAnswer()).append(":").append(option.getValue()).append("|");// se concatena la respuesta-valor de cada una a la respuesta general
            }else
                bndAnswered=false;
        }
        return answer.toString();
    }

    /**
     * Se prepara el manejador de la vista cuando se crea para cada uno
     * @param paramViewGroup
     * @param paramInt
     * @return manejador del la vista del item
     */
    @NonNull
    public multioptionViewHolder onCreateViewHolder(@NonNull ViewGroup paramViewGroup, int paramInt)
    {
        return new multioptionViewHolder(LayoutInflater.from(paramViewGroup.getContext()), paramViewGroup, paramInt);
    }


    /**
     * Vincular las acciones que tendra cada item de la lista
     * @param multioptionViewHolder manejador del item
     * @param i valor del index del item
     */
    @Override
    public void onBindViewHolder(@NonNull final multioptionViewHolder multioptionViewHolder, final int i) {
        multioptionViewHolder.myOption = ((multipleOption)this.listOptions.get(i));//item de la lista a vincular
        multioptionViewHolder.tagValue.setText(multioptionViewHolder.myOption.getAnswer());//al textview se pone las respuesta correspondiente

        if(multioptionViewHolder.myOption.getSelected())// si esta seleccionado
            selectedItem(multioptionViewHolder); //se muestra en pantalla como el item seleecionado
       else
           unselectedItem(multioptionViewHolder);//se muestra como item no seleccionado
//evento de click en le item individual de cada opcion de respuesta
        multioptionViewHolder.cv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {

               //se muestra un cuadro de dialogo para para opcion con la lista de respuestas a escoger
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(activity);
                localBuilder.setTitle("Posibles respuestas");
                localBuilder.setItems(note.split(","), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                        multioptionViewHolder.myOption.setValue(String.valueOf(paramAnonymous2Int)); //se guarda el valor de las respuesta a la opción
                        selectedItem(multioptionViewHolder); //se muestra en pantalla como el item seleecionado

                        if (checkedPosition != i)
                        {
                            notifyItemChanged(checkedPosition);//se notifica el item seleccionado
                            checkedPosition = i;
                        }
                    }
                });
                localBuilder.show();
            }
        });
    }

    /**
     * Devuelve el numero de items en la lista que se quieres mostrar
     * @return numero de items en la lista
     */
    @Override
    public int getItemCount() {
        return this.listOptions.size();
    }

    /**
     * cuando la vista se agrega a la vista del recyclerview
     * @param paramRecyclerView
     */
    public void onAttachedToRecyclerView(@NonNull RecyclerView paramRecyclerView)
    {
        super.onAttachedToRecyclerView(paramRecyclerView);
    }

    /**
     * Para poder realizar alguna acción cuando nuestro item de lista sale de la pantalla (como borrar una imagen para ahorrar memoria)
     * @param paramtagViewHolder
     */
    public void onViewDetachedFromWindow(@NonNull multioptionViewHolder paramtagViewHolder)
    {
        super.onViewDetachedFromWindow(paramtagViewHolder);
        paramtagViewHolder.itemView.clearAnimation();
    }

    /**
     * Manejador de la vista para poder declarar los componentes que tiene nuestro item
     */
    static class multioptionViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        multipleOption myOption;
        TextView tagValue;
        TextView valueSelected;

        public multioptionViewHolder(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, int paramInt) {
            super(paramLayoutInflater.inflate(R.layout.item_lickert_group,paramViewGroup,false));

             cv = (CardView)this.itemView.findViewById(R.id.itemLickertCv);
             tagValue = (TextView)this.itemView.findViewById(R.id.itemLickertText);
             valueSelected = (TextView) this.itemView.findViewById(R.id.lblValueLickert);
        }
    }
}
