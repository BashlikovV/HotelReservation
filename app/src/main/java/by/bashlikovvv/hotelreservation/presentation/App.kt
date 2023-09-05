package by.bashlikovvv.hotelreservation.presentation

import android.app.Application
import by.bashlikovvv.hotelreservation.di.dataModule
import by.bashlikovvv.hotelreservation.di.domainModule
import by.bashlikovvv.hotelreservation.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(dataModule, domainModule, appModule)
        }
    }
}