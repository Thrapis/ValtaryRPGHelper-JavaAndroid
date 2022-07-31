package wa.mobile.rpghelper.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.context.DatabaseContext;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.dao.CharacterCharacteristicLinkDao;
import wa.mobile.rpghelper.database.dao.CharacteristicDao;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.CharacterCharacteristicLink;
import wa.mobile.rpghelper.database.entity.Characteristic;

public class CharacterCharacteristicModal {

    final static float valueStep = 0.1f;

    @SuppressLint("DefaultLocale")
    public static void Create(Context context, int characterId, int characteristicId, Runnable onPositiveComplete) {

        CharacteristicDao characteristicDao = DatabaseContextSingleton
                .getDatabaseContext(context).characteristicDao();

        CharacterCharacteristicLinkDao linkDao = DatabaseContextSingleton
                .getDatabaseContext(context).characterCharacteristicLinkDao();

        Characteristic characteristic = characteristicDao.get(characteristicId);

        LayoutInflater inflater = LayoutInflater.from(context);
        View inflated = inflater.inflate(R.layout.modal_choose_characteristic_value, null);

        ((TextView) inflated.findViewById(R.id.characteristic_name)).setText(characteristic.getName());
        NumberPicker numberPicker = inflated.findViewById(R.id.characteristic_value);
        String[] numbers = new String[(int) (10 / valueStep + 1)];
        for (int i = 0; i * valueStep <= 10; i++) {
            numbers[i] = String.format("%.1f", i * valueStep);;
        }
        numberPicker.setMaxValue(numbers.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(numbers);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setWrapSelectorWheel(false);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setView(inflated);
        alert.setPositiveButton(R.string.placer_add, (dialogInterface, i) -> {
            int index = numberPicker.getValue();
            String val = numbers[index].replace(',', '.');
            float value = Float.parseFloat(val);
            CharacterCharacteristicLink link = new CharacterCharacteristicLink(characterId, characteristicId, value);
            linkDao.insert(link);
            onPositiveComplete.run();
        });
        alert.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        alert.show();
    }

    @SuppressLint("DefaultLocale")
    public static void Update(Context context, int characterCharacteristicId, Runnable onPositiveComplete) {

        CharacteristicDao characteristicDao = DatabaseContextSingleton
                .getDatabaseContext(context).characteristicDao();

        CharacterCharacteristicLinkDao linkDao = DatabaseContextSingleton
                .getDatabaseContext(context).characterCharacteristicLinkDao();

        CharacterCharacteristicLink link = linkDao.get(characterCharacteristicId);
        Characteristic characteristic = characteristicDao.get(link.getCharacteristicId());

        LayoutInflater inflater = LayoutInflater.from(context);
        View inflated = inflater.inflate(R.layout.modal_choose_characteristic_value, null);

        ((TextView) inflated.findViewById(R.id.characteristic_name)).setText(characteristic.getName());
        NumberPicker numberPicker = inflated.findViewById(R.id.characteristic_value);
        int currentIndex = 0;
        String[] numbers = new String[(int) (10 / valueStep + 1)];
        for (int i = 0; i * valueStep <= 10; i++) {
            numbers[i] = String.format("%.1f", i * valueStep);
            if (i * valueStep == link.getValue()){
                currentIndex = i;
            }
        }
        numberPicker.setMaxValue(numbers.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(numbers);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(currentIndex);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setView(inflated);
        alert.setPositiveButton(R.string.placer_edit, (dialogInterface, i) -> {
            int index = numberPicker.getValue();
            String val = numbers[index].replace(',', '.');
            float value = Float.parseFloat(val);
            link.setValue(value);
            linkDao.update(link);
            onPositiveComplete.run();
        });
        alert.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        alert.show();
    }

    public static void Delete(Context context, int characterCharacteristicId, Runnable onPositiveComplete) {

        DatabaseContext databaseContext = DatabaseContextSingleton
                .getDatabaseContext(context);

        CharacterCharacteristicLinkDao linkDao = databaseContext
                .characterCharacteristicLinkDao();

        CharacterCharacteristicLink link = linkDao.get(characterCharacteristicId);
        Character character = databaseContext.characterDao()
                .get(link.getCharacterId());
        Characteristic characteristic = databaseContext.characteristicDao()
                .get(link.getCharacteristicId());

        AlertDialog.Builder modal = new AlertDialog.Builder(context);

        String message = String.format(context.getString(R.string.question_character_characteristic_delete),
                characteristic.getName(), character.getName());

        modal.setMessage(message);
        modal.setPositiveButton(R.string.placer_delete, (dialogInterface, i) -> {
            linkDao.delete(link);
            onPositiveComplete.run();
        });
        modal.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        modal.show();
    }
}
