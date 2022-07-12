package wa.mobile.rpghelper.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.adapter.TableAbilityListAdapter;
import wa.mobile.rpghelper.adapter.TableCharacteristicListAdapter;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.dao.AbilityDao;
import wa.mobile.rpghelper.database.entity.Ability;
import wa.mobile.rpghelper.database.entity.CharacterCharacteristicLink;
import wa.mobile.rpghelper.database.entity.EntityImage;
import wa.mobile.rpghelper.database.entity.relational.CharacterInfo;
import wa.mobile.rpghelper.util.ContextMenuType;
import wa.mobile.rpghelper.util.IntentKey;

public class CharacterProfileActivity extends AppCompatActivity {

    private final int VIEW_MODE = 0;
    private final int EDIT_MODE = 1;

    private final int STATISTICS_TAB = 0;
    private final int INVENTORY_TAB = 1;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.edit_switch)
    Switch editSwitch;

    @BindView(R.id.statistics_tab_button)
    Button statisticsTabButton;
    @BindView(R.id.inventory_tab_button)
    Button inventoryTabButton;
    @BindView(R.id.statistics_block)
    LinearLayout statisticsBlock;
    @BindView(R.id.inventory_block)
    LinearLayout inventoryBlock;

    @BindView(R.id.characteristic_add_button)
    Button characteristicAddButton;
    @BindView(R.id.ability_add_button)
    Button abilityAddButton;

    @BindView(R.id.character_name)
    TextView nameTextView;
    @BindView(R.id.character_image)
    ImageView characterImageView;
    @BindView(R.id.character_characteristics)
    ListView characteristicsListView;
    @BindView(R.id.character_abilities)
    ListView abilitiesListView;

    int currentMode = VIEW_MODE;

    int charId;
    CharacterInfo info;

    ActivityResultLauncher<Intent> startForCharacteristicResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    int characteristicId = bundle.getInt(IntentKey.CHARACTERISTIC_ID);
                    float value = bundle.getFloat(IntentKey.CHARACTERISTIC_VALUE);
                    CharacterCharacteristicLink link = new CharacterCharacteristicLink(charId, characteristicId, value);
                    DatabaseContextSingleton
                            .getDatabaseContext(this)
                            .characterCharacteristicLinkDao()
                            .insert(link);
                }
                updatePage();
            });

    ActivityResultLauncher<Intent> startForImageResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    int resourceId = data.getExtras().getInt(IntentKey.CARD_IMAGES_RESULT);
                    info.character.image = new EntityImage(this, resourceId);
                    DatabaseContextSingleton.getDatabaseContext(this)
                            .characterDao()
                            .update(info.character);
                }
                updatePage();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_profile);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intentData = getIntent();
        if (!intentData.hasExtra(IntentKey.CHARACTER_ID)) {
            finish();
        }
        charId = intentData.getExtras().getInt(IntentKey.CHARACTER_ID);

        statisticsTabButton.setOnClickListener(view -> selectTab(STATISTICS_TAB));

        inventoryTabButton.setOnClickListener(view -> selectTab(INVENTORY_TAB));

        editSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) selectMode(EDIT_MODE);
            else selectMode(VIEW_MODE);
        });

        characteristicAddButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CharacteristicAddActivity.class);
            intent.putExtra(IntentKey.USED_CHARACTERISTIC_IDS, info.getCharacterCharacteristicsIds());
            startForCharacteristicResult.launch(intent);
        });

        characterImageView.setOnClickListener(view -> {
            Intent intent = new Intent(this, ImageSelectActivity.class);
            intent.putExtra(IntentKey.CARD_IMAGES, getResources().getStringArray(R.array.character_images));
            startForImageResult.launch(intent);
        });

        configCreateAbilityModal();

        updatePage();

        selectMode(VIEW_MODE);
        selectTab(STATISTICS_TAB);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderTitle("Context Menu");

        MenuInflater inflater = getMenuInflater();

        int menuType = (int) view.getTag(R.id.context_menu_type);
        menu.clearHeader();
        if (currentMode == EDIT_MODE) {
            int itemId = (int) view.getTag(R.id.item_id);
            Intent data = new Intent();
            data.putExtra(IntentKey.CONTEXT_MENU_TYPE, menuType);
            data.putExtra(IntentKey.ITEM_ID, itemId);

            switch (menuType) {
                case ContextMenuType.CHARACTERISTIC:
                    inflater.inflate(R.menu.menu_context_characteristic, menu);
                    break;
                case ContextMenuType.ABILITY:
                    inflater.inflate(R.menu.menu_context_ability, menu);
            }

            menu.findItem(R.id.edit).setIntent(data);
            menu.findItem(R.id.delete).setIntent(data);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        Intent intent = item.getIntent();
        int id = intent.getExtras().getInt(IntentKey.ITEM_ID);
        switch (item.getItemId())
        {
            case R.id.edit:
                Toast.makeText(this, "edit " + id, Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "delete " + id, Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    void configCreateAbilityModal() {
        abilityAddButton.setOnClickListener(view -> {

            AbilityDao abilityDao = DatabaseContextSingleton
                    .getDatabaseContext(this)
                    .abilityDao();

            final View layout = getLayoutInflater().inflate(R.layout.modal_add_ability, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setView(layout);
            alert.setPositiveButton(R.string.placer_create, (dialogInterface, i) -> {
                String name = ((EditText) layout.findViewById(R.id.ability_name)).getText().toString();
                String description = ((EditText) layout.findViewById(R.id.ability_description)).getText().toString();
                Ability ability = new Ability(name, info.character.getId(), description);
                abilityDao.insert(ability);
                updatePage();
            });
            alert.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
            alert.show();

        });
    }

    void selectTab(int tab) {
        switch (tab) {
            case STATISTICS_TAB:
                statisticsBlock.setVisibility(View.VISIBLE);
                inventoryBlock.setVisibility(View.GONE);
                break;
            case INVENTORY_TAB:
                statisticsBlock.setVisibility(View.GONE);
                inventoryBlock.setVisibility(View.VISIBLE);
        }
    }

    void selectMode(int mode) {
        switch (mode) {
            case VIEW_MODE:
                currentMode = VIEW_MODE;
                characteristicAddButton.setVisibility(View.INVISIBLE);
                abilityAddButton.setVisibility(View.INVISIBLE);
                characterImageView.setClickable(false);
                break;
            case EDIT_MODE:
                currentMode = EDIT_MODE;
                characteristicAddButton.setVisibility(View.VISIBLE);
                abilityAddButton.setVisibility(View.VISIBLE);
                characterImageView.setClickable(true);
        }
    }

    void updatePage() {
        info = DatabaseContextSingleton
                .getDatabaseContext(this)
                .characterDao()
                .getInfo(charId);

        nameTextView.setText(info.character.getName());
        characterImageView.setImageBitmap(info.character.image.getBitmap(this));

        TableCharacteristicListAdapter adapter_1 =
                new TableCharacteristicListAdapter(this,
                        info.getCharacteristicSummary());
        characteristicsListView.setAdapter(adapter_1);

        TableAbilityListAdapter adapter_2
                = new TableAbilityListAdapter(this, info.abilities);
        abilitiesListView.setAdapter(adapter_2);
    }
}