package wa.mobile.rpghelper.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.adapter.ItemCardAdapter;
import wa.mobile.rpghelper.adapter.SpacesItemDecoration;
import wa.mobile.rpghelper.adapter.TableAbilityListAdapter;
import wa.mobile.rpghelper.adapter.TableCharacteristicListAdapter;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.entity.Ability;
import wa.mobile.rpghelper.database.entity.EntityImage;
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.database.entity.relational.CharacterInfo;
import wa.mobile.rpghelper.database.entity.relational.ItemWithCharacteristics;
import wa.mobile.rpghelper.drag.DropRecycleItem;
import wa.mobile.rpghelper.window.AbilityModal;
import wa.mobile.rpghelper.window.CharacterCharacteristicModal;
import wa.mobile.rpghelper.util.ContextMenuType;
import wa.mobile.rpghelper.util.IntentKey;
import wa.mobile.rpghelper.window.InfoPopup;
import wa.mobile.rpghelper.window.ItemModal;

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

    /*@BindView(R.id.drag_item)
    ImageView drag_item;*/
    @BindView(R.id.container_1)
    RecyclerView container_1;
    @BindView(R.id.container_2)
    RecyclerView container_2;

    ItemCardAdapter itemCardAdapter_1;
    ItemCardAdapter itemCardAdapter_2;

    private int currentMode = VIEW_MODE;

    private int charId;
    private CharacterInfo info;

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

        container_1.setOnDragListener(myOnDragListener);
        container_2.setOnDragListener(myOnDragListener);
        container_1.addItemDecoration(new SpacesItemDecoration(2));
        container_2.addItemDecoration(new SpacesItemDecoration(2));

        updatePage();

        selectMode(VIEW_MODE);
        selectTab(INVENTORY_TAB);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater inflater = getMenuInflater();

        int menuType = (int) view.getTag(R.id.context_menu_type);
        menu.clearHeader();
        if (currentMode == EDIT_MODE) {
            int itemId = (int) view.getTag(R.id.item_id);
            String itemName = (String) view.getTag(R.id.item_name);
            Intent data = new Intent();
            data.putExtra(IntentKey.CONTEXT_MENU_TYPE, menuType);
            data.putExtra(IntentKey.ITEM_ID, itemId);
            switch (menuType) {
                case ContextMenuType.CHARACTER_CHARACTERISTIC:
                    inflater.inflate(R.menu.menu_context_character_characteristic, menu);
                    break;
                case ContextMenuType.ABILITY:
                    inflater.inflate(R.menu.menu_context_ability, menu);
                    break;
                case ContextMenuType.ITEM:
                    inflater.inflate(R.menu.menu_context_item, menu);
                    break;
            }
            menu.findItem(R.id.edit).setIntent(data);
            menu.findItem(R.id.delete).setIntent(data);
            menu.setHeaderTitle(itemName);
        }
        else if (currentMode == VIEW_MODE) {
            int itemId = (int) view.getTag(R.id.item_id);
            if (menuType == ContextMenuType.ABILITY) {
                Ability ability = DatabaseContextSingleton
                        .getDatabaseContext(this)
                        .abilityDao().get(itemId);
                InfoPopup.displayAbilityInfo(this, view, ability);
            } else if (menuType == ContextMenuType.ITEM) {
                ItemWithCharacteristics itemInfo = DatabaseContextSingleton
                        .getDatabaseContext(this)
                        .itemDao().getItemWithCharacteristics(itemId);
                InfoPopup.displayItemInfo(this, view, itemInfo);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
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
                    AbilityModal.Update(this, id, this::updatePage);
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
                    AbilityModal.Delete(this, id, this::updatePage);
                }
                else if (menuType == ContextMenuType.ITEM) {
                    ItemModal.Delete(this, id, this::updatePage);
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
                itemCardAdapter_1.setMode(ItemCardAdapter.VIEW_MODE);
                itemCardAdapter_2.setMode(ItemCardAdapter.VIEW_MODE);
                break;
            case EDIT_MODE:
                currentMode = EDIT_MODE;
                characteristicAddButton.setVisibility(View.VISIBLE);
                abilityAddButton.setVisibility(View.VISIBLE);
                characterImageView.setClickable(true);
                itemCardAdapter_1.setMode(ItemCardAdapter.EDIT_MODE);
                itemCardAdapter_2.setMode(ItemCardAdapter.EDIT_MODE);
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
                new TableCharacteristicListAdapter(this, info.getCharacteristicSummary());
        characteristicsListView.setAdapter(adapter_1);

        TableAbilityListAdapter adapter_2
                = new TableAbilityListAdapter(this, info.abilities);
        abilitiesListView.setAdapter(adapter_2);

        itemCardAdapter_1 = new ItemCardAdapter(this, info.getItems(true));
        container_1.setAdapter(itemCardAdapter_1);
        GridLayoutManager layoutManager_1 = new GridLayoutManager(this, 4);
        container_1.setLayoutManager(layoutManager_1);

        itemCardAdapter_2 = new ItemCardAdapter(this, info.getItems(false));
        container_2.setAdapter(itemCardAdapter_2);
        GridLayoutManager layoutManager_2 = new GridLayoutManager(this, 4);
        container_2.setLayoutManager(layoutManager_2);

        selectMode(currentMode);
    }

    View.OnDragListener myOnDragListener = (v, event) -> {

        String area;
        if(v == container_1){
            area = "area1";
        }else if(v == container_2){
            area = "area2";
        }else{
            area = "unknown";
        }

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.i("", "ACTION_DRAG_STARTED: " + area  + "\n");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.i("", "ACTION_DRAG_ENTERED: " + area  + "\n");
                v.setBackgroundResource(R.drawable.background_box_round_fill);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.i("", "ACTION_DRAG_EXITED: " + area  + "\n");
                v.setBackgroundResource(R.drawable.background_box_round);
                break;
            case DragEvent.ACTION_DROP:
                Log.i("", "ACTION_DROP: " + area  + "\n");

                DropRecycleItem passObj = (DropRecycleItem)event.getLocalState();
                View passedItemView = passObj.view;
                Item passedItem = passObj.item;
                RecyclerView oldParent = (RecyclerView)passedItemView.getParent();
                RecyclerView newParent = (RecyclerView)v;

                if (oldParent != newParent) {
                    DatabaseContextSingleton.getDatabaseContext(this)
                            .itemDao().toggleEquipped(passedItem.getId());
                    passedItem.toggleEquipped();
                }
                updatePage();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.i("", "ACTION_DRAG_ENDED: " + area  + "\n");
                ((DropRecycleItem)event.getLocalState()).view.setVisibility(View.VISIBLE);
                v.setBackgroundResource(R.drawable.background_box_round);
            default:
                break;
        }
        return true;
    };

}