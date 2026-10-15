package br.com.fiap.inovagab

import android.app.Application
import br.com.fiap.inovagab.network.ApiClient

class InovaGabApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClient.inicializar(this)
    }
}
