package com.data;

/**
 * Created with IntelliJ IDEA.
 * User: Wolfram
 * Date: 11.11.13
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.activities.R;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public ChatAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.layout_chathistory_list, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_chathistory_list, parent, false);
        TextView msg = (TextView) rowView.findViewById(R.id.msg);
        TextView time = (TextView) rowView.findViewById(R.id.timestamp);
        msg.setText(values.get(position));
        time.setText("1:00");
        return rowView;
    }
}