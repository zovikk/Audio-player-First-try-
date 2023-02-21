package com.example.auvi_app

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.sbClapping)
        handler = Handler(Looper.getMainLooper())
        val btnPlay = findViewById<FloatingActionButton>(R.id.fabPlay)
        val btnStop = findViewById<FloatingActionButton>(R.id.fabStop)
        val btnPause = findViewById<FloatingActionButton>(R.id.fabPause)

        btnPlay.setOnClickListener{
            if(mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(this, R.raw.applauding)
                initializeSeekBar()
            }

              mediaPlayer?.start()
        }
        btnPause.setOnClickListener{
            mediaPlayer?.pause()
        }
        btnStop.setOnClickListener{
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }

    }

    private fun initializeSeekBar(){
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        val tvPlayed = findViewById<TextView>(R.id.tvPlayed)
        val tvDue = findViewById<TextView>(R.id.tvDue)
        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable{
            seekBar.progress = mediaPlayer!!.currentPosition

            val playedTime = mediaPlayer!!.currentPosition/500
            tvPlayed.text = "$playedTime sec"
            val duration = mediaPlayer!!.duration/500
            val dueTime = duration-playedTime
            tvDue.text = "$dueTime sec"
            handler.postDelayed(runnable,500)
        }
        handler.postDelayed(runnable,500)

    }
}