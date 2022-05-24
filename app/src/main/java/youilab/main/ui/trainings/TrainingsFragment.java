package youilab.main.ui.trainings;

import Adapters.rv_trainings;
import AppTools.NetworkStatus;
import MVP.Interfaces.Configuration.ConfigurationPresenter;
import MVP.Interfaces.Configuration.ConfigurationView;
import MVP.Interfaces.NewAvatar.AvatarsPresenter;
import MVP.Interfaces.NewAvatar.AvatarsView;
import MVP.Interfaces.Trainings.TrainingPresenter;
import MVP.Interfaces.Trainings.TrainingsView;
import MVP.Presenters.AvatarsPresenterImpl;
import MVP.Presenters.ConfigurationPresenterImpl;
import MVP.Presenters.TrainingsPresenterImpl;
import SenSkyCore.Avatars.Avatar;
import SenSkyCore.Trainings.Training;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import youilab.main.R;
import youilab.main.Services.synchronizationService;

import java.util.List;
import java.util.regex.Pattern;

public class TrainingsFragment extends Fragment implements AvatarsView, TrainingsView, ConfigurationView {

    private AvatarsPresenter avatarsPresenter;//presentador de las acciones de los avatares
    private TrainingPresenter trainingPresenter;//presentador de las acciones de los avatares

    private TextView txtCurrentAvatarName;//nombre del avatar actual
    private TextView txtWitoutTrainings;//texview de cuando no tienen capacitaciones registrdas
    private ImageView imgAvatar;//para la imagen del avatar
    private RecyclerView rvTrainings;//lista de capacitaciones del avatar

    private CardView cvAllTrainings,cvActiveTrainings, cvSoonTrainings;//botones para filtrar las capacitaciones

    private CardView cvNewTraining;//boton para agregar una nueva capacitación

    ProgressDialog progressDialog;
    AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trainings, container, false);

        txtCurrentAvatarName = (TextView) root.findViewById(R.id.txtAvatarName);
        imgAvatar = (ImageView) root.findViewById(R.id.imgAvatar);
        rvTrainings = (RecyclerView) root.findViewById(R.id.rvTrainingsList);
        txtWitoutTrainings = (TextView) root.findViewById(R.id.txtWithoutTrainings);

        cvAllTrainings = (CardView) root.findViewById(R.id.cvAllTrainings);
        cvSoonTrainings = (CardView) root.findViewById(R.id.cvSoonTrainings);
        cvActiveTrainings = (CardView) root.findViewById(R.id.cvActiveTrainings);

        cvNewTraining = (CardView) root.findViewById(R.id.cvNewTraining);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Cambio del color de texto del status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.transparent));
        }

        //instancias a los manejadores de avatares y de capacitaciones
        avatarsPresenter = new AvatarsPresenterImpl(this, getActivity());
        trainingPresenter = new TrainingsPresenterImpl(this,getActivity());

        //progress dialog cuando se estan sincronizacndo las evidencias
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Registrando la capacitación");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Espera un momento...");

        cvAllTrainings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainingPresenter.GetAllTrainings();
            }
        });
        cvSoonTrainings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainingPresenter.GetSoonTrainings();
            }
        });
        cvActiveTrainings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainingPresenter.GetActiveTrainings();
            }
        });

        cvNewTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creación del cuadro de dialogo de captura de llave
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_key_training, null);

                //campos de texto para 6 caracteres
                final EditText mKey1 = (EditText)  mView.findViewById(R.id.key_training1);
                final EditText mKey2 = (EditText)  mView.findViewById(R.id.key_training2);
                final EditText mKey3 = (EditText)  mView.findViewById(R.id.key_training3);
                final EditText mKey4 = (EditText)  mView.findViewById(R.id.key_training4);
                final EditText mKey5 = (EditText)  mView.findViewById(R.id.key_training5);
                final EditText mKey6 = (EditText)  mView.findViewById(R.id.key_training6);


                //agregar un validador de texto a cada campo de texto para ademas cambiar de campo cuando se digita en uno
                mKey1.addTextChangedListener(validatorTrainingCode(mKey1,mKey2));
                mKey2.addTextChangedListener(validatorTrainingCode(mKey2,mKey3));
                mKey3.addTextChangedListener(validatorTrainingCode(mKey3,mKey4));
                mKey4.addTextChangedListener(validatorTrainingCode(mKey4,mKey5));
                mKey5.addTextChangedListener(validatorTrainingCode(mKey5,mKey6));
                mKey6.addTextChangedListener(validatorTrainingCode(mKey6,null));


                CardView mButtonNewTraining = (CardView) mView.findViewById(R.id.btnNewTraining);
                mButtonNewTraining.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String keyTraining = mKey1.getText().toString()+ mKey2.getText().toString()+ mKey3.getText().toString()+mKey4.getText().toString()+mKey5.getText().toString()+mKey6.getText().toString();

                        if(keyTraining.length() == 6) {
                            dialog.dismiss();
                            trainingPresenter.NewTraining(keyTraining);
                            progressDialog.show();
                        }
                        else Toast.makeText(getActivity(),"ingresa correctamente el código",Toast.LENGTH_SHORT).show();
                    }
                });

                //boton de cancelar el ingreso de el código de la capacitación
                CardView mButtonCancelTraining = (CardView) mView.findViewById(R.id.btnCancel);
                mButtonCancelTraining.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();


                 mKey1.requestFocus();
            }
        });

        avatarsPresenter.getCurrentAvatar();//obtiene la información del avatar actual
        trainingPresenter.GetAllTrainings();//obtiene todas las capacitaciones del avatar

    }

    @Override
    public void AvatarList(List<Avatar> avatarList) {

    }

    /**
     * información del avatar actual
     * @param avatar objeto avatar con su información
     */
    @Override
    public void CurrentAvatar(Avatar avatar) {
        txtCurrentAvatarName.setText(avatar.getName());
        Uri imgProfile= Uri.parse(avatar.getImagePath());
        Glide.with(getActivity()).load(imgProfile).into(imgAvatar);
    }

    /**
     * Lista de capacitaciones del avatar
     * @param trainingList lista de objetos del avatar, sino no hay se muestra un mensaje
     */
    @Override
    public void TrainingsList(List<Training> trainingList) {
        if(trainingList.size()!=0){
            rvTrainings.setVisibility(View.VISIBLE);
            txtWitoutTrainings.setVisibility(View.GONE);
        setupRecyclerViewTrainings(getActivity(), trainingList);
        }
        else{
            rvTrainings.setVisibility(View.GONE);
            txtWitoutTrainings.setVisibility(View.VISIBLE);
        }

    }


    /**
     * Ocurrio algo al registrar la capacitación
     * @param message
     */
    @Override
    public void Error(String message) {
        progressDialog.dismiss();
        createDialogMessage(message);
    }

    /**
     * Capacitación registrada
     */
    @Override
    public void trainingSaved() {
        progressDialog.dismiss();
        //createDialogMessage("Se ha registrado una nueva capacitación");
    }

    /**
     * mensaje de consulta del registro de una capacitación
     * @param message mensaje
     */
    private void createDialogMessage(String message){

        //alert dialog con el mensaje con el resultado del registro de una nueva capacitación
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_message, null);
        final TextView mMessage = (TextView)  mView.findViewById(R.id.lblMessage);

        mMessage.setText(message);

        CardView mButtonOk = (CardView) mView.findViewById(R.id.btnOk);
        mButtonOk.setOnClickListener(new View.OnClickListener() {
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
     * Envio al recyclerview la lista de capacitaciones para ser mostradas
     * @param activity actividad en que se usa la recyclerview
     * @param trainingList lista de lista de capacitaciones
     */
    private void setupRecyclerViewTrainings(Activity activity, List<Training> trainingList){

        rv_trainings itemAvatarAdapter = new rv_trainings(trainingList,getActivity(),getContext());
        rvTrainings.setHasFixedSize(true);
        rvTrainings.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false));
        rvTrainings.setAdapter(itemAvatarAdapter);
    }

    /**
     * exito del registro y descarga de las encuestas de la capaciatción registrada
     * @param status esto de la tarea
     */
    @Override
    public void DirectoryProcessStatus(boolean status) {
        if(status) Toast.makeText(getContext(),"Encuestas descargadas",Toast.LENGTH_SHORT).show();
    }

    /**
     * Validador de los campos del codigo de una capacitación
     * @param currenCharacterInput
     * @param nextCharacterInput
     * @return vigia de los campos de texto
     */
    TextWatcher validatorTrainingCode(final EditText currenCharacterInput,final EditText nextCharacterInput){
        TextWatcher txtValidator = new TextWatcher() {
            boolean bndAceptabletext=true;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                bndAceptabletext= Pattern.matches("^[a-zA-Z0-9ñÑáÁéÉíÍóÓúÚ]*$", editable.toString());


                if (!bndAceptabletext || editable.length()>1)
                    editable.delete(editable.length()-1,editable.length());
                else if(bndAceptabletext && editable.length() == 1) {
                    //String upCase = editable.toString().toUpperCase();
                    //editable.replace(0,1,upCase);
                    if(nextCharacterInput!= null)
                        nextCharacterInput.requestFocus();
                }

            }
        };

        return txtValidator;
    }
}