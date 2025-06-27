package com.example.expensetracker.Data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.expensetracker.Data.model.ExpenseEntity
import com.example.expensetracker.Data.model.ExpenseSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense_table ")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT type , date , SUM(amount) AS total_amount From expense_table where type =:type GROUP BY type , date ORDER BY date")
    fun getAllExpenseByDate(type:String ="Expense"): Flow<List<ExpenseSummary>>
    @Insert
    suspend fun insertExpenses(expenseEntity: ExpenseEntity)

    @Delete
    suspend fun deleteExpenses(expenseEntity: ExpenseEntity)

    @Update
    suspend fun updateExpenses(expenseEntity: ExpenseEntity)
}