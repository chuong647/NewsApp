package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.news.adapter.News_Adapter;
import com.example.news.enity.Item;
import com.example.news.xmlpullparser.XmlPullParserHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    ListView lv;
    public List<Item> ItemLists = new ArrayList<>();
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        lv = findViewById(R.id.lv_news);//anh xa id lv_news
        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        Toast.makeText(getApplicationContext(), ""+link, Toast.LENGTH_SHORT).show();
        if (checkInternet()){
            downloadNew();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openLink(i);
            }
        });
    }
    //phuong thuc kiem Internet
    public boolean checkInternet(){
        //khoi tao connect
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected() || mobile.isConnected()){
            return true;
        }
        return false;
    }
    // phuong thuc openlink  chuyen trang webviewActivity
    public void openLink(int i){
        Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NewsActivity.this, WebViewActivity.class);
        intent.putExtra("linknews", ItemLists.get(i).getLink());
        startActivity(intent);
    }
    //phuong thuc down
    public void downloadNew(){
        new downloadXML(NewsActivity.this, lv).execute(link);
    }
    //lớp downloadXML su dung ham AsyncTask(ham da phuong thuc)
    public class downloadXML extends AsyncTask<String, Void, List<Item>> {

        News_Adapter adapter;
        private ListView listView;
        private Context context;
        //phuong thuc khoi tao
        public downloadXML(Context context, ListView lv) {
            this.context = context;
            this.listView = lv;
        }

        @Override
        //phuong thuc doInBackground
        protected List<Item> doInBackground(String... strings) {
            try {
                ItemLists  =  loadURLfromNetWork(strings[0]);
                return ItemLists;
            }catch (Exception e){
            }
            return null;
        }
        @Override
        //phuong thuc onPostExecute
        protected void onPostExecute(List<Item> list) {
            super.onPostExecute(list);
            adapter = new News_Adapter(context, (ArrayList<Item>) list);
            if (list != null){
                listView.setAdapter(adapter);
            }
        }
        //ham inputStream
        private InputStream downloadURL(String url)throws IOException {
            //khoi tạo moi Url
            URL url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setReadTimeout(10000); //tg cho read
            conn.setConnectTimeout(15000);//tg cho connect
            conn.setRequestMethod("GET");// lenh y/c may chu http
            conn.setDoInput(true);// cau hinh kn dau vao
            conn.connect();
            InputStream in = conn.getInputStream();// luong dau vao va doc tu connect dang mo
            return in;
        }
        // phuong thuc loadURLfromNetWork
        public List<Item> loadURLfromNetWork(String strUrl)throws Exception{
            InputStream stream = null;
            XmlPullParserHandler handler = new XmlPullParserHandler();
            ItemLists = null;
            try {
                stream = downloadURL(strUrl);
                Log.i("00000", stream.toString());
                ItemLists = handler.Pasers(stream);
            }finally {
                if (stream != null){
                    stream.close();
                }
            }
            Log.i("00000", String.valueOf(ItemLists.size()));
            return ItemLists;
        }
    }
}