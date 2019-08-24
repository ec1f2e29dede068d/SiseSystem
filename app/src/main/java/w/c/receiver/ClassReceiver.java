package w.c.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.c.demo.R;

public class ClassReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("cLog","onReceive()");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker("该上课了")        //通知时在状态栏显示的通知内容
                .setContentTitle("星期五，该提前上课了")        //设置通知标题。
                .setAutoCancel(true)                //点击通知后通知消失
                .build();
        notificationManager.notify(1, notification);
    }
}
