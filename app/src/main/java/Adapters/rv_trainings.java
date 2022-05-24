package Adapters;

import MVP.Interfaces.Trainings.TrainingActivateView;
import MVP.Interfaces.Trainings.TrainingPresenter;
import MVP.Presenters.TrainingsPresenterImpl;
import SenSkyCore.Trainings.Training;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import youilab.main.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by leonardo on 30/10/2017.
 */

public class rv_trainings extends RecyclerView.Adapter<rv_trainings.TrainingViewHolder> implements TrainingActivateView{


    static List<Training> trainingList;
    private TrainingPresenter trainingPresenter;
    Context mContext;
    Activity mActivity;
    static String USER;

    AlertDialog dialog;

    int itemActivated= -1;



    /**
     * Constructor del adaptador que presenta la información del reto en la aplicación

     * @param trainings Lista de capacitaciones del usuario
     * @param context Contexto de la aplicación en la que se presenta el adaptador
     */
    public rv_trainings(List<Training> trainings,Activity activity, Context context){
        this.trainingList = trainings;
        this.mContext=context;
        this.mActivity = activity;
        trainingPresenter = new TrainingsPresenterImpl(this, context);
        notifyDataSetChanged();//notificar cuando el conjunto de datos cambien
    }

    @Override
    public void activatedTraining(boolean result) {

    }

    /**
     * Manejador de la vista de un reto, muestra información e incluye un evento cuando se da click en la vista
     */
    public static class TrainingViewHolder extends RecyclerView.ViewHolder  {
        CardView cv;
        Training  myTraining;
        TextView trainingNamePlace;
        TextView trainingEmail;
        TextView trainingDate;

        View line;



        TrainingViewHolder(final LayoutInflater inflater, ViewGroup parent, final int position) {
            super(inflater.inflate(R.layout.item_training, parent, false));
            cv = (CardView)itemView.findViewById(R.id.card_view);

            trainingNamePlace = (TextView)itemView.findViewById(R.id.training_title);

            trainingEmail = (TextView) itemView.findViewById(R.id.training_email);
            trainingDate = (TextView) itemView.findViewById(R.id.training_date);

            line = (View) itemView.findViewById(R.id.line);

        }


    }

    /**
     * Devuelve la cantidad de elementos que se mostrarán
     * @return Valor de cantidad de elementos
     */
    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    /**
     * Crea el maejador de la vista de un reto
     * @param viewGroup Grupo de vista el que pertenece
     * @param i Posición de la vista
     * @return Regresa el manejador de la vista
     */
    @Override
    public TrainingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater v = LayoutInflater.from(viewGroup.getContext());
        return new TrainingViewHolder(v,viewGroup,i);

    }

    /**
     * Marca la acciones que tomarán de forma individual cada casilla en la que se muestran los retos
     * @param ViewHolder Componente donde se muestra la información del reto
     * @param i Posición de la vista del reto
     */
    @Override
    public void onBindViewHolder(final TrainingViewHolder ViewHolder,final int i) {

        ViewHolder.trainingNamePlace.setText(trainingList.get(i).getPlaceName());
        ViewHolder.myTraining = trainingList.get(i);
        ViewHolder.trainingEmail.setText(trainingList.get(i).getEmailTeam());



       if( this.itemActivated==-1) {//si no ha sido seleccionada alguna opción

           if(trainingList.get(i).getStatus()==1) {//si la capacitación esta habilitada por defecto
               this.itemActivated = i;
               selectAnswer(ViewHolder,i);//establecer como seleccionada
           }else
               unselectedItem(ViewHolder);//no es una opcion seleccionada por defecto
       }else {
           if (this.itemActivated == i) //si la seleccionada tiene el mismo index se cambia en pantalla
               selectedItem(ViewHolder);
           else
               unselectedItem(ViewHolder);
       }



        Date currentDate = new Date(trainingList.get(i).getTimeStampRunTraining());//fecha de la capacitación

        DateFormat df = new SimpleDateFormat("dd/MM/yy hh:mm a");//fecha en formato para mostrar
        ViewHolder.trainingDate.setText(df.format(currentDate));

        if (System.currentTimeMillis() > trainingList.get(i).getTimeStampRunTraining())//si la fecha de la capicitación ya corresponde con la del dispositivo
            ViewHolder.line.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));//se puede seleccionar la capacitación para usarse


        //evento de click para cada item de capacitación
        ViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (System.currentTimeMillis() > trainingList.get(i).getTimeStampRunTraining()){//se puede seleccionar solo si la fecha del dispositvo es mayor que la del inicio de la capacitación
                    trainingPresenter.ActivateTraining(ViewHolder.myTraining.getKey());
                    selectAnswer(ViewHolder,i);
                }else
                    Toast.makeText(mContext,"Aún no está disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Atrapa la vista donde se mostratrán las capacitaciones del usuario
     * @param recyclerView Vista donde se prensentan los retos
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    /**
     * Realizar el cambio en la vista del item que seleccionado
     * @param paramtagViewHolder
     */
    private void selectedItem(TrainingViewHolder paramtagViewHolder)
    {
        paramtagViewHolder.trainingDate.setTextColor(mContext.getResources().getColor(R.color.white));
        paramtagViewHolder.trainingEmail.setTextColor(mContext.getResources().getColor(R.color.white));
        paramtagViewHolder.trainingNamePlace.setTextColor(mContext.getResources().getColor(R.color.white));
        paramtagViewHolder.cv.setCardBackgroundColor(mContext.getResources().getColor(R.color.blue));
        paramtagViewHolder.myTraining.setStatus(1);
    }

    /**
     * Realizar el cambio en la vista del item que no esta seleccionado
     * @param paramtagViewHolder
     */
    private void unselectedItem(TrainingViewHolder paramtagViewHolder)
    {

        paramtagViewHolder.trainingDate.setTextColor(mContext.getResources().getColor(R.color.grey));
        paramtagViewHolder.trainingEmail.setTextColor(mContext.getResources().getColor(R.color.black));
        paramtagViewHolder.trainingNamePlace.setTextColor(mContext.getResources().getColor(R.color.black));
        paramtagViewHolder.cv.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));

        paramtagViewHolder.myTraining.setStatus(0);
    }


    /**
     * Para el cambio de la capacitación seleccionada y notificar que el item en el recyclerview a sido cambiado
     * @param paramViewHolder el manejador de la vista que ha sido seleccionado
     * @param i el indice de posición del item seleccionado
     */
    private void selectAnswer(TrainingViewHolder paramViewHolder , int i){

        selectedItem(paramViewHolder);
        if (this.itemActivated != i) {
            notifyItemChanged(this.itemActivated);//notificar que el objeto en el indice itemActivated a cambiado en algo
            this.itemActivated = i;//asignar como itemselected como el nuevo
        }
    }

}