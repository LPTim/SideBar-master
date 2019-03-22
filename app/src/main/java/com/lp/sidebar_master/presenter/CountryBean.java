package com.lp.sidebar_master.presenter;

/**
 * File descripition:
 *
 * @author lp
 * @date 2018/7/13
 */

public class CountryBean {

    /**
     * code : 86
     * name : China
     */
    private String code;
    private String name;
    private String sortLetters;
    //是否是字母
    private Boolean isLetter = false;

    public Boolean getLetter() {
        return isLetter;
    }

    public void setLetter(Boolean letter) {
        isLetter = letter;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CountryBean{" +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
