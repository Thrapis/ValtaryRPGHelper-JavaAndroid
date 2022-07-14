package wa.mobile.rpghelper.modal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.dao.AbilityDao;
import wa.mobile.rpghelper.database.dao.CharacteristicDao;
import wa.mobile.rpghelper.database.entity.Ability;
import wa.mobile.rpghelper.database.entity.Characteristic;

public class CharacteristicModal {

    public static void Create(Context context, Runnable onPositiveComplete) {

        CharacteristicDao characteristicDao = DatabaseContextSingleton
                .getDatabaseContext(context)
                .characteristicDao();

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        EditText edittext = new EditText(context);

        alert.setView(edittext);
        alert.setPositiveButton(R.string.placer_create, (dialogInterface, i) -> {
            String name = edittext.getText().toString();
            if (characteristicDao.exist(name)) {
                Toast.makeText(context, R.string.toast_characteristic_already_exist, Toast.LENGTH_LONG).show();
            } else {
                Characteristic characteristic = new Characteristic(name);
                characteristicDao.insert(characteristic);
                onPositiveComplete.run();
            }
        });
        alert.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        alert.show();
    }

    public static void Update(Context context, int characteristicId, Runnable onPositiveComplete) {

        CharacteristicDao characteristicDao = DatabaseContextSingleton
                .getDatabaseContext(context)
                .characteristicDao();

        Characteristic characteristic = characteristicDao.get(characteristicId);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        EditText edittext = new EditText(context);
        edittext.setText(characteristic.getName());

        alert.setView(edittext);
        alert.setPositiveButton(R.string.placer_edit, (dialogInterface, i) -> {
            String name = edittext.getText().toString();
            if (characteristicDao.exist(name)) {
                Toast.makeText(context, R.string.toast_characteristic_already_exist, Toast.LENGTH_LONG).show();
            } else {
                characteristic.setName(name);
                characteristicDao.update(characteristic);
                onPositiveComplete.run();
            }
        });
        alert.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        alert.show();
    }

    public static void Delete(Context context, int characteristicId, Runnable onPositiveComplete) {

        CharacteristicDao characteristicDao = DatabaseContextSingleton
                .getDatabaseContext(context)
                .characteristicDao();

        Characteristic characteristic = characteristicDao.get(characteristicId);

        AlertDialog.Builder modal = new AlertDialog.Builder(context);

        String message = String.format(context.getString(R.string.question_characteristic_delete), characteristic.getName());

        modal.setMessage(message);
        modal.setPositiveButton(R.string.placer_delete, (dialogInterface, i) -> {
            characteristicDao.delete(characteristic);
            onPositiveComplete.run();
        });
        modal.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        modal.show();
    }
}
