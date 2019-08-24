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

public class ScoreActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> electiveScore = new ArrayList<>();

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
        setContentView(R.layout.activity_score);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.content);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 10);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0) {
                    return 3;
                } else if (position % 3 == 2) {
                    return 1;
                } else {
                    return 6;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        final ArrayList[] arrayList = new ArrayList[]{new ArrayList()};
        class MyAdapter extends RecyclerView.Adapter {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_textview_normal, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                int row = position / 3;
                if (position > (3 * arrayList[0].size() / 10 - 1)) {
                    switch (position % 3) {
                        case 0:
                            ((MyHolder) holder).textView.setText(String.valueOf(electiveScore.get(9 * (row - arrayList[0].size() / 10) + 6)));
                            break;
                        case 1:
                            ((MyHolder) holder).textView.setText(String.valueOf(electiveScore.get(9 * (row - arrayList[0].size() / 10) + 1)));
                            break;
                        case 2:
                            ((MyHolder) holder).textView.setText(String.valueOf(electiveScore.get(9 * (row - arrayList[0].size() / 10) + 7)));
                            break;
                    }
                } else {
                    switch (position % 3) {
                        case 0:
                            ((MyHolder) holder).textView.setText(String.valueOf(arrayList[0].get(10 * row + 7)));
                            break;
                        case 1:
                            ((MyHolder) holder).textView.setText(String.valueOf(arrayList[0].get(10 * row + 2)));
                            break;
                        case 2:
                            ((MyHolder) holder).textView.setText(String.valueOf(arrayList[0].get(10 * row + 8)));
                            break;
                    }
                }
                if (position / 3 % 2 == 0) {
                    ((MyHolder) holder).textView.setBackgroundColor(0xFF555555);
                } else {
                    ((MyHolder) holder).textView.setBackgroundColor(0xFF333333);
                }
            }

            @Override
            public int getItemCount() {
                return arrayList[0].size() - 7 * (arrayList[0].size() / 10) + electiveScore.size() - 6 * (electiveScore.size() / 9);
            }

        }
        final MyAdapter myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        swipeRefreshLayout.setRefreshing(true);
        new Thread() {
            @Override
            public void run() {
                super.run();
                arrayList[0] = MyController.getScore();
                electiveScore = MyController.getElectiveScore();
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
                        arrayList[0] = MyController.getScore();
                        electiveScore = MyController.getElectiveScore();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
