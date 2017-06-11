package mbaas.com.nifty.androidpayloadapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.nifty.cloud.mb.core.NCMBGcmListenerService;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomGcmListenerService extends NCMBGcmListenerService {

    private static final String TAG = "GcmService";
    private static final int REQUEST_RESULT = 0;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        //ペイロードデータの取得
        if (data.containsKey("com.nifty.Data")) {
            try {
                JSONObject json = new JSONObject(data.getString("com.nifty.Data"));
            } catch (JSONException e) {
                //エラー処理
            }
        } else if (data.containsKey("com.nifty.PushId")) {
            String pushid = data.getString("com.nifty.PushId");
            Log.d(TAG, pushid);
        } else if (data.containsKey("com.nifty.RichUrl")) {
            String url = data.getString("com.nifty.RichUrl");
            Log.d(TAG, url);
        }

        //デフォルトの通知
        super.onMessageReceived(from, data);
    }

}
