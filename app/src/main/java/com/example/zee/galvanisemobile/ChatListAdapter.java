package com.example.zee.galvanisemobile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Query;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;
    private Context context;

    public ChatListAdapter(Context context, Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
        this.context = context;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {

        String author = chat.getAuthor();
        TextView authorText = (TextView) view.findViewById(R.id.author);
        ImageView authorAvatar = (ImageView) view.findViewById(R.id.chat_avatar);

        if (author != null && author.equals(mUsername)) {
            authorText.setText("You:");
            authorText.setTextColor(context.getResources().getColor(R.color.primaryColor));
            authorAvatar.setImageResource(R.drawable.ic_chat_user);
        } else {
            // If the message was sent by the admin, color it and label it differently
            authorText.setText("Galvanise Cafe:");
            authorText.setTextColor(context.getResources().getColor(R.color.asphaltColor));
            authorAvatar.setImageResource(R.drawable.ic_cafe_staff);
        }
        ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());
    }
}
