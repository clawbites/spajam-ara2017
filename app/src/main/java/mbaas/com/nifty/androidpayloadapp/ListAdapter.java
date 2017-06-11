package mbaas.com.nifty.androidpayloadapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    MainActivity main;
    List<JSONObject> listJson = new ArrayList<>();

    ListAdapter(MainActivity main, JSONObject json) {
        this.main = main;
        if (json != null) {
            Iterator<?> keys = json.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                try {
                    if ( json.get(key) != null ) {
                        JSONObject tmpJson = new JSONObject();
                        tmpJson.put("key", key);
                        tmpJson.put("value",json.get(key).toString());
                        listJson.add(tmpJson);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getCount() {
        return listJson.size();
    }

    @Override
    public Object getItem(int position) {
        return listJson.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolderItem {
        TextView key;
        TextView value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem holder = new ViewHolderItem();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cell, null);

            holder.key = (TextView) convertView.findViewById(R.id.key);
            holder.value = (TextView) convertView.findViewById(R.id.value);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolderItem) convertView.getTag();
        }
        try {
            holder.key.setText("Key: " + this.listJson.get(position).get("key").toString());
            holder.value.setText("Value: " + this.listJson.get(position).get("value").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

}