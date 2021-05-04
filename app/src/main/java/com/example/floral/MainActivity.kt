package com.example.floral

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.background = null

        val flowerFragment = FlowerFragment()
        val assistfragment = AssistFragment()
        val shopfragment = ShopFragment()
        val suggestfragment = SuggestFragment()

        setCurrentFragment(flowerFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miFlow -> setCurrentFragment(flowerFragment)
                R.id.miAssis -> setCurrentFragment(assistfragment)
                R.id.miShop -> setCurrentFragment(shopfragment)
                R.id.miSug -> setCurrentFragment(suggestfragment)
            }
            true
        }

        bottomNavigationView.getOrCreateBadge(R.id.miSug).apply{
        number = 10
        isVisible = true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}