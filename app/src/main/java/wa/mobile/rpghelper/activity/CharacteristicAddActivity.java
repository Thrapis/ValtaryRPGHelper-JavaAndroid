package wa.mobile.rpghelper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.adapter.CharacteristicListAdapter;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.dao.CharacteristicDao;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.modal.AbilityModal;
import wa.mobile.rpghelper.modal.CharacterCharacteristicModal;
import wa.mobile.rpghelper.modal.CharacteristicModal;
import wa.mobile.rpghelper.util.ContextMenuType;
import wa.mobile.rpghelper.util.IntentKey;

public class CharacteristicAddActivity extends AppCompatActivity {

    @BindView(R.id.characteristic_list)
    ListView characteristicList;

    @BindView(R.id.create_new_characteristic_button)
    Button createNewCharacteristicButton;

    CharacteristicDao characteristicDao;

    int characterId;
    int[] exceptIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic_add);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intentData = getIntent();
        characterId = intentData.getExtras().getInt(IntentKey.CHARACTER_ID);
        exceptIds = intentData.getExtras().getIntArray(IntentKey.USED_CHARACTERISTIC_IDS);

        characteristicDao = DatabaseContextSingleton.getDatabaseContext(this).characteristicDao();

        createNewCharacteristicButton.setOnClickListener(view -> {
            CharacteristicModal.Create(this, this::updateList);
        });

        updateList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater inflater = getMenuInflater();

        int menuType = (int) view.getTag(R.id.context_menu_type);
        menu.clearHeader();

        int itemId = (int) view.getTag(R.id.item_id);
        Intent data = new Intent();
        data.putExtra(IntentKey.CONTEXT_MENU_TYPE, menuType);
        data.putExtra(IntentKey.ITEM_ID, itemId);

        if (menuType == ContextMenuType.CHARACTERISTIC) {
            inflater.inflate(R.menu.menu_context_characteristic, menu);
        }

        menu.findItem(R.id.edit).setIntent(data);
        menu.findItem(R.id.delete).setIntent(data);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        Intent intent = item.getIntent();
        int id = intent.getExtras().getInt(IntentKey.ITEM_ID);
        int menuType = intent.getExtras().getInt(IntentKey.CONTEXT_MENU_TYPE);

        switch (item.getItemId()) {
            case R.id.edit:
                if (menuType == ContextMenuType.CHARACTERISTIC) {
                    CharacteristicModal
                            .Update(this, id, this::updateList);
                }
                break;
            case R.id.delete:
                if (menuType == ContextMenuType.CHARACTERISTIC) {
                    CharacteristicModal
                            .Delete(this, id, this::updateList);
                }
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    void updateList() {
        List<Characteristic> characteristics = characteristicDao.getAllExcept(exceptIds);
        CharacteristicListAdapter adapter = new CharacteristicListAdapter(this, characteristics, itemId -> {
            CharacterCharacteristicModal.Create(this, characterId, itemId, () -> {
                setResult(RESULT_OK, null);
                finish();
            });
        });
        characteristicList.setAdapter(adapter);
    }
}