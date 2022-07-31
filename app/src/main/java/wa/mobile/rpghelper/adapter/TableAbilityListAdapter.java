package wa.mobile.rpghelper.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.entity.Ability;
import wa.mobile.rpghelper.util.ContextMenuType;

public class TableAbilityListAdapter extends BaseAdapter {
    Context _context;
    LayoutInflater _inflater;
    List<Ability> _objects;

    public TableAbilityListAdapter(Context context, List<Ability> abilities) {
        _context = context;
        _objects = abilities;
        _inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _objects.size();
    }

    @Override
    public Object getItem(int position) {
        return _objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = _inflater.inflate(R.layout.table_list_element_ability, parent, false);
        }

        Ability a = (Ability) getItem(position);

        ((TextView) view.findViewById(R.id.ability_name)).setText(a.getName());

        view.setTag(R.id.context_menu_type, ContextMenuType.ABILITY);
        view.setTag(R.id.item_id, a.getId());
        view.setTag(R.id.item_name, a.getName());

        ((Activity)_context).registerForContextMenu(view);

        return view;
    }
}