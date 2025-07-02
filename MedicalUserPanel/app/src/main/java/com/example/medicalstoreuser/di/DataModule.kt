package com.example.medicalstoreuser.di

import android.content.Context
import androidx.room.Room
import com.example.medicalstoreuser.data_layer.Dao.AppDatabase
import com.example.medicalstoreuser.data_layer.Dao.UserDao
import com.example.medicalstoreuser.repo.Repo
import com.example.medicalstoreuser.user_pref.UserPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideRepository() = Repo()

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideUserPreferenceManager(@ApplicationContext context: Context) = UserPreferenceManager(context=context)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}

