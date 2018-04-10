package ru.vmsystems.vibrocalc

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Switch
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import ru.vmsystems.vibrocalc.calc.EdIzm
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        CompoundButton.OnCheckedChangeListener{

    private val english = EdIzm.getEnglish()
    private val metric = EdIzm.getMetric()

    private fun setEnglish() {
        setEdIzm(english.values)
    }

    private fun setMetric() {
        setEdIzm(metric.values)
    }

    private fun setEdIzm(values: Collection<String>) {
        accelerationGSelectEdIzm.getItems().addAll(values)
        accelerationMsec2SelectEdIzm.getItems().addAll(values)
        accelerationMmSec2SelectEdIzm.getItems().addAll(values)
        velocityMsecSelectEdIzm.getItems().addAll(values)
        velocityMmSecSelectEdIzm.getItems().addAll(values)
        displacementMSelectEdIzm.getItems().addAll(values)
        displacementMmSelectEdIzm.getItems().addAll(values)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        var switch = nav_view.getHeaderView(0).findViewById<Switch>(R.id.switchMetricSystem)
        if (switch != null) {
            switch.setOnCheckedChangeListener(this)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {
                println("nav_share")
            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration

        if (isChecked) {
            conf.locale = Locale("en")
        }
        else {
            conf.locale = Locale("ru")
        }
        res.updateConfiguration(conf, dm)

        textFreqHz.text = getString(R.string.freqHz)
        textFreqCpm.text = getString(R.string.freqCpm)
        textAdb.text = getString(R.string.aDb)
        textVdbMm.text = getString(R.string.vDbMmSec)
        textVdbM.text = getString(R.string.vDbMSec)
        textAccelerationG.text = getString(R.string.AccelerationG)
        textAccelerationM.text = getString(R.string.AccelerationMSec2)
        textAccelerationMm.text = getString(R.string.AccelerationMmSec2)
        textVelocityM.text = getString(R.string.velocityMSec)
        textVelocityMm.text = getString(R.string.velocityMmSec)
        textDisplacementM.text = getString(R.string.displacementM)
        textDisplacementMm.text = getString(R.string.displacementMm)
        textTemperatureC.text = getString(R.string.temperatureC)
        textTemperatureF.text = getString(R.string.temperatureF)

    }
}
