package com.example.android.sf;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Beds> allBeds = new ArrayList<>();
    BedsAdapter adapter;
    DatabaseReference mDatabseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabseReference = FirebaseDatabase.getInstance().getReference().child("beds");

        listView = (ListView) findViewById(R.id.list_view_main);
        adapter = new BedsAdapter(this, allBeds);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                ab.setTitle("Remove this Item?");
                ab.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ab.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Beds b1 = allBeds.get(position);
                        String key = b1.getKey();
                        mDatabseReference.child(key).removeValue();
                    }
                });
                AlertDialog al = ab.create();
                al.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = this.getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.custom_dialog, null);
                builder.setView(dialogView);

                //Initialize the EditTexts
                final EditText etLen = (EditText) dialogView.findViewById(R.id.et_length);
                final EditText etWid = (EditText) dialogView.findViewById(R.id.et_width);
                final EditText etThk = (EditText) dialogView.findViewById(R.id.et_thickness);
                final AutoCompleteTextView actvMod = (AutoCompleteTextView) dialogView.findViewById(R.id.actv_model);
                builder.setTitle("Add a new Item");
                builder.setPositiveButton("Save", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button p = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        p.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (etLen.getText().toString().equals("") || etWid.getText().toString().equals("") || etThk.getText().toString().equals("") || actvMod.getText().toString().equals("")) {
                                    Toast.makeText(MainActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                                } else {
                                    //allBeds.add(new Beds(etLen.getText().toString(), etWid.getText().toString(), etThk.getText().toString(), actvMod.getText().toString()));
                                    //adapter.notifyDataSetChanged();
                                    Beds b = new Beds(etLen.getText().toString(), etWid.getText().toString(), etThk.getText().toString(), actvMod.getText().toString());
                                    String key = mDatabseReference.push().getKey();
                                    b.setKey(key);
                                    mDatabseReference.child(key).setValue(b);
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allBeds.clear();
                for(DataSnapshot bedsSnapShot : dataSnapshot.getChildren()) {
                    Beds beds = bedsSnapShot.getValue(Beds.class);
                    allBeds.add(beds);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Beds beds = dataSnapshot.getValue(Beds.class);
                allBeds.add(beds);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Beds beds = dataSnapshot.getValue(Beds.class);
                allBeds.add(beds);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
