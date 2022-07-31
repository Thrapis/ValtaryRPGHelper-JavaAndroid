package wa.mobile.rpghelper.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.entity.Ability;
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.database.entity.relational.ItemCharacteristic;
import wa.mobile.rpghelper.database.entity.relational.ItemWithCharacteristics;
import wa.mobile.rpghelper.util.ViewUtility;

public class InfoPopup {

    @SuppressLint("RtlHardcoded")
    public static void displayAbilityInfo(@NonNull Context context, @NonNull View anchorView, @NonNull Ability ability) {
        PopupWindow popup = new PopupWindow(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_info_ability, null);
        ((TextView) layout.findViewById(R.id.ability_info)).setText(ability.getDescription());
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        Rect location = ViewUtility.locateView(anchorView);
        popup.showAtLocation(anchorView, Gravity.TOP|Gravity.RIGHT,
                location.left, location.bottom);
    }

    @SuppressLint({"RtlHardcoded", "DefaultLocale"})
    public static void displayItemInfo(@NonNull Context context, @NonNull View anchorView,
                                       @NonNull ItemWithCharacteristics itemInfo) {

        String info = "";
        for (ItemCharacteristic ic : itemInfo.itemCharacteristics) {
            if (info.length() > 0) {
                info += "\n";
            }
            info += String.format("%s: %.1f", ic.characteristic.getName(), ic.link.getValue());
        }
        if (itemInfo.item.getDescription().trim().length() > 0) {
            if (info.length() > 0) {
                info += "\n\n";
            }
            info += String.format("%s", itemInfo.item.getDescription());
        }

        PopupWindow popup = new PopupWindow(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_info_item, null);
        ((TextView) layout.findViewById(R.id.item_info)).setText(info);
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        Rect location = ViewUtility.locateView(anchorView);
        popup.showAtLocation(anchorView, Gravity.TOP|Gravity.LEFT,
                location.left + location.width() / 4,
                location.top + location.height() / 4);
    }
}
