package youilab.main.ui.evidences;

import Adapters.lickert_group_adapter;
import Adapters.multi_answer_adapter;
import Adapters.multi_option_adapter;
import MVP.Interfaces.Survey.SurveyPresenter;
import MVP.Interfaces.Survey.SurveyView;
import MVP.Presenters.SurveyPresenterImpl;
import SenSkyCore.Surveys.SurveyControl.Question;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.flexbox.*;
import youilab.main.R;

import java.util.regex.Pattern;

import static SenSkyCore.Surveys.SurveyControl.SurveyHandler.*;

public class SurveyActivity extends AppCompatActivity implements SurveyView {

    private SurveyPresenter surveyPresenter;//manejador de las acciones con la encuesta
    private TextView lblQuestion;//mortrar la pregunta
    private TextView lblNote;//nota de la pregunta
    private TextView lblSection;//nombre de la seccion en la que se encuentra

    private RelativeLayout rlyOpenQuestion;//vista para preguntas abiertas
    private RelativeLayout rlyMultioptionQuestion;//vista para preguntas de opción multiple
    private RelativeLayout rlyMultianswerQuestion;//vista para preguntas de respuesta multiple
    private RelativeLayout rlyEndSurvey;////vista para preguntas abiertas
    private RelativeLayout rlyLickertGroup;//vista para preguntas lickert en grupo
    private RelativeLayout rlyLickertQuestion;//vista para preguntas lickert

    RecyclerView rvMultiAnswer;//lista de opciones para preguntas de respuesta multiple
    RecyclerView rvLickertGroup;//lista de opciones para preguntas lickert en grupo
    RecyclerView localRecyclerView;//lista de opciones para preguntas de opción multiple

    private CardView fabSaveOpenQuestion;
    private CardView btnSaveMultipleOption;
    private CardView btnSaveMultipleAnswer;
    private CardView btnSaveLickertGroup;
    private CardView btnEndSurvey;
    private CardView fabSaveLickertQuestion;

    private SeekBar seekBarLickert;

    private EditText txtAnswer;

    private TextView txtAnswerLickert;
    private TextView txtLowValue;//muestra en valor minimo en preguntas lickert
    private TextView txtHighValue;//muestra en valor maximo en preguntas lickert

    private Question currentQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);



        //pregunta que se hace
        lblQuestion = (TextView) findViewById(R.id.txtQuestion);
        lblSection = (TextView) findViewById(R.id.txtSection);
        lblQuestion.setMovementMethod(new ScrollingMovementMethod());
        lblNote = (TextView) findViewById(R.id.txtNote);


        //layouts de los diferentes tipos de preguntas
        rlyMultianswerQuestion = (RelativeLayout) findViewById(R.id.multiAnswerQuestion);
        rlyMultioptionQuestion = (RelativeLayout)findViewById(R.id.multioptionQuestion);
        rlyOpenQuestion = (RelativeLayout)findViewById(R.id.openQuestion);
        rlyEndSurvey = (RelativeLayout)findViewById(R.id.rlyEndSurvey);
        rlyLickertGroup = (RelativeLayout)findViewById(R.id.lickertgroupQuestion);
        rlyLickertQuestion = findViewById(R.id.lickertQuestion);


        rvMultiAnswer = (RecyclerView) findViewById(R.id.rvMultiAnswer);
        rvLickertGroup = (RecyclerView) findViewById(R.id.rvLickertgroup);
        localRecyclerView = (RecyclerView) findViewById(R.id.rvMultiOption);

        txtAnswerLickert = findViewById(R.id.txtValueLickert);
        txtLowValue = findViewById(R.id.txtLowerValue);
        txtHighValue = findViewById(R.id.txtHighestValue);

        seekBarLickert = findViewById(R.id.seekBarLickert);

        seekBarLickert.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtAnswerLickert.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //campo de ingreso de respuesta abierta (numerica o textual)
        txtAnswer = (EditText) findViewById(R.id.txtAnswerOpen);
        txtAnswer.addTextChangedListener(new TextWatcher() {
            boolean bndAceptabletext=true;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bndAceptabletext= Pattern.matches("^[a-zA-Z0-9ñÑ., áÁéÉíóÍÓúÚ]*$", charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!bndAceptabletext )
                    editable.delete(editable.length()-1,editable.length());
                if(currentQuestion.getType().compareTo(SURVEY_TYPE_NUMERIC)==0 && txtAnswer.getText().toString().compareTo("")!=0 && txtAnswer.getText().toString().length()>2)
                    editable.delete(editable.length()-1,editable.length());
            }
        });


        fabSaveOpenQuestion = (CardView) findViewById(R.id.btnSaveQuestionOpen);
        fabSaveOpenQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentQuestion.getType().compareTo(SURVEY_TYPE_NUMERIC)==0 && txtAnswer.getText().toString().compareTo("")!=0)
                {
                    double number = Double.parseDouble(txtAnswer.getText().toString());
                    if (number >= currentQuestion.getMinNumeric()   && number <= currentQuestion.getMaxNumeric())
                    {
                        currentQuestion.setAnswer(txtAnswer.getText().toString());
                        if(txtAnswer.getText().toString().compareTo("")!=0)
                            currentQuestion.setQuestionAnswered(true);
                        surveyPresenter.nextQuestion();
                    }else {
                        txtAnswer.setText("");
                        Toast.makeText(getApplicationContext(),"El valor debe ser mayor a "+(currentQuestion.getMinNumeric()-1)+" y menor a "+currentQuestion.getMaxNumeric(),Toast.LENGTH_SHORT ).show();
                    }
                }
                else{
                    currentQuestion.setAnswer(txtAnswer.getText().toString());
                    if(txtAnswer.getText().toString().compareTo("")!=0){
                        currentQuestion.setQuestionAnswered(true);
                        surveyPresenter.nextQuestion();
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Responde",Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        });



        btnSaveMultipleOption = (CardView) findViewById(R.id.btnNextQuestionOption);
        btnSaveMultipleOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surveyPresenter.nextQuestion();

            }
        });

        btnSaveMultipleAnswer = (CardView) findViewById(R.id.btnNextQuestionAnswer);
        btnSaveMultipleAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surveyPresenter.nextQuestion();
            }
        });

        fabSaveLickertQuestion = (CardView) findViewById(R.id.btnSaveQuestionLickert);
        fabSaveLickertQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentQuestion.setAnswer(txtAnswerLickert.getText().toString());

                if(txtAnswerLickert.getText().toString().compareTo("")!=0)
                    currentQuestion.setQuestionAnswered(true);

                surveyPresenter.nextQuestion();

            }
        });

        btnSaveLickertGroup = (CardView) findViewById(R.id.btnNextQuestionLickert);
        btnSaveLickertGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surveyPresenter.nextQuestion();
            }
        });
        btnEndSurvey = (CardView) findViewById(R.id.cvEndSurvey);
        btnEndSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Fin de la encuesta",Toast.LENGTH_SHORT).show();
            }
        });




        Bundle extras = getIntent().getExtras();
        if (extras != null) {//se obtiene el id de la encuesta que se aplicará
            surveyPresenter = new SurveyPresenterImpl(this,this);
            surveyPresenter.loadSurvey(getIntent().getStringExtra("SURVEY_ID"));
        }
    }





    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * Devuelve la información de la pregunta que se mostrará en la pantalla
     * @param currentQuestion objeto pregunta
     */
    @Override
    public void currentQuestion(Question currentQuestion) {

        this.currentQuestion = currentQuestion;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        identifyTypeQuestion(currentQuestion.getType());
        lblSection.setText(currentQuestion.getSectionTitle());
        lblQuestion.setText(currentQuestion.getQuestion());
        lblQuestion.scrollTo(0,0);//para devolver el scroll del texview al top
        lblNote.setText(currentQuestion.getNote());
    }

    @Override
    public void nameSurvey(String name) {
       // getSupportActionBar().setTitle(name);

    }

    /**
     * Cuando termina de contestarse una encuesta, esta es guardada y se termina la ctividad
     * @param bnd señala si se termino de contestar la encuesta
     */
    @Override
    public void endSurvey(boolean bnd) {
        if(bnd)
        {
            String idSurvey= getIntent().getStringExtra("SURVEY_ID");

            Location localLocation = new Location(LOCATION_SERVICE);
            localLocation.setAltitude(Double.parseDouble(getIntent().getStringExtra("ALTITUDE")));
            localLocation.setLongitude(Double.parseDouble(getIntent().getStringExtra("LONGITUDE")));
            localLocation.setLatitude(Double.parseDouble(getIntent().getStringExtra("LATITUDE")));
            surveyPresenter.saveSurvey(Integer.parseInt(idSurvey),localLocation.getLatitude(), localLocation.getLongitude(),localLocation.getAltitude());
            finish();
        }

    }

    @Override
    public void surveySaved() {
        Toast.makeText(this,"Encuesta Guardada",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Error() {
        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requiredResponse() {
        Toast.makeText(this, "Responde",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surveyNotExist() {

    }


    /**
     * limpia todas las vistas que involucra a las preguntas
     */
    private void cleanViews() {
        lblQuestion.setText("");
        lblNote.setText("");
        txtAnswer.setText("");
        txtAnswer.setEnabled(false);
        rlyOpenQuestion.setVisibility(View.GONE);
        rlyMultioptionQuestion.setVisibility(View.GONE);
        rlyMultianswerQuestion.setVisibility(View.GONE);
        rlyLickertGroup.setVisibility(View.GONE);
        rlyLickertQuestion.setVisibility(View.GONE);
    }

    /**
     *
     * @param typeQuestion
     */
    private void identifyTypeQuestion(String typeQuestion) {

        //lipiar vistas de cada pregunat para habiltar la correcta
        cleanViews();

        switch (typeQuestion) {//de acuerdo al tipo de pregunta
            case SURVEY_TYPE_LONG_TEXT://en caso de que sea de tipo texto largo
                rlyOpenQuestion.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                txtAnswer.setInputType(InputType.TYPE_CLASS_TEXT);
                txtAnswer.setEnabled(true);
                break;
            case SURVEY_TYPE_SHORT_TEXT://en caso de que sea de texto corto
                rlyOpenQuestion.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                txtAnswer.setInputType(InputType.TYPE_CLASS_TEXT);
                txtAnswer.setEnabled(true);
                break;
            case SURVEY_TYPE_MULTIPLE_ANSWER://en caso de que sea de respuesta multiple
                rlyMultianswerQuestion.setVisibility(View.VISIBLE);
                setupRecyclerViewMultiAnswer();
                break;
            case SURVEY_TYPE_MULTIPLE_OPTION://en caso de que sea de opción multiple
                rlyMultioptionQuestion.setVisibility(View.VISIBLE);
                setupRecyclerViewMultiOption();
                break;
            case SURVEY_TYPE_NUMERIC://en caso de que sea de tipo numerico
                rlyOpenQuestion.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                txtAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);
                txtAnswer.setEnabled(true);
                break;
            case SURVEY_TYPE_LICKERT://en caso de que sea de tipo lickert
                rlyLickertQuestion.setVisibility(View.VISIBLE);
                txtHighValue.setText(currentQuestion.getLickertHigh());
                txtLowValue.setText(currentQuestion.getLickertLow());
                seekBarLickert.setProgress(2);
                txtAnswerLickert.setText("3");
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
            case SURVEY_TYPE_LICKERT_GROUP://en caso de que sea de lickert group
                rlyLickertGroup.setVisibility(View.VISIBLE);
                setupRecyclerViewLickertGroup();
                break;
        }

    }

    /**
     * Lista para las opciones de una pregunta de opción multiple
     */
    private void setupRecyclerViewMultiOption() {



        multi_option_adapter localmulti_option_adapter = new multi_option_adapter(currentQuestion, this);
        FlexboxLayoutManager localFlexboxLayoutManager = new FlexboxLayoutManager(this);
        localFlexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        localFlexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        localFlexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        localFlexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
        localRecyclerView.setLayoutManager(localFlexboxLayoutManager);
        localRecyclerView.setAdapter(localmulti_option_adapter);
        localmulti_option_adapter.notifyDataSetChanged();



    }

    /**
     * Lista para las opciones de una pregunta de multi respuesta
     */
    private void setupRecyclerViewMultiAnswer(){

        multi_answer_adapter myMultiAwnswerAdapter = new multi_answer_adapter(currentQuestion, this);
        FlexboxLayoutManager flexboxLayoutManagerMultiAnswer = new FlexboxLayoutManager(this);
        flexboxLayoutManagerMultiAnswer.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManagerMultiAnswer.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManagerMultiAnswer.setAlignItems(AlignItems.FLEX_START);
        flexboxLayoutManagerMultiAnswer.setJustifyContent(JustifyContent.CENTER);
        rvMultiAnswer.setLayoutManager(flexboxLayoutManagerMultiAnswer);
        rvMultiAnswer.setAdapter(myMultiAwnswerAdapter);
        myMultiAwnswerAdapter.notifyDataSetChanged();
    }


    /**
     * Lista para las opciones de una pregunta lickert grupal
     */
    private void setupRecyclerViewLickertGroup(){

        lickert_group_adapter myLickertGroupAdapter = new lickert_group_adapter(currentQuestion, this);
        FlexboxLayoutManager flexboxLayoutManagerMultiAnswer = new FlexboxLayoutManager(this);
        flexboxLayoutManagerMultiAnswer.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManagerMultiAnswer.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManagerMultiAnswer.setAlignItems(AlignItems.FLEX_START);
        flexboxLayoutManagerMultiAnswer.setJustifyContent(JustifyContent.CENTER);
        rvLickertGroup.setLayoutManager(flexboxLayoutManagerMultiAnswer);
        rvLickertGroup.setAdapter(myLickertGroupAdapter);
        myLickertGroupAdapter.notifyDataSetChanged();
    }
}
