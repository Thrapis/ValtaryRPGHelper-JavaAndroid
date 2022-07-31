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
import wa.mobile.rpghelper.database.dao.CharacteristicDao;
import wa.mobile.rpghelper.database.dao.ItemCharacteristicLinkDao;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.database.entity.ItemCharacteristicLink;

public class ItemCharacteristicModal {

    final static float valueStep = 0.1f;

    @SuppressLint("DefaultLocale")
    public static void Create(Context context, int itemId, int characteristicId, Runnable onPositiveComplete) {

        CharacteristicDao characteristicDao = DatabaseContextSingleton
                .getDatabaseContext(context).characteristicDao();

        ItemCharacteristicLinkDao linkDao = DatabaseContextSingleton
                .getDatabaseContext(context).itemCharacteristicLinkDao();

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
            ItemCharacteristicLink link = new ItemCharacteristicLink(itemId, characteristicId, value);
            linkDao.insert(link);
            onPositiveComplete.run();
        });
        alert.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        alert.show();
    }

    @SuppressLint("DefaultLocale")
    public static void Update(Context context, int itemCharacteristicId, Runnable onPositiveComplete) {

        CharacteristicDao characteristicDao = DatabaseContextSingleton
                .getDatabaseContext(context).characteristicDao();

        ItemCharacteristicLinkDao linkDao = DatabaseContextSingleton
                .getDatabaseContext(context).itemCharacteristicLinkDao();

        ItemCharacteristicLink link = linkDao.get(itemCharacteristicId);
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

    public static void Delete(Context context, int itemCharacteristicId, Runnable onPositiveComplete) {

        DatabaseContext databaseContext = DatabaseContextSingleton
                .getDatabaseContext(context);

        ItemCharacteristicLinkDao linkDao = DatabaseContextSingleton
                .getDatabaseContext(context).itemCharacteristicLinkDao();

        ItemCharacteristicLink link = linkDao.get(itemCharacteristicId);
        Item item = databaseContext.itemDao()
                .get(link.getItemId());
        Characteristic characteristic = databaseContext.characteristicDao()
                .get(link.getCharacteristicId());

        AlertDialog.Builder modal = new AlertDialog.Builder(context);

        String message = String.format(context.getString(R.string.question_item_characteristic_delete),
                characteristic.getName(), item.getName());

        modal.setMessage(message);
        modal.setPositiveButton(R.string.placer_delete, (dialogInterface, i) -> {
            linkDao.delete(link);
            onPositiveComplete.run();
        });
        modal.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        modal.show();
    }
}
