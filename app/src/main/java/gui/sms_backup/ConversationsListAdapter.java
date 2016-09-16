package gui.sms_backup;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.softc.app.R;
import utils.Font;

public class ConversationsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Conversation> conversations;


    public ConversationsListAdapter(Context context, ArrayList<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
    }


    @Override
    public int getCount() {
        return conversations.size();
    }


    @Override
    public Object getItem(int index) {
        return conversations.get(index);
    }


    @Override
    public long getItemId(int index) {
        return index;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(context, R.layout.activity_sms_backup_conversation_list_row, null);
            view.setClickable(true);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (AppCompatCheckBox) view.findViewById(R.id.checkbox);
            viewHolder.senderAddress = (TextView) view.findViewById(R.id.txt_sender_address);
            viewHolder.recentAddress = (TextView) view.findViewById(R.id.txt_recent_message);

            viewHolder.senderAddress.setTypeface(Font.LatoRegular);
            viewHolder.recentAddress.setTypeface(Font.LatoLight);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Conversation conversation = (Conversation) getItem(position);
        Sms sms = conversation.allSms.get(0);
        viewHolder.senderAddress.setText(String.format(conversation.senderAddress + "(%s)", conversation.allSms.size()));
        viewHolder.recentAddress.setText(sms.getMessageBody());
        viewHolder.checkBox.setChecked(conversation.isSelected);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conversation.isSelected = !conversation.isSelected;
                viewHolder.checkBox.setChecked(conversation.isSelected);
            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conversation.isSelected = !conversation.isSelected;
                viewHolder.checkBox.setChecked(conversation.isSelected);
            }
        });

        return view;
    }


    public ArrayList<Conversation> getSelectedConversations() {
        ArrayList<Conversation> list = new ArrayList<>();
        for (Conversation con : conversations)
            if (con.isSelected)
                list.add(con);

        return list;
    }


    public void selectAllConversation(boolean isSelect) {
        for (Conversation con : conversations)
            con.isSelected = isSelect;
    }


    public static class ViewHolder {
        public TextView senderAddress, recentAddress;
        public AppCompatCheckBox checkBox;
    }

}
