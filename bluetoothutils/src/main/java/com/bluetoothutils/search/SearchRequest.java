package com.bluetoothutils.search;

/**
 * Created by sunxiaoyu on 2017/2/9.
 */

public class SearchRequest {


    /**
     * 设置扫描经典蓝牙时间
     */
    private long searchClassicsDevTime;

    /**
     * 设置扫描低功耗蓝牙时间
     */
    private long searchBleDevTime;



    public long getSearchClassicsDevTime() {
        return searchClassicsDevTime;
    }

    public SearchRequest setSearchClassicsDevTime(long searchClassicsDevTime) {
        this.searchClassicsDevTime = searchClassicsDevTime;
        return this;
    }

    public long getSearchBleDevTime() {
        return searchBleDevTime;
    }

    public SearchRequest setSearchBleDevTime(long searchBleDevTime) {
        this.searchBleDevTime = searchBleDevTime;
        return this;
    }
}
