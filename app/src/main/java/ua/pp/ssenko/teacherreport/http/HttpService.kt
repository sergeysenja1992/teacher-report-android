package ua.pp.ssenko.teacherreport.http;

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.POST

interface HttpService {

    data class LoginDto (val phoneNumber: String, val authCode: String? = null)

    @POST("login/{qrCodeId}")
    fun login(@Path("qrCodeId") qrCodeId: String, @Body body: LoginDto): Deferred<Response<Void>>

    companion object Factory {
        val instance: HttpService by lazy {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .baseUrl("http://192.168.0.104:8080/")
                    .build()

            retrofit.create(HttpService::class.java);
        }
    }
}