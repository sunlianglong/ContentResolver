package com.sunlianglong.content;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView contactsview; //声明一个ListView对象
    List<String> contactslist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsview = (ListView) findViewById(R.id.contacts_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactslist);
        contactsview.setAdapter(adapter);
        readContacts();

        contactsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取得要拨的电话号码
                String phoneNum =contactslist.get(position);
                System.out.println(phoneNum);
                //使用Intent来切换到打电话的界面上，并将要播的电话传进去，
                Intent in = new Intent();
                //设置现在要切换的功能
                in.setAction(Intent.ACTION_CALL);
                in.setData(Uri.parse("tel:" + phoneNum));
                startActivity(in);
            }
        });
    }
    private void readContacts() {
        Cursor cursor = null;

        cursor  = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {      //向下移动光标，循环：将所有列表显示出来。对cursor对象进行遍历
            String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactslist.add(displayName + "\n" + number);
        }
        cursor.close();
        //将Cursor对象关掉。


    }

}
