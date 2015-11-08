package com.example.zee.galvanisemobile.chat;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zee.galvanisemobile.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    // Firebase URL
    private static final String FIREBASE_URL = "https://fiery-fire-9963.firebaseio.com/";

    private Toolbar toolbar;
    private SharedPreferences preferences;
    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    private ProgressBar progressBar;
    private LinearLayout feedNotAvailable;
    private RelativeLayout chatSectionLayout;
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_chat);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        feedNotAvailable = (LinearLayout)findViewById(R.id.feedNotAvailable);
        chatSectionLayout = (RelativeLayout)findViewById(R.id.chat_section_layout);

        setToolbar();
        setupUsername();

        // Setup input methods. User can press enter key on the keyboard or push the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    public void setToolbar() {

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupFirebaseAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    // Get username that has already been saved in the device (if any), or create a new one
    private void setupUsername() {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsername = preferences.getString("ChatUserName", null);

        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            mUsername = "JavaUser" + r.nextInt(100000);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ChatUserName", mUsername);
            editor.apply();

        }

        mFirebaseRef = new Firebase(FIREBASE_URL).child(mUsername);

    }

    // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
    private void setupFirebaseAdapter() {

        final ListView listView = (ListView)findViewById(R.id.list);

        try {
            // Tell our list adapter that we only want 50 messages at a time
            mChatListAdapter = new ChatListAdapter(this, mFirebaseRef.limit(50), this, R.layout.chat_message, mUsername);
            listView.setAdapter(mChatListAdapter);
            mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(mChatListAdapter.getCount() - 1);
                }
            });

            mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    connected = (Boolean) dataSnapshot.getValue();

                    if (connected) {
                        progressBar.setVisibility(View.GONE);
                        chatSectionLayout.setVisibility(View.VISIBLE);
                    }

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!connected) {
                                progressBar.setVisibility(View.GONE);
                                feedNotAvailable.setVisibility(View.VISIBLE);
                                chatSectionLayout.setVisibility(View.GONE);
                            }
                        }
                    }, 6000);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    progressBar.setVisibility(View.GONE);
                    feedNotAvailable.setVisibility(View.VISIBLE);
                }

            });

        } catch (Exception e) {

        }
    }

    // Send non-empty text messages
    private void sendMessage() {

        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();

        if (!input.equals("")) {

            Chat chat = new Chat(input, mUsername);
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }

    // Provide user an option to reconnect if there was no connection previously
    public void onClick_reconnect(View view) {
        progressBar.setVisibility(View.VISIBLE);
        feedNotAvailable.setVisibility(View.GONE);
        setupFirebaseAdapter();
    }
}
