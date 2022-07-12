package wa.mobile.rpghelper.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.adapter.CharacteristicListAdapter;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.dao.CharacteristicDao;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.util.IntentKey;

public class CharacteristicAddActivity extends AppCompatActivity {

    @BindView(R.id.characteristic_list)
    ListView characteristicList;

    @BindView(R.id.create_new_characteristic_button)
    Button createNewCharacteristicButton;

    CharacteristicDao characteristicDao;

    int[] exceptIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic_add);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intentData = getIntent();
        /*if (!intentData.hasExtra(IntentKey.USED_CHARACTERISTIC_IDS)) {
            finish();
        }*/
        exceptIds = intentData.getExtras().getIntArray(IntentKey.USED_CHARACTERISTIC_IDS);

        characteristicDao = DatabaseContextSingleton.getDatabaseContext(this).characteristicDao();

        configCreateCharacteristicModal();

        updateList();
    }

    void configCreateCharacteristicModal() {
        createNewCharacteristicButton.setOnClickListener(view -> {

            final EditText edittext = new EditText(this);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setView(edittext);
            alert.setPositiveButton(R.string.placer_create, (dialogInterface, i) -> {
                String name = edittext.getText().toString();
                if (characteristicDao.exist(name)) {
                    Toast.makeText(this, R.string.toast_characteristic_already_exist, Toast.LENGTH_LONG).show();
                } else {
                    Characteristic characteristic = new Characteristic(name);
                    characteristicDao.insert(characteristic);
                    updateList();
                    dialogInterface.dismiss();
                }
            });
            alert.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            alert.show();

        });
    }

    void updateList() {
        List<Characteristic> characteristics = characteristicDao.getAllExcept(exceptIds);

        CharacteristicListAdapter adapter = new CharacteristicListAdapter(this, characteristics);
        characteristicList.setAdapter(adapter);
        configOnItemClickModal();
    }

    @SuppressLint("DefaultLocale")
    void configOnItemClickModal() {
        characteristicList.setOnItemClickListener((adapterView, view, itemId, l) -> {

            Characteristic characteristic = (Characteristic) adapterView.getItemAtPosition(itemId);

            View inflated = getLayoutInflater().inflate(R.layout.modal_choose_characteristic_value, null);
            ((TextView) inflated.findViewById(R.id.characteristic_name)).setText(characteristic.getName());
            NumberPicker numberPicker = inflated.findViewById(R.id.characteristic_value);
            float step = 0.1f;
            String[] numbers = new String[(int) (10 / step + 1)];
            for (int i = 0; i * step <= 10; i++) {
                numbers[i] = String.format("%.1f", i * step);;
            }
            numberPicker.setMaxValue(numbers.length-1);
            numberPicker.setMinValue(0);
            numberPicker.setDisplayedValues(numbers);
            numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numberPicker.setWrapSelectorWheel(false);

            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setView(inflated);
            alert.setPositiveButton(R.string.placer_add, (dialogInterface, i) -> {
                Intent intent = new Intent();
                intent.putExtra(IntentKey.CHARACTERISTIC_ID, characteristic.getId());
                int index = numberPicker.getValue();
                String val = numbers[index];
                float selectedFloat = Float.parseFloat(val);
                intent.putExtra(IntentKey.CHARACTERISTIC_VALUE, selectedFloat);
                setResult(RESULT_OK, intent);
                finish();
            });
            alert.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            alert.show();

        });
    }
}