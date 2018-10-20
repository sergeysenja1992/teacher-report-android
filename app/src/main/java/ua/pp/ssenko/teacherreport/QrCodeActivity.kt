package ua.pp.ssenko.teacherreport

import android.Manifest
import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.*
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_qr_code.*
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import ua.pp.ssenko.teacherreport.http.HttpService
import ua.pp.ssenko.teacherreport.utils.tag
import kotlin.coroutines.experimental.CoroutineContext


class QrCodeActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main.immediate


    val READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)
        progressBar.visibility = GONE

        if (checkSelfPermission(this, CAMERA) == PERMISSION_GRANTED) {
            initQRCodeReaderView()
        } else {
            requestCameraPermission()
        }
    }



    override fun onResume() {
        super.onResume()
        qrCodeReaderView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        qrCodeReaderView.stopCamera()
    }

    private fun initQRCodeReaderView() {

        qrCodeReaderView.setAutofocusInterval(2000L)
        qrCodeReaderView.setOnQRCodeReadListener{text, _ ->
            Log.i("TEACHER_REPORT", text)
            qrCodeReaderView.stopCamera()
            progressBar.visibility = VISIBLE

            if (!checkPermission(READ_PHONE_STATE)) {
                requestPermission(READ_PHONE_STATE);
            } else {
                Log.i(tag(), "1 Phone number: " + getPhone());
            }

            Log.i(tag(), "3");

            launch {
                HttpService.instance.login(text, HttpService.LoginDto("mPhoneNumber1")).await()
            }

        }
        qrCodeReaderView.setBackCamera()
        qrCodeReaderView.setTorchEnabled(true)
        qrCodeReaderView.setQRDecodingEnabled(true)
        qrCodeReaderView.startCamera()
    }

    private fun requestCameraPermission() {
        shouldShowRequestPermissionRationale(this, CAMERA)
        requestPermissions(this, arrayOf(CAMERA), 1)

    }

    private fun getPhone(): String {

        val phoneMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ""
        } else phoneMgr.subscriberId
    }

    private fun requestPermission(permission: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(this, "Для использования приложения надо дать права на использование номера телефона.", Toast.LENGTH_LONG).show()
        }
        ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(tag(), "2 Phone number: " + getPhone())
            } else {
                Toast.makeText(this, "Для использования приложения надо дать права на использование номера телефона.", Toast.LENGTH_LONG).show()
            }
            1 -> if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Для использования приложения надо дать права на использование камеры.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            return result == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

}
