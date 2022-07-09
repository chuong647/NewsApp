package com.example.news.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.news.R;
import com.example.news.enity.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class News_Adapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> lists;
    TextView tv_title, tv_link, tv_date;
    ImageView iv_news;
    //phuong thuc khoi tao
    public News_Adapter(@NonNull Context context, ArrayList<Item> list) {
        super(context, 0, list);//ke thua arrayadapter
        this.context = context;
        this.lists = list;
    }

    @NonNull
    @Override
    //phuong thuc custom list view
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView; //khoi tao view co ten v
        if (v == null){
            //chuyen tep bo cuc xml thanh ma java
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_newss, null);
        }
        //anh xa cac id trong layout item_newss
        Item item = lists.get(position);
        if (item != null){
            tv_title = v.findViewById(R.id.tv_title_item_news);
            tv_link = v.findViewById(R.id.tv_link_news);
            tv_date = v.findViewById(R.id.tv_date_news);
            iv_news = v.findViewById(R.id.iv_news);
            tv_title.setText(item.getTitle());
            tv_link.setText(item.getLink());
            //load link hinh
            Picasso.with(getContext()).load(item.getLinkImg())
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .into(iv_news);
            try {
                String date = item.getDate().substring(4, 16);
                tv_date.setText(date);
            }catch (Exception e){

            }
        }
        return v;
    }
}
