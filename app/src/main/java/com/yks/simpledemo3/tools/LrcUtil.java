package com.yks.simpledemo3.tools;

import com.yks.simpledemo3.bean.LrcBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：歌词类工具
 * 作者：zzh
 * time:2019/10/26
 */
public class LrcUtil {

    /**
     * 描述：解析lrc歌词
     * @param lrcStr lrc原文
     * @return 解析过后的数组
     */
    public static List<LrcBean> parseStr2List(String lrcStr){
        List<LrcBean> list = new ArrayList<>();
        String lrcText = lrcStr.replaceAll("&#58;",":")
                .replaceAll("&#10;","\n")
                .replaceAll("&#46;",".")
                .replaceAll("&#32;"," ")
                .replaceAll("&#45;","-")
                .replaceAll("&#13;","\r")
                .replaceAll("&#39;","'");
        String[] split = lrcText.split("\n");//通过换行拆分
        for (int i=0;i<split.length;i++){
            String lrc = split[i];
            if (lrc.contains(".")){
                String minute = lrc.substring(lrc.indexOf("[")+1,lrc.indexOf("[")+3);//分钟
                String second = lrc.substring(lrc.indexOf(":")+1,lrc.indexOf(":")+3);//秒
                String mills = lrc.substring(lrc.indexOf(".")+1,lrc.indexOf(".")+3);//毫秒

                long startTime = Long.valueOf(minute)*60*1000 + Long.valueOf(second)*1000 + Long.valueOf(mills)*10;
                String text = lrc.substring(lrc.indexOf("]")+1);
                if (text == null || "".equals(text)){
                    text = "music";
                }
                LrcBean bean = new LrcBean();
                bean.setStart(startTime);
                bean.setLrc(text);
                list.add(bean);
                if (list.size() > 1){
                    list.get(list.size() - 2).setEnd(startTime);
                }
                if (i == split.length - 1){
                    list.get(list.size() - 1).setEnd(startTime+10000);
                }
            }
        }
        return list;
    }
}
