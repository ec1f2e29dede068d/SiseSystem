package w.c.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import w.c.controller.MyController;

public class StoreService extends Service {
    SQLiteDatabase sqLiteDatabase;

    public StoreService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("CLog", "StoreService onCreate");
        new Thread() {
            @Override
            public void run() {
                try {
                    MyDBHelper myDBHelper = new MyDBHelper(StoreService.this
                            , "student.db", null, 1);
                    sqLiteDatabase = myDBHelper.getWritableDatabase();
                    sqLiteDatabase.beginTransaction();
                    ArrayList<String> stringArrayList = MyController.getSyllabus();
                    for (int i = 0; i < (stringArrayList.size() - 1) / 8 + 1; i++) {
                        ArrayList<String> stringArrayList1 = new ArrayList<>();
                        for (int j = 0; j < 8; j++) {
                            stringArrayList1.add(stringArrayList.get(i * 8 + j));
                        }
                        sqLiteDatabase.execSQL("insert into syllabus values(?, ?, ?, ?, ?, ?, ?, ?)"
                                , stringArrayList1.toArray());
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } catch (Exception ignored) {
                } finally {
                    sqLiteDatabase.endTransaction();
                    sqLiteDatabase.close();
                }
            }
        }.start();
    }

    class MyDBHelper extends SQLiteOpenHelper {

        MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("CLog", "MyDBHelper onCreate");
            db.execSQL("create table syllabus(section varchar(50) primary key, monday varchar(100), tuesday varchar(100), wednesday varchar(50), thursday varchar(100), friday varchar(100), saturday varchar(100), sunday varchar(100))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("CLog", "MyDBHelper onUpgrade");
        }
    }
}
