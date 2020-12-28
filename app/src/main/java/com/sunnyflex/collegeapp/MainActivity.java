package com.sunnyflex.collegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private ArrayList<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noticeListView = findViewById(R.id.notice_listview);

        // ** Notice.java class List 추가 및 Adapter 실행 **
        noticeList = new ArrayList<Notice>();

        adapter = new NoticeListAdapter(getApplicationContext(), noticeList);
        noticeListView.setAdapter(adapter);
        final Button courseButton = findViewById(R.id.course_button);
        final Button statisticsButton = findViewById(R.id.statistics_button);
        final Button scheduleButton = findViewById(R.id.schedule_button);
        final LinearLayout notice = findViewById(R.id.notice);

        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE);
                courseButton.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary_variant));
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.colorSecond));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorSecond));

                // ** Fragment레이아웃을 사용하기 **
                // * Manager불러오기, transaction가져오기, 대체할 fragment지정하기, commit로 실행하기
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new CourseFragment());
                fragmentTransaction.commit();
            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE);
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorSecond));
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorSecond));

                // ** Fragment레이아웃을 사용하기 **
                // * Manager불러오기, transaction가져오기, 대체할 fragment지정하기, commit로 실행하기
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new StatisticsFragment());
                fragmentTransaction.commit();
            }
        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notice.setVisibility(View.GONE);
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorSecond));
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.colorSecond));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                // ** Fragment레이아웃을 사용하기 **
                // * Manager불러오기, transaction가져오기, 대체할 fragment지정하기, commit로 실행하기
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new ScheduleFragment());
                fragmentTransaction.commit();
            }
        });
        new BackGroundTask().execute();
    }

    // *** Notice(공지사항) 서버에서 DB가져오기 ***
    // ** AsyncTask활용 **
    class BackGroundTask extends AsyncTask<Void, Void, String>{
        String target;

        // * onPreExcute로 target 실행 *
        @Override
        protected void onPreExecute() {
            target = "http://happyhunte.cafe24.com/NoticeList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "Win");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String noticeContent, noticeName, noticeDate;
                while(count < jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    noticeContent = object.getString("noticeContent");
                    noticeName = object.getString("noticeName");
                    noticeDate = object.getString("noticeDate");
                    Notice notice = new Notice(noticeContent, noticeName, noticeDate);
                    noticeList.add(notice);
                    adapter.notifyDataSetChanged();
                    count++;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // ** 빠르게 backButton을 빠르게 두번 누르면 앱을 종료하기 **
    // * 긴숫자변수 long 변수 선언 *
   private long lastTimeBackPressed;
    @Override
    public void onBackPressed() {
        // 만약 클릭 간격이 1.5초 이하 이면
        if (System.currentTimeMillis() < lastTimeBackPressed + 1500){
            finish();
            return;
        }
        Toast.makeText(this, "'Back' 버튼을 한번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}

