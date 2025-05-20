package com.example.travalms.util;

import java.lang.System;

/**
 * 提供城市和地区名称拼音与中文之间的映射
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007J\u000e\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/example/travalms/util/CityNameMapping;", "", "()V", "pinyinToChineseMap", "", "", "pinyinListToChineseNames", "", "pinyinList", "pinyinToChineseName", "pinyin", "app_debug"})
public final class CityNameMapping {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.util.CityNameMapping INSTANCE = null;
    
    /**
     * 拼音到中文的映射表
     */
    private static final java.util.Map<java.lang.String, java.lang.String> pinyinToChineseMap = null;
    
    private CityNameMapping() {
        super();
    }
    
    /**
     * 将拼音转换为中文名称
     * @param pinyin 拼音名称
     * @return 中文名称，如果没有对应中文则返回原拼音
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String pinyinToChineseName(@org.jetbrains.annotations.NotNull
    java.lang.String pinyin) {
        return null;
    }
    
    /**
     * 批量将拼音转换为中文名称
     * @param pinyinList 拼音名称列表
     * @return 中文名称列表
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> pinyinListToChineseNames(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> pinyinList) {
        return null;
    }
}