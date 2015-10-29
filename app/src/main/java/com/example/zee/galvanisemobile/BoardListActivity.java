package com.example.zee.galvanisemobile;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BoardListActivity extends AppCompatActivity {

    public static final String TAG = "AndroidDrawing";
    private static String FIREBASE_URL = "https://galvanize-drawing.firebaseIO.com/";

    private com.example.zee.galvanisemobile.MenuItem customFood;

    private Firebase mRef;
    private Firebase mBoardsRef;
    private Firebase mSegmentsRef;
    private FirebaseListAdapter2<HashMap> mBoardListAdapter;
    private ValueEventListener mConnectedListener;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
        setToolbar();

        Firebase.setAndroidContext(this);
        mRef = new Firebase(FIREBASE_URL);
        getCustomizableFood();
    }

    public void getCustomizableFood() {

        Intent i = getIntent();
        customFood = i.getParcelableExtra("customFoodObject");

        setBoardsRef();
    }

    private void setBoardsRef() {

        /*if (customFood != null) {
            mBoardsRef = mRef.child(customFood.getId() + "boardmetas");
            mSegmentsRef = mRef.child(customFood.getId() + "boardsegments");
        } else {
            mBoardsRef = mRef.child("boardmetas");
            mSegmentsRef = mRef.child("boardsegments");
        }*/
        mBoardsRef = mRef.child("boardmetas");
        mSegmentsRef = mRef.child("boardsegments");
        mBoardsRef.keepSynced(true);
        SyncedBoardManager.setContext(this);
        SyncedBoardManager.restoreSyncedBoards(mSegmentsRef);

        if (customFood != null)
            createBoard();
    }

    public void setToolbar() {

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set up a notification to let us know when we're connected or disconnected from the Firebase servers
        mConnectedListener = mRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(BoardListActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BoardListActivity.this, "Disconnected from Firebase", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });

        final ListView boardList = (ListView) this.findViewById(R.id.BoardList);
        mBoardListAdapter = new FirebaseListAdapter2<HashMap>(mBoardsRef, HashMap.class, R.layout.board_in_list, this) {
            @Override
            protected void populateView(View v, HashMap model) {
                final String key = BoardListActivity.this.mBoardListAdapter.getModelKey(model);
                ((TextView)v.findViewById(R.id.board_title)).setText(key);

                // show if the board is synced and listen for clicks to toggle that state
                CheckBox checkbox = (CheckBox) v.findViewById(R.id.keepSynced);
                checkbox.setChecked(SyncedBoardManager.isSynced(key));
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SyncedBoardManager.toggle(mSegmentsRef, key);
                    }
                });

                // display the board's thumbnail if it is available
                ImageView thumbnailView = (ImageView) v.findViewById(R.id.board_thumbnail);
                if (model.get("thumbnail") != null){
                    try {
                        thumbnailView.setImageBitmap(DrawingActivity.decodeFromBase64(model.get("thumbnail").toString()));
                        thumbnailView.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    thumbnailView.setVisibility(View.INVISIBLE);
                }
            }
        };
        boardList.setAdapter(mBoardListAdapter);
        boardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBoard(mBoardListAdapter.getModelKey(position));
            }
        });
        mBoardListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                boardList.setSelection(mBoardListAdapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Clean up our listener so we don't have it attached twice.
        mRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mBoardListAdapter.cleanup();

    }

    private void createBoard() {
        // create a new board
        final Firebase newBoardRef = mBoardsRef.push();
        Map<String, Object> newBoardValues = new HashMap<>();
        newBoardValues.put("createdAt", ServerValue.TIMESTAMP);
        android.graphics.Point size = new android.graphics.Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        newBoardValues.put("width", size.x);
        newBoardValues.put("height", size.y);
        newBoardRef.setValue(newBoardValues, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase ref) {
                if (firebaseError != null) {
                    Log.e(TAG, firebaseError.toString());
                    throw firebaseError.toException();
                } else {
                    // once the board is created, start a DrawingActivity on it
                    openBoard(newBoardRef.getKey());
                }
            }
        });
    }

    private void openBoard(String key) {
        Log.i(TAG, "Opening board " + key);
        Toast.makeText(BoardListActivity.this, "Opening board: "+key, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DrawingActivity.class);
        intent.putExtra("FIREBASE_URL", FIREBASE_URL);
        intent.putExtra("BOARD_ID", key);
        intent.putExtra("customFoodObject", customFood);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.i(TAG, "Selected item " + id);

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_new_board) {
            createBoard();
        }
        return super.onOptionsItemSelected(item);
    }

}
