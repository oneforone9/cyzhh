package com.essence.common.enums;

/**
 *  断面水质等级
 *         数据库存的（1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;6 无水/结冰；）
 * @author bird
 */

public enum SectionWaterQualityLevelEnum {

    ONE_LEVEL(1,"Ⅰ类"),
    TWO_LEVEL(2,"Ⅱ类"),
    THREE_LEVEL(3,"Ⅲ类"),
    FOUR_LEVEL(4,"Ⅳ类"),
    FIVE_LEVEL(5,"Ⅴ类"),
    NO_LEVEL(7,"无水/结冰"),
    LIE_LEVEL(6,"劣Ⅴ");;

    private Integer type;

    private String text;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    SectionWaterQualityLevelEnum(Integer type, String text) {
        this.type = type;
        this.text = text;
    }

}




