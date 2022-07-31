package wa.mobile.rpghelper.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.MotionEventCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.helper.ItemTouchHelperAdapter;
import wa.mobile.rpghelper.helper.ItemTouchHelperViewHolder;
import wa.mobile.rpghelper.helper.OnStartDragListener;

public class ItemCardAdapter extends RecyclerView.Adapter<ItemCardAdapter.ItemHolder>
        implements ItemTouchHelperAdapter {

    private final Context _context;
    private final List<Item> _items;
    //private final View.OnClickListener _listener;
    private final OnStartDragListener _dragStartListener;

    public ItemCardAdapter(Context context, List<Item> items,
                           OnStartDragListener dragStartListener) {
        _items = items;
        _context = context;
        _dragStartListener = dragStartListener;
    }

    public void appendItem(Item i) {
        _items.add(i);
    }

    public void removeItem(Item i) {
        _items.remove(i);
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
        holder.applyItem(item);

        // holder.itemView.setOnClickListener(_listener);
    }

    @Override
    public void onItemDismiss(int position) {
        _items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(_items, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        View itemView;
        Item item;

        @BindView(R.id.item_image)
        ImageView itemImage;

        /*@BindView(R.id.reorder_image)
        ImageView reorderImage;*/

        @BindView(R.id.item_name)
        TextView itemName;

        public ItemHolder(@NonNull View itemView){
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void applyItem(Item item) {
            this.item = item;
            itemImage.setImageBitmap(item.image.getBitmap(_context));
            itemName.setText(item.getName());
            itemView.setTag(item);

            itemView.setOnTouchListener((v, event) -> {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    _dragStartListener.onStartDrag(this);
                }
                return false;
            });
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
