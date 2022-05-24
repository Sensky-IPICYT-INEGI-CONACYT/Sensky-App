package Adapters;

import MVP.Interfaces.EvidenceView;
import SenSkyCore.Evidences.Evidence;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import youilab.main.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static AppTools.DirectoryManager.SENSKY_EVIDENCE_TYPE_PHOTO;

/**
 * Created by leonardo on 30/10/2017.
 */

public class rv_evidences extends RecyclerView.Adapter<rv_evidences.EvidenceViewHolder>{


    static List<Evidence> Evidences;
    Activity mContext;
    EvidenceView evidenceView;


    /**
     * Constructor del adaptador que presenta la información del reto en la aplicación

     * @param evidenceList Lista de evidencias del usuario
     * @param activity Contexto de la aplicación en la que se presenta el adaptador
     */
    public rv_evidences(List<Evidence> evidenceList, Activity activity, EvidenceView evidenceView){
        this.Evidences = evidenceList;
        this.mContext=activity;
        this.evidenceView = evidenceView;
        notifyDataSetChanged();//se notifica que se cambia un conjunto de datos
    }

    /**
     * Manejador de la vista de un reto, muestra información e incluye un evento cuando se da click en la vista
     */
    public static class EvidenceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView date;
        ImageView imageView;
        CardView cvEvidenceUploaded;



        EvidenceViewHolder(final LayoutInflater inflater, ViewGroup parent, final int position) {
            super(inflater.inflate(R.layout.item_evidence, parent, false));
            cv = (CardView)itemView.findViewById(R.id.card_view);

            date = (TextView)itemView.findViewById(R.id.evidence_date);
            imageView= (ImageView) itemView.findViewById(R.id.evidence_photo);

            cvEvidenceUploaded = itemView.findViewById(R.id.cvEvidenceUploaded);


        }
    }

    /**
     * Devuelve la cantidad de elementos que se mostrarán
     * @return Valor de cantidad de elementos
     */
    @Override
    public int getItemCount() {
        return Evidences.size();
    }

    /**
     * Crea el maejador de la vista de un reto
     * @param viewGroup Grupo de vista el que pertenece
     * @param i Posición de la vista
     * @return Regresa el manejador de la vista
     */
    @Override
    public EvidenceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater v = LayoutInflater.from(viewGroup.getContext());
        return new EvidenceViewHolder(v,viewGroup,i);

    }

    /**
     * Marca la acciones que tomarán de forma individual cada casilla en la que se muestran los retos
     * @param ViewHolder Componente donde se muestra la información del reto
     * @param i Posición de la vista del reto
     */
    @Override
    public void onBindViewHolder(final EvidenceViewHolder ViewHolder,final int i) {

        //creating Date from millisecond
        Date currentDate = new Date(Evidences.get(i).getTimeStamp());

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");

        if(Evidences.get(i).getStatusSync()== 1)//si la evidencia ha sido subida la servidor
            ViewHolder.cvEvidenceUploaded.setVisibility(View.VISIBLE);//se muestra un icono de que la evidencia se sincronizó


        ViewHolder.date.setText(df.format(currentDate));//se muestra la fecha de toma de evidencia


        //evento de click del la vista de la evidencia
        ViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evidenceView.showEvidenceSelected(Evidences.get(i));//llama la funcion que muestra del información de la evidencia en pantalla
            }
        });
    }

    /**
     * Para poder realizar alguna acción cuando nuestro item de lista sale de la pantalla (como borrar una imagen para ahorrar memoria)
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(EvidenceViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();

        Glide.with(mContext).clear(holder.imageView);//limpia la imagen de la evidencia
        holder.imageView.clearAnimation();
        holder.imageView.setImageBitmap(null);//limpia el imageview
    }


    /**
     * Para poder realizar alguna acción cuando nuestro item de lista entra a la pantalla (como mostrar una imagen para ahorrar memoria)
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(@NonNull EvidenceViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if(Evidences.get(holder.getAdapterPosition()).getEvidenceType()== SENSKY_EVIDENCE_TYPE_PHOTO){
            File filePhoto = new File(Evidences.get(holder.getAdapterPosition()).getFileImagePath());

            Glide.with(mContext)
                    .load(filePhoto.getAbsoluteFile())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(GenericTransitionOptions.with(R.anim.nav_default_pop_enter_anim))
                    .into(holder.imageView);
        }
        else{
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.survey));
        }

    }
}