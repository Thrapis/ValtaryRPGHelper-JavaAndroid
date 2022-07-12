package wa.mobile.rpghelper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.activity.CharacterAddActivity;
import wa.mobile.rpghelper.adapter.CategoryExpandableListAdapter;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.dao.CategoryDao;

public class CharacterFragment extends Fragment {

    Context context;
    CategoryDao categoryDao;
    View inflatedView;

    ExpandableListView expListView;
    CategoryExpandableListAdapter listAdapter;

    ActivityResultLauncher<Intent> startForUpdate = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> updateData()
    );

    public CharacterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        categoryDao = DatabaseContextSingleton.getDatabaseContext(context).categoryDao();
        inflatedView = inflater.inflate(R.layout.fragment_character, container, false);
        expListView = inflatedView.findViewById(R.id.category_list);

        Button addCharacter = inflatedView.findViewById(R.id.add_character_button);
        addCharacter.setOnClickListener(view -> {
            Intent intent = new Intent(context, CharacterAddActivity.class);
            startForUpdate.launch(intent);
        });

        listAdapter = new CategoryExpandableListAdapter(context, categoryDao.getAllWithCharacters());
        expListView.setAdapter(listAdapter);

        return inflatedView;
    }

    private void updateData() {
        listAdapter.setListData(categoryDao.getAllWithCharacters());

        /*for (int i = 0; i < listAdapter.getGroupCount(); i++)
            expListView.expandGroup(i);*/
    }
}
