package wa.mobile.rpghelper.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.adapter.TableAbilityListAdapter;
import wa.mobile.rpghelper.adapter.TableCharacteristicListAdapter;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.entity.Ability;
import wa.mobile.rpghelper.database.entity.EntityImage;
import wa.mobile.rpghelper.database.entity.relational.CharacterInfo;
import wa.mobile.rpghelper.modal.AbilityModal;
import wa.mobile.rpghelper.modal.CharacterCharacteristicModal;
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

    ActivityResultLauncher<Intent> startForNextUpdate = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> updatePage());

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
            intent.putExtra(IntentKey.CHARACTER_ID, info.character.getId());
            intent.putExtra(IntentKey.USED_CHARACTERISTIC_IDS, info.getCharacterCharacteristicsIds());
            startForNextUpdate.launch(intent);
        });

        characterImageView.setOnClickListener(view -> {
            Intent intent = new Intent(this, ImageSelectActivity.class);
            intent.putExtra(IntentKey.CARD_IMAGES, getResources().getStringArray(R.array.character_images));
            startForImageResult.launch(intent);
        });

        abilityAddButton.setOnClickListener(view -> {
            AbilityModal.Create(this, info.character.getId(), this::updatePage);
        });

        updatePage();

        selectMode(VIEW_MODE);
        selectTab(STATISTICS_TAB);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater inflater = getMenuInflater();

        int menuType = (int) view.getTag(R.id.context_menu_type);
        menu.clearHeader();
        if (currentMode == EDIT_MODE) {
            int itemId = (int) view.getTag(R.id.item_id);
            Intent data = new Intent();
            data.putExtra(IntentKey.CONTEXT_MENU_TYPE, menuType);
            data.putExtra(IntentKey.ITEM_ID, itemId);
            switch (menuType) {
                case ContextMenuType.CHARACTER_CHARACTERISTIC:
                    inflater.inflate(R.menu.menu_context_character_characteristic, menu);
                    break;
                case ContextMenuType.ABILITY:
                    inflater.inflate(R.menu.menu_context_ability, menu);
            }
            menu.findItem(R.id.edit).setIntent(data);
            menu.findItem(R.id.delete).setIntent(data);
        }
        else if (currentMode == VIEW_MODE) {
            int itemId = (int) view.getTag(R.id.item_id);
            if (menuType == ContextMenuType.ABILITY) {
                Ability ability = DatabaseContextSingleton
                        .getDatabaseContext(this)
                        .abilityDao().get(itemId);
                displayAbilityInfoPopup(view, ability);
            }
        }
    }

    public static Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    @SuppressLint("RtlHardcoded")
    private void displayAbilityInfoPopup(View anchorView, Ability ability) {
        PopupWindow popup = new PopupWindow(this);
        View layout = getLayoutInflater().inflate(R.layout.popup_info_ability, null);
        ((TextView) layout.findViewById(R.id.ability_info)).setText(ability.getDescription());
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        Rect location = locateView(anchorView);
        popup.showAtLocation(anchorView, Gravity.TOP|Gravity.RIGHT,
                location.left, location.bottom);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        Intent intent = item.getIntent();
        int id = intent.getExtras().getInt(IntentKey.ITEM_ID);
        int menuType = intent.getExtras().getInt(IntentKey.CONTEXT_MENU_TYPE);

        switch (item.getItemId()) {
            case R.id.edit:
                if (menuType == ContextMenuType.CHARACTER_CHARACTERISTIC) {
                    int linkId = DatabaseContextSingleton
                            .getDatabaseContext(this)
                            .characterCharacteristicLinkDao()
                            .getId(charId, id);
                    CharacterCharacteristicModal
                            .Update(this, linkId, this::updatePage);
                }
                else if (menuType == ContextMenuType.ABILITY) {
                    AbilityModal
                            .Update(this, id, this::updatePage);
                }
                break;
            case R.id.delete:
                if (menuType == ContextMenuType.CHARACTER_CHARACTERISTIC) {
                    int linkId = DatabaseContextSingleton
                            .getDatabaseContext(this)
                            .characterCharacteristicLinkDao()
                            .getId(charId, id);
                    CharacterCharacteristicModal
                            .Delete(this, linkId, this::updatePage);
                }
                else if (menuType == ContextMenuType.ABILITY) {
                    AbilityModal
                            .Delete(this, id, this::updatePage);
                }
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
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