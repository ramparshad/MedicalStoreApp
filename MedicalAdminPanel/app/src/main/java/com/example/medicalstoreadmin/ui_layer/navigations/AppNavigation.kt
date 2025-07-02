package com.example.medicalstoreadmin.ui_layer.navigations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ProductionQuantityLimits
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.medicalstoreadmin.MyApp
import com.example.medicalstoreadmin.ui_layer.screens.AddProductScreenUI
import com.example.medicalstoreadmin.ui_layer.screens.HomeScreenUI
import com.example.medicalstoreadmin.ui_layer.screens.OrderDetailsScreenUI
import com.example.medicalstoreadmin.ui_layer.screens.OrderScreenUI
import com.example.medicalstoreadmin.ui_layer.screens.StockScreenUI
import com.example.medicalstoreadmin.ui_layer.screens.UserDetailsScreenUI

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    val items = listOf(

        BottomNavigationItems("Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavigationItems("Add", Icons.Filled.AddBox, Icons.Outlined.AddBox),
        BottomNavigationItems("Orders", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
        BottomNavigationItems(
            "Product",
            Icons.Filled.ProductionQuantityLimits,
            Icons.Outlined.ProductionQuantityLimits
        )

    )


    var selectedItemIndex = remember { mutableIntStateOf(0) }

    val shouldShowBottomBar = remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomBar.value) {
                NavigationBar {
                    items.forEachIndexed { index, navigationItem ->
                        NavigationBarItem(
                            selected = selectedItemIndex.intValue == index,
                            onClick = {
                                selectedItemIndex.intValue = index

                                when (selectedItemIndex.intValue) {
                                    0 -> navController.navigate(Routes.HomeScreen)
                                    1 -> navController.navigate(Routes.AddProductScreen)
                                    2 -> navController.navigate(Routes.OrdersScreen)
                                    3 -> navController.navigate(Routes.ProductScreen)
                                }
                            },
                            icon = {
                                Icon(
                                    if (selectedItemIndex.intValue == index) navigationItem.selectedIcon else navigationItem.unselectedIcon,
                                    contentDescription = navigationItem.title
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        Box(modifier = modifier.fillMaxSize().padding(innerPadding)) {


            NavHost(navController = navController, startDestination = Routes.HomeScreen) {

                composable< Routes.HomeScreen> {
                    HomeScreenUI(navController)
                }

                composable< Routes.UserDetailScreen> {
                    UserDetailsScreenUI(navController)
                }

                composable< Routes.AddProductScreen> {
                    AddProductScreenUI(navController)
                }

                composable< Routes.ProductScreen> {
                    StockScreenUI(navController)
                }

                composable<Routes.OrdersScreen>{
                    OrderScreenUI(navController)
                }

                composable<Routes.OrderDetailScreen>{
                    val data = it.toRoute<Routes.OrderDetailScreen>()
                    OrderDetailsScreenUI(orderID = data.orderID , userID = data.userID.toString(), category = data.category , orderDate = data.orderDate , productID = data.productID , isApproved = data.isApproved , productExpiryDate = data.productExpiryDate , productQuantity = data.productQuantity , totalPrice = data.totalPrice )
                }

                composable<Routes.CheckOutScreen>{
                    val data = it.toRoute<Routes.CheckOutScreen>()
                    MyApp(userID = data.userID, value = data.value, navController = navController)
                }
            }
        }
    }
}

data class BottomNavigationItems(val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector)