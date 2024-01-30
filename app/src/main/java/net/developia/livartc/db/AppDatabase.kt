package net.developia.livartc.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * 작업 내용: 룸데이터베이스 장바구니 목록 조회
 */

@Database(entities = [CartEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCartDao() : CartDao

    companion object {
        val databaseName = "db_cart"
        var appDatabase : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase? {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
            }
            return appDatabase
        }
    }
}