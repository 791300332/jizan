package com.sgx.jizan.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sgx.jizan.R;
import com.sgx.jizan.activity.GiftDetailActivity;
import com.sgx.jizan.adapter.GalleryAdapter;
import com.sgx.jizan.adapter.listview.IndexListViewAdapter;
import com.sgx.jizan.loader.GlideImageLoader;
import com.sgx.jizan.model.IndexModel;
import com.sgx.jizan.model.RecyclerModel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by bruce on 2016/11/1.
 * BaseFragment
 */

public class BaseFragment extends Fragment {

    private Banner banner;

    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private ListView listView;

    public static BaseFragment newInstance(String info,String msg) {
        Bundle args = new Bundle();
        BaseFragment fragment = new BaseFragment();
        args.putString("info", info);
        args.putString("msg",msg);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        String info = getArguments().getString("info");
        String msg = getArguments().getString("msg");

        if("0".equals(info)) {
            view = inflater.inflate(R.layout.index, null);
            try {
                JSONObject obj = new JSONObject(msg);
                setBanner(view,obj);
                setGridView(view,obj);
                setListView(view,obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if("1".equals(info)) {
            view = inflater.inflate(R.layout.activity_recycler_item, null);
        } else if("2".equals(info)) {
            view = inflater.inflate(R.layout.activity_recovery, null);
        }
        return view;
    }

    private void setListView(View view ,JSONObject msy) throws JSONException {
        listView = (ListView) view.findViewById(R.id.indexList);

        List<IndexModel> m = new ArrayList<IndexModel>();
        JSONArray hotList = msy.getJSONArray("hotList");
        for(int i = 0;i<hotList.length();i++) {
            IndexModel d = new IndexModel();
            JSONObject o = hotList.optJSONObject(i);
            d.setId(o.optInt("id"));
            d.setImgUrl(o.optString("giftSmallImg"));
            d.setTitle(o.optString("giftName"));
            d.setGroupCount(o.optInt("groupCount",0) + "");
            d.setLookCount(o.optInt("lookDown",0) + "");
            m.add(d);
        }

        listView.setAdapter(new IndexListViewAdapter(getContext(),m));
        listView.setDivider(new ColorDrawable(Color.GRAY));
        listView.setDividerHeight(1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getContext(), GiftDetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

    private void setGridView(View view ,JSONObject msg) throws JSONException {

        List<RecyclerModel> list = new ArrayList<RecyclerModel>();
        JSONArray recommendList = msg.getJSONArray("recommendList");

        for(int i = 0;i<recommendList.length();i++) {
            RecyclerModel d = new RecyclerModel();
            JSONObject o = recommendList.optJSONObject(i);
            d.setImgUrl(o.optString("giftSmallImg"));
            d.setId(o.optInt("id"));
            list.add(d);
        }

        //得到控件
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(getContext(), list);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setBanner(View view ,JSONObject msy) {
        banner = (Banner) view.findViewById(R.id.banner);

        banner.setImageLoader(new GlideImageLoader());
        Integer[] list = {R.mipmap.noimg,R.mipmap.noimg,R.mipmap.noimg,R.mipmap.noimg};

        banner.setImages(new ArrayList<Object>(Arrays.asList(list)));
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
}
