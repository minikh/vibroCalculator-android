package ru.vmsystems.vibrocalc

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import ru.vmsystems.vibrocalc.R.string.defaultValue
import ru.vmsystems.vibrocalc.calc.EdIzm
import ru.vmsystems.vibrocalc.calc.VibroCalcByAcceleration
import ru.vmsystems.vibrocalc.calc.VibroCalcByDisplacement
import ru.vmsystems.vibrocalc.calc.VibroCalcByVelocity
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        CompoundButton.OnCheckedChangeListener {

    private val vibroCalcByAcceleration = VibroCalcByAcceleration()
    private val vibroCalcByVelocity = VibroCalcByVelocity()
    private val vibroCalcByDisplacement = VibroCalcByDisplacement()

    private var english: ArrayAdapter<String>? = null
    private var metric: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
            //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("AppAlert", null).show()
//        }

        fab.setOnClickListener {
            reset()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

//        val switch = findViewById<Switch>(R.id.switchMetricSystem)
        val switch = nav_view.getHeaderView(0).findViewById<Switch>(R.id.switchMetricSystem)
        switch.setOnCheckedChangeListener(this)

        english = ArrayAdapter(this, android.R.layout.simple_spinner_item, EdIzm.getEnglishList())
        metric = ArrayAdapter(this, android.R.layout.simple_spinner_item, EdIzm.getMetricList())

        editTemperatureC.setOnEditorActionListener { view, actionId, event ->
            if (actionId == IME_ACTION_DONE || actionId == IME_ACTION_NEXT) {
                onEditTemperatureC(view)
                editTemperatureC.selectAll()
                true
            } else {
                false
            }
        }
        editTemperatureF.setOnEditorActionListener { view, actionId, event ->
            if (actionId == IME_ACTION_DONE || actionId == IME_ACTION_NEXT) {
                onEditTemperatureF(view)
                editTemperatureF.selectAll()
                true
            } else {
                false
            }
        }
        editFreqCpm.setOnEditorActionListener { view, actionId, event ->
            if (actionId == IME_ACTION_DONE || actionId == IME_ACTION_NEXT) {
                onEditFreqCpm(view)
                editFreqCpm.selectAll()
                true
            } else {
                false
            }
        }
        editFreqHz.setOnEditorActionListener { view, actionId, event ->
            if (actionId == IME_ACTION_DONE || actionId == IME_ACTION_NEXT) {
                onEditFreqHz(view)
                editFreqHz.selectAll()
                true
            } else {
                false
            }
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
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
            setEnglish()
        } else {
            conf.locale = Locale("ru")
            setMetric()
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

    private fun reset() {
        resetFreq()
        resetResult()
    }

    private fun resetFreq() {
        editFreqCpm.setText(defaultValue)
        editFreqHz.setText(defaultValue)
    }

    private fun resetResult() {
        editAdb.setText(defaultValue)
        editVdbMm.setText(defaultValue)
        editVdbM.setText(defaultValue)
        editAccelerationG.setText(defaultValue)
        editAccelerationM.setText(defaultValue)
        editAccelerationMm.setText(defaultValue)
        editVelocityM.setText(defaultValue)
        editVelocityMm.setText(defaultValue)
        editDisplacementM.setText(defaultValue)
        editDisplacementMm.setText(defaultValue)
        editTemperatureC.setText(defaultValue)
        editTemperatureF.setText(defaultValue)
    }

    private fun setEnglish() {
        setEdIzm(english)
    }

    private fun setMetric() {
        setEdIzm(metric)
    }

    private fun setEdIzm(values: ArrayAdapter<String>?) {
        spinnerG.adapter = values
        spinnerAccelerationM.adapter = values
        spinnerAccelerationMm.adapter = values
        spinnerVelocityM.adapter = values
        spinnerVelocityMm.adapter = values
        spinnerDisplacementM.adapter = values
        spinnerDisplacementMm.adapter = values
    }

    private fun onEditTemperatureC(view: TextView) {
        val temp = java.lang.Double.parseDouble(view.text.toString())

        if (temp < -273.15 || temp > 10000) {
            AppAlert.showErrorAlert("Введите число от -273.15 до 10 000", view)
            return
        }

        editTemperatureF.setText((1.8 * temp + 32).toString())
    }

    private fun onEditTemperatureF(view: TextView) {
        val temp = java.lang.Double.parseDouble(view.text.toString())

        if (temp < -459.67 || temp > 10000) {
            AppAlert.showErrorAlert("Введите число от -459.67 до 10 000", view)
            return
        }

        editTemperatureC.setText((1.8 * temp + 32).toString())
    }

    fun onEditFreqHz(view: TextView) {
        val freq = java.lang.Double.parseDouble(view.text.toString())

        if (freq > 100000) {
            AppAlert.showErrorAlert("Введите число от 0 до 100 000", view)
            return
        }

        resetResult()
        editFreqCpm.setText((freq * 60).toString())
    }

    fun onEditFreqCpm(view: TextView) {
        val freq = java.lang.Double.parseDouble(view.text.toString())

        if (freq > 6000000) {
            AppAlert.showErrorAlert("Введите число от 0 до 6 000 000", view)
            return
        }

        resetResult()
        editFreqHz.setText((freq / 60).toString())
    }
}
