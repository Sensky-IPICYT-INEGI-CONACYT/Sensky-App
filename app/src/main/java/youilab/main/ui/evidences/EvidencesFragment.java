package youilab.main.ui.evidences;

import Adapters.rv_evidences;
import Adapters.tag_adapter;
import AppTools.DirectoryManager;
import AppTools.EvidenceFiles;
import AppTools.NetworkStatus;
import AppTools.PermissionsAplication;
import MVP.Interfaces.CurrentTraining.CurrentTrainingPresenter;
import MVP.Interfaces.CurrentTraining.CurrentTrainingView;
import MVP.Interfaces.EvidenceView;
import MVP.Interfaces.LocationView;
import MVP.Interfaces.NewAvatar.AvatarsPresenter;
import MVP.Interfaces.NewAvatar.AvatarsView;
import MVP.Presenters.AvatarsPresenterImpl;
import MVP.Presenters.CurrentTrainingPresenterImpl;
import SenSkyCore.Avatars.Avatar;
import SenSkyCore.Evidences.Evidence;
import SenSkyCore.Surveys.Survey;
import SenSkyCore.Surveys.SurveyControl.Tag;
import SenSkyCore.Trainings.Training;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.flexbox.*;
import youilab.main.GPS;
import youilab.main.R;
import youilab.main.Services.synchronizationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static AppTools.DirectoryManager.*;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static youilab.main.Services.synchronizationService.*;

public class EvidencesFragment extends Fragment implements AvatarsView, CurrentTrainingView , LocationView, EvidenceView {

    private AvatarsPresenter avatarsPresenter;
    private CurrentTrainingPresenter currentTrainingPresenter;


    private Avatar mAvatar= null;
    private Training mTraining= null;

    private TextView txtCurrentAvatarName,txtCurrentTrainingName;
    private ImageView imgAvatar;

    private RecyclerView rvEvidences;
    private TextView txtWitoutEvidences;

    private CardView cvAddPhoto,cvAddSurvey;
    private CardView cvGPSStatus, cvGPS1,cvGPS2,cvGPS3;

    private int MIN_ACCURACY_REQUERED = 500;//presicion minima requerida para crear una evidencia nueva
    static boolean bndCameraPermission = false;
    private GPS gpsService;


    private Boolean bndAceptableAccuracyGPS=false;

    String filename;
    Uri fileUri;

    ///Views de evidencia
    private CardView cvBackToEvidences;//regresa a la lista de evidencias
    private RelativeLayout rlyEvidenceInfo;//vista para mostrar la información de una evidencia
    private RelativeLayout rlyOptionsEvidences;//vista para las 2 opciones de evidencias (foto o encuesta)
    private TextView txtEvidenceName;//nombre de la evidencia
    private ImageView imgEvidenceImage;//imagen de la evidencia
    private TextView txtComment;//comentario de la evidencia
    private RecyclerView rvTags;//lista de etiquetas
    private ImageView imgComment;//opcion para ver el comentario de la evidencia
    private ImageView imgTags;//opcion para ver las etiquetas de la evidencia
    private CardView cvUploadEvidences;//boton para subir las evidencias y no esperar a que el servicio lo haga

    SyncronizationEvidencesReceiver syncronizationEvidencesReceiver;//


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_evidences, container, false);

        txtCurrentAvatarName = (TextView) root.findViewById(R.id.txtAvatarName);
        txtCurrentTrainingName = (TextView) root.findViewById(R.id.txtTrainingName);

        txtWitoutEvidences = (TextView) root.findViewById(R.id.txtWithoutEvidences);

        imgAvatar = (ImageView) root.findViewById(R.id.imgAvatar);

        rvEvidences = (RecyclerView) root.findViewById(R.id.rvEvidencesList);

        cvAddPhoto = (CardView) root.findViewById(R.id.cvAddImage);
        cvAddSurvey = (CardView) root.findViewById(R.id.cvAddSurvey);


        cvGPS1 = (CardView) root.findViewById(R.id.cvGpsRed);
        cvGPS2 = (CardView) root.findViewById(R.id.cvGpsYellow);
        cvGPS3 = (CardView) root.findViewById(R.id.cvGpsGreen);


        //
        cvBackToEvidences = (CardView) root.findViewById(R.id.cvBack);
        rlyEvidenceInfo = (RelativeLayout) root.findViewById(R.id.rlyEvidenceInfo);
        rlyOptionsEvidences = root.findViewById(R.id.rlyActions);


        txtEvidenceName = root.findViewById(R.id.txtNameEvidence);
        imgEvidenceImage = root.findViewById(R.id.imgEvidence);
        txtComment = root.findViewById(R.id.evidenceComment);
        rvTags = root.findViewById(R.id.rvTags);

        imgComment = root.findViewById(R.id.ImvComment);
        imgTags = root.findViewById(R.id.ImvTags);
        cvUploadEvidences = root.findViewById(R.id.cvUploadEvidences);




        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //cambio de color del texto en el status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.transparent));
        }

        syncronizationEvidencesReceiver = new SyncronizationEvidencesReceiver();//recividor de broadcast del servicio de sincronización
        getActivity().registerReceiver(syncronizationEvidencesReceiver,new IntentFilter(RECEIVER_SYNCRONIZATION_SERVICE));//registro del recividor de broadcast

        //instancia para el servicio de localización gps
        gpsService = new GPS(getContext(),getActivity(), this);
        gpsService.startGPSLocation();//inicia de localización gps

        //intancia para el manejador de las acciones con los avatres
        avatarsPresenter = new AvatarsPresenterImpl(this, getActivity());
        avatarsPresenter.getCurrentAvatar();//obtener la información del avatar activo

        //instancia para el manejador de las acciones de las capacitaciones
        currentTrainingPresenter = new CurrentTrainingPresenterImpl(this,getActivity());
        currentTrainingPresenter.getCurrentTraining();//obtener la información de la capacitación activa

        //progress dialog cuando se estan sincronizacndo las evidencias
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Sincronizando evidencias");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Espera un momento...");

        //dar los eventos de click de los botones de agregar una foto o encuesta
      clickEventoNewEvidences();

      //regresa de ver la información de una evidencia a la lista de evidencias
        cvBackToEvidences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvEvidences.setVisibility(View.VISIBLE);
                cvUploadEvidences.setVisibility(View.VISIBLE);
                txtWitoutEvidences.setVisibility(View.GONE);
                rlyEvidenceInfo.setVisibility(View.GONE);


                //animación al momento de mostrar la lista de evidencias
                Animation anim= AnimationUtils.loadAnimation(getContext(),R.anim.slide_top_in);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        rlyOptionsEvidences.setVisibility(View.VISIBLE);
                        clickEventoNewEvidences();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                rlyOptionsEvidences.startAnimation(anim);
            }
        });

        //para mostrar las etiquetas de una evidencia
        imgTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtComment.setVisibility(View.GONE);
                rvTags.setVisibility(View.VISIBLE);
            }
        });

        //para mostrar el comentrio de una evidencia
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtComment.setVisibility(View.VISIBLE);
                rvTags.setVisibility(View.GONE);
            }
        });

        //boton de subir las evidencias
        cvUploadEvidences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    int bndConection = NetworkStatus.getStatus(getActivity());
                    if (bndConection == NetworkStatus.WIFI_INTERNET_ACCCESS) {//si hay conexion a internet


                            //inicia el servicio de sincrnización
                        Intent myService = new Intent(getContext(), synchronizationService.class);
                        myService.setAction(synchronizationService.ACTION_RUN_ISERVICE);
                        getActivity().startService(myService);
                        progressDialog.show();

                    }else Toast.makeText(getActivity(),"No estas conectado a una red WiFi",Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * click de los botones que permiten agregar evidencias
     */
    private void clickEventoNewEvidences(){
        cvAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionsAplication.accessCameraPermission(getActivity()) ) {
                    if(bndAceptableAccuracyGPS)
                        Fotografia();
                    else
                        Toast.makeText(getActivity(),"Señal GPS debil",Toast.LENGTH_SHORT).show();
                }


            }
        });

        cvAddSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bndAceptableAccuracyGPS) {
                    currentTrainingPresenter.getSurveysOfCurrentTraining();
                }else
                    Toast.makeText(getActivity(),"Señal GPS debil",Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * cada vez que se muestra la pantalla se inicia la localización de gps
     * si existe una capacitacion activa se muestra la información
     */
    @Override
    public void onResume() {
        super.onResume();
        gpsService.startGPSLocation();//inicio de obtención de la ubicación gps
        if(mTraining!=null && mTraining.getTimeStampRunTraining() < System.currentTimeMillis()){
            currentTrainingPresenter.getEvidencesOfCurrentTraining();//obtener las evidencias de la capacitación seleccionada
            currentTrainingPresenter.existPendingEvidencesStatus();//ver si existen evidencias pendientes por subir
        }
    }

    /**
     * cada vez que se oculta la pantalla se detiene el gps
     */
    @Override
    public void onStop() {
        super.onStop();
        gpsService.stopGPSLocation();

    }

    /**
     * si se cierra la aplicacion se quita el recividor de broadcast
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(syncronizationEvidencesReceiver);

    }

    @Override
    public void AvatarList(List<Avatar> avatarList) {

    }

    /**
     * informacion del avatar que se esta usando
     * @param avatar obejto del avatar actual
     */
    @Override
    public void CurrentAvatar(Avatar avatar) {
        mAvatar = avatar;//se pasa aun objeto general para poder acceder a el en otras partes del código

        //se muestra su informacion
        txtCurrentAvatarName.setText(avatar.getName());
        Uri imgProfile= Uri.parse(avatar.getImagePath());
        Glide.with(getActivity()).load(imgProfile).into(imgAvatar);
    }

    @Override
    public void Error(String message) {

    }


    /**
     * información de la capacitación actual
     * @param training objeto con la informacion de la capacitación actual
     */
    @Override
    public void currentTraining(Training training) {
        if(training!=null){// en caso de que hay al menos una capacitación  registrada
            mTraining = training;

        txtCurrentTrainingName.setText(training.getPlaceName());

        if(training.getTimeStampRunTraining()<System.currentTimeMillis()) {//en caso de que aun no inicia se ucultan los botones de agregar evidencias
            rlyOptionsEvidences.setVisibility(View.VISIBLE);
            currentTrainingPresenter.getEvidencesOfCurrentTraining();//obtener las evidencias tomadas en esa capacitacion por el usuario

        }else{
            rlyOptionsEvidences.setVisibility(View.GONE);
            Date trainingDate = new Date(training.getTimeStampRunTraining());
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
            txtWitoutEvidences.setText("La capacitación inicia el "+df.format(trainingDate));
        }

        }
        else {
            rlyOptionsEvidences.setVisibility(View.GONE);
            txtCurrentTrainingName.setText("Sin capacitación");
            txtWitoutEvidences.setText("Ve a la sección Retos y activa una capacitación");
        }
    }

    /**
     * Al agregar una encuesta al usuario se le muestra una lista de encuestas disponibles
     * @param surveys lista de objetos surveys
     */
    @Override
    public void surveysToTraining(final  List<Survey> surveys) {
// se inicia el cuadro de dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Selecciona una encuesta");

// A la lista de titilos de encuesta se le agregan todas las disponibles
        ArrayList<String> stringSurveyList = new ArrayList<String>();
        for (Survey survey: surveys
             ) {
            stringSurveyList.add(survey.getTitle());
        }

        //creamos un arreglo adaptable a la cantidad de encuesta en la lista
        String[] arr = new String[stringSurveyList.size()];
        arr = stringSurveyList.toArray(arr);

        //creamos al conjunto de datos en el cuadro de dialogo y les ponemos un envento de click a cada uno
        builder.setItems(arr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (gpsService.getCurrentLocation() != null && gpsService.getCurrentLocation().getAccuracy() <= MIN_ACCURACY_REQUERED) {


                    Intent photoIntent = new Intent(getActivity(), SurveyActivity.class);

                    photoIntent.putExtra("LATITUDE", String.valueOf(gpsService.getCurrentLocation().getLatitude()));
                    photoIntent.putExtra("LONGITUDE",  String.valueOf(gpsService.getCurrentLocation().getLongitude()));
                    photoIntent.putExtra("ALTITUDE", String.valueOf(gpsService.getCurrentLocation().getAltitude()));
                    photoIntent.putExtra("SURVEY_ID", String.valueOf(surveys.get(which).getId()));
                    getActivity().startActivity(photoIntent);
                    //se abre la actividad para la captura de la encuesta selecciond
                    //se envia a la actividad la infoamcrion de latitud, altitud, longitud y el id de la encuesta seleccinada y que se va a aplicar



                }else{
                    Toast.makeText(getContext(), "Se esta obteniendo tu ubicación, reintenta en un momento", Toast.LENGTH_SHORT).show();

                }
            }
        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * da la lista de evidencias de la capaciatción
     * @param evidences lista de objetos evidence
     */
    @Override
    public void evidencesOfTraining(List<Evidence> evidences) {
        if(rlyEvidenceInfo.getVisibility() != View.VISIBLE){//solo se muestra la lista de evidencias si la vista de evidencia individual esta oculta

            if (evidences.size() != 0) {//si tiene evidencias las muestra
                rvEvidences.setVisibility(View.VISIBLE);
                cvUploadEvidences.setVisibility(View.VISIBLE);
                txtWitoutEvidences.setVisibility(View.GONE);
                setupRecyclerViewEvidences(evidences);
            } else {//si no tiene muestra un mensaje en pantalla
                rvEvidences.setVisibility(View.GONE);
                cvUploadEvidences.setVisibility(View.GONE);
                txtWitoutEvidences.setVisibility(View.VISIBLE);
                txtWitoutEvidences.setText("No has tomado evidencias");
            }
        }
    }

    @Override
    public void existpendingEvidences() {
        cvUploadEvidences.setVisibility(View.VISIBLE);
    }

    @Override
    public void notExistPendingEvidences() {
        cvUploadEvidences.setVisibility(View.GONE);
    }

    /**
     * Cargar la lista de evidencias en el recyclerview de evidencias de la capacitación
     * @param evidenceList lista de evidencias
     */
    private void setupRecyclerViewEvidences( List<Evidence> evidenceList){

        rv_evidences itemAvatarAdapter = new rv_evidences(evidenceList,getActivity(), this);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        rvEvidences.setLayoutManager(staggeredGridLayoutManager);
        rvEvidences.setAdapter(itemAvatarAdapter);

        itemAvatarAdapter.notifyDataSetChanged();

    }


    /**
     * llamada a la app de camara del dispositivo
     */
    private void Fotografia() {
        if (gpsService.getCurrentLocation() != null && gpsService.getCurrentLocation().getAccuracy() <= MIN_ACCURACY_REQUERED)//si la precision es buena
        {
            filename="IMG"+System.currentTimeMillis();//nombre del archivo de evidencia
            Intent localIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//intent a la app de camara
            fileUri = EvidenceFiles.getOutputMediaFileUri(SENSKY_EVIDENCE_TYPE_PHOTO,DirectoryManager.getDirectoryToPhotos(mAvatar.getID(),mTraining.getKey()),filename);//path donde se guardará la fotografia
            if(fileUri!=null) {
                localIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                localIntent = EvidenceFiles.cameraIntent(localIntent, getContext(), this.fileUri);
                if (localIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(localIntent, 100);//llamada a la app de camara
                } else
                    Toast.makeText(getContext(), "Algo paso al abrir la app de camara", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(getContext(), "No se creo el archivo", Toast.LENGTH_SHORT).show();

        }else
            Toast.makeText(getContext(), "Se esta obteniendo tu ubicación, reintenta en un momento", Toast.LENGTH_SHORT).show();

    }

    /**
     * resultado de la toma de una fotografia con al app de camara del dispositivo
     * @param resulCode codigo de resulatdo de la app de camara
     * @param paramInt2 codigo de que la fotografia se alla creado y guardado con exito
     * @param paramIntent
     */
    public void onActivityResult(int resulCode, int paramInt2, Intent paramIntent) {
        super.onActivityResult(resulCode, paramInt2, paramIntent);
        if (resulCode == 100) {
            if ((this.fileUri != null) && (resultEvidence(paramInt2) == 1)) {// si todo esta bien tomado

                Intent photoIntent = new Intent(getActivity(), photo.class);

                photoIntent.putExtra("LATITUDE", String.valueOf(gpsService.getCurrentLocation().getLatitude()));
                photoIntent.putExtra("LONGITUDE",  String.valueOf(gpsService.getCurrentLocation().getLongitude()));
                photoIntent.putExtra("ALTITUDE", String.valueOf(gpsService.getCurrentLocation().getAltitude()));
                photoIntent.putExtra("PHOTO", this.fileUri.getPath());
                getActivity().startActivity(photoIntent);
                //se abre la pantalla para poder comentar y/o agregar etiquetas a la fotografia
                //se le envia la informacion necesaria para guardar la evidencia
            }
        }
    }

    /**
     * resultadod de la toma de una fotografias con la app de camara del dispositivo
     * @param resultCode
     * @return
     */
    private int resultEvidence(int resultCode) {
        if (resultCode == RESULT_OK) {
            return 1;
        }
        if (resultCode == RESULT_CANCELED)
        {
            Toast.makeText(getContext(), "Toma cancelada", Toast.LENGTH_SHORT).show();
            return 0;
        }else {
            Toast.makeText(getContext(), "Ocurrió un imprevisto.\nPor favor intenta nuevamente", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    /**
     * Se establece la presición de la localización gps y se asigna el color a mostrar
     * @param currentLocation
     */
    @Override
    public void currentLocation(Location currentLocation) {
        if(currentLocation.getAccuracy()>500){//mayor a 500 metros
            setGPSColor(getActivity().getResources().getColor(R.color.pink));
            bndAceptableAccuracyGPS=false;
        }
        else//entre 100 y 500 metros
        if(currentLocation.getAccuracy()<500 && currentLocation.getAccuracy()>100) {
            setGPSColor(getActivity().getResources().getColor(R.color.yellow));
            bndAceptableAccuracyGPS=true;
        }//menor a 100 metros
        else if(currentLocation.getAccuracy()<100){
            setGPSColor(getActivity().getResources().getColor(R.color.green));
            bndAceptableAccuracyGPS=true;
        }
    }

    /**
     * cambio de color de los puntos de precisión de gps
     * @param color
     */
    private void setGPSColor(int color){
        cvGPS1.setCardBackgroundColor(color);
        cvGPS2.setCardBackgroundColor(color);
        cvGPS3.setCardBackgroundColor(color);
    }

    /**
     * oculta y muestra las vistas necesarias para que la informacion de una evidencia seleccionada sea mostrada
     * @param evidence enformacionde la evidencia
     */
    @Override
    public void showEvidenceSelected(final Evidence evidence) {
        rvEvidences.setVisibility(View.GONE);
        cvUploadEvidences.setVisibility(View.GONE);
        txtWitoutEvidences.setVisibility(View.GONE);

        Animation anim= AnimationUtils.loadAnimation(getContext(),R.anim.slide_top_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlyOptionsEvidences.setVisibility(View.GONE);
                showEvidenceInformation(evidence);
                cvAddPhoto.setOnClickListener(null);
                cvAddSurvey.setOnClickListener(null);
                rlyEvidenceInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rlyOptionsEvidences.startAnimation(anim);


    }

    /**
     * cuando se selecciona una evidencia se muestra su información
     * @param evidence informacion de la evidencia seleccionada
     */
    private void showEvidenceInformation(Evidence evidence){

        Date currentDate = new Date(evidence.getTimeStamp());

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");

        txtEvidenceName.setText(df.format(currentDate));
        txtComment.setText(evidence.getComment());

        txtComment.setVisibility(View.GONE);
        rvTags.setVisibility(View.GONE);

        setupRecyclerViewTags(evidence.getTagList());

        if(evidence.getEvidenceType() == SENSKY_EVIDENCE_TYPE_PHOTO){//mostrar foto si es una fotografia
            Glide.with(getActivity())
                    .load(evidence.getFileImagePath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .transition(GenericTransitionOptions.with(R.anim.nav_default_pop_enter_anim))
                    .into(imgEvidenceImage);
        }
        else{
            imgEvidenceImage.setImageDrawable(getResources().getDrawable(R.mipmap.survey));//icono de encuesta
        }

    }


    /**
     * para la lista de etiquetas de evidencias
     * @param tagList lista de etiquetas
     */
    private void setupRecyclerViewTags(List<Tag> tagList){

        tag_adapter myLickertGroupAdapter = new tag_adapter(tagList, tag_adapter.TAG_NOT_SELECTABLE, getActivity());
        FlexboxLayoutManager flexboxLayoutManagerMultiAnswer = new FlexboxLayoutManager(getContext());
        flexboxLayoutManagerMultiAnswer.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManagerMultiAnswer.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManagerMultiAnswer.setAlignItems(AlignItems.FLEX_START);
        flexboxLayoutManagerMultiAnswer.setJustifyContent(JustifyContent.CENTER);
        rvTags.setLayoutManager(flexboxLayoutManagerMultiAnswer);
        rvTags.setAdapter(myLickertGroupAdapter);
        myLickertGroupAdapter.notifyDataSetChanged();
    }

    /**
     * Receiver para ver cuando un servicio de sincronizacion de evidencias esta activo o termina, 0-> esta activo 1-> termino el servicio
     */

    ProgressDialog progressDialog;

    class SyncronizationEvidencesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(RECEIVER_SYNCRONIZATION_SERVICE))
            {
                String status = intent.getStringExtra(RECEIVER_SYNCRONIZATION_SERVICE_STATUS);

                assert status != null;
                if(status.compareTo(ACTION_SYNCRONITATION_FINISHED)==0)
                {

                    currentTrainingPresenter.getEvidencesOfCurrentTraining();
                    currentTrainingPresenter.existPendingEvidencesStatus();
                    if(progressDialog != null)
                        progressDialog.dismiss();
                }else
                {
                    currentTrainingPresenter.existPendingEvidencesStatus();
                }
            }
        }

    }
}