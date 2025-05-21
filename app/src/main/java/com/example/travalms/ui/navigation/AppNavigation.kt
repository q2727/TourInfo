package com.example.travalms.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.travalms.ui.auth.LoginScreen
import com.example.travalms.ui.publish.PublishScreen
import com.example.travalms.ui.auth.RegisterScreen
import com.example.travalms.ui.message.MessageScreen
import com.example.travalms.ui.screens.HomeScreen
import com.example.travalms.ui.screens.MyPostsScreen
import com.example.travalms.ui.screens.SubscribeSettingScreen
import com.example.travalms.ui.screens.ProfileScreen
import com.example.travalms.ui.screens.ProfileEditScreen
import com.example.travalms.ui.screens.VerificationScreen
import com.example.travalms.ui.screens.PostDetailScreen
import com.example.travalms.ui.screens.CompanyDetailScreen
import com.example.travalms.ui.screens.PersonDetailScreen
import com.example.travalms.ui.screens.ChatRoomScreen
import com.example.travalms.ui.screens.MessageListScreen
import com.example.travalms.ui.screens.MyFavoritesScreen
import com.example.travalms.ui.screens.TailListScreen
import com.example.travalms.ui.publish.PublishNodeSelectorScreen
import com.example.travalms.ui.screens.CreateGroupScreen
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.example.travalms.ui.screens.GroupChatScreen
import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.travalms.ui.screens.EditProfileScreen
import com.example.travalms.ui.screens.TailOrderDetailScreen
import com.example.travalms.ui.screens.FriendDetailScreen
import com.example.travalms.ui.screens.MyTailOrderDetailScreen

/**
 * 应用导航路由常量
 */
object AppRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val TAIL_LIST = "tailList"
    const val TAIL_ORDER_DETAIL = "tailOrderDetail/{tailOrderId}"
    const val MY_TAIL_ORDER_DETAIL = "myTailOrderDetail/{tailOrderId}"
    const val POST_DETAIL = "postDetail/{postId}"
    const val POST_EDIT = "postEdit/{postId}"
    const val MESSAGE = "message"
    const val GROUP_CHAT = "group_chat/{groupJid}"
    const val CHAT_ROOM = "chat_room/{sessionId}/{targetName}/{targetType}"
    const val PUBLISH = "publish"
    const val PUBLISH_NODE_SELECTOR = "publishNodeSelector"
    const val MY_POSTS = "myPosts"
    const val MY_FAVORITES = "myFavorites"
    const val PRODUCT_DETAIL = "productDetail/{productId}"
    const val SEARCH = "search"
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profileEdit"
    const val VERIFICATION = "verification"
    const val COMPANY_BINDING = "companyBinding"
    const val COMPANY_DETAIL = "companyDetail/{companyId}"
    const val PERSON_DETAIL = "personDetail/{personId}"
    const val CREATE_GROUP = "createGroup"
    const val SUBSCRIBE_SETTING = "subscribeSetting"
    const val FRIEND_DETAIL = "friendDetail/{username}"
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
                onPublishClick = { /* 已在我的发布页面，不执行导航 */ },
                onMessageClick = { navController.navigate(AppRoutes.MESSAGE) },
                onProfileClick = { navController.navigate(AppRoutes.PROFILE) },
                onTailListClick = navigateToTailList,
                onPublishNewClick = {
                    navController.navigate(AppRoutes.PUBLISH)
                },
                onEditPost = { /* 编辑功能已移除 */ },
                onTailOrderClick = { post ->
                    navController.navigate(AppRoutes.MY_TAIL_ORDER_DETAIL.replace("{tailOrderId}", post.id.toString()))
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

        // 发布节点选择页面
        composable(AppRoutes.PUBLISH_NODE_SELECTOR) {
            PublishNodeSelectorScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 消息页面
        composable(AppRoutes.MESSAGE) {
            MessageScreen(
                navController = navController,
                onHomeClick = { navController.navigate(AppRoutes.HOME) },
                onPublishClick = navigateToMyPosts,
                onTailListClick = navigateToTailList,
                onProfileClick = { navController.navigate(AppRoutes.PROFILE) }
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
            // 获取上下文以便退出登录时使用
            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            ProfileScreen(
                onLogoutClick = {
                    // 调用XMPPManager的logout函数进行退出登录
                    scope.launch {
                        // 调用logout函数清除凭据和断开连接
                        try {
                            XMPPManager.getInstance().logout(context)
                        } catch (e: Exception) {
                            // 即使logout失败也继续导航到登录页面
                            e.printStackTrace()
                        }

                        // 退出登录后，导航到登录页面
                        navController.navigate(AppRoutes.LOGIN) {
                            // 清除整个回退栈，防止用户返回到需要登录的页面
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
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

        // 个人资料编辑页面
        composable(AppRoutes.PROFILE_EDIT) {
            EditProfileScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
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
                    onChatClick = { username ->
                        // 导航到好友详情页面
                        navController.navigate(AppRoutes.FRIEND_DETAIL.replace("{username}", username))
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
            // 已移除 PostEditScreen
            Text("编辑页面已移除")
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
                onChatClick = { username ->
                    // 导航到好友详情页面
                    navController.navigate(AppRoutes.FRIEND_DETAIL.replace("{username}", username))
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
                onTailOrderClick = { tailOrder ->
                    navController.navigate(AppRoutes.TAIL_ORDER_DETAIL.replace("{tailOrderId}", tailOrder.id.toString()))
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

        // 添加创建群聊的路由目标
        composable(AppRoutes.CREATE_GROUP) {
            // 将占位符替换为我们的CreateGroupScreen
            CreateGroupScreen(navController = navController)
        }

        // 添加群聊页面路由目标
        composable(
            route = AppRoutes.GROUP_CHAT,
            arguments = listOf(navArgument("roomJid") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomJid = backStackEntry.arguments?.getString("roomJid") ?: ""
            GroupChatScreen(navController = navController, roomJid = roomJid)
        }

        // 添加尾单详情页面
        composable(
            route = AppRoutes.TAIL_ORDER_DETAIL,
            arguments = listOf(navArgument("tailOrderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tailOrderId = backStackEntry.arguments?.getString("tailOrderId") ?: "0"
            TailOrderDetailScreen(
                tailOrderId = tailOrderId,
                onBack = { navController.navigateUp() },
                onChatRoomClick = { roomId ->
                    navController.navigate("${AppRoutes.GROUP_CHAT.replaceBefore("/", "")}$roomId")
                },
                onFriendDetailClick = { username ->
                    navController.navigate("${AppRoutes.FRIEND_DETAIL.replaceBefore("/", "")}$username")
                }
            )
        }

        // 我的尾单详情 - 精简版尾单详情界面
        composable(
            route = AppRoutes.MY_TAIL_ORDER_DETAIL,
            arguments = listOf(navArgument("tailOrderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tailOrderId = backStackEntry.arguments?.getString("tailOrderId") ?: "0"
            MyTailOrderDetailScreen(
                tailOrderId = tailOrderId,
                onBack = { navController.navigateUp() },
                onDeleted = {
                    navController.navigateUp()
                    // 显示删除成功提示
                    showSnackbar("尾单已删除")
                }
            )
        }

        // 添加好友详情页面
        composable(
            route = AppRoutes.FRIEND_DETAIL,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            FriendDetailScreen(
                username = username,
                onBack = { navController.popBackStack() },
                onSendMessage = { targetUsername, targetNickname ->
                    // 导航到聊天界面
                    navController.navigate(
                        AppRoutes.CHAT_ROOM
                            .replace("{sessionId}", targetUsername)
                            .replace("{targetName}", targetNickname)
                            .replace("{targetType}", "person")
                    )
                }
            )
        }
    }
}

/**
 * 显示Snackbar消息
 * 由于这是一个私有函数，只在AppNavigation内使用
 */
private fun showSnackbar(message: String) {
    // 由于这个函数是直接在NavHost中调用的，无法访问SnackbarHostState
    // 实际使用时，我们会改为在Composable中调用SnackbarHostState.showSnackbar
    // 这里留空，在相应屏幕中实现实际的Snackbar逻辑
} 