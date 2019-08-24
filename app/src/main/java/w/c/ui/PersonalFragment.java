package w.c.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.c.demo.R;

import java.util.ArrayList;

import w.c.controller.MyController;

/**
 * Created by C on 5/12/2019.
 */

public class PersonalFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;

    public PersonalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.personal_fragment_content);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(container.getContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 2 == 0) {
                    return 1;
                } else {
                    return 3;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        final ArrayList[] arrayList = new ArrayList[]{new ArrayList()};

        class MyHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            MyHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.text);
            }
        }

        class MyAdapter extends RecyclerView.Adapter {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_textview_large, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (position % 2 == 1) {
                    ((MyHolder) holder).textView.setText(String.valueOf(arrayList[0].get(position / 2)));
                } else {
                    switch (position / 2) {
                        case 0:
                            ((MyHolder) holder).textView.setText("学号");
                            break;
                        case 1:
                            ((MyHolder) holder).textView.setText("姓名");
                            break;
                        case 2:
                            ((MyHolder) holder).textView.setText("年级");
                            break;
                        case 3:
                            ((MyHolder) holder).textView.setText("专业");
                            break;
                        case 4:
                            ((MyHolder) holder).textView.setText("身份证");
                            break;
                        case 5:
                            ((MyHolder) holder).textView.setText("邮箱");
                            break;
                        case 6:
                            ((MyHolder) holder).textView.setText("学习导师");
                            break;
                        case 7:
                            ((MyHolder) holder).textView.setText("班级");
                            break;
                        case 8:
                            ((MyHolder) holder).textView.setText("辅导员");
                            break;
                    }
                }
            }

            @Override
            public int getItemCount() {
                return arrayList[0].size() * 2;
            }

        }
        final MyAdapter myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        new Thread() {
            @Override
            public void run() {
                arrayList[0] = MyController.getPersonalInfo();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        }.start();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getActivity() != null) {
                    new Thread() {
                        @Override
                        public void run() {
                            arrayList[0] = MyController.getPersonalInfo();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myAdapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }.start();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
