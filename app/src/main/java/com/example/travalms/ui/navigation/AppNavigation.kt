package com.example.travalms.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.travalms.ui.screens.HomeScreen
import com.example.travalms.ui.screens.LoginScreen
import com.example.travalms.ui.screens.MyPostsScreen
import com.example.travalms.ui.screens.PublishScreen
import com.example.travalms.ui.screens.RegisterScreen
import com.example.travalms.ui.screens.MessageScreen
import com.example.travalms.ui.screens.SubscribeSettingScreen
import com.example.travalms.ui.screens.ProfileScreen
import com.example.travalms.ui.screens.ProfileEditScreen
import com.example.travalms.ui.screens.CompanyBindingScreen
import com.example.travalms.ui.screens.VerificationScreen
import com.example.travalms.ui.screens.CompanyRegisterScreen
import com.example.travalms.ui.screens.PostDetailScreen
import com.example.travalms.ui.screens.PostEditScreen
import com.example.travalms.ui.screens.CompanyDetailScreen
import com.example.travalms.ui.screens.PersonDetailScreen
import com.example.travalms.ui.screens.ChatRoomScreen
import com.example.travalms.ui.screens.MessageListScreen
import com.example.travalms.ui.screens.MyFavoritesScreen
import com.example.travalms.ui.screens.TailListScreen

// 定义应用中的路由路径
object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val MY_POSTS = "my_posts"
    const val PUBLISH = "publish"
    const val MESSAGE = "message"
    const val SUBSCRIBE_SETTING = "subscribe_setting"
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile_edit"
    const val COMPANY_BINDING = "company_binding"
    const val VERIFICATION = "verification"
    const val COMPANY_REGISTER = "company_register"
    const val POST_DETAIL = "post_detail/{postId}"
    const val POST_EDIT = "post_edit/{postId}"
    const val COMPANY_DETAIL = "company_detail/{companyId}"
    const val PERSON_DETAIL = "person_detail/{personId}"
    const val CHAT_ROOM = "chat_room/{sessionId}/{targetName}/{targetType}"
    const val MESSAGE_LIST = "message_list"
    const val MY_FAVORITES = "my_favorites"
    const val TAIL_LIST = "tail_list"
}

/**
 * 应用导航组件
 * 管理不同屏幕之间的导航
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppRoutes.LOGIN
) {
    // 获取当前路由
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    
    // 为所有屏幕设置一致的导航逻辑
    val navigateToMyPosts = {
        if ((currentRoute ?: "") != AppRoutes.MY_POSTS) {
            navController.navigate(AppRoutes.MY_POSTS) {
                // 避免重复创建页面
                launchSingleTop = true
            }
        }
    }

    val navigateToTailList = {
        if ((currentRoute ?: "") != AppRoutes.TAIL_LIST) {
            navController.navigate(AppRoutes.TAIL_LIST) {
                launchSingleTop = true
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 登录屏幕
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onRegisterClick = { navController.navigate(AppRoutes.REGISTER) },
                onLoginSuccess = { navController.navigate(AppRoutes.HOME) {
                    // 清除回退栈，防止用户返回登录页
                    popUpTo(AppRoutes.LOGIN) { inclusive = true }
                }}
            )
        }
        
        // 注册屏幕
        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onLoginClick = { navController.navigate(AppRoutes.LOGIN) }
            )
        }
        
        // 首页
        composable(AppRoutes.HOME) {
            HomeScreen(
                onItemClick = { postId -> 
                    navController.navigate(AppRoutes.POST_DETAIL.replace("{postId}", postId.toString()))
                },
                onPublishClick = navigateToMyPosts,
                onMessageClick = { navController.navigate(AppRoutes.MESSAGE) },
                onProfileClick = { navController.navigate(AppRoutes.PROFILE) },
                onTailListClick = navigateToTailList,
                onCompanyClick = { companyId ->
                    navController.navigate(AppRoutes.COMPANY_DETAIL.replace("{companyId}", companyId))
                },
                navController = navController
            )
        }
        
        // 我的帖子页面
        composable(AppRoutes.MY_POSTS) {
            MyPostsScreen(
                onBackClick = { navController.popBackStack() },
                onHomeClick = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                // 已经在我的发布页面，点击不做导航
                onPublishClick = { /* 已在我的发布页面，不执行导航 */ },
                onMessageClick = { navController.navigate(AppRoutes.MESSAGE) },
                onProfileClick = { navController.navigate(AppRoutes.PROFILE) },
                onTailListClick = navigateToTailList,
                onItemClick = { post -> 
                    navController.navigate(AppRoutes.POST_EDIT.replace("{postId}", post.id.toString()))
                },
                // 只有点击"发布新信息"按钮才导航到发布页面
                onPublishNewClick = { 
                    navController.navigate(AppRoutes.PUBLISH)
                }
            )
        }
        
        // 发布页面
        composable(AppRoutes.PUBLISH) {
            PublishScreen(
                onBackClick = { navController.popBackStack() },
                onHomeClick = { 
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                onMessageClick = { navController.navigate(AppRoutes.MESSAGE) },
                onProfileClick = { navController.navigate(AppRoutes.PROFILE) },
                navController = navController
            )
        }
        
        // 消息页面
        composable(AppRoutes.MESSAGE) {
            MessageListScreen(
                onSessionClick = { sessionId ->
                    // 这里简单处理，实际应该根据sessionId查询目标信息
                    val targetName = when(sessionId) {
                        "1" -> "上海旅行社"
                        "2" -> "曾圆圆"
                        "3" -> "北京导游协会"
                        else -> "聊天对象"
                    }
                    val targetType = when(sessionId) {
                        "2" -> "person"
                        else -> "company"
                    }
                    
                    navController.navigate(
                        AppRoutes.CHAT_ROOM
                            .replace("{sessionId}", sessionId)
                            .replace("{targetName}", targetName)
                            .replace("{targetType}", targetType)
                    )
                },
                onNotificationClick = { postId ->
                    // 处理推送消息点击，跳转到相应的帖子/景点详情页
                    navController.navigate("post_detail/$postId")
                },
                onHomeClick = { navController.navigate(AppRoutes.HOME) },
                onPublishClick = navigateToMyPosts,
                onProfileClick = { navController.navigate(AppRoutes.PROFILE) },
                onTailListClick = navigateToTailList,
                navController = navController
            )
        }
        
        // 订阅设置页面
        composable(AppRoutes.SUBSCRIBE_SETTING) {
            SubscribeSettingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // 个人主页
        composable(AppRoutes.PROFILE) {
            ProfileScreen(
                onLogoutClick = {
                    // 退出登录，返回登录页面
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                onProfileEditClick = { navController.navigate(AppRoutes.PROFILE_EDIT) },
                onVerificationClick = { navController.navigate(AppRoutes.VERIFICATION) },
                onBusinessCertClick = { navController.navigate(AppRoutes.COMPANY_BINDING) },
                onHomeClick = { 
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                onPublishClick = navigateToMyPosts,
                onMessageClick = { navController.navigate(AppRoutes.MESSAGE) },
                onTailListClick = navigateToTailList,
                navController = navController
            )
        }
        // Profile Edit Screen
        composable(AppRoutes.PROFILE_EDIT) {
            ProfileEditScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { 
                    // Save profile changes
                    // You could add logic to update user profile data here
                    navController.popBackStack() 
                },
                onBindCompanyClick = { 
                    navController.navigate(AppRoutes.COMPANY_BINDING) 
                },
                onVerificationClick = {
                    navController.navigate(AppRoutes.VERIFICATION)
                }
            )
        }
        
        // 企业绑定页面
        composable(AppRoutes.COMPANY_BINDING) {
            CompanyBindingScreen(
                onBackClick = { navController.popBackStack() },
                onCompanySelect = { company ->
                    // 处理企业选择，这里可以先返回编辑页面
                    navController.popBackStack()
                },
                onRegisterCompany = {
                    // 企业注册流程，导航到注册页面
                    navController.navigate(AppRoutes.COMPANY_REGISTER)
                }
            )
        }
        
        // 企业注册页面
        composable(AppRoutes.COMPANY_REGISTER) {
            CompanyRegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSubmit = {
                    // 处理注册表单提交
                    // 这里可以添加表单验证和提交逻辑
                    // 提交后返回到个人中心页面
                    navController.navigate(AppRoutes.PROFILE) {
                        popUpTo(AppRoutes.COMPANY_REGISTER) { inclusive = true }
                    }
                }
            )
        }
        
        // 实名认证页面
        composable(AppRoutes.VERIFICATION) {
            VerificationScreen(
                onBackClick = { navController.popBackStack() },
                onVerificationComplete = {
                    // 认证完成后返回编辑页面
                    navController.popBackStack()
                }
            )
        }
        
        // 信息详情页面
        composable(
            route = AppRoutes.POST_DETAIL,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")
            if (postId != null) {
                PostDetailScreen(
                    postId = postId,
                    onBackClick = { navController.popBackStack() },
                    onChatClick = {
                        // 导航到与企业的聊天室
                        navController.navigate(
                            AppRoutes.CHAT_ROOM
                                .replace("{sessionId}", "1")  // 使用"上海旅行社"的会话ID
                                .replace("{targetName}", "上海旅行社")
                                .replace("{targetType}", "company")
                        )
                    },
                    onCompanyClick = { companyId ->
                        navController.navigate(AppRoutes.COMPANY_DETAIL.replace("{companyId}", companyId))
                    },
                    onPersonClick = { personId ->
                        navController.navigate(AppRoutes.PERSON_DETAIL.replace("{personId}", personId))
                    }
                )
            }
        }

        // 帖子编辑页面
        composable(
            route = AppRoutes.POST_EDIT,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")
            PostEditScreen(
                postId = postId,
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { 
                    // 保存成功后返回到我的帖子页面
                    navController.navigate(AppRoutes.MY_POSTS) {
                        popUpTo(AppRoutes.POST_EDIT) { inclusive = true }
                    }
                }
            )
        }

        // 企业详情页面
        composable(
            route = AppRoutes.COMPANY_DETAIL,
            arguments = listOf(navArgument("companyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val companyId = backStackEntry.arguments?.getString("companyId")
            CompanyDetailScreen(
                companyId = companyId,
                onBackClick = { navController.popBackStack() },
                onChatClick = { 
                    // 根据公司ID选择合适的会话ID
                    val sessionId = when(companyId) {
                        "上海旅行社" -> "1"
                        "彩云旅游社" -> "3" 
                        else -> companyId ?: "1"
                    }
                    navController.navigate(
                        AppRoutes.CHAT_ROOM
                            .replace("{sessionId}", sessionId)
                            .replace("{targetName}", companyId ?: "公司")
                            .replace("{targetType}", "company")
                    )
                }
            )
        }

        // 个人详情页面
        composable(
            route = AppRoutes.PERSON_DETAIL,
            arguments = listOf(navArgument("personId") { type = NavType.StringType })
        ) { backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId")
            PersonDetailScreen(
                personId = personId,
                onBackClick = { navController.popBackStack() },
                onChatClick = { 
                    navController.navigate(
                        AppRoutes.CHAT_ROOM
                            .replace("{sessionId}", "2")  // 使用曾圆圆的会话ID
                            .replace("{targetName}", personId ?: "用户")
                            .replace("{targetType}", "person")
                    )
                },
                onCompanyClick = { companyId ->
                    navController.navigate(AppRoutes.COMPANY_DETAIL.replace("{companyId}", companyId))
                }
            )
        }

        // 聊天室页面
        composable(
            route = AppRoutes.CHAT_ROOM,
            arguments = listOf(
                navArgument("sessionId") { type = NavType.StringType },
                navArgument("targetName") { type = NavType.StringType },
                navArgument("targetType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            val targetName = backStackEntry.arguments?.getString("targetName") ?: ""
            val targetType = backStackEntry.arguments?.getString("targetType") ?: "company"
            
            ChatRoomScreen(
                sessionId = sessionId,
                targetName = targetName,
                targetType = targetType,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 我的收藏页面
        composable(AppRoutes.MY_FAVORITES) {
            MyFavoritesScreen(
                onBackClick = { navController.popBackStack() },
                onItemClick = { postId -> 
                    navController.navigate(AppRoutes.POST_DETAIL.replace("{postId}", postId))
                }
            )
        }

        // 尾单页面
        composable(AppRoutes.TAIL_LIST) {
            TailListScreen(
                onItemClick = { postId -> 
                    navController.navigate(AppRoutes.POST_DETAIL.replace("{postId}", postId.toString()))
                },
                onHomeClick = { 
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                onPublishClick = navigateToMyPosts,
                onMessageClick = { navController.navigate(AppRoutes.MESSAGE) },
                onProfileClick = { navController.navigate(AppRoutes.PROFILE) },
                onCompanyClick = { companyId ->
                    navController.navigate(AppRoutes.COMPANY_DETAIL.replace("{companyId}", companyId))
                },
                onContactClick = { phoneNumber ->
                    // 这里可以添加拨打电话的意图
                },
                onPersonClick = { personId ->
                    navController.navigate(AppRoutes.PERSON_DETAIL.replace("{personId}", personId))
                },
                onReportItem = { itemId, reason ->
                    // 处理举报逻辑，包含举报原因
                },
                onDeleteItem = { itemId ->
                    // 处理删除逻辑
                },
                navController = navController
            )
        }
    }
} 