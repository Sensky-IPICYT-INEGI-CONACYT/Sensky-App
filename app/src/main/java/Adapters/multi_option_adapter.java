package Adapters;


import SenSkyCore.Surveys.SurveyControl.ParseSurveyV3;
import SenSkyCore.Surveys.SurveyControl.Question;
import SenSkyCore.Surveys.SurveyControl.multipleOption;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import youilab.main.R;

import java.util.List;
import java.util.regex.Pattern;

import static SenSkyCore.Surveys.SurveyControl.ParseSurveyV3.SURVEY_OPTION_OPEN;

public class multi_option_adapter extends RecyclerView.Adapter<multi_option_adapter.multioptionViewHolder> {

    private Activity activity;
    private int checkedPosition = -1;
    private List<multipleOption> listOptions;
    private Question questionAdapter;
    AlertDialog dialog;


    /**
     * Constructor del adaptador pata la lista de opcion multiple
     * @param paramQuestion objeto pregunta de opcion multiple
     * @param paramActivity actividad donde se muestra la lista
     */
    public multi_option_adapter(Question paramQuestion, Activity paramActivity)
    {
        this.listOptions = paramQuestion.getListMultipleOptions();
        this.activity = paramActivity;
        this.questionAdapter = paramQuestion;
        notifyDataSetChanged();//se notifica que se cambia un conjunto de datos
    }

    /**
     * Mostrar un cuadro de dialogo cuando se lecciona una respuesta de tipo texto abierto
     * @param multioptionHolder
     */
    private void showDialogAnotheAnswer(final multioptionViewHolder multioptionHolder, final int i){
        //creación del cuadro de dialogo de captura de respuesta libre
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.dialog_another_answer, null);
        final EditText mKey = (EditText)  mView.findViewById(R.id.key_training);

        //validación de entrada de caracteres aceptados
        mKey.addTextChangedListener(new TextWatcher() {
            boolean bndAceptabletext=true;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bndAceptabletext= Pattern.matches("^[a-zA-Z0-9ñ ÑáÁéÉíóÍÓúÚ]*$", charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!bndAceptabletext || editable.length()>15)
                    editable.delete(editable.length()-1,editable.length());
            }
        });

        //boton de guardar las respuesta libre
        Button mButtonNewTraining = (Button) mView.findViewById(R.id.btnNewTraining);
        mButtonNewTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//ocultar el dialogo
                multioptionHolder.tagValue.setText(mKey.getText().toString());//mostrar la respuesta en pantalla
                multioptionHolder.myOption.setAnswer("otro&&"+mKey.getText().toString());//guardar la respuesta en el objeto de pregunta con identificador (otro&&) que es respuesta libre
                selectAnswer(multioptionHolder, i);
            }
        });

        //boton de cancelar las respuesta libre
        Button mButtonCancel = (Button) mView.findViewById(R.id.btnCancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    /**
     * Modifica en la vista los colores y textos cuando un item esta seleccionado
     * @param paramtagViewHolder Manejador del item que no esta seleccionado
     */
    private void selectedItem(multioptionViewHolder paramtagViewHolder)
    {
        int j;
        if (Build.VERSION.SDK_INT <= 23)
        {
            j = this.activity.getResources().getColor(R.color.colorPrimary);
        }
        else
        {
            j = this.activity.getResources().getColor(R.color.colorPrimary, null);
        }
        paramtagViewHolder.myOption.setSelected(true);//especifica en el objeto del item que esta seleccionado
        this.questionAdapter.setQuestionAnswered(true);
        paramtagViewHolder.cvCircle.setCardBackgroundColor(j);
    }

    /**
     * Modifica en la vista los colores y textos cuando un item no esta seleccionado
     * @param paramtagViewHolder Manejador del item que no esta seleccionado
     */
    private void unSelectedItem(multioptionViewHolder paramtagViewHolder)
    {
        int j;
        if (Build.VERSION.SDK_INT <= 23)
        {
            j = this.activity.getResources().getColor(R.color.white);
        }
        else
        {
            j = this.activity.getResources().getColor(R.color.white, null);
        }
        paramtagViewHolder.myOption.setSelected(false);// la respuesta no ha sido seleccionada
        paramtagViewHolder.cvCircle.setCardBackgroundColor(j);
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
        multioptionViewHolder.myOption = ((multipleOption)this.listOptions.get(i));
        multioptionViewHolder.tagValue.setText(multioptionViewHolder.myOption.getAnswer());

        if (this.checkedPosition == -1) {//si no ha sido seleccionada alguna opción
            unSelectedItem(multioptionViewHolder);
        } else{
            if (this.checkedPosition == i)//si la seleccionada tiene el mismo index se cambia en pantalla
                selectedItem(multioptionViewHolder);
            else
                unSelectedItem(multioptionViewHolder);
        }

        //evento de click en le item individual de cada opción

        multioptionViewHolder.cv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {



                if( multioptionViewHolder.myOption.getType().compareTo(SURVEY_OPTION_OPEN)!=0) {//si la opción seleccionada no es de texto abierto
                    selectAnswer(multioptionViewHolder,i);
                }else{
                    showDialogAnotheAnswer(multioptionViewHolder, i);//si es de texto abierto se abre el cuadro de dialogo
                }

            }
        });
    }

    /**
     * notificar un cambio en in item
     * @param paramViewHolder
     * @param i index del item seleccionado
     */
    private void selectAnswer(multioptionViewHolder paramViewHolder ,int i){
        selectedItem(paramViewHolder);
        if (checkedPosition != i) {
            notifyItemChanged(checkedPosition);
            checkedPosition = i;
        }
    }

    /**
     * Devuelve el numero de items en la lista que se quieres mostrar
     * @return numero de items en la lista
     */
    @Override
    public int getItemCount() {
        return this.listOptions.size();
    }

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
        RelativeLayout backColor;
        CardView cv;
        CardView cvCircle;
        multipleOption myOption;
        TextView tagValue;

        public multioptionViewHolder(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, int paramInt) {
            super(paramLayoutInflater.inflate(R.layout.item_multiple_option,paramViewGroup,false));

             backColor = (RelativeLayout)this.itemView.findViewById(R.id.itemTagBackground);
             cv = (CardView)this.itemView.findViewById(R.id.itemTagCv);
             cvCircle = (CardView) this.itemView.findViewById(R.id.circle_selected);
             tagValue = (TextView)this.itemView.findViewById(R.id.itemTagText);
        }
    }
}
