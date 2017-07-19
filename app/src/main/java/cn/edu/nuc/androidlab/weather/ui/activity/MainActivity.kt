package cn.edu.nuc.androidlab.weather.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import cn.edu.nuc.androidlab.weather.R
import cn.edu.nuc.androidlab.weather.service.AutoUpdateService
import cn.edu.nuc.androidlab.weather.ui.fragment.ChooseAreaFragment

/**
 * MainActivity
 */
class MainActivity : AppCompatActivity() {
    private val TAG : String = this.javaClass.simpleName

    private val fragment : ChooseAreaFragment = ChooseAreaFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit()

        val intent : Intent = Intent(this, AutoUpdateService::class.java)
        startService(intent)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val weather_id : String? = pref.getString("weather_id", null)
        if(weather_id != null){
            Log.i(TAG, weather_id)
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
