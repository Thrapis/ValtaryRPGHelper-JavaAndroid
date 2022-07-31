package wa.mobile.rpghelper.drag;

import android.view.View;

import java.util.List;

import wa.mobile.rpghelper.database.entity.Item;

public class DropRecycleItem {

    public View view;
    public Item item;

    public DropRecycleItem(View v, Item i){
        view = v;
        item = i;
    }
}
