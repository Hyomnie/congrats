package com.ncapdevi.sample.activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ncapdevi.sample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class startActivity extends AppCompatActivity {
    boolean isBackShow = false;
    private static View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getFragmentManager().beginTransaction().replace(R.id.container, new CardFrontFragment()).commit();
    }

    @SuppressLint("ResourceType")
    public void onclicklogin(View view) {
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out)
                .replace(R.id.container, new LoginFragment()).commit();
    }

    @SuppressLint("ResourceType")
    public void onclickregister(View view) {
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out)
                .replace(R.id.container, new RegisterFragment()).commit();
    }

    @SuppressLint("ResourceType")
    public void onclickright(View view) {
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out)
                .replace(R.id.container, new CardFrontFragment()).commit();
    }

    @SuppressLint("ResourceType")
    public void onclickleft(View view) {
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out)
                .replace(R.id.container, new CardFrontFragment()).commit();
    }

    public static class CardFrontFragment extends Fragment {
        public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }

    @SuppressLint("ValidFragment")
    public class RegisterFragment extends Fragment {
        public RegisterFragment() {
        }

        private static final String TAG = "RegisterActivity";
        private static final String URL_FOR_REGISTRATION = "http://203.252.195.136/register.php";
        ProgressDialog progressDialog;

        private EditText fullName, userEmailId, password, signup_input_age;
        private Button signUpBtn;
        private CheckBox female_radio_btn, male_radio_btn;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_register, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Progress dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);

            fullName = (EditText) getView().findViewById(R.id.fullName);
            userEmailId = (EditText) getView().findViewById(R.id.userEmailId);
            password = (EditText) getView().findViewById(R.id.password);
            signup_input_age = (EditText) getView().findViewById(R.id.signup_input_age);
            signUpBtn = (Button) getView().findViewById(R.id.signUpBtn);

            female_radio_btn = (CheckBox) getView().findViewById(R.id.female_radio_btn);
            male_radio_btn = (CheckBox) getView().findViewById(R.id.male_radio_btn);

            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkValidation()) {
                        submitForm();
                    }
                }
            });
        }

        private boolean checkValidation() {
            String getEmailId = userEmailId.getText().toString();
            Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher m = p.matcher(getEmailId);
            if (!m.find()) {
                new CustomToast().Show_Toast(getActivity(), getView(),
                        "이메일 양식(아이디@이메일주소)에 맞춰 작성해주세요");
                return false;
            } else
                return true;
        }

        private void submitForm() {

            String gender;
            if (female_radio_btn.isChecked())
                gender = "Female";
            else
                gender = "Male";

            registerUser(fullName.getText().toString(),
                    userEmailId.getText().toString(),
                    password.getText().toString(),
                    gender,
                    signup_input_age.getText().toString());
        }

        private void registerUser(final String name, final String email, final String password,
                                  final String gender, final String dob) {
            // Tag used to cancel the request
            String cancel_req_tag = "register";

            progressDialog.setMessage("Adding you ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    URL_FOR_REGISTRATION, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        if (!error) {

                            String user = jObj.getJSONObject("user").getString("name");
                            Toast.makeText(getApplicationContext(), user + "님 회원가입이 성공적으로 완료됐습니다", Toast.LENGTH_SHORT).show();

                            // Launch login activity
                            Intent intent = new Intent(
                                    startActivity.this,
                                    startActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("gender", gender);
                    params.put("age", dob);
                    return params;
                }
            };
            // Adding request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
        }

        private void showDialog() {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        private void hideDialog() {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    @SuppressLint("ValidFragment")
    public class LoginFragment extends Fragment {
        public LoginFragment() {
        }

        private static final String TAG = "LoginFragment";
        private static final String URL_FOR_LOGIN = "http://203.252.195.136/login.php";
        ProgressDialog progressDialog;
        private EditText loginInputId, loginInputPassword;
        private Button btnlogin;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_login, container, false);

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            loginInputId = (EditText) getView().findViewById(R.id.id);
            loginInputPassword = (EditText) getView().findViewById(R.id.password);
            btnlogin = (Button) getView().findViewById(R.id.btnLogin);
            // Progress dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);

            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginUser(loginInputId.getText().toString(),
                            loginInputPassword.getText().toString());
                }
            });

        }

        private void loginUser(final String name, final String password) {
            // Tag used to cancel the request
            String cancel_req_tag = "login";
            progressDialog.setMessage("로그인 중");
            showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    URL_FOR_LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        if (!error) {
                            String user = jObj.getJSONObject("user").getString("name");
                            // Launch User activity
                            Intent intent = new Intent(
                                    startActivity.this,
                                    BottomTabsActivity.class);
                            intent.putExtra("username", user);
                            startActivity(intent);
                            finish();
                        } else {

                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }

            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("password", password);
                    return params;
                }
            };
            // Adding request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
        }

        private void showDialog() {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        private void hideDialog() {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }


}