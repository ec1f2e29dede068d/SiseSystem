package w.c.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.c.demo.R;

import w.c.controller.MyController;

public class LoginActivity extends Activity implements View.OnFocusChangeListener {
    EditText usernameEditText, passWordEditText;
    Button loginButton;
    //    Button resetButton;
    Context context = null;
    ProgressBar progressBar;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findElements();

        context = this.getApplicationContext();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setClickable(false);
                loginButton.setText("登录中...");
                progressBar.setVisibility(View.VISIBLE);
                new Thread() {
                    @Override
                    public void run() {
                        final boolean ifLoginSuccess = MyController.login(usernameEditText.getText().toString()
                                , passWordEditText.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                if (ifLoginSuccess) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                } else {
                                    /*View view = getLayoutInflater().inflate(R.layout.item_toast, null);
                                    Toast toast = new Toast(LoginActivity.this);
                                    toast.setView(view);
                                    TextView tv = view.findViewById(R.id.TextViewInfo);
                                    tv.setText("登录失败，请检查学号密码");
                                    toast.show();*/
                                    Toast.makeText(LoginActivity.this, "登录失败，请检查网络或学号密码"
                                            , Toast.LENGTH_SHORT).show();
                                    loginButton.setText("登录");
                                    loginButton.setClickable(true);
                                }
                            }
                        });
                    }
                }.start();
            }
        });

        /*resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText.setText("");
                passWordEditText.setText("");
            }
        });*/

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passWordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passWordEditText.setSelection(passWordEditText.getText().length());
                } else {
                    passWordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    passWordEditText.setSelection(passWordEditText.getText().length());
                }
            }
        });

    }

    private void findElements() {
        usernameEditText = findViewById(R.id.usernameEditText);
        passWordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
//        resetButton = findViewById(R.id.resetButton);
        progressBar = findViewById(R.id.progressBar);
        checkBox = findViewById(R.id.hideOrShowPassword);

        usernameEditText.setOnFocusChangeListener(this);
        passWordEditText.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    }
}
