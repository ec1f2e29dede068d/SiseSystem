package w.c.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.c.demo.R;

import w.c.service.StoreService;

/**
 * Created by C on 5/12/2019.
 */

public class SettingFragment extends Fragment
        implements AdapterView.OnItemClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        /*GridLayout gridLayout = view.findViewById(R.id.setting_fragment_gridlayout);
        //获取屏幕像素
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        for (int i = 0; i < 35; i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i / 5, 1, 1.0f);
            GridLayout.Spec columnSpec = GridLayout.spec(i % 5, 1, 1.0f);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            //每个子布局矿都为屏幕宽度的五分之一
            layoutParams.width = screenWidth / 5;
            TextView textView = new TextView(container.getContext());
            textView.setText("test3");
            textView.setPadding(0, 0, 0, 100);
            gridLayout.addView(textView, layoutParams);

        }*/

        /*final RecyclerView recyclerView = view.findViewById(R.id.setting_fragment_content);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext()
                , LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(container.getContext()
                , DividerItemDecoration.VERTICAL));


        class MyHolder extends RecyclerView.ViewHolder {

            private TextView textView;
            private SwitchCompat switchCompat;

            MyHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.text);
                switchCompat = itemView.findViewById(R.id.switch_button);
            }
        }

        class MyAdapter extends RecyclerView.Adapter {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_recycler_view_setting, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((MyHolder) holder).switchCompat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            @Override
            public int getItemCount() {
                return 5;
            }

        }
        final MyAdapter myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);*/
        /*String[] itemText = {"离线模式"};
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("itemText", itemText[0]);
        list.add(map);*/
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(getContext(), R.layout.item_recycler_view_setting,
                            null);
                }
                TextView textView = convertView.findViewById(R.id.text);
                textView.setText("离线模式");
                SwitchCompat switchCompat = convertView.findViewById(R.id.switch_button);
                switchCompat.setOnCheckedChangeListener(SettingFragment.this);
                if (getContext().getSharedPreferences("setting", Context.MODE_PRIVATE)
                        .getBoolean("offLine", false)) {
                    switchCompat.setChecked(true);
                } else {
                    switchCompat.setChecked(false);
                }
                return convertView;
            }
        };

        ListView listView = view.findViewById(R.id.setting_fragment_content);
        listView.setOnItemClickListener(this);

        /*ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<Object>(container.getContext()
                , android.R.layout.simple_list_item_multiple_choice, itemText);*/

        /*SimpleAdapter simpleAdapter = new SimpleAdapter(container.getContext(), list
                , R.layout.item_recycler_view_setting, new String[]{"itemText"}
                , new int[]{R.id.text});*/

        //listView.setAdapter(arrayAdapter);
        //listView.setAdapter(simpleAdapter);
        listView.setAdapter(baseAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            SwitchCompat switchCompat = view.findViewById(R.id.switch_button);
            switchCompat.toggle();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_button:
                Intent intent = new Intent(getActivity(), StoreService.class);
                if (isChecked) {
                    getActivity().startService(intent);
                } else {
                    getActivity().stopService(intent);
                }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("setting"
                        , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("offLine", isChecked);
                editor.apply();
                break;
        }
    }
}
