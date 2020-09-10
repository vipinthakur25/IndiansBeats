package com.tetravalstartups.dingdong.modules.discover.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<SearchResponse> searchResponses = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SearchResponse> getSearchResponses() {
        return searchResponses;
    }

    public void setData(List<SearchResponse> searchResponses) {
        this.searchResponses = searchResponses;
    }

}