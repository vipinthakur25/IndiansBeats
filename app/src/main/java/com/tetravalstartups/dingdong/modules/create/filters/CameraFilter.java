package com.tetravalstartups.dingdong.modules.create.filters;

public class CameraFilter {
    private int id;
    private String name;
    private int sample;

    public CameraFilter() {
    }


    public CameraFilter(int id, String name, int sample) {
        this.id = id;
        this.name = name;
        this.sample = sample;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }
}
