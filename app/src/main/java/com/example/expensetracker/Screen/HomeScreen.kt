package com.example.expensetracker.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.Data.model.ExpenseEntity
import com.example.expensetracker.R
import com.example.expensetracker.ViewModel.HomeViewModel
import com.example.expensetracker.ViewModel.HomeViewModelFactory
import com.example.expensetracker.ui.theme.zinc
import com.example.expensetracker.widget.ExpenseTextView

@Composable
fun HomeScreen(navController: NavController){
    val viewModel: HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val(nameRow,list,card,topBar,add) = createRefs()
            Image(painter = painterResource(R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top =64.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .constrainAs (
                        nameRow
                    ){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }

            ){
                Column {
                    ExpenseTextView(
                        text = "Good Afternoon",
                        fontSize = 16.sp,
                        color =  Color.White
                    )
                    ExpenseTextView(
                        "Mr.electron",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color =  Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                )
            }

            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expenses = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)

            CardItem(
                modifier = Modifier
                    .constrainAs(card) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                balance,
                income,
                expenses
            )

            TransactionList(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(
                    list
                ){
                    top.linkTo(card.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
                list = state.value,
                viewModel= viewModel
            )

            Image(
                painter = painterResource(R.drawable.ic_addbutton),
                contentDescription = null,
                modifier = Modifier.constrainAs (add){
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable{navController.navigate("/add")}
            )

        }
    }
}


@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expenses: String){
    Column(
        modifier=modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(zinc)
            .padding(16.dp)
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .weight(1f)
        ){
            Column {
                ExpenseTextView(
                    text = "Total Balance",
                    fontSize = 16.sp,
                    color = Color.White)
                ExpenseTextView(
                    text = balance,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd),
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        ){

            CardRowItem(
                modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image = R.drawable.ic_income,
            )

            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expenses,
                image = R.drawable.ic_expense,
            )

        }

    }

}


@Composable
fun TransactionList(modifier: Modifier, list: List<ExpenseEntity>, viewModel: HomeViewModel){
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
    ){
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                ExpenseTextView (text = "Recent Transactions", fontSize = 20.sp)
                ExpenseTextView(
                    text = "See All",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }

         items(list){
             item->
             TransactionItem(
                 title = item.title,
                 amount = item.amount.toString(),
                 icon = viewModel.getItemIncome(item),
                 date = item.date.toString(),
                 color = if(item.type == "Income" ) Color.Green else Color.Red

             )
         }
    }

}


@Composable
fun TransactionItem(title: String, amount: String,  icon: Int, date: String, color: Color){
    Box(modifier = Modifier.fillMaxWidth()
        .padding(vertical = 8.dp)){
        Row{
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))
            Column {
                ExpenseTextView (text = title, fontSize =  16.sp, fontWeight = FontWeight.Bold)
                ExpenseTextView(text= date , fontSize = 12.sp)
            }
        }
        ExpenseTextView(
            text = amount,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun CardRowItem(modifier: Modifier,title: String , amount: String , image:Int){
    Column(
        modifier = modifier
    ) {
        Row{
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseTextView(text = title , fontSize = 16.sp, color = Color.White)
        }
        ExpenseTextView(text = amount, fontSize = 20.sp, color = Color.White)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview(){
    MaterialTheme {
        HomeScreen(rememberNavController())
    }
}