package com.yks.simpledemo3.bean;

/**
 * 描述：股票信息
 * 作者：zzh
 * time:2019/07/29
 * http://hq.sinajs.cn/list=sh000001
 */
public class StockBean {

    private String stockName;//股票名称
    private String todayOpenPrice;//今日开盘价
    private String yesterdayClosePrice;//昨日收盘价
    private String currentPrice;//当前价
    private String highestPriceToday;//今日最高价
    private String lowestPriceToday;//今日最低价
    private String dealCount;//交易量
    private String dealMoney;//交易金额
    private String stockDate;//日期
    private String stockTime;//时间

    public StockBean(String stockName, String todayOpenPrice, String yesterdayClosePrice, String currentPrice, String highestPriceToday, String lowestPriceToday, String dealCount, String dealMoney, String stockDate, String stockTime) {
        this.stockName = stockName;
        this.todayOpenPrice = todayOpenPrice;
        this.yesterdayClosePrice = yesterdayClosePrice;
        this.currentPrice = currentPrice;
        this.highestPriceToday = highestPriceToday;
        this.lowestPriceToday = lowestPriceToday;
        this.dealCount = dealCount;
        this.dealMoney = dealMoney;
        this.stockDate = stockDate;
        this.stockTime = stockTime;
    }

    public String getStockName() {
        return stockName;
    }

    public String getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public String getYesterdayClosePrice() {
        return yesterdayClosePrice;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public String getHighestPriceToday() {
        return highestPriceToday;
    }

    public String getLowestPriceToday() {
        return lowestPriceToday;
    }

    public String getDealCount() {
        return dealCount;
    }

    public String getDealMoney() {
        return dealMoney;
    }

    public String getStockDate() {
        return stockDate;
    }

    public String getStockTime() {
        return stockTime;
    }
}
