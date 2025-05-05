package com.example.travalms.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.util.Log;

/**
 * 拼音工具类，用于处理汉字转拼音功能
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\f\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0004J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004J\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/example/travalms/utils/PinyinUtils;", "", "()V", "TAG", "", "containsChinese", "", "str", "getFirstLetter", "chinese", "isChineseChar", "c", "", "app_debug"})
public final class PinyinUtils {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.utils.PinyinUtils INSTANCE = null;
    private static final java.lang.String TAG = "PinyinUtils";
    
    private PinyinUtils() {
        super();
    }
    
    /**
     * 获取汉字的拼音首字母（大写）
     * 如果字符串中有多个汉字，只返回第一个汉字的拼音首字母
     * 如果不是汉字，则原样返回字符
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getFirstLetter(@org.jetbrains.annotations.NotNull
    java.lang.String chinese) {
        return null;
    }
    
    /**
     * 判断一个字符是否是汉字
     */
    public final boolean isChineseChar(char c) {
        return false;
    }
    
    /**
     * 判断一个字符串是否包含汉字
     */
    public final boolean containsChinese(@org.jetbrains.annotations.NotNull
    java.lang.String str) {
        return false;
    }
}