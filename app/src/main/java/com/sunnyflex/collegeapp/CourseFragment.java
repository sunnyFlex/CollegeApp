package com.sunnyflex.collegeapp;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // ** 세부전공 과목 선택 관련 DB 항목 변수정의 **
    // * Activity와 List를 연결할 Adapter 및 Spinner 정의 *
    private ArrayAdapter yearAdapter;
    private Spinner yearSpinner;
    private ArrayAdapter termAdapter;
    private Spinner termSpinner;
    private ArrayAdapter areaAdapter;
    private Spinner areaSpinner;
    private ArrayAdapter majorAdapter;
    private Spinner majorSpinner;

    // * 세부항목 String 변수 empty로 정의 *
    private String courseUniversity = "";
    private String courseYear = "";
    private String courseTerm = "";
    private String courseArea = "";

    // *** activity가 변경관련 이벤트 정의 ***
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ** 재학생 신분관련 RadioGroup 및 RadioButton 정의 **
        // RadioGroup 변수 정의
        RadioGroup courseUniversityGroup = getView().findViewById(R.id.course_university_group);
        // Spinner 사용될 DB(년도,전공,전공영역)변수 layout연결
        yearSpinner = getView().findViewById(R.id.year_spinner);
        termSpinner = getView().findViewById(R.id.term_spinner);
        areaSpinner = getView().findViewById(R.id.area_spinner);
        majorSpinner = getView().findViewById(R.id.major_spinner);

        // ** RadioButton 선택 체크시 이벤트 정의 **
        courseUniversityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // 선택된 course(과정)의 위치값 i를 RadioButton 변수에 담고 그변수의 text를 담아서 선택된 대학교 과정(학부,대학원)을 받아온다.
                RadioButton courseButton = getView().findViewById(i);
                courseUniversity = courseButton.getText().toString();

                // * Spinner에 List에 들어갈 DB항목 별 Adapter연결 *
                // 해당 array.xml의 해당 항목을 소스로 연결해서 해당 Adapter변수에 저장
                yearAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.year, android.R.layout.simple_spinner_dropdown_item);
                yearSpinner.setAdapter(yearAdapter);
                termAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.term, android.R.layout.simple_spinner_dropdown_item);
                termSpinner.setAdapter(termAdapter);

                // * 선택된 재학생 신분 선택 (CheckedRadioButton)에 따라 areaSpinner 내용 변경 *
                if (courseUniversity.equals("학부")){
                    // 학부 선택시 areaSpinner에 학부영역List(universityArea)연결
                    areaAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityArea, android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areaAdapter);
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.universityMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                } else if (courseUniversity.equals("대학원")){
                    // 대학원 선택시 areaSpinner에 대학원영역List(graduateArea)연결
                    areaAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateArea, android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areaAdapter);
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
            }
        });

        // ** areaSpinner탭에서 아이템 선택시 이벤트 **
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // * 아이템이 선택되었을때 이벤트 *
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // 교양선택시 교양 array 항목 연결
                if (areaSpinner.getSelectedItem().equals("교양")){
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityRefinementMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
                // 전공선택시 전공 array 항목 연결
                if (areaSpinner.getSelectedItem().equals("전공")){
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
                // 일반대학원선택시 일반대학원 array 항목 연결
                if (areaSpinner.getSelectedItem().equals("일반대학원")){
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
            }

            // * 아이템이 아무것도 선택되지 않았을때 *
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // ** searchButton 클릭시 이벤트 **
        Button searchButton = getView().findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서치버튼 클릭시 BackgroundTask()메소드를 실행(execute)
                new BackgroundTask().execute();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(Uri uri);
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        // * onPreExcute로 target 실행 *
        @Override
        protected void onPreExecute() {
            // ** courseList.php에 해당변수를 포함해서 정보를 보낸다.
            try {
                // spinner에서 선택된 대학신분, 년도String0~4까지, 검색 색인들의 정보등을 courseList.php로 보낸다.
                target = "http://happyhunte.cafe24.com/courseList.php?courseUniversity=" + URLEncoder.encode(courseUniversity,"UTF-8")
                + "&courseYear=" + URLEncoder.encode(yearSpinner.getSelectedItem().toString().substring(0,4), "UTF-8") + "&courseTerm=" + URLEncoder.encode(termSpinner.getSelectedItem().toString(), "UTF-8")
                + "&courseArea=" + URLEncoder.encode(areaSpinner.getSelectedItem().toString(), "UTF-8") + "&courseMajor=" + URLEncoder.encode(majorSpinner.getSelectedItem().toString(), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseFragment.this.getContext());
                dialog = builder.setMessage(result)
                        .setPositiveButton("확인", null)
                        .create();
                dialog.show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}