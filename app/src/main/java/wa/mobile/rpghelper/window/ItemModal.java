package wa.mobile.rpghelper.window;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.dao.CharacteristicDao;
import wa.mobile.rpghelper.database.dao.ItemDao;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.database.entity.Item;

public class ItemModal {

    public static void Delete(Context context, int itemId, Runnable onPositiveComplete) {

        ItemDao itemDao = DatabaseContextSingleton
                .getDatabaseContext(context)
                .itemDao();

        Item item = itemDao.get(itemId);

        AlertDialog.Builder modal = new AlertDialog.Builder(context);

        String message = String.format(context.getString(R.string.question_item_delete), item.getName());

        modal.setMessage(message);
        modal.setPositiveButton(R.string.placer_delete, (dialogInterface, i) -> {
            itemDao.delete(item);
            onPositiveComplete.run();
        });
        modal.setNegativeButton(R.string.placer_cancel, (dialogInterface, i) -> {});
        modal.show();
    }
}
