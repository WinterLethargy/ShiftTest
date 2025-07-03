package com.example.user.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): SHDataBase {
        return Room.databaseBuilder(
            context,
            SHDataBase::class.java,
            "simbirsoft_database"
        ).build()
    }

    @Provides
    internal fun provideUserDao(database: SHDataBase): UsersDao {
        return database.usersDao()
    }

    @Provides
    internal fun provideRemoteKeyDao(database: SHDataBase): RemoteKeysDao {
        return database.remoteKeysDao()
    }
}