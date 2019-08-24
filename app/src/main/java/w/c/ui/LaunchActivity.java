package w.c.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import w.c.service.ClassService;

public class LaunchActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread() {
            @Override
            public void run() {
                //耗时任务，比如加载网络数据
                Log.i("cLog","run()");
                startService(new Intent(LaunchActivity.this, ClassService.class));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 这里可以睡几秒钟，如果要放广告的话
                        /*try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                        startActivity(intent);
                        LaunchActivity.this.finish();
                    }
                });
            }
        }.start();
    }
}
