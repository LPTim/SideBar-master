package com.lp.sidebar_master.utils;

import android.text.TextUtils;

import com.lp.sidebar_master.presenter.CountryBean;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 拼音工具类
 */
public class PinYinKit {
    public static String getPingYin(String chineseStr) throws BadHanyuPinyinOutputFormatCombination {
        String zhongWenPinYin = "";
        char[] chars = chineseStr.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i], getDefaultOutputFormat());
            if (pinYin != null)
                zhongWenPinYin += pinYin[0];
            else
                zhongWenPinYin += chars[i];
        }
        return zhongWenPinYin;
    }

    private static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        return format;
    }


    //排序
    public static List<CountryBean> filledData(List<CountryBean> mList) throws BadHanyuPinyinOutputFormatCombination {
        for (int i = 0; i < mList.size(); i++) {
            String pinyin = PinYinKit.getPingYin(mList.get(i).getName());
            String sortString = "";
            if (!TextUtils.isEmpty(pinyin)) {
                sortString = pinyin.substring(0, 1).toUpperCase();
            }
            if (sortString.matches("[A-Z]")) {
                mList.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                mList.get(i).setSortLetters("#");
            }
        }
        //排序
        Collections.sort(mList, new PinyinComparatorAdmin());
        initLetter(mList);
        return mList;
    }

    public static void initLetter(List<CountryBean> mList) {
        for (int i = 0; i < mList.size(); i++) {
            if (i == getPositionForSection(mList, mList.get(i).getSortLetters().charAt(0))) {
                mList.get(i).setLetter(true);
            } else {
                mList.get(i).setLetter(false);
            }
        }
    }

    /**
     * 方法含义：将当前字母传入方法体中， 来获取当前字母在集合中第一次出现的位置position  如果等于当前item的position，UI字母栏
     * 显示，如果不是，UI字母栏隐藏
     *
     * @param section
     * @return 对应集合中第一个出现的字母
     */
    public static int getPositionForSection(List<CountryBean> mList, int section) {
        for (int i = 0; i < mList.size(); i++) {
            String sortStr = mList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public static class PinyinComparatorAdmin implements Comparator<CountryBean> {
        @Override
        public int compare(CountryBean o1, CountryBean o2) {
            if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }
}