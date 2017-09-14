package com.java.team_33.view.detailinfo;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import com.java.team_33.MyApplication;
import com.java.team_33.R;
import com.java.team_33.presenter.IDetailPresenterCompl;

import com.java.team_33.presenter.*;

import org.json.JSONException;

import java.util.List;


public class DetailInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,IDetailView {

    private String category;
    private NavigationView mNavigationView;
    private TextView content;
    private TextView articleTitle;
    private ImageButton rtnBtn, shareBtn;
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
        final String title = bundle.getString("Title");
        category = bundle.getString("Category");
        Author = bundle.getString("Author");
        Time = bundle.getString("Time");

        Id = bundle.getString("ID");

        articleTitle = (TextView) findViewById(R.id.detail_bar_title);
        content = (TextView) findViewById(R.id.news_content);
        mAuthor = (TextView) findViewById(R.id.detail_author);
        mTime = (TextView) findViewById(R.id.detail_time);
        rtnBtn = (ImageButton) findViewById(R.id.detail_return);
        shareBtn = (ImageButton) findViewById(R.id.detail_share);
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

        //分享按钮
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WeiXinShareUtil.sharePhotoToWX(DetailInfoActivity.this, title, "");

                //String pMessage = "测试信息";
//
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI"));
//                intent.setAction(Intent.ACTION_SEND);
//                intent.setType("image/*");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.putExtra("Kdescription",pMessage);
//
//                Uri uri = Uri.parse("http://f.hiphotos.baidu.com/baike/pic/item/f31fbe096b63f624861f5c158d44ebf81a4ca362.jpg");
//                intent.putExtra(Intent.EXTRA_STREAM,uri);
//
//                startActivity(intent);

                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                intent.setComponent(componentName);
                intent.setAction(Intent.ACTION_SEND);


                MyApplication ap = MyApplication.getInstance();

                Map<String,Object>news = null;
                try {
                    news = ap.GetNews(Id);

                    String title = news.get("news_Title").toString();
                    String time = news.get("news_Time").toString();
                    String url = news.get("news_URL").toString();

                    intent.setType("text/*");
                    //intent.putExtra("Kdescription",pMessage);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Title");
                    intent.putExtra(Intent.EXTRA_TEXT, "Title: "+title+"\n"+"Time: "+time+"\n"+"URL: "+url+"\n");
                    startActivity(Intent.createChooser(intent, "分享图片"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        try{
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
        } catch (Exception e){
            Log.i("aaa", "SetKeyWords: ", e);
        }

    }
}
