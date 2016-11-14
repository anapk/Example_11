package com.yidou.wandou.example_11;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.yidou.wandou.example_11.bean.DataBean;
import com.yidou.wandou.example_11.bean.News;
import com.yidou.wandou.example_11.dao.DataBeanDao;
import com.yidou.wandou.example_11.db.DbCore;
import com.yidou.wandou.example_11.utils.HttpGets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
{
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;
    private List<DataBean> mList = new ArrayList<>();//新闻数据集合
    private BaseRecyclerAdapter<DataBean> mAdapter;

    private List<String> mlist = new ArrayList<>();//加载新闻类型的集合


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabs();//设置tab的标题，及新闻的类型
        initRecyclerViews();
        initToolbars();//
        initDrawer();//侧滑菜单的收藏按钮
        getDatas("toutiao", "每日一看");

    }

    private void initTabs()
    {
        mTabLayout = (TabLayout) this.findViewById(R.id.main_tabs);
        setTablaout();//设置TabLayout的标题内容
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                String text = tab.getText().toString();
                if ("头条".equals(text))
                {
                    getDatas("toutiao", text);
                } else if ("社会".equals(text))
                {
                    getDatas("shehui", text);
                } else if ("国内".equals(text))
                {
                    getDatas("guonei", text);
                } else if ("国际".equals(text))
                {
                    getDatas("guoji", text);
                } else if ("娱乐".equals(text))
                {
                    getDatas("yule", text);
                } else if ("体育".equals(text))
                {
                    getDatas("tiyu", text);
                } else if ("军事".equals(text))
                {
                    getDatas("junshi", text);
                } else if ("科技".equals(text))
                {
                    getDatas("keji", text);
                } else if ("财经".equals(text))
                {
                    getDatas("caijing", text);
                } else if ("时尚".equals(text))
                {
                    getDatas("shishang", text);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
    }

    private void setTablaout()
    {
        mlist = Arrays.asList(getResources().getStringArray(R.array.array));
        for (int i = 0; i < mlist.size(); i++)
        {
            mTabLayout.addTab(mTabLayout.newTab().setText(mlist.get(i)));
        }
    }

    private void initDrawer()
    {
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.main_drawer);
        NavigationView navigationView = (NavigationView) this.findViewById(R.id.main_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_collection:
                        DataBeanDao dao = DbCore.getDaoSession().getDataBeanDao();
                        List<DataBean> been = dao.loadAll();
                        if (been == null)
                        {
                            Toast.makeText(MainActivity.this, "小主还未添加任何收藏！", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            mAdapter.setData(been);
                        }

                        setTitle("收藏");
                        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void initRecyclerViews()
    {
        mRecyclerView = (RecyclerView) this.findViewById(R.id.main_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseRecyclerAdapter<DataBean>(this, null, R.layout.item)
        {
            @Override
            protected void convert(BaseViewHolder helper, final DataBean item)
            {
                helper.setText(R.id.texts_title, item.getTitle());
                helper.setText(R.id.texts_date, item.getDate());
                helper.setText(R.id.texts_authorname, item.getAuthor_name());
                ImageView images = (ImageView) helper.getConvertView().findViewById(R.id.images_name);
                Picasso.with(MainActivity.this).load(item.getThumbnail_pic_s()).fit().into(images);
                LinearLayout linearLayout = (LinearLayout) helper.getConvertView().findViewById(R.id.item_linear);
                linearLayout.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constances.TAG, item);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getDatas(String msg, String title)//网络请求数据
    {
        mList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constances.NEWS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        retrofit.create(HttpGets.class).getNews(msg, Constances.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<News>()
                {
                    @Override
                    public void onCompleted()
                    {
                        mAdapter.setData(mList);
                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onNext(News news)
                    {
                        mList = news.getResult().getData();
                    }
                });
        setTitle(title);
    }


    private void initToolbars()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_menu_black_24dp);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT, true);
                break;
            case R.id.action_problem:
                Toast.makeText(this, "小主，如果你有什么问题或者好的想法，请联系1070138445@qq.com", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        return true;
    }

}
