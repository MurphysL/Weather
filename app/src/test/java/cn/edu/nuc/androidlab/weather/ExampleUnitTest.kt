

import cn.edu.nuc.androidlab.weather.service.Service
import org.junit.Test

import org.junit.Assert.*
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun locationApiTest(){
        Service.api_area.getProvinces().subscribe {
            it.map(::print)
        }
    }

    @Test
    fun cityTest(){
        Service.api_area.getCity(14).subscribe {
            it.map(::println)
        }
    }

    @Test
    fun countryTest(){
        Service.api_area.getCountry(14, 85).subscribe {
            it.map(::println)
        }
    }

    @Test
    fun bingTest(){
        val url = URL("http://guolin.tech/api/bing_pic")
        println(url.readText()) // 执行请求
    }

}
