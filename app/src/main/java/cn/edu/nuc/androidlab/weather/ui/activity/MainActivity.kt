package cn.edu.nuc.androidlab.weather.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.edu.nuc.androidlab.weather.R
import cn.edu.nuc.androidlab.weather.service.Service
import cn.edu.nuc.androidlab.weather.ui.fragment.ChooseAreaFragment
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.net.URL

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
    }
}
