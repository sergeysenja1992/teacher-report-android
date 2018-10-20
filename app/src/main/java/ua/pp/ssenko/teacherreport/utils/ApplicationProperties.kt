package ua.pp.ssenko.teacherreport.utils


/**
 * Created by ssenko on 09.09.17.
 */
data class ApplicationProperties(
        val google: GoogleProperties
)

data class GoogleProperties (
        val clientId: String
)
