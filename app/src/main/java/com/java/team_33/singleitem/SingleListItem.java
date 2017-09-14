package com.java.team_33.singleitem;

import java.util.HashMap;

/**
 * Created by junxian on 9/8/2017.
 */

public class SingleListItem {
    public String type;
    public boolean read;
    public HashMap<String, Object> map;

    public SingleListItem(String type, HashMap<String, Object> map) {
        this.type = type;
        this.map = map;
    }
}
