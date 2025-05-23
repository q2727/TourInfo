package com.example.travalms.ui.model;

import java.lang.System;

/**
 * 尾单数据模型
 * 用于UI展示的统一数据结构
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b$\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0087\b\u0018\u0000 82\u00020\u0001:\u00018B}\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u000f\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0012J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00050\u000fH\u00c6\u0003J\u0010\u0010\'\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001bJ\u000b\u0010(\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010)\u001a\u00020\u0005H\u00c6\u0003J\t\u0010*\u001a\u00020\u0005H\u00c6\u0003J\t\u0010+\u001a\u00020\u0005H\u00c6\u0003J\t\u0010,\u001a\u00020\tH\u00c6\u0003J\t\u0010-\u001a\u00020\u0005H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0005H\u00c6\u0003J\t\u00100\u001a\u00020\u0003H\u00c6\u0003J\u0090\u0001\u00101\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00052\b\b\u0002\u0010\r\u001a\u00020\u00032\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001\u00a2\u0006\u0002\u00102J\u0013\u00103\u001a\u0002042\b\u00105\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00106\u001a\u00020\u0003H\u00d6\u0001J\t\u00107\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\n\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u0011\u0010\f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0014R\u0015\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b\u001a\u0010\u001bR\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0014R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0014R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0014\u00a8\u00069"}, d2 = {"Lcom/example/travalms/ui/model/PostItem;", "", "id", "", "title", "", "feature", "publisher", "publishTime", "", "dates", "remainingSlots", "price", "daysExpired", "publishLocations", "", "productId", "productTitle", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;ILjava/lang/String;ILjava/util/List;Ljava/lang/Integer;Ljava/lang/String;)V", "getDates", "()Ljava/lang/String;", "getDaysExpired", "()I", "getFeature", "getId", "getPrice", "getProductId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getProductTitle", "getPublishLocations", "()Ljava/util/List;", "getPublishTime", "()J", "getPublisher", "getRemainingSlots", "getTitle", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;ILjava/lang/String;ILjava/util/List;Ljava/lang/Integer;Ljava/lang/String;)Lcom/example/travalms/ui/model/PostItem;", "equals", "", "other", "hashCode", "toString", "Companion", "app_debug"})
public final class PostItem {
    private final int id = 0;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String feature = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String publisher = null;
    private final long publishTime = 0L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String dates = null;
    private final int remainingSlots = 0;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String price = null;
    private final int daysExpired = 0;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> publishLocations = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer productId = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String productTitle = null;
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.ui.model.PostItem.Companion Companion = null;
    
    /**
     * 尾单数据模型
     * 用于UI展示的统一数据结构
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.ui.model.PostItem copy(int id, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String feature, @org.jetbrains.annotations.NotNull
    java.lang.String publisher, long publishTime, @org.jetbrains.annotations.NotNull
    java.lang.String dates, int remainingSlots, @org.jetbrains.annotations.NotNull
    java.lang.String price, int daysExpired, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> publishLocations, @org.jetbrains.annotations.Nullable
    java.lang.Integer productId, @org.jetbrains.annotations.Nullable
    java.lang.String productTitle) {
        return null;
    }
    
    /**
     * 尾单数据模型
     * 用于UI展示的统一数据结构
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 尾单数据模型
     * 用于UI展示的统一数据结构
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 尾单数据模型
     * 用于UI展示的统一数据结构
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public PostItem(int id, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String feature, @org.jetbrains.annotations.NotNull
    java.lang.String publisher, long publishTime, @org.jetbrains.annotations.NotNull
    java.lang.String dates, int remainingSlots, @org.jetbrains.annotations.NotNull
    java.lang.String price, int daysExpired, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> publishLocations, @org.jetbrains.annotations.Nullable
    java.lang.Integer productId, @org.jetbrains.annotations.Nullable
    java.lang.String productTitle) {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getFeature() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPublisher() {
        return null;
    }
    
    public final long component5() {
        return 0L;
    }
    
    public final long getPublishTime() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDates() {
        return null;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int getRemainingSlots() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPrice() {
        return null;
    }
    
    public final int component9() {
        return 0;
    }
    
    public final int getDaysExpired() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getPublishLocations() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getProductId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getProductTitle() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/travalms/ui/model/PostItem$Companion;", "", "()V", "createDefault", "Lcom/example/travalms/ui/model/PostItem;", "id", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * 创建默认实例
         */
        @org.jetbrains.annotations.NotNull
        public final com.example.travalms.ui.model.PostItem createDefault(int id) {
            return null;
        }
    }
}