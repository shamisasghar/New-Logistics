package com.appinspire.dailybudget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appinspire.dailybudget.api.ApiInterface;
import com.appinspire.dailybudget.models.Profile;
import com.appinspire.dailybudget.models.User;
import com.appinspire.dailybudget.models.WebAPIResponse;
import com.appinspire.dailybudget.utils.LoginUtils;
import com.appinspire.dailybudget.utils.MyTextWatcher;
import com.appinspire.dailybudget.utils.ScheduleUtils;

import java.util.HashMap;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Metis on 20-Jan-18.
 */

public class LoginActivity extends AppCompatActivity {

    BlurView mBlurView;
    private final float mRadius = 4;
    Button btn_sign;
    private EditText edit_username, edit_password;
    private TextInputLayout inputLayout_username, inputLayout_password;
    private ProgressBar progressBar;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String email, driver_name, driver_id, url,getUserAssociatedEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_sign = (Button) findViewById(R.id.sign_in);
        edit_username = (EditText) findViewById(R.id.edittext_username);
        edit_password = (EditText) findViewById(R.id.edittext_password);
        inputLayout_username = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputLayout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        edit_username.addTextChangedListener(new MyTextWatcher(edit_username));
        edit_password.addTextChangedListener(new MyTextWatcher(edit_password));


//        mBlurView = (BlurView) findViewById(R.id.blurView);
//        final View decorView = getWindow().getDecorView();
//
//        final View rootView = decorView.findViewById(android.R.id.content);
//        //set background, if your root layout doesn't have one
//        final Drawable windowBackground = decorView.getBackground();
//        mBlurView.setupWith(rootView)
//                .windowBackground(windowBackground)
//                .blurAlgorithm(new RenderScriptBlur(this, true)) //Preferable algorithm, needs RenderScript support mode enabled
//                .blurRadius(mRadius);
        pref = getApplicationContext().getSharedPreferences("TAG", MODE_PRIVATE);

        if (LoginUtils.isUserLogin(getApplicationContext())) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            btn_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit();
                }
            });
        }

    }

    private void submit() {
        if (!validateName())
            return;
        if (!validatePassword())
            return;

        String username = edit_username.getText().toString(); //    "driver1@kotal.com"
        String password = edit_password.getText().toString();//    "driver@2017"

        HashMap<String, Object> body = new HashMap<>();
        body.put("email", username);
        body.put("password", password);
       // body.put("push_key", ScheduleUtils.getUserOneSignalId(this));
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(this);
        ApiInterface.retrofit.loginUser(body).enqueue(new Callback<WebAPIResponse<User>>() {
            @Override
            public void onResponse(Call<WebAPIResponse<User>> call, Response<WebAPIResponse<User>> response) {
  //              progressBar.setVisibility(View.GONE);
                if (response.body().status) {


                   // Toast.makeText(LoginActivity.this, response.body().response.getToken(), Toast.LENGTH_SHORT).show();
                    LoginUtils.saveUserToken(LoginActivity.this, response.body().response.getToken(), Integer.toString(response.body().response.getAssociatedEntity()));
                    LoginUtils.userLoggedIn(LoginActivity.this);


//                    ApiInterface.retrofit.getprofile(Integer.parseInt(getUserAssociatedEntity)).enqueue(new Callback<WebAPIResponse<Profile>>() {
//                        @Override
//                        public void onResponse(Call<WebAPIResponse<Profile>> call, Response<WebAPIResponse<Profile>> response) {
//                            if (response.body().status) {
//
//
//                                url = response.body().response.getPhoto();
//                                driver_name = response.body().response.getName();
//                                driver_id = Integer.toString(response.body().response.getId());
//
//                                editor = pref.edit();
//                                editor.putString("Email", email);
//                                editor.putString("Url", url);
//                                editor.putString("Name", driver_name);
//                                editor.putString("Id", driver_id);
//                                editor.commit();
//
////                                Glide.with(getApplicationContext()).load(url).into(img_profile);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<WebAPIResponse<Profile>> call, Throwable t) {
//                        }
//                    });

                    email = response.body().response.getEmail();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

                else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Wrong Email & Password", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    view.setBackgroundColor(ContextCompat.getColor(getApplication(), R.color.colorPrimary));
                    tv.setTextColor(ContextCompat.getColor(getApplication(), R.color.colorDialogToolbarText));
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<WebAPIResponse<User>> call, Throwable t) {

               // progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Establish Network Connection!", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                view.setBackgroundColor(ContextCompat.getColor(getApplication(), R.color.colorPrimaryDark));
                tv.setTextColor(ContextCompat.getColor(getApplication(), R.color.colorDialogToolbarText));
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                snackbar.show();
            }
        });

    }
    private boolean validateName() {
        if (edit_username.getText().toString().trim().isEmpty()) {
            inputLayout_username.setError(getString(R.string.err_msg_name));
            requestFocus(edit_username);
            return false;
        } else {
            inputLayout_username.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (edit_password.getText().toString().trim().isEmpty()) {
            inputLayout_password.setError(getString(R.string.err_msg_password));
            requestFocus(inputLayout_password);
            return false;
        } else {
            inputLayout_password.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.edittext_username:
                    validateName();
                    break;
                case R.id.edittext_password:
                    validatePassword();
                    break;
            }

        }
    }

}