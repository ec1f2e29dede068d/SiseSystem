package w.c.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.c.demo.R;

import net.simonvt.numberpicker.NumberPicker;

import java.util.ArrayList;

import w.c.controller.MyController;

/**
 * 程序登录后主界面
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*static int screenHeight;
    static int screenWidth;*/

    ProgressBar progressBar;

    FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    //底部导航栏
    BottomNavigationView navigation;

    SyllabusFragment syllabusFragment = new SyllabusFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取屏幕长和宽
        /* 方法1 */
        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;*/
        /* 方法2 */
        /*Display display = getWindowManager().getDefaultDisplay();
        Point outSize = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(outSize);//不能省略,必须有
            screenHeight = outSize.x;//得到屏幕的宽度
            screenWidth = outSize.y;//得到屏幕的高度
        }*/

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        showSyllabus();
                        invalidateOptionsMenu();
                        return true;
                    case R.id.navigation_dashboard:
                        PersonalFragment personalFragment = new PersonalFragment();
                        fragmentTransaction.replace(R.id.content, personalFragment);
                        fragmentTransaction.commit();
                        invalidateOptionsMenu();
                        return true;
                    case R.id.navigation_notifications:
                        SettingFragment settingFragment = new SettingFragment();
                        fragmentTransaction.replace(R.id.content, settingFragment);
                        fragmentTransaction.commit();
                        invalidateOptionsMenu();
                        return true;
                }
                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //动作栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.index);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        new Thread() {
            @Override
            public void run() {
                super.run();
                MyController.getIndexContent();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showSyllabus();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }.start();


    }

    /**
     * 显示课程表fragment
     */
    public void showSyllabus() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, syllabusFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (navigation.getSelectedItemId()) {
            case R.id.navigation_home:
                menu.findItem(R.id.changeSemester).setVisible(true);
                break;
            case R.id.navigation_dashboard:
                menu.findItem(R.id.changeSemester).setVisible(false);
                break;
            case R.id.navigation_notifications:
                menu.findItem(R.id.changeSemester).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeSemester:
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_semester_select, null, false);
                final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
                numberPicker.setMinValue(2017);
                numberPicker.setMaxValue(2020);
                numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                final NumberPicker numberPicker2 = view.findViewById(R.id.numberPicker2);
                numberPicker2.setMinValue(1);
                numberPicker2.setMaxValue(2);
                numberPicker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择学期").setView(view);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        syllabusFragment.swipeRefreshLayout.setRefreshing(true);
                        new Thread() {
                            @Override
                            public void run() {
                                final ArrayList<String> arrayList = MyController.getSyllabus(numberPicker.getValue(), numberPicker2.getValue());
                                for (int i = arrayList.size() - 1; i >= 0; i--) {
                                    if (i % 8 == 6 || i % 8 == 7) {
                                        arrayList.remove(i);
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        syllabusFragment.setData(arrayList);
                                        syllabusFragment.swipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            }
                        }.start();
                    }
                }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //侧边导航栏子项目点击触发函数
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, ScoreActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, AttendanceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, TestTimeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.card_top_up) {
            Intent intent = new Intent(this, CartTopUpActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.index);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyController.logOut();
    }

}
