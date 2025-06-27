package com.example.expensetracker.Screen

import android.R.attr.data
import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.AndroidFont
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.room.util.TableInfo
import com.example.expensetracker.R
import com.example.expensetracker.ViewModel.StatsViewModel
import com.example.expensetracker.ViewModel.StatsViewModelFactory
import com.example.expensetracker.widget.ExpenseTextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlin.enums.EnumEntries

@Composable
fun StatsScreen(navController: NavController){
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    )

            ) {
                ExpenseTextView(
                    "Statistics",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(
                    painter = painterResource(R.drawable.dots_menu),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    colorFilter = ColorFilter.tint(Color.Black)

                )
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart),
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        }
    ) {
        val viewModel = StatsViewModelFactory(context = navController.context).create(StatsViewModel::class.java)
        val dataState = viewModel.etries.collectAsState(emptyList())
        Column(
            modifier = Modifier.padding(it)
        ) {
            val entries = viewModel.getEntriesForChart(dataState.value)
                LineChart(entries = entries)
        }
    }
}


@Composable
fun LineChart(entries: List<Entry>){

    val context = LocalContext.current

    AndroidView(
        factory = {
            val view = LayoutInflater.from(context).inflate(R.layout.stats_line_chart , null)
            view
        },
        modifier = Modifier){
        view->
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        val dataset = LineDataSet(entries,"Expenses").apply {
            color = android.graphics.Color.parseColor("#FF2F7E79")
            valueTextColor = android.graphics.Color.BLACK
            lineWidth=3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize =12f
            valueTextColor = android.graphics.Color.parseColor("#FF2F7E79")
        }

        lineChart.data = com.github.mikephil.charting.data.LineData(dataset)
        lineChart.invalidate()
    }


}