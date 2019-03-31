package com.silverhetch.pheme

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.silverhetch.aura.storage.SPCeres
import com.silverhetch.ourea.OureaImpl
import com.google.android.things.contrib.driver.sensehat.SenseHat;
import com.google.android.things.contrib.driver.sensehat.LedMatrix



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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Color the LED matrix.
        val display = SenseHat.openDisplay()
//        display.draw(Color.MAGENTA)

        OureaImpl(
            SPCeres(
                getSharedPreferences(
                    "preference",
                    Context.MODE_PRIVATE
                )
            )
        ).run {
            init()
            addObserver { observable, data ->
                data.forEach { t, u ->
                    System.out.println(
                        """ Device: ${u.ip()} ${u.isRegistered()}
                """.trimIndent()
                    )
                    display.draw(
                        Color.valueOf(
                            (Math.random()*255).toFloat(),
                            (Math.random()*255).toFloat(),
                            (Math.random()*255).toFloat()
                        ).toArgb()
                    )
                }
            }
        }
    }
}
