package tjeit.kr.apipractice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import tjeit.kr.apipractice.datas.User;
import tjeit.kr.apipractice.utils.ConnectServer;
import tjeit.kr.apipractice.utils.ContextUtil;
import tjeit.kr.apipractice.utils.GlobalData;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

    }

    @Override
    public void setValues() {

//        토큰이 SharedPreference에 저장되어 있는지? 검사.
//        저장되어있다? => 자동로그인으로 간주하고 처리.
//        저장이 안되어있다? => 다시 로그인을 하도록 유도.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ContextUtil.getToken(mContext).equals("")) {
//            토큰이 저장되어있지 않은 경우. => 로그인 필요

                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();


                }
                else {

//            저장된 토큰이 있다 => 사용자 정보를 서버에서 받아서 자동로그인 처리
//                    받은 사용자 정보를 GlobalData.loginUser에 대입
//                    GlobalData.token에 SharedPrefernce에 저장된 토큰값을먼저 대입.
                    GlobalData.token = ContextUtil.getToken(mContext);


                    ConnectServer.getRequestMyInfo(mContext, new ConnectServer.JsonResponseHandler() {
                        @Override
                        public void onResponse(JSONObject json) {
                            Log.d("내정보응답", json.toString());

                            try {
                                JSONObject data = json.getJSONObject("data");
                                JSONObject user = data.getJSONObject("user");
//                                JSONObject => User클래스로 파싱.

                                User userObject = User.getUserFromJson(user);
                                GlobalData.loginUser = userObject;

                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });




                }

            }
        }, 1500);



    }

    @Override
    public void bindViews() {

    }
}
