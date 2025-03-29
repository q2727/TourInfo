package com.example.travalms.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travalms.ui.screens.LoginScreen
import com.example.travalms.ui.screens.RegisterScreen

// 定义应用中的路由路径
object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
}

/**
 * 应用导航组件
 * 管理不同屏幕之间的导航
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        // 登录屏幕
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(AppRoutes.REGISTER)
                }
            )
        }
        
        // 注册屏幕
        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }
    }
} 