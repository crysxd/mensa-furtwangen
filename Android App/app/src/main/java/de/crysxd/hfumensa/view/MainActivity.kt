package de.crysxd.hfumensa.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.crysxd.hfumensa.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = ""
    }

    override fun setTitle(title: CharSequence?) {
        textViewTitle.text = title
    }

    override fun setTitle(titleId: Int) {
        textViewTitle.text = getString(titleId)
    }
}
