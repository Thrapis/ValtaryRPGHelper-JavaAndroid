package wa.mobile.rpghelper.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jsibbold.zoomage.ZoomageView;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.localstore.manager.UserStoreManager;
import wa.mobile.rpghelper.localstore.entity.User;

public class MapFragment extends Fragment {

    public MapFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View createdView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(createdView, savedInstanceState);
        String[] maps = getResources().getStringArray(R.array.map_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(createdView.getContext(), android.R.layout.simple_list_item_1, maps);
        Spinner mapSpinner = createdView.findViewById(R.id.map_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(adapter);
        //ImageView loading = createdView.findViewById(R.id.loading_view);
        ZoomageView pad = createdView.findViewById(R.id.zoom_view);
        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: pad.setImageResource(R.drawable.map_detailed_elrady); break;
                    case 1: pad.setImageResource(R.drawable.map_detailed_valtary); break;
                    case 2: pad.setImageResource(R.drawable.map_lite_elrady); break;
                    case 3: pad.setImageResource(R.drawable.map_lite_valtary); break;
                }
                User user = UserStoreManager.getUser(createdView.getContext());
                user.setLastMapSelection(position);
                UserStoreManager.updateUser(createdView.getContext(), user);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        User user = UserStoreManager.getUser(createdView.getContext());
        mapSpinner.setSelection(user.getLastMapSelection());
    }
}