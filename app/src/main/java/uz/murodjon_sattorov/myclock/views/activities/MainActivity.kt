package uz.murodjon_sattorov.myclock.views.activities

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.murodjon_sattorov.myclock.R
import uz.murodjon_sattorov.myclock.core.adapters.MainPagerAdapter

import uz.murodjon_sattorov.myclock.databinding.ActivityMainBinding
import uz.murodjon_sattorov.myclock.views.fragments.AlarmsFragment


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    private var adapter: MainPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        adapter = MainPagerAdapter(supportFragmentManager)

        loadFragments()

        mainBinding.viewPagers.adapter = adapter
        mainBinding.tabLayout.setupWithViewPager(mainBinding.viewPagers)

        loadIcons()

    }

    private fun loadFragments() {
        adapter!!.addPagerFragment(AlarmsFragment(), "")

    }

    private fun loadIcons() {
        mainBinding.tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_round_access_alarm_24)

    }
}