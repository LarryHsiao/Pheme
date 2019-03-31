package com.silverhetch.pheme

import android.content.Context
import android.graphics.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.things.contrib.driver.sensehat.SenseHat
import com.silverhetch.aura.storage.SPCeres
import com.silverhetch.ourea.Device
import com.silverhetch.ourea.Ourea
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.Log
import com.silverhetch.ourea.OureaImpl


/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : AppCompatActivity() {
    private val display = SenseHat.openDisplay()
    private val device = ArrayList<Device>()
    private lateinit var ourea: Ourea
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        ourea = OureaImpl(
            SPCeres(
                getSharedPreferences(
                    "preference",
                    Context.MODE_PRIVATE
                )
            )
        )
        ourea.init()
        ourea.addObserver { observable, data ->
            device.clear()
            device.addAll(data.values)
            data.forEach { t, u ->
                Log.i("MAIN", "device: " + u.ip())
            }

            display.draw(
                Color.valueOf(
                    (Math.random()*255).toFloat(),
                    (Math.random()*255).toFloat(),
                    (Math.random()*255).toFloat()
                ).toArgb()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        ourea.shutdown()
    }

    override fun onDestroy() {
        super.onDestroy()
        display.close()
    }
}
