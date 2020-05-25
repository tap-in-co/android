package com.tapin.tapin.viewmodels

import android.content.Context
import com.tapin.tapin.App

object AppViewModelProvider {
    fun provideSplashViewModelFactory(context: Context): SplashViewModelFactory {
        val app: App = context.applicationContext as App

        return SplashViewModelFactory(app.api)
    }

    fun provideMarketViewModelFactory(context: Context): MarketViewModelFactory {
        val app: App = context.applicationContext as App

        return MarketViewModelFactory(app.api)
    }

    fun provideBusinessListViewModel(context: Context, ids: String): BusinessListViewModelFactory {
        val app: App = context.applicationContext as App

        return BusinessListViewModelFactory(app.api, ids)
    }

    fun provideProfileViewModelFactory(context: Context): ProfileViewModelFactory {
        val app: App = context.applicationContext as App

        return ProfileViewModelFactory(app.api)
    }

    fun provideCardDetailsViewModelFactory(context: Context): CardDetailsViewModelFactory {
        val app: App = context.applicationContext as App

        return CardDetailsViewModelFactory(app.api)
    }
}