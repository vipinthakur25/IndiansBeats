package com.tetravalstartups.dingdong.modules.discover;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tetravalstartups.dingdong.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivGoBack;
    private RecyclerView contactsRecyclerView;
    private List<ContactModel> contactModelList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private static String[] PERMISSION_CONTACT = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    private static final int REQUEST_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        initViews();
    }

    private void initViews() {
        ivGoBack = findViewById(R.id.ivGoBack);
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);


        ivGoBack.setOnClickListener(this);
        setDataToAdapter();

        requestContactsPermissions();
    }

    private void setDataToAdapter() {
        contactAdapter = new ContactAdapter(contactModelList);
        initRecyclerView();
    }

    private void initRecyclerView() {
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsRecyclerView.setHasFixedSize(true);
        contactsRecyclerView.setAdapter(contactAdapter);
    }

    private void requestContactsPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)){

                Snackbar.make(contactsRecyclerView, "permission Contact", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(ContactsActivity.this,PERMISSION_CONTACT,REQUEST_CONTACT);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(ContactsActivity.this,PERMISSION_CONTACT,REQUEST_CONTACT);
            }
        } else {
            getContactInfo();
        }
    }


    @Override
    public void onClick(View view) {
        if (view == ivGoBack) {
            onBackPressed();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults){
            if (result == PackageManager.PERMISSION_GRANTED){
                getContactInfo();
            }
        }
    }

    private void getContactInfo(){
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PHONE_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String PHONE_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI,null,null,null,DISPLAY_NAME);

        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String CONTACT_ID = cursor.getString(cursor.getColumnIndex(ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(HAS_PHONE_NUMBER));
                ContactModel contactModel = new ContactModel();
                if (hasPhoneNumber > 0){
                    contactModel.setName(name);

                    Cursor phoneCursor = contentResolver.query(PHONE_URI, new String[]{NUMBER},PHONE_ID+" = ?",new String[]{CONTACT_ID},null);
                    List<String> contactList = new ArrayList<>();
                    phoneCursor.moveToFirst();
                    while (!phoneCursor.isAfterLast()){
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)).replace(" ","");
                        contactList.add(phoneNumber);
                        phoneCursor.moveToNext();
                    }
                    contactModel.setNumber(contactList);
                    contactModelList.add(contactModel);
                    phoneCursor.close();
                }
            }
            contactAdapter.notifyDataSetChanged();
        }
    }
}