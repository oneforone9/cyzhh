package com.essence.service.enums;

public enum SourceEnum {
    ;


    private Integer id;

    private String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    SourceEnum(Integer id, String text){
        this.id = id;
        this.text = text;
    }



}
