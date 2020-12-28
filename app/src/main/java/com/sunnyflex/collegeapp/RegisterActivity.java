package com.sunnyflex.collegeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner majorSpinner;

    private String userGender;

    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText rIdText = findViewById(R.id.r_id_text);
        final EditText rPasswordText = findViewById(R.id.r_password_text);
        final EditText rEmailText = findViewById(R.id.r_email_text);
        final EditText rMobileText = findViewById(R.id.r_mobile_text);
        final EditText rAgeText = findViewById(R.id.r_age_text);

        // ** 과목선택 관련 정의 **
        // 변수와 layout 연결
        majorSpinner = findViewById(R.id.major_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.major, android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(adapter);

        // Gender 라디오 버튼 연결
        RadioGroup genderGroup = findViewById(R.id.r_gender_group);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedGender = genderGroup.getCheckedRadioButtonId();
                userGender = ((RadioButton)findViewById(checkedGender)).getText().toString();
            }
        });


        // *** id중복확인 button 클릭시 정의 ***
        final Button validateButton = findViewById(R.id.r_validate_button);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = rIdText.getText().toString();

                if (validate){
                    return;
                }
                if (userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("ID를 입력해 주세요")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            if (success){
                                dialog = builder.setMessage("사용가능한 아이디 입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                rIdText.setEnabled(false);
                                validateButton.setEnabled(false);
                                validate = true;
                                rIdText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));

                            } else{
                                builder.setMessage("아이디를 사용할수 없습니다.")
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });


        // *** 회원가입 버튼 클릭 이벤트 (DB 서버 저장, 로그인 화면가기) ***
        Button rRegisterButton = findViewById(R.id.r_register_button);
        rRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // * 입력한 가입정보 정보 **
                String userID = rIdText.getText().toString();
                String userPassword = rPasswordText.getText().toString();
                String userEmail = rEmailText.getText().toString();
                int userMobile = Integer.parseInt(rMobileText.getText().toString());
                int userAge = Integer.parseInt(rAgeText.getText().toString());
                String userMajor = majorSpinner.getSelectedItem().toString();

                // * DB서버에 회원가입 성공 응답 받기 *
                if (userID.equals("") || userPassword.equals("") || userEmail.equals("") || String.valueOf(userMobile).equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("모든 정보를 기입해야 합니다.")
                            .create()
                            .show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // DB서버에서 응답 받기
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
                            if (success) {
                                builder.setMessage("회원등록에 성공 하였습니다.")
                                        .create()
                                        .show();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {

                                builder.setMessage("회원등록에 실패 하였습니다.")
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // ** DB서버에 회원정보 보내기 **
                // RegisterRequest class 에 registerRequest 해당 데이터를 넣어서 변수 선언 후 queue에 담아서 서버 보내기
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userEmail, userMobile, userAge, userGender, userMajor, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
//                Log.e("gendergroup", "성별은 : " + userGender);
            }
        });
    }

//        // ** backButton 클릭시 정의 **
//        ImageButton rBackButton = findViewById(R.id.r_back_Button);
//        rBackButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (dialog != null){
//            dialog.dismiss();
//            dialog = null;
//        }
//    }
}