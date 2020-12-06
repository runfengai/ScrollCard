package com.jarry.scrollcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = mutableListOf<String>()
        val list2 = mutableListOf<String>()
        for (i in 0 until 10) {
            if (i<6){
                list.add("$i $i")
            }
            list2.add("$i $i")
        }

        card0.setData(Entity("苹果",list))
        card1.setData(Entity("香蕉",list2))
        card2.setData(Entity("柿子",list))
    }
}
