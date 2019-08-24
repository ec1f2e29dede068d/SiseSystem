package w.c.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.c.demo.R;

import java.util.ArrayList;

import w.c.controller.MyController;

public class TestTimeActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;

    class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;

        MyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_time);

        final ArrayList[] arrayList = new ArrayList[]{new ArrayList<String>()};

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.test_time_content);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 21);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 6 == 0) {
                    return 5;
                } else if (position % 6 == 1) {
                    return 4;
                } else {
                    return 3;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        class MyAdapter extends RecyclerView.Adapter {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(TestTimeActivity.this)
                        .inflate(R.layout.item_textview_normal, parent, false);
                return new TestTimeActivity.MyHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                switch (position) {
                    case 0:
                        ((TestTimeActivity.MyHolder) holder).textView.setText("课程名称");
                        break;
                    case 1:
                        ((TestTimeActivity.MyHolder) holder).textView.setText("考试日期");
                        break;
                    case 2:
                        ((TestTimeActivity.MyHolder) holder).textView.setText("考试时间");
                        break;
                    case 3:
                        ((TestTimeActivity.MyHolder) holder).textView.setText("考场名称");
                        break;
                    case 4:
                        ((TestTimeActivity.MyHolder) holder).textView.setText("考试座位");
                        break;
                    case 5:
                        ((TestTimeActivity.MyHolder) holder).textView.setText("考试状态");
                        break;
                    default:
                        ((TestTimeActivity.MyHolder) holder).textView.setText(arrayList[0].get(position).toString());
                }
            }

            @Override
            public int getItemCount() {
                return arrayList[0].size();
            }

        }
        final MyAdapter myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        swipeRefreshLayout.setRefreshing(true);
        new Thread() {
            @Override
            public void run() {
                super.run();
                arrayList[0] = MyController.getTestTime();
                for (int i = arrayList[0].size() - 1; i >= 0; i--) {
                    if (i % 8 == 0 || i % 8 == 4) {
                        arrayList[0].remove(i);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }.start();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        arrayList[0] = MyController.getTestTime();
                        for (int i = arrayList[0].size() - 1; i >= 0; i--) {
                            if (i % 8 == 0 || i % 8 == 4) {
                                arrayList[0].remove(i);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myAdapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }.start();
            }
        });
    }
}
