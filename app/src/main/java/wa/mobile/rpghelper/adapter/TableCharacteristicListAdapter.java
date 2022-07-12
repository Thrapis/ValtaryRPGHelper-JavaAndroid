package wa.mobile.rpghelper.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.util.ContextMenuType;

public class TableCharacteristicListAdapter extends BaseAdapter {
    Context _context;
    LayoutInflater _inflater;
    List<Characteristic> _keyList;
    HashMap<Characteristic, Pair<Float, Float>> _objects;

    public TableCharacteristicListAdapter(Context context,
                                          HashMap<Characteristic, Pair<Float, Float>> characteristicsSummary) {
        _context = context;
        _objects = characteristicsSummary;
        _keyList = new ArrayList<> (characteristicsSummary.keySet());
        _inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _objects.size();
    }

    @Override
    public Object getItem(int position) {
        return _objects.get(_keyList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = _inflater.inflate(R.layout.table_list_element_characteristic, parent, false);
        }

        Characteristic c = _keyList.get(position);
        Pair<Float, Float> summary = (Pair<Float, Float>) getItem(position);

        ((TextView) view.findViewById(R.id.characteristic_name)).setText(c.getName());
        ((TextView) view.findViewById(R.id.characteristic_clear)).setText(String.format("%.1f", summary.first));
        ((TextView) view.findViewById(R.id.characteristic_full)).setText(String.format("%.1f", summary.first));

        view.setTag(R.id.context_menu_type, ContextMenuType.CHARACTERISTIC);
        view.setTag(R.id.item_id, c.getId());

        ((Activity)_context).registerForContextMenu(view);

        return view;
    }
}