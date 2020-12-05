package com.jarry.scrollcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = mutableListOf<String>()
        for (i in 0 until 50) {
            list.add("$i $i")
        }

        card0.setData(Entity("水果",list))
    }
}
