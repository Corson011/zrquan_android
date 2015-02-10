package com.zrquan.mobile.controller;

import com.android.volley.VolleyError;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zrquan.mobile.event.DiscussionEvent;
import com.zrquan.mobile.model.Discussion;
import com.zrquan.mobile.support.util.LogUtils;
import com.zrquan.mobile.support.volley.VolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class FeedController {

    public static void getDiscussionFeed(String userId, String page) {
        // pass second argument as "null" for GET requests
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("page", page);
        final String url =
                "http://192.168.1.104:3000/home/hot_posts?userId=" + userId + "&page=" + page;

        LogUtils.d(url);
        VolleyJsonRequest.get(url, new VolleyJsonRequest.ResponseHandler() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    LogUtils.d("收到请求的回复了");
                    LogUtils.d("Response:" + response);
                    JSONArray results = response.getJSONArray("result");
                    List<Discussion> discussionList = new ArrayList<Discussion>();
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();
                    for (int i = 0; i < results.length(); i ++ ) {
                        Discussion d = gson.fromJson(results.getJSONObject(i).toString(), Discussion.class);
                        discussionList.add(d);
                    }
                    LogUtils.d("discussion list size:" + discussionList.size());
                    EventBus.getDefault().post(new DiscussionEvent(discussionList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {}
        });
    }
}