package youilab.main.ui.avatares;

import Adapters.avatar_adapter;
import MVP.Interfaces.NewAvatar.AvatarsPresenter;
import MVP.Interfaces.NewAvatar.AvatarsView;
import MVP.Presenters.AvatarsPresenterImpl;
import SenSkyCore.Avatars.Avatar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import youilab.main.NewAvatar;
import youilab.main.R;

import java.util.List;

public class AvataresFragment extends Fragment implements AvatarsView {

    private TextView txtCurrentAvatarName;//nombre del avatar en uso
    private AvatarsPresenter avatarsPresenter;//presentador para las acciones con los avatares
    private RecyclerView rvAvatares;//vista para la lista de avatares registrados en el dispositivo
    private ImageView imgAvatar;//imagen descriptiva del avatar
    private CardView cvNewAvatar;//boton para crear un nuevo avatar


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_avatares, container, false);
        txtCurrentAvatarName = (TextView) root.findViewById(R.id.txtCurrentAvatarName);
        rvAvatares = (RecyclerView) root.findViewById(R.id.rvAvataresList);
        imgAvatar = (ImageView) root.findViewById(R.id.imgAvatar);
        cvNewAvatar = (CardView) root.findViewById(R.id.cvNewAvatar);

        //evento de click para llamar a la actividad que permite crear un nuevo avatar
        cvNewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAvatar = new Intent(getActivity(),NewAvatar.class);
                startActivity(intentNewAvatar);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int decorView = getActivity().getWindow().getDecorView().getSystemUiVisibility();
            getActivity().getWindow().getDecorView().setSystemUiVisibility(decorView & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.blue));
        }

        //se instancia el presentador de los avatares
        avatarsPresenter = new AvatarsPresenterImpl(this,getActivity());

        //se manda pedir la informacion del avatar actual
        avatarsPresenter.getCurrentAvatar();

        //se manda pedir la lista de avatares en el dispositivo
        avatarsPresenter.getAvatarList();
    }


    /**
     * Se obtuvo la lista de avatares y se manda mostrar
     * @param avatarList
     */
    @Override
    public void AvatarList(List<Avatar> avatarList) {
        setupRecyclerView(getActivity(),avatarList);
    }

    /**
     * se obtuvo la informacion del avatar activo actualmente
     * @param avatar
     */
    @Override
    public void CurrentAvatar(Avatar avatar) {
        txtCurrentAvatarName.setText(avatar.getName());
        Uri imgProfile= Uri.parse(avatar.getImagePath());
        Glide.with(getActivity()).load(imgProfile).into(imgAvatar);//muestra la imagen del avatar
    }

    @Override
    public void Error(String message) {

    }

    /**
     * Presenta la lista de avatares en un recyclerview
     * @param activity necesario para el contexto
     * @param avatarList lista de avatres en el dispositivo
     */
    private void setupRecyclerView(Activity activity, List<Avatar> avatarList){

        avatar_adapter itemAvatarAdapter = new avatar_adapter(avatarList,activity,avatarsPresenter);//instancia al adapatador de avatar para los items personalizados de avatares
        rvAvatares.setHasFixedSize(true);
        rvAvatares.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false));
        rvAvatares.setAdapter(itemAvatarAdapter);
    }
}