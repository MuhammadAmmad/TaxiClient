package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;

public class Reason {
    private static final String ID = "ID";
    private static final String NAME = "DN";

    @SerializedName(ID)
    private String id;

    @SerializedName(NAME)
    private String name;

    public Reason(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
