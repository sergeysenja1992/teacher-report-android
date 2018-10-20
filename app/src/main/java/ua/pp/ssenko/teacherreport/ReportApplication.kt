package ua.pp.ssenko.teacherreport

import android.app.Application
import ua.pp.ssenko.teacherreport.utils.ApplicationProperties

class ReportApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appTmp = loadApplicationProperties()
    }

    private fun loadApplicationProperties(): ApplicationProperties {
        val inputStream = getAssets().open("application.yml")
        val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
        mapper.registerModule(KotlinModule()) // Enable Kotlin support
        return mapper.readValue(inputStream, ApplicationProperties::class.java)
    }

    companion object {
        private var appTmp: ApplicationProperties? = null
        val appSettings: ApplicationProperties by lazy { appTmp!! }
    }
}