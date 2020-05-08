package com.example.geogooglemapslab8.repositories;

import com.example.geogooglemapslab8.RequestEmun;

import java.util.List;

public class DataModel {

    private List<String> dataList;
    private RequestEmun requestEmun;

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public RequestEmun getRequestEmun() {
        return requestEmun;
    }

    public void setRequestEmun(RequestEmun requestEmun) {
        this.requestEmun = requestEmun;
    }
}
