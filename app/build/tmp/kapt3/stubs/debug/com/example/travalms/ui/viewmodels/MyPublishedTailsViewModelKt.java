package com.example.travalms.ui.viewmodels;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.travalms.api.dto.TailOrderResponse;
import com.example.travalms.data.api.NetworkModule;
import com.example.travalms.data.remote.ConnectionState;
import com.example.travalms.data.remote.PubSubNotification;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.ui.model.PostItem;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import android.app.Application;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0002"}, d2 = {"TAG", "", "app_debug"})
public final class MyPublishedTailsViewModelKt {
    private static final java.lang.String TAG = "MyPublishedTailsVM";
}