package test.com.mygapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import test.com.mygapplication.DetailPages.DetailActivity;
import test.com.mygapplication.Logins.LoginRegisterActivitySimple;
import test.com.mygapplication.Utills.Screen;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {
    RecyclerView list;
    ArrayList<String> activities = new ArrayList<>();
    int pad = Screen.dp(16);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this, DetailActivity.class));
        activities.add("LoginRegistrationSimple");
        activities.add("LoginRegistrationStrechAnimation");
        activities.add("SimpleDetailActivity");
        activities.add("SimpleDetailActivity with toolbar slide in/drop down animation");
        setUpRecyclerView();
    }

    void setUpRecyclerView() {
        list = (RecyclerView) findViewById(R.id.list);
        list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new ListAdapter());

    }

    void startActivity(int choice) {
        Intent intent;
        switch (choice) {
            case 0:
            case 1:
                intent = new Intent(this, LoginRegisterActivitySimple.class);
                intent.putExtra("choice", choice);
                startActivity(intent);
                return;
            case 2:
            case 3:
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra("choice", choice - 2);
                startActivity(intent);
                return;
        }

    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView tv = new TextView(MainActivity.this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(pad, pad, pad, pad);
            tv.setTextSize(18);
            tv.setTextColor(Color.BLACK);
            return new ViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv.setText(activities.get(position));
        }

        @Override
        public int getItemCount() {
            return activities.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(getAdapterPosition());
                    }
                });
            }
        }
    }
}
