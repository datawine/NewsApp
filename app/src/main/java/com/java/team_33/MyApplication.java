package com.java.team_33;

import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;

import com.java.team_33.model.MySqlite;
import com.java.team_33.model.NewsManager;
import com.java.team_33.view.briefinfo.BriefInfoActivity;
import com.java.team_33.view.briefinfo.PageListFragment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.*;
import com.iflytek.cloud.SynthesizerListener;


/**
 * Created by junxian on 9/9/2017.
 */

public class MyApplication extends Application {
    private boolean dayMode;
    private boolean picMode;
    UiModeManager mUiModeManager;

    private MySqlite mySqlite;
    private NewsManager newsManager;

    private String[] tags = {"科技","教育","军事","国内","社会", "文化", "汽车","国际","体育","财经","健康","娱乐"};

    private
    static MyApplication instance;

    private String query;

    private boolean headerfooterflag;

    private List<Map<String,Object>> newList;

    private Map<String , Integer>PageNum;

    private SpeechSynthesizer mTts;
    private SynthesizerListener mSynListener;

    public
    static MyApplication getInstance() {

        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        setDayMode(true);
        mUiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);

        instance = this;

        PageNum = new HashMap<String, Integer>();

        query = "";


        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59b8deca");



//1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
//2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
//设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
//保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
//如果不需要保存合成音频，注释该行代码
//mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

//合成监听器
        mSynListener = new SynthesizerListener(){
            //会话结束回调接口，没有错误时，error为null
            public void onCompleted(SpeechError error)
            {
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }

            //缓冲进度回调
            //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
            public void onBufferProgress(int percent, int beginPos, int endPos, String info)
            {
            }

            //开始播放
            public void onSpeakBegin()
            {
            }

            //暂停播放
            public void onSpeakPaused()
            {
            }

            //播放进度回调
            //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
            public void onSpeakProgress(int percent, int beginPos, int endPos)
            {
            }

            //恢复播放回调接口
            public void onSpeakResumed()
            {
            }

            //会话事件回调接口
        };




        mySqlite = new MySqlite();
                mySqlite.init();

                newsManager = new NewsManager(mySqlite);



    }

    public boolean getDayMode() {
        return dayMode;
    }

    public void setDayMode(boolean mode) {
        dayMode = mode;
    }

    public List<Map<String, Object>> GetLatestNewsList() throws InterruptedException
    {
        return newsManager.getLatestNewsList();
    }

    public List<Map<String, Object>> GetLatestNewsList(String tag) throws InterruptedException
    {
        return newsManager.getLatestNewsList(tag);
    }

    public List<Map<String, Object>> GetLatestNewsList(int page) throws InterruptedException
    {
        return newsManager.getLatestNewsList(page);
    }

    public List<Map<String, Object>> GetSearchNewsList(String tag)  throws InterruptedException
    {
        return newsManager.getSearchedNewsList(tag,GetTagPage("搜索"));
    }

    public Map<String,Object> GetNews(String ID) throws InterruptedException, JSONException
    {
        return newsManager.getNews(ID);
    }

    public List<Map<String, Object>> GetStarNews()
    {
        return mySqlite.getStaredNews();
    }

    public List<Map<String, Object>> GetKeyWords(String ID) throws InterruptedException, JSONException
    {
        return (List<Map<String, Object>>) newsManager.getNews(ID).get("Keywords");
    }

    public String[] GetTags()
    {
        List<String> tags = new ArrayList<String>();

        tags.add("推荐");
        tags.add("收藏夹");
        tags.add("搜索");

        PageNum.put("搜索",1);
        PageNum.put("推荐",1);

        List<String> tmp = mySqlite.getTags();

        for(int i=0;i<tmp.size();i++) {
            tags.add(tmp.get(i));
            PageNum.put(tmp.get(i),1);
        }

        String[] tagstring = tags.toArray(new String[tags.size()]);

        return tagstring;
    }

    public void AddTag(String key)
    {
        mySqlite.addTag(key);

        PageNum.put(key,1);

        BriefInfoActivity bri = BriefInfoActivity.getInstance();

        bri.onResume();
    }

    public void DelTag(String key)
    {
        mySqlite.delTag(key);

        BriefInfoActivity bri = BriefInfoActivity.getInstance();

        bri.onResume();
    }

    public void SetSearchText(String query)
    {
        this.query = query;



        PageListFragment page = PageListFragment.getInstance();


        PageNum.put("搜索",1);

        page.InitSearch();

    }

    public String GetQuery()
    {
        return this.query;
    }

    public boolean IsStared(String ID)
    {
        return mySqlite.isStared(ID);
    }

    public void Star(String ID)
    {
        mySqlite.star(ID);
    }

    public void UnStar(String ID)
    {
        mySqlite.unstar(ID);
    }

    public List<Map<String,Object>> GetNewList() {

        return this.newList;
    }

    public void AddTagPage(String mCategory) {
        int tmp = PageNum.get(mCategory);

        PageNum.put(mCategory,tmp+1);

    }

    public int GetTagPage(String tag)
    {
        return PageNum.get(tag);
    }

    public void SetNewList(String mCategory) {

        if(mCategory == "收藏夹")
        {
            newList = mySqlite.getStaredNews();

        }
        else {

            if(mCategory == "搜索")
            {
                try {
                    newList = this.GetSearchNewsList(this.query);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            else {
                if (mCategory == "推荐") {
                    try {
                        newList = newsManager.getLatestNewsList(PageNum.get(mCategory));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        newList = newsManager.getLatestNewsList(mCategory, PageNum.get(mCategory));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void GetVoice(String content)
    {
        mTts.startSpeaking(content, mSynListener);

    }
    public boolean isSpeaking()
    {
        return mTts.isSpeaking();
    }
    public void StopVoice()
    {
        mTts.stopSpeaking();
    }

    public void SetHeaderorFooter(boolean i) {

        this.headerfooterflag = i;
    }

    public boolean GetFlag()
    {
        return this.headerfooterflag;
    }

    public boolean IsRead(String ID) {

        return mySqlite.hasRead(ID);
    }

    public ArrayList<String> GetBanList()
    {
        ArrayList<String> list;

        list = (ArrayList<String>) mySqlite.getBlack();


        return list;
    }

    public void AddBanWord(String word)
    {
        mySqlite.addBlack(word);


    }

    public void DelBanWord(String word)
    {
        mySqlite.delBlack(word);
    }

    public boolean getPicMode() {

        return picMode;
    }

    public void setPicMode(boolean b) {picMode = b;
    }

    public String GetPic(String ID) {
        String tmp = null;
        try {
            tmp = mySqlite.getPicture(ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tmp;

    }

    public void SetRead(String id) {

        mySqlite.read(id);
    }
}
