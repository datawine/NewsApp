package com.example.newsapp.view.detailinfo;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.MyApplication;
import com.example.newsapp.R;
import com.example.newsapp.presenter.IDetailPresenterCompl;
import com.example.newsapp.view.settings.MySettingsActivity;

import com.example.newsapp.presenter.*;

import org.json.JSONException;

import java.util.List;

public class DetailInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,IDetailView {

    private String category;
    private NavigationView mNavigationView;
    private TextView content;
    private TextView articleTitle;
    private ImageButton rtnBtn;
    private TextView link;
    private TextView link2;
    private TextView link3;
    private String Id;
    private TextView mAuthor, mTime;
    private String Author, Time;

    private IDetailPresenter iDetailPresenter;

    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        iDetailPresenter = new IDetailPresenterCompl(this);

        Bundle bundle = this.getIntent().getExtras();
        String title = bundle.getString("Title");
        category = bundle.getString("Category");
        Author = bundle.getString("Author");
        Time = bundle.getString("Time");

        Id = bundle.getString("ID");

        articleTitle = (TextView) findViewById(R.id.detail_bar_title);
        content = (TextView) findViewById(R.id.news_content);
        mAuthor = (TextView) findViewById(R.id.detail_author);
        mTime = (TextView) findViewById(R.id.detail_time);
        rtnBtn = (ImageButton) findViewById(R.id.detail_return);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mAuthor.setText(Author);
        mTime.setText(Time);
        mAuthor.setTextColor(getResources().getColor(R.color.secondary_text));
        mTime.setTextColor(getResources().getColor(R.color.secondary_text));

        //浮动按钮
        FloatingActionButton fab_mac = (FloatingActionButton) findViewById(R.id.fab_mac);
        FloatingActionButton fab_star = (FloatingActionButton) findViewById(R.id.fab_star);

        fab_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                app = MyApplication.getInstance();

                if(app.IsStared(Id))
                {
                    app.UnStar(Id);
                    Snackbar.make(view, "取消收藏", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    app.Star(Id);
                    Snackbar.make(view, "收藏成功！", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        fab_mac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                app = MyApplication.getInstance();

                if(app.isSpeaking())
                {
                    Snackbar.make(view, "停止语音播报", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    app.StopVoice();
                }
                else {
                    Snackbar.make(view, "开始语音播报", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    iDetailPresenter.GetVoice(Id);
                }
            }
        });
        //


                //这里是数据加载


        try {
            iDetailPresenter.GetTitle(Id);
            iDetailPresenter.GetContent(Id);
        }
        catch(JSONException e){}
        catch(InterruptedException e){}
        //


        MyApplication app = MyApplication.getInstance();

        //超链接测试

        try {
            iDetailPresenter.GetKeyWords(Id);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }





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

    public void SetKeyWords(List<String> keyWords)
    {
        String html = "<a href='"+"https://baike.baidu.com/item/"+keyWords.get(0)+"'>"+keyWords.get(0)+"</a> ";
        CharSequence charSequence = Html.fromHtml(html);
        link = (TextView) findViewById(R.id.test_link_0);
        link.setLinksClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        link.setText(charSequence);
        link.setLinkTextColor(getResources().getColor(R.color.icons));


        String html2 = "<a href='"+"https://baike.baidu.com/item/"+keyWords.get(1)+"'>"+keyWords.get(1)+"</a> ";
        CharSequence charSequence2 = Html.fromHtml(html2);
        link2 = (TextView) findViewById(R.id.test_link_1);
        link2.setLinksClickable(true);
        link2.setMovementMethod(LinkMovementMethod.getInstance());
        link2.setText(charSequence2);
        link2.setLinkTextColor(getResources().getColor(R.color.icons));

        String html3 = "<a href='"+"https://baike.baidu.com/item/"+keyWords.get(2)+"'>"+keyWords.get(2)+"</a> ";
        CharSequence charSequence3 = Html.fromHtml(html3);
        link3 = (TextView) findViewById(R.id.test_link_2);
        link3.setLinksClickable(true);
        link3.setMovementMethod(LinkMovementMethod.getInstance());
        link3.setText(charSequence3);
        link3.setLinkTextColor(getResources().getColor(R.color.icons));
    }
}
