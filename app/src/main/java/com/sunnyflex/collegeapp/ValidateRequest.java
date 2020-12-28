package com.sunnyflex.collegeapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// *** DB table에서 ID 중복확인 정의 ***
public class ValidateRequest extends StringRequest {

    // * DB 데이터 URL주소 입력
    final static private String URL = "http://happyhunte.cafe24.com/CollegeMembersValidate.php";
    // * 자료형 으로 정보를 보낼 문자형 Map 콜렉션 변수 선언
    private Map<String, String> parameters;

    // ** class 의 생성자 정의 **
    public ValidateRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        // HashMap으로 선언
        parameters = new HashMap<>();
        // 요청할 데이터 정의
        parameters.put("userID", userID);
    }

    public Map<String, String> getParams(){
        return parameters;
    }

}
