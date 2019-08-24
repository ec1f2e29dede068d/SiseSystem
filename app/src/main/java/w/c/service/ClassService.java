package w.c.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

import w.c.receiver.ClassReceiver;

public class ClassService extends Service {
    public ClassService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("cLog", "onStartCommand");

        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        if (week == 6) {
            Log.i("cLog","Today is Friday");
            Intent notificationIntent = new Intent(this, ClassReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 50);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent);
        }else {
            Log.i("cLog","Today is not Friday");
        }

        return Service.START_REDELIVER_INTENT;
    }
}
