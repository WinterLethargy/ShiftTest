package com.example.user.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.user.database.models.RemoteKeysDbModel
import com.example.user.database.models.UsersDbModel

@Database(
    entities = [UsersDbModel::class, RemoteKeysDbModel::class],
    version = 2,
    exportSchema = false
)
internal abstract class SHDataBase : RoomDatabase() {

    abstract fun usersDao(): UsersDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: SHDataBase? = null

        fun getInstance(context: Context): SHDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SHDataBase::class.java, "Shift.db"
            ).build()
    }
}