package com.example.boge.laonianbao.Music;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.boge.laonianbao.R;
import com.panxw.android.imageindicator.AutoPlayManager;
import com.panxw.android.imageindicator.ImageIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Date;

public class MusicActivity extends AppCompatActivity {

    public static final int SHOW_RESPOMSE = 0;

    private Button sendRequest;

    private TextView responseText;

    ImageIndicatorView imageIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);

//        sendRequest = (Button) findViewById(R.id.send_request);
//        sendRequest.setOnClickListener(this);

        //加的
        imageIndicatorView = (ImageIndicatorView) findViewById(R.id.indicate_view);
        final Integer[] resArray = new Integer[] { R.drawable.ic_9, R.drawable.ic_10
                , R.drawable.ic_11 , R.drawable.ic_12};
        imageIndicatorView.setupLayoutByDrawable(resArray);
        imageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_ARROW_ROUND_STYLE);
        imageIndicatorView.show();

        AutoPlayManager autoBrocastManager =  new AutoPlayManager(imageIndicatorView);
        autoBrocastManager.setBroadcastEnable(true);
        autoBrocastManager.setBroadCastTimes(5);//loop times
        autoBrocastManager.setBroadcastTimeIntevel(3 * 1000, 3 * 1000);//set first play time and interval
        autoBrocastManager.loop();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        sendRequestWithClient();
    }

    //加的


    private void sendRequestWithClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://120.25.225.163:8080/ServletTest/Movie");
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        final String response = EntityUtils.toString(entity, "utf-8");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                parseJSONWithJSONObject(response);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://120.25.225.163:8080/ServletTest/Movie");
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        final String response2 = EntityUtils.toString(entity, "utf-8");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                parseJSONWithJSONObject2(response2);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONTokener jsonTokener = new JSONTokener(jsonData);
            JSONObject jsonObject = new JSONObject(jsonTokener);
            String result = "";
            for (int i = 1; i <= 10; i++) {
                result = jsonObject.getString(i + "");
                JSONArray jsonArray = new JSONArray(result);
                String result1 = jsonArray.getString(0);
                JSONTokener jsonTokener1 = new JSONTokener(result1);
                JSONObject jsonObject1 = new JSONObject(jsonTokener1);


                final String href = jsonObject1.getString("Href");
                String title = jsonObject1.getString("标题");

                LinearLayout ll  = (LinearLayout) findViewById(R.id.MovieMusic);

                //加TextView
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(0,15,0,10);

                TextView tips = new TextView(MusicActivity.this);
                tips.setText(title);
                tips.setClickable(true);
                tips.setTextSize(20);
                tips.setTextColor(0xff000000);

                ll.addView(tips);

                tips.setLayoutParams(lp);
                tips.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MusicActivity.this,MusicwebviewActivity.class);
                        intent.putExtra("href",href);
                        startActivity(intent);
                    }
                });
                //加灰色字
                LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                pp.setMargins(0,0,0,15);

                TextView from = new TextView(MusicActivity.this);
                Date date = new Date();
                from.setText("老年宝音乐   "+ (date.getYear() + 1900) + "年" + (date.getMonth()+1) + "月");
                from.setClickable(true);
                from.setTextSize(10);
                from.setTextColor(0xffaaaaaa);
                from.setLayoutParams(pp);
                ll.addView(from);

                View line = new View(MusicActivity.this);

                LinearLayout.LayoutParams linelp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );
                line.setLayoutParams(linelp);
                line.setBackgroundColor(0xffaaaaaa);
                ll.addView(line);
//                Message message = new Message();
//                message.what = SHOW_RESPOMSE;
//                message.obj = title.toString();
//                handler.sendMessage(message);

                //System.out.println(href + title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void parseJSONWithJSONObject2(String jsonData) {
        try {
            JSONTokener jsonTokener = new JSONTokener(jsonData);
            JSONObject jsonObject = new JSONObject(jsonTokener);
            String result = "";
            for (int i = 1; i <= 10; i++) {
                result = jsonObject.getString(i + "");
                JSONArray jsonArray = new JSONArray(result);
                String result1 = jsonArray.getString(0);
                JSONTokener jsonTokener1 = new JSONTokener(result1);
                JSONObject jsonObject1 = new JSONObject(jsonTokener1);


                final String href = jsonObject1.getString("Href");
                String title = jsonObject1.getString("标题");

                LinearLayout ll  = (LinearLayout) findViewById(R.id.MovieMusic);

                //加TextView
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(0,15,0,10);

                TextView tips = new TextView(MusicActivity.this);
                tips.setText(title);
                tips.setClickable(true);
                tips.setTextSize(20);
                tips.setTextColor(0xff000000);

                ll.addView(tips);

                tips.setLayoutParams(lp);
                tips.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MusicActivity.this,MusicwebviewActivity.class);
                        intent.putExtra("href",href);
                        startActivity(intent);
                    }
                });
                //加灰色字
                LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                pp.setMargins(0,0,0,15);

                TextView from = new TextView(MusicActivity.this);
                Date date = new Date();
                from.setText("老年宝电影   "+ (date.getYear() + 1900) + "年" + (date.getMonth()+1) + "月");
                from.setClickable(true);
                from.setTextSize(10);
                from.setTextColor(0xffaaaaaa);
                from.setLayoutParams(pp);
                ll.addView(from);

                View line = new View(MusicActivity.this);

                LinearLayout.LayoutParams linelp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );
                line.setLayoutParams(linelp);
                line.setBackgroundColor(0xffaaaaaa);
                ll.addView(line);
//                Message message = new Message();
//                message.what = SHOW_RESPOMSE;
//                message.obj = title.toString();
//                handler.sendMessage(message);

                //System.out.println(href + title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
