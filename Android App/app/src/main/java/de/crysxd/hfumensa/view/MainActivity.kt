package de.crysxd.hfumensa.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import de.crysxd.hfumensa.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = ""

        textSwitcherTitle.inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        textSwitcherTitle.outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        textSwitcherTitle.setFactory {
            LayoutInflater.from(this).inflate(R.layout.view_title, null)
        }
    }

    override fun setTitle(title: CharSequence?) {
        textSwitcherTitle.setText(title)
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }
}
