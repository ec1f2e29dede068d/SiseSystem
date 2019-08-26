package w.c.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.c.demo.R;

import w.c.data.CardHandler;

public class CartTopUpActivity extends AppCompatActivity {
    Bitmap bitmap;
    CardHandler cardHandler;
    ImageView imageView;
    Button submitBtn, loginBtn;
    EditText username, password, amount, checkCode;
    TextView info;
    String result;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_top_up);

        imageView = (ImageView) findViewById(R.id.checkCodeImg);
        submitBtn = (Button) findViewById(R.id.submit);
        loginBtn = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        amount = (EditText) findViewById(R.id.amount);
        checkCode = (EditText) findViewById(R.id.checkCode);
        info = (TextView) findViewById(R.id.info);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        new Thread() {
            @Override
            public void run() {
                super.run();
                progressBar.setVisibility(View.VISIBLE);
                cardHandler = new CardHandler();
                cardHandler.setCookie1();
                bitmap = BitmapFactory.decodeStream(cardHandler.getRandomImg());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }.start();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        cardHandler.setPassword(password.getText().toString());
                        cardHandler.login(username.getText().toString(), checkCode.getText().toString());
                        result = cardHandler.getInfo();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                info.setText(result);
                            }
                        });
                    }
                }.start();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        cardHandler.getTransferPage();
                        cardHandler.transfer(amount.getText().toString());
                    }
                }.start();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        bitmap = BitmapFactory.decodeStream(cardHandler.getRandomImg());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread() {
            @Override
            public void run() {
                super.run();
                cardHandler.logout();
            }
        }.start();
    }
}
