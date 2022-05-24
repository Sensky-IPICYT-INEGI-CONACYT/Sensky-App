package youilab.main.ui.evidences;

import Adapters.tag_adapter;
import MVP.Interfaces.NewEvidence.NewEvidencePresenter;
import MVP.Interfaces.NewEvidence.PhotoEvidenceView;
import MVP.Presenters.NewEvidencePresenterImpl;
import SenSkyCore.Surveys.SurveyControl.Tag;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import youilab.main.R;

public class photo extends AppCompatActivity  implements PhotoEvidenceView {


    private FloatingActionButton fabSaveEvidence;
    private EditText editTextComment;

    RecyclerView rvTags;

    List<Tag> tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        init();
    }


    /**
     * se inicializan los controles de la pantalla
     */
    private void init(){


        editTextComment = (EditText) findViewById(R.id.txtAnswerOpen);
        editTextComment.addTextChangedListener(new TextWatcher() {
            boolean bndAceptabletext=true;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bndAceptabletext= Pattern.matches("^[a-zA-Z0-9ñÑ. ,áÁéÉíóÍÓúÚ]*$", charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!bndAceptabletext)
                    editable.delete(editable.length()-1,editable.length());
            }
        });



        rvTags = (RecyclerView) findViewById(R.id.rvTags);

        setupRecyclerViewTags();
        loadData();
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    /**
     * Carga la fotografía tomada y demas información de la evidencia
     */
    private void loadData(){
        Date currentDate = new Date(System.currentTimeMillis());

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");

        TextView lblEvidenceName = (TextView) findViewById(R.id.Title);
        lblEvidenceName.setText(df.format(currentDate));

        fabSaveEvidence = (FloatingActionButton) findViewById(R.id.btnSaveQuestionOpen);
        fabSaveEvidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePhotoEvidence();
            }
        });

        File localFile = new File(getIntent().getStringExtra("PHOTO"));
        ImageView localImageView = (ImageView)findViewById(R.id.evidence_photo);
        Glide.with(this).load(localFile.getAbsolutePath()).into(localImageView);
    }

    /**
     * Se reune toda la información de la evidencia fotografica y se guarda
     */
    private void savePhotoEvidence(){

        File localFile = new File(getIntent().getStringExtra("PHOTO"));
        Location localLocation = new Location(LOCATION_SERVICE);
        localLocation.setAltitude(Double.parseDouble(getIntent().getStringExtra("ALTITUDE")));
        localLocation.setLongitude(Double.parseDouble(getIntent().getStringExtra("LONGITUDE")));
        localLocation.setLatitude(Double.parseDouble(getIntent().getStringExtra("LATITUDE")));


        NewEvidencePresenter newEvidencePresenter = new NewEvidencePresenterImpl(this,getApplicationContext());//instancia al presentador de una nueva evidencia
        //guardado de una nueva evidencia de fotografía
        newEvidencePresenter.savePhotoEvidence(editTextComment.getText().toString(),tagList,  localFile.getAbsolutePath(),localLocation.getLatitude(), localLocation.getLongitude(),localLocation.getAltitude());
    }

    /**
     * Si la evidencia se guardo exitosamente la actividad se cierra
     * @param status estado de guardado de la evidencia
     */
    @Override
    public void photoEvidenceSaved(boolean status) {
        if(status)
            finish();
    }


    /**
     * Lista de etiquetas predefinidas para las evidencias
     * @return lista de etiquetas
     */
    private List<Tag> createListOfTags(){
        tagList = new ArrayList<>();
        Tag tag = new Tag();
        tag.setValue("#Humo");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Fábrica");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Ladrillera");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Cigarro");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Ganadería");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#DesechosDeAnimales");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Solventes");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#QuemaDeBasura");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Basura");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Polvo");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#QuemaDeResiduos");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Gas");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Vapor");
        tagList.add(tag);


        tag = new Tag();
        tag.setValue("#NataDeContaminación");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#QuemaDeCarbón");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#Smog");
        tagList.add(tag);

        tag = new Tag();
        tag.setValue("#QuemaDeLeña");
        tagList.add(tag);

        return tagList;
    }

    /**
     * Lista de etiquetas que se pueden seleccionar para una evidencia
     */
    private void setupRecyclerViewTags(){

        tag_adapter myLickertGroupAdapter = new tag_adapter(createListOfTags(), tag_adapter.TAG_SELECTABLE,this);
        FlexboxLayoutManager flexboxLayoutManagerMultiAnswer = new FlexboxLayoutManager(getApplicationContext());
        flexboxLayoutManagerMultiAnswer.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManagerMultiAnswer.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManagerMultiAnswer.setAlignItems(AlignItems.FLEX_START);
        flexboxLayoutManagerMultiAnswer.setJustifyContent(JustifyContent.CENTER);
        rvTags.setLayoutManager(flexboxLayoutManagerMultiAnswer);
        rvTags.setAdapter(myLickertGroupAdapter);
        myLickertGroupAdapter.notifyDataSetChanged();
    }
}
