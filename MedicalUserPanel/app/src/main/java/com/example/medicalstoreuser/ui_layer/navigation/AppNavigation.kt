package com.example.medicalstoreuser.ui_layer.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import com.example.medicalstoreuser.ui_layer.AppViewModel
import com.example.medicalstoreuser.ui_layer.Screens.*
import com.example.medicalstoreuser.user_pref.UserPreferenceManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.DelicateCoroutinesApi

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun AppNavigation(userPreferenceManager: UserPreferenceManager) {
    val navController = rememberNavController()
    val viewModel: AppViewModel = hiltViewModel()
    var selectedTab by remember { mutableIntStateOf(0) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val shouldShowBottomBar = remember { mutableStateOf(true) }

    val firebaseUser = FirebaseAuth.getInstance().currentUser

    val bottomNavColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )

    LaunchedEffect(currentRoute) {
        shouldShowBottomBar.value = when (currentRoute) {
            Routes.LogInScreen::class.qualifiedName,
            Routes.SignUpScreen::class.qualifiedName,
            Routes.StartScreen::class.qualifiedName -> false
            else -> true
        }

        // Update selected tab based on current route
        selectedTab = when (currentRoute) {
            Routes.HomeScreen::class.qualifiedName -> 0
            Routes.SearchScreen::class.qualifiedName -> 1
            Routes.CartList::class.qualifiedName -> 2
            Routes.Profile::class.qualifiedName -> 3
            else -> selectedTab
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Rounded.Home, Routes.HomeScreen),
        BottomNavItem("Search", Icons.Rounded.Search, Routes.SearchScreen),
        BottomNavItem("Orders", Icons.Rounded.ShoppingCart, Routes.CartList),
        BottomNavItem("Profile", Icons.Rounded.Person,
            Routes.Profile(
                name = firebaseUser?.displayName ?: "Unknown",
                email = firebaseUser?.email ?: "No Email",
                phoneNumber = firebaseUser?.phoneNumber ?: "N/A",
                address = "Demo Address",
                pinCode = "000000"
            )
        )
    )

    Surface(color = MaterialTheme.colorScheme.background) {
        Scaffold(
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                AnimatedVisibility(
                    visible = shouldShowBottomBar.value,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        bottomNavItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedTab == index,
                                onClick = {
                                    selectedTab = index
                                    navController.navigate(item.route) {
                                        popUpTo(Routes.HomeScreen) { saveState = true }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.name,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    )
                                },
                                colors = bottomNavColors
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = if (firebaseUser == null) Routes.LogInScreen else Routes.HomeScreen
                ) {
                    composable<Routes.StartScreen> { StartScreen(navController) }
                    composable<Routes.LogInScreen> { LogInScreenUI(navController, viewModel) }
                    composable<Routes.SignUpScreen> { SignUpScreenUI(navController, viewModel) }
                    composable<Routes.HomeScreen> { HomeScreenUI(navController) }
                    composable<Routes.CartList> { OrderHistoryUI(navController) }
                    composable<Routes.OrderHistory> { OrderHistoryUI(navController) }
                    composable<Routes.Profile> { ProfileUI(navController) }
                    composable<Routes.ProductDetailsScreen> {
                        val data = it.toRoute<Routes.ProductDetailsScreen>()
                        ProductDetailsScreenUI(data.product_id, navController)
                    }
                    composable<Routes.Categories> { CategoriesUI(navController) }
                    composable<Routes.SearchScreen> { SearchScreenUI(navController = navController) }
                    composable<Routes.OrderDetailsScreen> {
                        val data = it.toRoute<Routes.OrderDetailsScreen>()
                        OrderDetailsScreenUI(data.product_id, navController, userPreferenceManager)
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val name: String,
    val icon: ImageVector,
    val route: Any
)