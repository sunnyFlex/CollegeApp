package com.sunnyflex.collegeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText lIdText = findViewById(R.id.l_id_text);
        EditText lPasswordText = findViewById(R.id.l_password_text);

        TextView lRegisterButton = findViewById(R.id.l_register_button);
        lRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ** Main 페이지로 보내기 정의 **
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button lLoginButton = findViewById(R.id.l_login_button);
        lLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ** DB 데이터 체크정의 **
                String userID = lIdText.getText().toString();
                String userPassword = lPasswordText.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                if (userID.equals("") || userPassword.equals("")) {
                    builder.setMessage("ID와 Password를 입력하세요")
                            .create()
                            .show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // DB서버에서 응답 받기
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                if (success) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    builder.setMessage("ID와 Password를 다시 입력하세요.")
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
                    LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
//                Log.e("gendergroup", "성별은 : " + userGender);

                }

            }
        });
    }
}