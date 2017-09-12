package com.example.newsapp.view.detailinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.presenter.IDetailPresenterCompl;
import com.example.newsapp.view.settings.MySettingsActivity;

import com.example.newsapp.presenter.*;

import org.json.JSONException;

public class DetailInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,IDetailView {

    private String category;
    private NavigationView mNavigationView;
    private TextView content;
    private TextView articleTitle;
    private ImageButton rtnBtn;
    private TextView link;
    private String Id;

    private IDetailPresenter iDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        iDetailPresenter = new IDetailPresenterCompl(this);

        Bundle bundle = this.getIntent().getExtras();
        String title = bundle.getString("Title");
        category = bundle.getString("Category");

        articleTitle = (TextView) findViewById(R.id.detail_bar_title);
        content = (TextView) findViewById(R.id.news_content);
        rtnBtn = (ImageButton) findViewById(R.id.detail_return);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        //这里是数据加载

        Id = bundle.getString("ID");

        try {
            iDetailPresenter.GetTitle(Id);
            iDetailPresenter.GetContent(Id);
        }
        catch(JSONException e){}
        catch(InterruptedException e){}
        //

        //超链接测试
        link = (TextView) findViewById(R.id.test_link);
        String html = "<a href='http://www.baidu.com'>百度一下</a> 测试";
        CharSequence charSequence = Html.fromHtml(html);
        SpannableStringBuilder builder = new SpannableStringBuilder(
                charSequence);
        URLSpan[] urlSpans = builder.getSpans(0, charSequence.length(),
                URLSpan.class);
        for (URLSpan span : urlSpans) {
            int start = builder.getSpanStart(span);
            int end = builder.getSpanEnd(span);
            int flag = builder.getSpanFlags(span);
            final String link = span.getURL();
            builder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // 捕获<a>标签点击事件，及对应超链接link
                }
            }, start, end, flag);
            builder.removeSpan(span);
        }
        link.setLinksClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        link.setText(charSequence);        //


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

        iDetailPresenter.CheckItemId(item,this);

        //关闭侧滑菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void GetActivityStart(Intent intent)
    {
        startActivity(intent);
    }

    public void GetToast(int id)
    {
        Toast.makeText(getApplicationContext(), id,Toast.LENGTH_SHORT).show();
    }

    public void GetFinished()
    {
        finish();
    }

    public void SetTitle(String title)
    {
        setArticleTitle(title);
    }

    public void SetContent(String content)
    {
        setContent(content);
    }
}
