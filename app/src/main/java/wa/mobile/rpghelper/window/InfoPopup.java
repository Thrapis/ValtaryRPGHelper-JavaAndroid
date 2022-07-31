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
import wa.mobile.rpghelper.database.entity.Ability;
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
}
