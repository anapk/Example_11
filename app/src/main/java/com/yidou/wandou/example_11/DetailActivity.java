package com.yidou.wandou.example_11;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.yidou.wandou.example_11.bean.DataBean;
import com.yidou.wandou.example_11.bean.News;
import com.yidou.wandou.example_11.dao.DataBeanDao;
import com.yidou.wandou.example_11.db.DbCore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity
{
    private WebView mWebView;
    private DataBean mBean;

    private DbCore mDbCore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initToolBars();
        initWebView();

    }

    private void initWebView()
    {
        mWebView = (WebView) findViewById(R.id.detail_webView);
        Intent intent = getIntent();
        mBean = (DataBean) intent.getSerializableExtra(Constances.TAG);
        mWebView.loadUrl(mBean.getUrl());
        setTitle(mBean.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_shares:
                Toast.makeText(this, "分享成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_collection:

                unique(mBean.getTitle(),mBean);
                break;
            default:
                break;
        }
        return true;
    }

    private void unique(String msg,DataBean bean)//判断数据库里面的数据是否存在
    {
        boolean isHave = false;
        DataBeanDao dbDao = mDbCore.getDaoSession().getDataBeanDao();
        List<DataBean> been = dbDao.loadAll();
        for (int i = 0; i < been.size(); i++)
        {
            String title = been.get(i).getTitle();
            isHave = msg.equals(title);
            break;
        }
        if (isHave)
        {
            Toast.makeText(this, "您已收藏，切勿重复添加！", Toast.LENGTH_SHORT).show();
        }else
        {
            dbDao.insert(bean);
            Toast.makeText(this, "收藏成功！", Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolBars()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
