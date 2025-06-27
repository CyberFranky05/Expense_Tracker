package com.example.expensetracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.Screen.AddExpense
import com.example.expensetracker.Screen.HomeScreen
import com.example.expensetracker.Screen.StatsScreen
import com.example.expensetracker.ui.theme.zinc

@Composable
fun NavHostScreen() {
    val navcontroller = rememberNavController()
    var bottomBarVisibiliyt by remember {
        mutableStateOf(true)
    }


    Scaffold (

            bottomBar = {
                AnimatedVisibility(visible = bottomBarVisibiliyt) {
                NavigationBottomBar(
                    navController = navcontroller,
                    items = listOf(
                        NavItem("/home", icon = R.drawable.ic_home),
                        NavItem(route = "/stats", icon = R.drawable.ic_stats)
                    )
                )
            }
        }
    ){
        NavHost(
            navController = navcontroller,
            startDestination = "/home",
            modifier = Modifier.padding(it)
        ) {
            composable(
                route = "/home"
            ) {
                bottomBarVisibiliyt = true
                HomeScreen(navcontroller)
            }

            composable(
                route = "/add"
            ) {
                bottomBarVisibiliyt=false
                AddExpense(navcontroller)
            }

            composable(
                route= "/stats"
            ) {
                bottomBarVisibiliyt=true
                StatsScreen(navcontroller)
            }

        }
    }


}



data class NavItem(
    val route:String,
    val icon: Int
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items:List<NavItem>
){
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar {
        items.forEach {
            item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                        restoreState = true

                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null
                    )

                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = zinc,
                    selectedIconColor = zinc,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                ),
            )
        }
    }

}