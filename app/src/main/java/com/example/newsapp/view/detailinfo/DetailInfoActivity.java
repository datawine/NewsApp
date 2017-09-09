package com.example.newsapp.view.detailinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.view.settings.MySettingsActivity;

public class DetailInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String category;
    private NavigationView mNavigationView;
    private TextView content;
    private TextView articleTitle;
    private Button rtnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        Bundle bundle = this.getIntent().getExtras();
        String title = bundle.getString("Title");
        category = bundle.getString("Category");

        articleTitle = (TextView) findViewById(R.id.detail_bar_title);
        content = (TextView) findViewById(R.id.news_content);
        rtnBtn = (Button) findViewById(R.id.detail_return);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        setArticleTitle(title);
        setContent("This is Content");

        mNavigationView.setNavigationItemSelectedListener(this);
        rtnBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setArticleTitle(String title) {
        articleTitle.setText(title);
    }

    public void setContent(String content) {
        this.content.setText(content);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_collection) {
            Toast.makeText(getApplicationContext(), id,
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DetailInfoActivity.this, MySettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), id,
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(getApplicationContext(), id,
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_homepage) {
            finish();
        }

        //关闭侧滑菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
