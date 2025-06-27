package com.example.expensetracker.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.Data.ExpenseDataBase
import com.example.expensetracker.Data.dao.ExpenseDao
import com.example.expensetracker.Data.model.ExpenseEntity
import com.example.expensetracker.Data.model.ExpenseSummary
import com.example.expensetracker.Utils
import com.github.mikephil.charting.data.Entry


class StatsViewModel(dao: ExpenseDao): ViewModel() {

    val etries = dao.getAllExpenseByDate()

    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry>{
        val list = mutableListOf<Entry>()
        for(entry in entries){
            val formattedDate = Utils.getMilliFromDate(entry.date)
            list.add(Entry(formattedDate.toFloat(),entry.total_amount.toFloat()))
        }
        return list
    }
}

class StatsViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(StatsViewModel::class.java)){
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}