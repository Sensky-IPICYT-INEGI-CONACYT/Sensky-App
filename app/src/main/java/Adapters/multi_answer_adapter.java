package Adapters;


import SenSkyCore.Surveys.SurveyControl.Question;
import SenSkyCore.Surveys.SurveyControl.multipleOption;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import youilab.main.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static SenSkyCore.Surveys.SurveyControl.ParseSurveyV3.SURVEY_OPTION_CLEAN;
import static SenSkyCore.Surveys.SurveyControl.ParseSurveyV3.SURVEY_OPTION_OPEN;

public class multi_answer_adapter extends RecyclerView.Adapter<multi_answer_adapter.multioptionViewHolder> {

    private Activity activity;
    private ArrayList<multioptionViewHolder> views = new ArrayList<>();
    private List<multipleOption> listOptions;
    private Question questionAdapter;
    private Boolean bndClean= false;
    private Boolean bndSetAnswerAvailable= true;

    private AlertDialog dialog;

    /**
     * Constructor del adaptador pata la lista de respuestas multiples
     * @param paramQuestion objeto pregunta de respuesta multiple
     * @param paramActivity actividad donde se muestra la lista
     */
    public multi_answer_adapter(Question paramQuestion, Activity paramActivity)
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
    private void showDialogAnotheAnswer(final multioptionViewHolder multioptionHolder){
        //creación del cuadro de dialogo de captura de respuesta libre
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.dialog_another_answer, null);
        final EditText mOpenAnswer = (EditText)  mView.findViewById(R.id.key_training);


        //validación de entrada de caracteres aceptados
        mOpenAnswer.addTextChangedListener(new TextWatcher() {
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
                if (!bndAceptabletext || editable.length()>60)
                    editable.delete(editable.length()-1,editable.length());
            }
        });

    //boton de guardar las respuesta libre
        CardView mButtonNewTraining = (CardView) mView.findViewById(R.id.btnOk);
        mButtonNewTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOpenAnswer.getText().toString().length()>0) {//si contesto algo
                    dialog.dismiss();//ocultar el dialogo
                    multioptionHolder.tagValue.setText(mOpenAnswer.getText().toString());//mostrar la respuesta en pantalla
                    multioptionHolder.myOption.setAnswer("otro&&" + mOpenAnswer.getText().toString()); //guardar la respuesta en el objeto de pregunta con identificador (otro&&) que es respuesta libre
                    clearTypeClean();
                    selectAnswer(multioptionHolder);
                }else
                    Toast.makeText(activity,"Contesta",Toast.LENGTH_SHORT).show();
            }
        });

        //boton de cancelar las respuesta libre
        CardView mButtonCancel = (CardView) mView.findViewById(R.id.btnCancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                multioptionHolder.tagValue.setText("Otro");//restablecer la respuesta
                multioptionHolder.myOption.setAnswer("");//limpiar respuesta
                clearTypeClean();
                unSelectedItem(multioptionHolder);//se muestra como item no seleccionado

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
        paramtagViewHolder.cvDone.setCardBackgroundColor(j);
        questionAdapter.setQuestionAnswered(true);//la pregunta ha sido respondida

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
        paramtagViewHolder.cvDone.setCardBackgroundColor(j);

        if(paramtagViewHolder.myOption.getType().compareTo(SURVEY_OPTION_OPEN)==0)//si el tipo de respuesta es abierta
        {
            paramtagViewHolder.tagValue.setText("Otro"); //poner como respuesta "Otro"
            paramtagViewHolder.myOption.setAnswer("");//limpiar la respuesta de la opción
        }

        questionAdapter.setQuestionAnswered(false);//la pregunta no ha sido respondida

        for (multipleOption option: listOptions
             ) {
            if(option.getSelected())//si alguna de las opciones han sido respondidas se marca como una pregunta respondida
                questionAdapter.setQuestionAnswered(true);
        }

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
        multioptionViewHolder newView =  new multioptionViewHolder(LayoutInflater.from(paramViewGroup.getContext()), paramViewGroup, paramInt);

        views.add(newView);
        return  newView;
    }

/**
 * Vincular las acciones que tendra cada item de la lista
 * @param multioptionViewHolder manejador del item
 * @param i valor del index del item
 */
    @Override
    public void onBindViewHolder(@NonNull final multioptionViewHolder multioptionViewHolder, final int i) {
        multioptionViewHolder.myOption = ((multipleOption) this.listOptions.get(i));


        //cada vez que se refresca el recyclerview se establecen los valores que se meestran en pantalla
        if(multioptionViewHolder.myOption.getAnswer().contains("otro&&"))
            multioptionViewHolder.tagValue.setText(multioptionViewHolder.myOption.getAnswer().substring(6));
        else
            multioptionViewHolder.tagValue.setText(multioptionViewHolder.myOption.getAnswer());



        if (!multioptionViewHolder.myOption.getSelected()) {
            unSelectedItem(multioptionViewHolder);//se muestra como item no seleccionado
        } else if (multioptionViewHolder.myOption.getSelected()) {
            selectedItem(multioptionViewHolder);//se muestra en pantalla como el item seleecionado
        }

        if (bndClean && multioptionViewHolder.myOption.getType().compareTo(SURVEY_OPTION_CLEAN)!=0)//si la respuesta seleccionada no es de tipo clean y la opcion de limpiar esta activada, las demas se muestran como no seleccionadas
            unSelectedItem(multioptionViewHolder);


        //evento de click en le item individual de cada opción
        multioptionViewHolder.cv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView) {


                if (questionAdapter.getNumberSelected() != -1) {//si hay limite de respuestas para escoger
                    int counterSelected = 0;
                    for (multipleOption option : listOptions) {
                        if (option.getSelected())
                            counterSelected++;//conteo de respuestas escogidas
                    }
                    if(counterSelected == questionAdapter.getNumberSelected()) {//si el limite de respuestas lelgo al limite
                        bndSetAnswerAvailable = false;//estblecer que ya no se puede escogewr mas opciones
                    }
                    else {
                        bndSetAnswerAvailable = true;//se pueden escoger mas opciones
                    }

                    //acciones dependiendo del tipo de opción escigida
                    chooseTypeOption(multioptionViewHolder);
                }else//si no hay limite de respuestas
                {
                    chooseTypeOption(multioptionViewHolder);
                }
            }
        });


    }

    /**
     * Acciones dependiendo de la respuesta seleccionada
     * @param multioptionViewHolder manejador del la vista de opción
     */
    private void chooseTypeOption(multioptionViewHolder multioptionViewHolder){
        if (multioptionViewHolder.myOption.getType().compareTo(SURVEY_OPTION_OPEN) == 0) {//si es de texto abierto se abre el cuadro de dialogo para ingresar el texto
            bndClean = false;//no se limpiaran las demas opciones
            showDialogAnotheAnswer(multioptionViewHolder);
        } else if (multioptionViewHolder.myOption.getType().compareTo(SURVEY_OPTION_CLEAN) == 0) {//si la opcion es de tipo limpiar opciones (ejem... "ninguna de las anteriores")
            cleanItems();//limpiar opciones que no sea la seleccionada
            bndClean = true;// se limpiaran las demas opciones
            selectAnswer(multioptionViewHolder);//se selecciona la respuesta normalmente
        } else {
            bndClean = false;
            clearTypeClean();
            selectAnswer(multioptionViewHolder);//se selecciona la respuesta normalmente
        }
    }

    /**
     *notificar un cambio en in item
     * @param multioptionViewHolder
     */
    private void selectAnswer(multioptionViewHolder multioptionViewHolder){
        if (multioptionViewHolder.myOption.getSelected()) {
            unSelectedItem(multioptionViewHolder);
        } else if(bndSetAnswerAvailable){
            selectedItem(multioptionViewHolder);
        }
    }

    /**
     * limpiar todas las opciones de respuesta
     */
    private void cleanItems(){
        for (multioptionViewHolder view: views
        ) {
            unSelectedItem(view);
        }
    }

    /**
     * Se limpia la la opcion que sea de tipo clean si se selecciona otra diferente (ya no aplica "ninguna de las anteriores")
     */
    private void clearTypeClean()
    {
        for (multioptionViewHolder view: views
        ) {
            if(view.myOption.getType().compareTo(SURVEY_OPTION_CLEAN)==0)
            unSelectedItem(view);
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
        if( paramtagViewHolder.myOption.getSelected())
            selectedItem(paramtagViewHolder);
        else
            unSelectedItem(paramtagViewHolder);
    }


    /**
     * Para poder realizar alguna acción cuando nuestro item de lista entra a la pantalla (como mostrar una imagen )
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(@NonNull multioptionViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if( holder.myOption.getSelected())
            selectedItem(holder);
        else
            unSelectedItem(holder);
    }

    /**
     * Manejador de la vista para poder declarar los componentes que tiene nuestro item
     */
    static class multioptionViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout backColor;
        CardView cv;
        CardView cvDone;
        multipleOption myOption;
        TextView tagValue;

        public multioptionViewHolder(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, int paramInt) {
            super(paramLayoutInflater.inflate(R.layout.item_multiple_answer,paramViewGroup,false));

             backColor = (RelativeLayout)this.itemView.findViewById(R.id.itemTagBackground);
             cv = (CardView)this.itemView.findViewById(R.id.itemTagCv);
             cvDone = (CardView) this.itemView.findViewById(R.id.cvImage);
             tagValue = (TextView)this.itemView.findViewById(R.id.itemTagText);
        }
    }
}
