package com.yks.simpledemo3.bean;

/**
 * 描述：
 * 作者：
 * time:2019/09/04
 */
public class BottomSheetBean {
    private int srcId;//图片id
    private int title;//名称

    public BottomSheetBean(int srcId, int title) {
        this.srcId = srcId;
        this.title = title;
    }

    public int getSrcId() {
        return srcId;
    }

    public int getTitle() {
        return title;
    }
}
