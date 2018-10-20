package ua.pp.ssenko.teacherreport

import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent




class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launchActivity = Intent(this, QrCodeActivity::class.java)
        startActivity(launchActivity)
    }

    override fun onResume() {
        super.onResume()

        val launchActivity = Intent(this, QrCodeActivity::class.java)
        startActivity(launchActivity)
    }

}
