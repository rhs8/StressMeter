package com.rana_hoshyarsadeghi.stressmeter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.rana_hoshyarsadeghi.stressmeter.databinding.ActivityMainBinding

// Audio imports
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin



// Comments : // I did use AI to help me implement a more shuffled version of "more images"
// and each pictures having an assigned value. I know it was not required based on the discussions posted
// but just wanted to try it out ;)
// Also for the sound I noticed that on apk the sound keep playing until a button is used but I made it shorter
// around 4 seconds
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // === Gentle vibration on startup (no sound from system beeps) ===
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = getSystemService(android.os.VibratorManager::class.java)
                vm?.defaultVibrator

            } else {
                @Suppress("DEPRECATION")
                getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.vibrate(
                        VibrationEffect.createOneShot(
                            300L,
                            160
                        )
                    )
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(300L)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        playSoothingTone(durationSec = 4.0)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_stress_meter, R.id.nav_results),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun playSoothingTone(
        durationSec: Double = 4.0,
        sampleRate: Int = 44100,
        baseFreqHz: Double = 432.0,
        volume: Float = 0.15f,
        attackMs: Int = 400,
        releaseMs: Int = 700
    ) {
        val totalSamples = (durationSec * sampleRate).toInt()
        val attackSamples = (attackMs / 1000.0 * sampleRate).toInt().coerceAtLeast(1)
        val releaseSamples = (releaseMs / 1000.0 * sampleRate).toInt().coerceAtLeast(1)
        val overtoneFreq = baseFreqHz * 1.5
        val buffer = ShortArray(totalSamples)
        for (n in 0 until totalSamples) {
            val t = n / sampleRate.toDouble()
            val env = when {
                n < attackSamples -> n.toFloat() / attackSamples
                n > totalSamples - releaseSamples -> {
                    val relPos = totalSamples - n
                    relPos.toFloat() / releaseSamples
                }
                else -> 1f
            }
            val primary = sin(2.0 * PI * baseFreqHz * t)
            val overtone = 0.25 * sin(2.0 * PI * overtoneFreq * t)
            val sample = (primary + overtone) * volume * env
            val pcm = (sample * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
            buffer[n] = pcm.toShort()
        }
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val minBuf = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat).coerceAtLeast(buffer.size * 2)

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attrs = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            val fmt = AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(audioFormat)
                .setChannelMask(channelConfig)
                .build()

            AudioTrack(
                attrs,
                fmt,
                minBuf,
                AudioTrack.MODE_STATIC,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )
        } else {
            @Suppress("DEPRECATION")
            AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                channelConfig,
                audioFormat,
                minBuf,
                AudioTrack.MODE_STATIC
            )
        }
        track.write(buffer, 0, buffer.size)
        track.setVolumeSafe(1.0f)
        track.play()
        val releaseDelayMs = (durationSec * 1000).toLong() + 200
        track.postDelayedRelease(releaseDelayMs)
    }
    private fun AudioTrack.setVolumeSafe(vol: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setVolume(vol)
        } else {
            @Suppress("DEPRECATION")
            this.setStereoVolume(vol, vol)
        }
    }
    private fun AudioTrack.postDelayedRelease(delayMs: Long) {
        val at = this
        window?.decorView?.postDelayed({
            try {
                at.stop()
                at.release()
            } catch (_: Exception) { }
        }, delayMs)
    }
}



