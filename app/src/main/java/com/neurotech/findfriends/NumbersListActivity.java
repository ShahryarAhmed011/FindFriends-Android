package com.neurotech.findfriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class NumbersListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NumbersListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mOpenContacts;
    private ImageView mOpenWhatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers_list);

        getSupportActionBar().setTitle("Friends Number List");
        bindViews();
        initialize();


    }

    private void startActivityForResult(Intent intent) {
    }

    private void bindViews(){
        mRecyclerView = findViewById(R.id.recycler_view);
        mOpenWhatsapp = findViewById(R.id.whatsapp_icon_button_number_list_screen);
        mOpenContacts  = findViewById(R.id.open_contacts_number_list_screen);

    }
    private void initialize() {
        initRecyclerView();

        mOpenContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DEFAULT, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);

            }
        });

        mOpenWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                startActivity(launchIntent);

            }
        });
    }

    private void initRecyclerView(){

        ArrayList<ContactNumber> contactNumbers = DatabaseHelper.getInstance(getBaseContext()).getAllNumber();

        if(contactNumbers.size()!=0) {

            mLayoutManager = new LinearLayoutManager(getBaseContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new NumbersListAdapter(getBaseContext(), contactNumbers);
            mRecyclerView.setAdapter(mAdapter);

        }else{
            Toast.makeText(getBaseContext(),"No Numbers",Toast.LENGTH_LONG).show();
        }

    }


}
