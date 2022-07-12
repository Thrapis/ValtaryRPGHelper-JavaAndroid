package wa.mobile.rpghelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wa.mobile.rpghelper.R;

public class ImageCardAdapter extends RecyclerView.Adapter<ImageCardAdapter.ItemHolder> {

    private final Context _context;
    private final List<String> _items;
    private final View.OnClickListener _listener;

    public ImageCardAdapter(Context context, List<String> items, View.OnClickListener listener) {
        _items = items;
        _context = context;
        _listener = listener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding vdb = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.image_card,
                parent, false
        );
        return new ItemHolder(vdb);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        String item = _items.get(position);
        final int resourceId = _context
                .getResources()
                .getIdentifier(item, "drawable",
                _context.getPackageName());

        ImageView imageView = holder.itemView.findViewById(R.id.imageCard);
        imageView.setImageDrawable(AppCompatResources.getDrawable(_context, resourceId));
        holder.itemView.setTag(resourceId);

        holder.itemView.setOnClickListener(_listener);
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ViewDataBinding itemBinding;
        String item;

        public ItemHolder(@NonNull ViewDataBinding itemBinding){
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;

            /*goodItemBinding.getRoot().setOnClickListener(view -> {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    ((MainActivity)context).newStateOfFragments(SELECT_INFO, item);
                }
                else {
                    Intent intent = new Intent(context, InfoGoodItem.class);
                    intent.putExtra("good", item);
                    context.startActivity(intent);
                }
            });*/
        }
    }
}
