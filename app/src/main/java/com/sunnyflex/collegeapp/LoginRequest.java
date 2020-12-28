package com.sunnyflex.collegeapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    // * DB 데이터 URL주소 입력
    final static private String URL = "http://happyhunte.cafe24.com/CollegeMembersLogin.php";
    // * 자료형 으로 정보를 보낼 문자형 Map 콜렉션 변수 선언
    private Map<String, String> map;

    // ** class 의 생성자 정의 **
    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        // HashMap으로 선언
        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
