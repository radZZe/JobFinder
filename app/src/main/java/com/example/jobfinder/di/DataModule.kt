package com.example.jobfinder.di

import android.content.Context
import android.content.SharedPreferences
import com.example.jobfinder.utils.KEY_PREFERENCSES
import com.example.jobfinder.utils.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun providePreferenceManager(sharedPreferences: SharedPreferences):PreferenceManager{
        return  PreferenceManager(sharedPreferences)

    }

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context):SharedPreferences {
        return context.getSharedPreferences(KEY_PREFERENCSES, Context.MODE_PRIVATE)
    }
}