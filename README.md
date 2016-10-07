
### 内容URI的标准格式：

content://com.example.app.provider.table1

content://com.example.app.provider.table2

内容URI主要由两部分组成：权限（authority）和路径（path），权限一般采用程序包名的方式进行命名，路径：区分不同的表，一般放到权限的后面。content：协议声明。—>清楚地表明我们想要访问哪张表里的数据

在得到内容URI的字符串之后，解析成Uri对象才可以作为参数传入。(调用Uri.parsse()方法)

Uri uri = Uri.parse("content://com.example.app.provider/table1")

---
### 查询表中的数据

```java
Cursor cursir = getContentResolver().query(
    uri,
        projection,
        selection,
        selectionArgs,
        sortOrder);
```
参数：

![](https://github.com/sunlianglong/ContentResolver/Img/ContentResolver.png)  

查询完之后，将数据从Cursor对象中逐个取出来：通过**移动游标的位置**来遍历Cursor的所有行，再取出每一行的数据

```java
if(cursor != null){
            while(cursor.moveToNext()){
                String column1 = cursor.getString(cursor.getColumnIndex("column1"));
                String column2 = cursor.getString(cursor.getColumnIndex("volumn2"));
            }
            cursor.close();
        }
```
### 添加数据


```java
 ContextValues values = new ContextValues():
        values.put("column1","text");
        values.put("column2","1");
        getContentResolver(),insert(uri,values);
```
### 更新新添加的数据


```java
ContextValues values = new ContextValues():
        values.put("column1", "");
        getContentResolver().update(uri, values, "column1 = ? column2 = ?", new String[]{"text", "1"});
        
```
使用了selection和selectioArgs参数来对想要更新的数据进行约束，来防止所有的行都会受到影响。
### 删除

```java
getContentResolver().delete(uri,"column2 = ?",new String[]{"1"});
```

---
## 读取系统联系人并实现拨打功能
布局LinearLayout里面只放一个ListView。

MainActivity中：
```java
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
```
AndroidManifest.xml文件中设置权限：

```java
    <uses-permission android:name="android.permission.READ_CONTACTS"></uses-permission>
<uses-permission android:name="android.permission.CALL_PHONE"></uses-permission>
```
