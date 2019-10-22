package com.schneider.imscore.enums;

/**
 * @author liyuan
 * @createDate 2019/09/09 14:21
 * @Description
 */
public enum  LanguageEnum {
    /**
     * 英文
     */
    LANGUAGE_ENGLISH("EN", "English"),
    /**
     * 中文
     */
    LANGUAGE_CHINESE("ZH", "Chinese"),
    /**
     * 葡萄牙语
     */
    LANGUAGE_PORTUGUESE("PT", "Portuguese"),

    /**
     * 俄语
     */
    LANGUAGE_RUSSIAN("RU", "Russian");
    private String key;
    private String desc;

    LanguageEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static boolean isInclude(int key) {
        boolean include = false;
        for (LanguageEnum e : LanguageEnum.values()) {
            if (e.getKey().equals(key)) {
                include = true;
                break;
            }
        }
        return include;
    }

    /**
     * 根据value  获取text
     *
     * @param value
     * @return
     */
    public static String getValueByText(String value) {
        for (LanguageEnum directoryTypeEnum : LanguageEnum.values()) {
            if (value .equals(directoryTypeEnum.getKey())) {
                return directoryTypeEnum.getDesc();
            }
        }
        return null;
    }
}
