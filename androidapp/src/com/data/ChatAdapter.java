package com.data;
/**
 * Created with IntelliJ IDEA.
 * User: Wolfram
 * Date: 11.11.13
 * Time: 14:56
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.activities.R;
import data.Message;
import data.contents.ChatContent;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<Message<ChatContent>> {
    private final Context context;
    private final ArrayList<Message<ChatContent>> messages;
    private final String you;

    public ChatAdapter(Context context, ArrayList<Message<ChatContent>> msg, String you) {
        super(context, R.layout.layout_chathistory_list_you, msg);
        this.context = context;
        this.you = you;
        messages = msg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message<ChatContent> currentMessage = messages.get(position);
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        /* todo: change layout names? */
        if (!(currentMessage.getSender().equals(you))) {
            rowView = LayoutInflater.from(context).inflate(R.layout.layout_chathistory_list_you, parent, false);
        } else {
            rowView = LayoutInflater.from(context).inflate(R.layout.layout_chathistory_list_notyou, parent, false);
        }
        TextView msg = (TextView) rowView.findViewById(R.id.msg);
        TextView time = (TextView) rowView.findViewById(R.id.timestamp);
        msg.setText(currentMessage.getContent().getMessage());
        time.setText(currentMessage.getTimestampString());
        return rowView;
    }
}
