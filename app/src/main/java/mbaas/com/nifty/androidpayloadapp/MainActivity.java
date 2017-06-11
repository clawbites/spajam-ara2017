package mbaas.com.nifty.androidpayloadapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    TextView _pushId;
    TextView _richurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
/*
        try {
            JSONObject tmpBlank = new JSONObject( "{'No key':'No value'}");
            ListView lv = (ListView) findViewById(R.id.lsJson);
            if (lv != null) {
                lv.setAdapter(new ListAdapter(this, tmpBlank));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/


        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this.getApplicationContext(), "87f3638e763da938280ae66e15b0d21ec13d1f2e9313cd12be3d0d642f2cec43", "ed4721862e508b0402417ca456513c5a1a3a735e837f364a9d72396e4dfc87c9");
        final NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();

        //GCMからRegistrationIdを取得しinstallationに設定する
        installation.getRegistrationIdInBackground("369381594409", new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    installation.saveInBackground(new DoneCallback() {
                        @Override
                        public void done(NCMBException e) {
                            if (e == null) {
                                //保存成功
                            } else if (NCMBException.DUPLICATE_VALUE.equals(e.getCode())) {
                                //保存失敗 : registrationID重複
                                updateInstallation(installation);
                            } else {
                                //保存失敗 : その他
                            }
                        }
                    });
                } else {
                    //ID取得失敗
                }
            }
        });
        
        setContentView(R.layout.activity_main);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリック時の処理
                Toast toast = Toast.makeText(MainActivity.this, "せいかいだぁ〜!", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public static void updateInstallation(final NCMBInstallation installation) {
        //installationクラスを検索するクエリの作成
        NCMBQuery<NCMBInstallation> query = NCMBInstallation.getQuery();
        //同じRegistration IDをdeviceTokenフィールドに持つ端末情報を検索する
        query.whereEqualTo("deviceToken", installation.getDeviceToken());
        //データストアの検索を実行
        query.findInBackground(new FindCallback<NCMBInstallation>() {
            @Override
            public void done(List<NCMBInstallation> results, NCMBException e) {
                //検索された端末情報のobjectIdを設定
                installation.setObjectId(results.get(0).getObjectId());
                //端末情報を更新する
                installation.saveInBackground();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            NCMBUser.logoutInBackground(new DoneCallback() {
                @Override
                public void done(NCMBException e) {
                    if (e != null) {
                        //エラー時の処理
                    }
                }
            });
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        //**************** ペイロード、リッチプッシュを処理する ***************
        Intent intent = getIntent();

        String richurl = intent.getStringExtra("com.nifty.RichUrl");

        WebView  myWebView = (WebView)findViewById(R.id.webView);

        //リンクをタップしたときに標準ブラウザを起動させない
        myWebView.setWebViewClient(new WebViewClient());

        //最初にgoogleのページを表示する。
        myWebView.loadUrl(richurl);
    }
}
