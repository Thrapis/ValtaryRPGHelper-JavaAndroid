package wa.mobile.rpghelper.adapter;

import static android.view.View.DRAG_FLAG_OPAQUE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.drag.DropRecycleItem;
import wa.mobile.rpghelper.util.ContextMenuType;

public class ItemCardAdapter extends RecyclerView.Adapter<ItemCardAdapter.ItemHolder> {

    public static final int VIEW_MODE = 0;
    public static final int EDIT_MODE = 1;

    private final Context _context;
    private final List<Item> _items;
    private int currentMode = VIEW_MODE;

    public ItemCardAdapter(Context context, List<Item> items) {
        _items = items;
        _context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMode(int mode) {
        currentMode = mode;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public boolean appendItem(Item i) {
        boolean res = _items.add(i);
        notifyDataSetChanged();
        return res;
    }

    @SuppressLint("NotifyDataSetChanged")
    public boolean removeItem(Item i) {
        boolean res = _items.remove(i);
        notifyDataSetChanged();
        return res;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(_context).inflate(R.layout.item_card, null);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = _items.get(position);
        holder.applyItem(item, currentMode);
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        View itemView;
        Item item;

        @BindView(R.id.item_image)
        ImageView itemImage;

        @BindView(R.id.reorder_image)
        ImageView reorderImage;

        @BindView(R.id.item_name)
        TextView itemName;

        public ItemHolder(@NonNull View itemView){
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void applyItem(Item item, int mode) {
            this.item = item;
            itemImage.setImageBitmap(item.image.getBitmap(_context));
            itemName.setText(item.getName());
            itemView.setTag(item);

            itemView.setTag(R.id.context_menu_type, ContextMenuType.ITEM);
            itemView.setTag(R.id.item_id, item.getId());
            itemView.setTag(R.id.item_name, item.getName());
            ((Activity)_context).registerForContextMenu(itemView);

            switch (mode) {
                case EDIT_MODE:
                    reorderImage.setVisibility(View.VISIBLE);
                    reorderImage.setOnTouchListener((view, motionEvent) -> {
                        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){
                            DropRecycleItem passObj = new DropRecycleItem(itemView, item);
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(itemView);
                            view.startDragAndDrop(data, shadowBuilder, passObj, DRAG_FLAG_OPAQUE);
                            itemView.setVisibility(View.GONE);
                            return true;
                        }
                        return false;
                    });
                    break;
                default:
                    reorderImage.setVisibility(View.GONE);
            }
        }
    }
}
