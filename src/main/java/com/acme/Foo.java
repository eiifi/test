package com.acme;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Foo {

    @JsonIgnore
    private String bar;

    private String baz;

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    @JsonIgnore
    public String getBaz() {
        return baz;
    }

    @JsonIgnore
    public void setBaz(String baz) {
        this.baz = baz;
    }
}
