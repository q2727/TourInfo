package com.example.travalms.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.PasswordVisualTransformation;
import androidx.compose.ui.text.input.VisualTransformation;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.ui.text.TextStyle;
import com.example.travalms.ui.viewmodels.ProfileEditViewModel;
import com.example.travalms.ui.viewmodels.ProfileEditUiState;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000N\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001a\b\u0010\t\u001a\u00020\nH\u0007\u001am\u0010\u000b\u001a\u00020\n2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\n0\r2/\u0010\u000e\u001a+\u0012!\u0012\u001f\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u00a2\u0006\f\b\u0010\u0012\b\b\u0011\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\n0\u000f2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\n0\r2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\n0\r2\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0007\u001aT\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u00022\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\n0\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u00022\b\b\u0002\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010 \u001a\u00020!H\u0007\u001a4\u0010\"\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u00022\b\b\u0002\u0010#\u001a\u00020\u001d2\u0010\b\u0002\u0010$\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\rH\u0007\u001a0\u0010%\u001a\u00020\n2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\n0\r2\u0018\u0010\'\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\n0(H\u0007\"#\u0010\u0000\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u00030\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006)"}, d2 = {"provinceCityData", "", "", "", "getProvinceCityData", "()Ljava/util/Map;", "provinces", "getProvinces", "()Ljava/util/List;", "ListDivider", "", "ProfileEditScreen", "onBackClick", "Lkotlin/Function0;", "onSaveClick", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "updatedInfo", "onBindCompanyClick", "onVerificationClick", "profileEditViewModel", "Lcom/example/travalms/ui/viewmodels/ProfileEditViewModel;", "ProfileEditableRow", "label", "value", "onValueChange", "placeholder", "singleLine", "", "keyboardOptions", "Landroidx/compose/foundation/text/KeyboardOptions;", "visualTransformation", "Landroidx/compose/ui/text/input/VisualTransformation;", "ProfileInfoRow", "editable", "onClick", "SimplePasswordChangeDialog", "onDismiss", "onConfirm", "Lkotlin/Function2;", "app_debug"})
public final class ProfileEditScreenKt {
    @org.jetbrains.annotations.NotNull
    private static final java.util.Map<java.lang.String, java.util.List<java.lang.String>> provinceCityData = null;
    @org.jetbrains.annotations.NotNull
    private static final java.util.List<java.lang.String> provinces = null;
    
    @org.jetbrains.annotations.NotNull
    public static final java.util.Map<java.lang.String, java.util.List<java.lang.String>> getProvinceCityData() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final java.util.List<java.lang.String> getProvinces() {
        return null;
    }
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void ProfileEditScreen(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.util.Map<java.lang.String, java.lang.String>, kotlin.Unit> onSaveClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBindCompanyClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onVerificationClick, @org.jetbrains.annotations.NotNull
    com.example.travalms.ui.viewmodels.ProfileEditViewModel profileEditViewModel) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void ProfileInfoRow(@org.jetbrains.annotations.NotNull
    java.lang.String label, @org.jetbrains.annotations.NotNull
    java.lang.String value, boolean editable, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void ProfileEditableRow(@org.jetbrains.annotations.NotNull
    java.lang.String label, @org.jetbrains.annotations.NotNull
    java.lang.String value, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, @org.jetbrains.annotations.NotNull
    java.lang.String placeholder, boolean singleLine, @org.jetbrains.annotations.NotNull
    androidx.compose.foundation.text.KeyboardOptions keyboardOptions, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.text.input.VisualTransformation visualTransformation) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void ListDivider() {
    }
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void SimplePasswordChangeDialog(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onConfirm) {
    }
}