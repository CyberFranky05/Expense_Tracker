package com.example.expensetracker.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.Data.dao.ExpenseDao
import com.example.expensetracker.Data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDataBase : RoomDatabase(){
    abstract fun expenseDao(): ExpenseDao

    companion object{
        const val DATABASE_NAME = "expense_table"


        @JvmStatic
        fun getDatabase(context: Context): ExpenseDataBase{
            return Room.databaseBuilder(
                context,
                ExpenseDataBase::class.java,
                DATABASE_NAME
            )
                .addCallback(object : Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase){
                        super.onCreate(db)
                        InitBasicData(context)
                    }

                    fun InitBasicData(context: Context){
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = getDatabase(context).expenseDao()
                            dao.insertExpenses(ExpenseEntity(
                                id = 1,
                                title = "Salary",
                                amount = 5000.0,
                                date = System.currentTimeMillis().toString(),
                                category = "Salary" ,
                                type = "Income"
                            ))

                            dao.insertExpenses(ExpenseEntity(
                                id = 2,
                                title = "Paypal",
                                amount = 2000.0,
                                date = System.currentTimeMillis().toString(),
                                category = "Paypal" ,
                                type = "Income"
                            ))

                            dao.insertExpenses(ExpenseEntity(
                                id = 3,
                                title = "Netflix",
                                amount = 200.0,
                                date = System.currentTimeMillis().toString(),
                                category = "Netflix" ,
                                type = "Expense"
                            ))

                            dao.insertExpenses(ExpenseEntity(
                                id = 4,
                                title = "Starbucks",
                                amount = 50.0,
                                date = System.currentTimeMillis().toString(),
                                category = "Starbucks" ,
                                type = "Expense"
                            ))


                        }
                    }
                }
                )
                .build()
        }
    }
}