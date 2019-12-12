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

public class SyllabusFragment extends Fragment {

    public SyllabusFragment() {
        // Required empty public constructor
    }

    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<String> arrayList = new ArrayList<>();

    MyAdapter myAdapter = new MyAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.main_fragment_contain_view);
        swipeRefreshLayout = view.findViewById(R.id.main_swipe_refresh);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 17);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 6 == 0) {
                    return 2;
                } else {
                    return 3;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(myAdapter);

        swipeRefreshLayout.setRefreshing(true);
        new Thread() {
            @Override
            public void run() {
                arrayList = MyController.getSyllabus();
                for (int i = arrayList.size() - 1; i >= 0; i--) {
                    if (i % 8 == 6 || i % 8 == 7) {
                        arrayList.remove(i);
                    }
                }
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
                new Thread() {
                    @Override
                    public void run() {
                        arrayList = MyController.getSyllabus();
                        for (int i = arrayList.size() - 1; i >= 0; i--) {
                            if (i % 8 == 6 || i % 8 == 7) {
                                arrayList.remove(i);
                            }
                        }
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
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setData(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        myAdapter.notifyDataSetChanged();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView textView;

        MyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_textview_normal, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MyHolder) holder).textView.setText(String.valueOf(arrayList.get(position)));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }

}
