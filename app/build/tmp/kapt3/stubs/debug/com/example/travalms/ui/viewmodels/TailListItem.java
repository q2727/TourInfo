package com.example.travalms.ui.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.data.remote.PubSubNotification;
import com.example.travalms.ui.screens.SubscriptionNodeItem;
import com.example.travalms.ui.screens.SubscriptionNodeType;
import com.example.travalms.data.remote.ConnectionState;
import com.example.travalms.api.dto.TailOrderRequest;
import com.example.travalms.api.dto.TailOrderResponse;
import com.example.travalms.data.api.NetworkModule;
import com.example.travalms.data.api.TailOrderApiService;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 尾单数据类，包含所有需要发布的信息
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0000\n\u0002\u0010\t\n\u0002\b%\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0085\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u0010\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0014J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00030\u0010H\u00c6\u0003J\u0010\u0010+\u001a\u0004\u0018\u00010\u0012H\u00c6\u0003\u00a2\u0006\u0002\u0010 J\u000b\u0010,\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u0006H\u00c6\u0003J\t\u0010/\u001a\u00020\u0006H\u00c6\u0003J\t\u00100\u001a\u00020\tH\u00c6\u0003J\t\u00101\u001a\u00020\tH\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u009e\u0001\u00105\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001\u00a2\u0006\u0002\u00106J\u0013\u00107\u001a\u0002082\b\u00109\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010:\u001a\u00020;H\u00d6\u0001J\t\u0010<\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001dR\u0015\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u00a2\u0006\n\n\u0002\u0010!\u001a\u0004\b\u001f\u0010 R\u0013\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0016R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001aR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0016R\u0013\u0010\r\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u0016\u00a8\u0006="}, d2 = {"Lcom/example/travalms/ui/viewmodels/TailListItem;", "", "title", "", "description", "price", "", "originalPrice", "startDate", "Ljava/util/Date;", "endDate", "contactPerson", "contactPhone", "tourGuide", "location", "tags", "", "productId", "", "productTitle", "(Ljava/lang/String;Ljava/lang/String;DDLjava/util/Date;Ljava/util/Date;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/lang/Long;Ljava/lang/String;)V", "getContactPerson", "()Ljava/lang/String;", "getContactPhone", "getDescription", "getEndDate", "()Ljava/util/Date;", "getLocation", "getOriginalPrice", "()D", "getPrice", "getProductId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getProductTitle", "getStartDate", "getTags", "()Ljava/util/List;", "getTitle", "getTourGuide", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;DDLjava/util/Date;Ljava/util/Date;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/lang/Long;Ljava/lang/String;)Lcom/example/travalms/ui/viewmodels/TailListItem;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class TailListItem {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String description = null;
    private final double price = 0.0;
    private final double originalPrice = 0.0;
    @org.jetbrains.annotations.NotNull
    private final java.util.Date startDate = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.Date endDate = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String contactPerson = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String contactPhone = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String tourGuide = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String location = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> tags = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long productId = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String productTitle = null;
    
    /**
     * 尾单数据类，包含所有需要发布的信息
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.ui.viewmodels.TailListItem copy(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String description, double price, double originalPrice, @org.jetbrains.annotations.NotNull
    java.util.Date startDate, @org.jetbrains.annotations.NotNull
    java.util.Date endDate, @org.jetbrains.annotations.NotNull
    java.lang.String contactPerson, @org.jetbrains.annotations.NotNull
    java.lang.String contactPhone, @org.jetbrains.annotations.Nullable
    java.lang.String tourGuide, @org.jetbrains.annotations.Nullable
    java.lang.String location, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> tags, @org.jetbrains.annotations.Nullable
    java.lang.Long productId, @org.jetbrains.annotations.Nullable
    java.lang.String productTitle) {
        return null;
    }
    
    /**
     * 尾单数据类，包含所有需要发布的信息
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 尾单数据类，包含所有需要发布的信息
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 尾单数据类，包含所有需要发布的信息
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public TailListItem(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String description, double price, double originalPrice, @org.jetbrains.annotations.NotNull
    java.util.Date startDate, @org.jetbrains.annotations.NotNull
    java.util.Date endDate, @org.jetbrains.annotations.NotNull
    java.lang.String contactPerson, @org.jetbrains.annotations.NotNull
    java.lang.String contactPhone, @org.jetbrains.annotations.Nullable
    java.lang.String tourGuide, @org.jetbrains.annotations.Nullable
    java.lang.String location, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> tags, @org.jetbrains.annotations.Nullable
    java.lang.Long productId, @org.jetbrains.annotations.Nullable
    java.lang.String productTitle) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDescription() {
        return null;
    }
    
    public final double component3() {
        return 0.0;
    }
    
    public final double getPrice() {
        return 0.0;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    public final double getOriginalPrice() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Date component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Date getStartDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Date component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Date getEndDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getContactPerson() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getContactPhone() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getTourGuide() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getTags() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getProductId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getProductTitle() {
        return null;
    }
}