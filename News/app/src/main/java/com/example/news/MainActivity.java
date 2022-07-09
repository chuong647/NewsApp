package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.news.adapter.NewsAdapter;
import com.example.news.dao.NewsDAO;
import com.example.news.enity.News;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ListView lv_main;
    View view_add;
    Dialog dialog;
    TextInputEditText ed_name, ed_link;
    Button btn_add, btn_del, btn_cancel;
    NewsAdapter adapter;
    NewsDAO dao;
    News news;
    ArrayList<News> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //gọi hoặc mở rộng phương thức lớp cha.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//đặt tệp XML muốn làm bố cục chính khi ứng dụng khởi động.
        lv_main = findViewById(R.id.lv_main); // truy suat vào list view dua vao id lv_main
        view_add = findViewById(R.id.view_add);

        dao = new NewsDAO(MainActivity.this);
        UpdateLV();

        deleteCache(getApplicationContext()); //xoa cache

        view_add.setOnClickListener(new View.OnClickListener() {
            @Override //su kien nhap chuot
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
                openDialog();
            }
        });

        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (checkNetwork()){
                    String link = list.get(i).getLink();
                    if (!link.isEmpty()){
                        Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                        intent.putExtra("link", link);
                        startActivity(intent);
                    }
                }else {
                    NoInternetToast();
                }
            }
        });

        lv_main.setOnItemLongClickListener((adapterView, view, i, l) -> {
            delete(list.get(i).getId());
            return true;
        });
    }
    // phuong thuc openDialog them bao
    public void openDialog(){
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_add);//hien thi layout dialog_add trong mainActivity
        ed_name = dialog.findViewById(R.id.ed_name);//anh xa id
        ed_link = dialog.findViewById(R.id.ed_link);
        btn_add = dialog.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_name.getText().toString().trim();//lay string name
                String link = ed_link.getText().toString().trim();//lay string link
                //kiem tra name va link co trong khong
                if (name.isEmpty() || link.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    News news = new News();//tao đoi tuong news
                    news.setName(name);// them name vao doi tuong news
                    news.setLink(link);
                    if (dao.insert(news)){
                        Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        UpdateLV();
                        dialog.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(), "Thêm không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        dialog.show();
    }
    //phuong thuc NoInternetToast (k co internet)
    public void NoInternetToast(){
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.no_internet_toast, null);
        Toast toast = new Toast(getApplicationContext());//tao thong bao
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);//hien thi giua man hinh
        toast.setView(v);
        toast.show();
    }
    //kiem tra trang thai mang
    private boolean checkNetwork() {
        boolean wifiAvailable = false;
        boolean mobileAvailable = false;

        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (netInfo.isConnected())
                    wifiAvailable = true;
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (netInfo.isConnected())
                    mobileAvailable = true;
        }
        return wifiAvailable || mobileAvailable;
    }
    //phuong thuc xoa tham so khoi hang tuong ung
    public void delete(int id){
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_del);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_del = dialog.findViewById(R.id.btn_del);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dao.delete(id)){
                    Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    UpdateLV();
                }else {
                    Toast.makeText(getApplicationContext(), "Xóa không thành công", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });dialog.show();
        UpdateLV();
    }
    //phuong thuc update
    public void UpdateLV(){
        list = (ArrayList<News>) dao.getALL();
        adapter = new NewsAdapter(getApplicationContext(),MainActivity.this, list);
        lv_main.setAdapter(adapter);
    }
    //phuong thuc su kien xoa cache
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            Toast.makeText(context.getApplicationContext(), "Xóa bộ nhớ thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Xóa bộ nhớ không thành công", Toast.LENGTH_SHORT).show();
        }
    }
    //phuong thuc kiem tra su kien xoa cache
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}