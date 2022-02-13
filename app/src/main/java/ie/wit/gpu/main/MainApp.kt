package ie.wit.gpu.main

import android.app.Application
import ie.wit.gpu.models.GPUJSONStore
import ie.wit.gpu.models.GPUMemStore
import ie.wit.gpu.models.GPUModel
import ie.wit.gpu.models.GPUStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var gpus: GPUStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        gpus = GPUJSONStore(applicationContext)
        i("GPU App started")
    }
}
