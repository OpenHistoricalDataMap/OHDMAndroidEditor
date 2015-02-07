package android.ohdm.de.ohdmviewer;

import android.ohdm.de.editor.Geometry.TagDates;
import android.ohdm.de.editor.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HashMapAdapter extends BaseAdapter {

    ArrayList data;

    public HashMapAdapter(HashMap<TagDates,String> mapData){
        data = new ArrayList();
        data.addAll(mapData.entrySet());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Map.Entry<TagDates, String> getItem(int i) {
        return (Map.Entry)data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_data_list_row, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<TagDates, String> item = getItem(position);

        ((TextView) result.findViewById(R.id.item_key)).setText(item.getKey().toString());
        ((TextView) result.findViewById(R.id.item_value)).setText(item.getValue());

        return result;
    }
}
