package com.example.newsapp.singleitem;

import java.util.HashMap;

/**
 * Created by junxian on 9/8/2017.
 */

public class SingleListItem {
    public String type;
    public HashMap<String, Object> map;

    public SingleListItem(String type, HashMap<String, Object> map) {
        this.type = type;
        this.map = map;
    }
}
