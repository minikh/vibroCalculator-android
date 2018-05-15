package ru.vmsystems.vibrocalc

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import ru.vmsystems.vibrocalc.R.string.defaultValue
import ru.vmsystems.vibrocalc.calc.*
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        CompoundButton.OnCheckedChangeListener {

    private val vibroCalcByAcceleration = VibroCalcByAcceleration()
    private val vibroCalcByVelocity = VibroCalcByVelocity()
    private val vibroCalcByDisplacement = VibroCalcByDisplacement()

    private var english: ArrayAdapter<String>? = null
    private var metric: ArrayAdapter<String>? = null

    private var lastView: TextView? = null
    private var fromMetricKoeff: Double = 1.0

    private var accelerationGSelectEdIzmLastValue: EdIzm? = null
    private var accelerationMsec2SelectEdIzmLastValue: EdIzm? = null
    private var accelerationMmSec2SelectEdIzmLastValue: EdIzm? = null
    private var velocityMsecSelectEdIzmLastValue: EdIzm? = null
    private var velocityMmSecSelectEdIzmLastValue: EdIzm? = null
    private var displacementMSelectEdIzmLastValue: EdIzm? = null
    private var displacementMmSelectEdIzmLastValue: EdIzm? = null

    private var accelerationGSelectEdIzmIndex = 0
    private var accelerationMsec2SelectEdIzmIndex = 0
    private var accelerationMmSec2SelectEdIzmIndex = 0
    private var velocityMsecSelectEdIzmIndex = 0
    private var velocityMmSecSelectEdIzmIndex = 0
    private var displacementMSelectEdIzmIndex = 2
    private var displacementMmSelectEdIzmIndex = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
        //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("AppAlert", null).show()
//        }

        fab.setOnClickListener { reset() }

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

        spinnerDisplacementM.setSelection(2)
        spinnerDisplacementMm.setSelection(2)

        editTemperatureC.editTextSetActionListener { view -> onEditTemperatureC(view) }
        editTemperatureF.editTextSetActionListener { view -> onEditTemperatureF(view) }

        editFreqCpm.editTextSetActionListener { view -> onEditFreqCpm(view) }
        editFreqHz.editTextSetActionListener { view -> onEditFreqHz(view) }

        editAdb.editTextSetActionListener { view -> onEditAdb(view) }
        editVdbM.editTextSetActionListener { view -> onEditVdbMsec(view) }
        editVdbMm.editTextSetActionListener { view -> onEditVdbMmSec(view) }

        editAccelerationG.editTextSetActionListener { view -> onEditAccelerationG(view) }
        editAccelerationM.editTextSetActionListener { view -> onEditAccelerationMsec2(view) }
        editAccelerationMm.editTextSetActionListener { view -> onEditAccelerationMmSec2(view) }

        editVelocityM.editTextSetActionListener { view -> onEditVelocityMsec(view) }
        editVelocityMm.editTextSetActionListener { view -> onEditVelocityMmSec(view) }

        editDisplacementM.editTextSetActionListener { view -> onEditDisplacementM(view) }
        editDisplacementMm.editTextSetActionListener { view -> onEditDisplacementMm(view) }


        spinnerAccelerationG.onItemSelectedListener = ItemSelectedListener { onChangeAccelerationGEdIzm() }
        spinnerAccelerationM.onItemSelectedListener = ItemSelectedListener { onChangeAccelerationMsec2EdIzm() }
        spinnerAccelerationMm.onItemSelectedListener = ItemSelectedListener { onChangeAccelerationMmSec2EdIzm() }
        spinnerVelocityM.onItemSelectedListener = ItemSelectedListener { onChangeVelocityMsecEdIzm() }
        spinnerVelocityMm.onItemSelectedListener = ItemSelectedListener { onChangeVelocityMmSecEdIzm() }
        spinnerDisplacementM.onItemSelectedListener = ItemSelectedListener { onChangeDisplacementMEdIzm() }
        spinnerDisplacementMm.onItemSelectedListener = ItemSelectedListener { onChangeDisplacementMmEdIzm() }

        onCheckedChanged(null, false)
    }

    class ItemSelectedListener(val function: () -> Unit) : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            function()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    private fun EditText.editTextSetActionListener(function: (EditText) -> Unit) {
        this.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_DONE || actionId == IME_ACTION_NEXT) {
                function(this)
                this.selectAll()
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
        getSelectedEdIzm()

        if (isChecked) {
            setEnglish()
        } else {
            setMetric()
        }

        setSelectedEdIzm()

        lastView?.onEditorAction(IME_ACTION_DONE)
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
        lastView?.let {
            val value: Double = it.text.toString().toDouble() * fromMetricKoeff
            it.text = getString(R.string.presigion).format(value)
                    .replace(",", ".")
        }

        vibroCalcByAcceleration.setMeasures(Measures.ENGLISH)
        vibroCalcByVelocity.setMeasures(Measures.ENGLISH)
        vibroCalcByDisplacement.setMeasures(Measures.ENGLISH)

        setEdIzm(english)

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

    private fun setMetric() {
        lastView?.let {
            val value: Double = it.text.toString().toDouble() / fromMetricKoeff
            it.text = getString(R.string.presigion).format(value)
                    .replace(",", ".")
        }

        vibroCalcByAcceleration.setMeasures(Measures.METRIC)
        vibroCalcByVelocity.setMeasures(Measures.METRIC)
        vibroCalcByDisplacement.setMeasures(Measures.METRIC)

        setEdIzm(metric)

        textFreqHz.text = getString(R.string.freqHz_ru)
        textFreqCpm.text = getString(R.string.freqCpm_ru)
        textAdb.text = getString(R.string.aDb_ru)
        textVdbMm.text = getString(R.string.vDbMmSec_ru)
        textVdbM.text = getString(R.string.vDbMSec_ru)
        textAccelerationG.text = getString(R.string.AccelerationG_ru)
        textAccelerationM.text = getString(R.string.AccelerationMSec2_ru)
        textAccelerationMm.text = getString(R.string.AccelerationMmSec2_ru)
        textVelocityM.text = getString(R.string.velocityMSec_ru)
        textVelocityMm.text = getString(R.string.velocityMmSec_ru)
        textDisplacementM.text = getString(R.string.displacementM_ru)
        textDisplacementMm.text = getString(R.string.displacementMm_ru)
        textTemperatureC.text = getString(R.string.temperatureC_ru)
        textTemperatureF.text = getString(R.string.temperatureF_ru)
    }

    private fun setEdIzm(values: ArrayAdapter<String>?) {
        spinnerAccelerationG.adapter = values
        spinnerAccelerationM.adapter = values
        spinnerAccelerationMm.adapter = values
        spinnerVelocityM.adapter = values
        spinnerVelocityMm.adapter = values
        spinnerDisplacementM.adapter = values
        spinnerDisplacementMm.adapter = values
    }

    private fun getSelectedEdIzm() {
        accelerationGSelectEdIzmIndex = spinnerAccelerationG.selectedItemPosition
        accelerationMsec2SelectEdIzmIndex = spinnerAccelerationM.selectedItemPosition
        accelerationMmSec2SelectEdIzmIndex = spinnerAccelerationMm.selectedItemPosition
        velocityMsecSelectEdIzmIndex = spinnerVelocityM.selectedItemPosition
        velocityMmSecSelectEdIzmIndex = spinnerVelocityMm.selectedItemPosition
        displacementMSelectEdIzmIndex = spinnerDisplacementM.selectedItemPosition
        displacementMmSelectEdIzmIndex = spinnerDisplacementMm.selectedItemPosition
    }

    private fun setSelectedEdIzm() {
        spinnerAccelerationG.setSelection(accelerationGSelectEdIzmIndex)
        spinnerAccelerationM.setSelection(accelerationGSelectEdIzmIndex)
        spinnerAccelerationMm.setSelection(accelerationMsec2SelectEdIzmIndex)
        spinnerVelocityM.setSelection(velocityMsecSelectEdIzmIndex)
        spinnerVelocityMm.setSelection(velocityMmSecSelectEdIzmIndex)
        spinnerDisplacementM.setSelection(displacementMSelectEdIzmIndex)
        spinnerDisplacementMm.setSelection(displacementMmSelectEdIzmIndex)
    }

    private fun getFreq(): Double {
        val text = editFreqHz.text.toString()
        return java.lang.Double.parseDouble(text)
    }

    private fun checkFreqByZero(): Boolean {
        try {
            val freqHz = java.lang.Double.parseDouble(editFreqHz.text.toString())
            val freqCpm = java.lang.Double.parseDouble(editFreqCpm.text.toString())

            if (freqCpm == 0.0 || freqHz == 0.0) return true
        } catch (e: NumberFormatException) {
            return true
        }

        return false
    }

    private fun getSelectedParameters(): Map<Parameter, EdIzm> {
        val parameters = HashMap<Parameter, EdIzm>()

        parameters[Parameter.A_g] = EdIzm.getEdIzm(spinnerAccelerationG.selectedItem as String)
        parameters[Parameter.A_m_sec2] = EdIzm.getEdIzm(spinnerAccelerationM.selectedItem as String)
        parameters[Parameter.A_mm_sec2] = EdIzm.getEdIzm(spinnerAccelerationMm.selectedItem as String)

        parameters[Parameter.V_m_sec] = EdIzm.getEdIzm(spinnerVelocityM.selectedItem as String)
        parameters[Parameter.V_mm_sec] = EdIzm.getEdIzm(spinnerVelocityMm.selectedItem as String)

        parameters[Parameter.D_m] = EdIzm.getEdIzm(spinnerDisplacementM.selectedItem as String)
        parameters[Parameter.D_mm] = EdIzm.getEdIzm(spinnerDisplacementMm.selectedItem as String)

        parameters[Parameter.A_db] = EdIzm.NONE
        parameters[Parameter.V_db_m_sec] = EdIzm.NONE
        parameters[Parameter.V_db_mm_sec] = EdIzm.NONE

        return parameters
    }

    private fun applyResult(result: Result, value: Value) {
        if (value.parameter !== Parameter.A_db) {
            val text = getString(R.string.presigion).format(result.values[Parameter.A_db.name]!!.value)
                    .replace(",", ".")
            editAdb.setText(text)
        }

        if (value.parameter !== Parameter.V_db_mm_sec) {
            val text = getString(R.string.presigion).format(result.values[Parameter.V_db_mm_sec.name]!!.value)
                    .replace(",", ".")
            editVdbMm.setText(text)
        }

        if (value.parameter !== Parameter.V_db_m_sec) {
            val text = getString(R.string.presigion).format(result.values[Parameter.V_db_m_sec.name]!!.value)
                    .replace(",", ".")
            editVdbM.setText(text)
        }

        if (value.parameter !== Parameter.A_g) {
            val text = getString(R.string.presigion).format(result.values[Parameter.A_g.name]!!.value)
                    .replace(",", ".")
            editAccelerationG.setText(text)
        }

        if (value.parameter !== Parameter.A_m_sec2) {
            val text = getString(R.string.presigion).format(result.values[Parameter.A_m_sec2.name]!!.value)
                    .replace(",", ".")
            editAccelerationM.setText(text)
        }

        if (value.parameter !== Parameter.A_mm_sec2) {
            val text = getString(R.string.presigion).format(result.values[Parameter.A_mm_sec2.name]!!.value)
                    .replace(",", ".")
            editAccelerationMm.setText(text)
        }

        if (value.parameter !== Parameter.V_m_sec) {
            val text = getString(R.string.presigion).format(result.values[Parameter.V_m_sec.name]!!.value)
                    .replace(",", ".")
            editVelocityM.setText(text)
        }

        if (value.parameter !== Parameter.V_mm_sec) {
            val text = getString(R.string.presigion).format(result.values[Parameter.V_mm_sec.name]!!.value)
                    .replace(",", ".")
            editVelocityMm.setText(text)
        }

        if (value.parameter !== Parameter.D_m) {
            val text = getString(R.string.presigion).format(result.values[Parameter.D_m.name]!!.value)
                    .replace(",", ".")
            editDisplacementM.setText(text)
        }

        if (value.parameter !== Parameter.D_mm) {
            val text = getString(R.string.presigion).format(result.values[Parameter.D_mm.name]!!.value)
                    .replace(",", ".")
            editDisplacementMm.setText(text)
        }
    }

    private fun onEditTemperatureC(view: TextView) {
        val temp = view.text.toString().toDouble()

        if (temp < -273.15 || temp > 10000) {
            AppAlert.showErrorAlert("Введите число от -273.15 до 10 000", view)
            return
        }

        editTemperatureF.setText(getString(R.string.presigion).format(1.8 * temp + 32))
    }

    private fun onEditTemperatureF(view: TextView) {
        val temp = view.text.toString().toDouble()

        if (temp < -459.67 || temp > 10000) {
            AppAlert.showErrorAlert("Введите число от -459.67 до 10 000", view)
            return
        }

        editTemperatureC.setText(getString(R.string.presigion).format((temp - 32) / 1.8))
    }

    private fun onEditFreqHz(view: TextView) {
        val freq = view.text.toString().toDouble()

        if (freq > 100000) {
            AppAlert.showErrorAlert("Введите число от 0 до 100 000", view)
            return
        }

        resetResult()
        editFreqCpm.setText((freq * 60).toString())
    }

    private fun onEditFreqCpm(view: TextView) {
        val freq = view.text.toString().toDouble()

        if (freq > 6000000) {
            AppAlert.showErrorAlert("Введите число от 0 до 6 000 000", view)
            return
        }

        resetResult()
        editFreqHz.setText((freq / 60).toString())
    }

    private fun onEditAdb(view: TextView) {
        val value = view.text.toString().toDouble()
        val acceleration = Value(value = value, edIzm = EdIzm.NONE, parameter = Parameter.A_db)

        vibroCalcByAcceleration.setParameters(getSelectedParameters())
        val result = vibroCalcByAcceleration.calculate(acceleration, getFreq())

        applyResult(result, acceleration)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.NONE
    }

    private fun onEditVdbMmSec(view: TextView) {
        val value = view.text.toString().toDouble()
        val velocity = Value(value = value, edIzm = EdIzm.NONE, parameter = Parameter.V_db_mm_sec)

        vibroCalcByVelocity.setParameters(getSelectedParameters())
        val result = vibroCalcByVelocity.calculate(velocity, getFreq())

        applyResult(result, velocity)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.MM_TO_INCH
    }

    private fun onEditVdbMsec(view: TextView) {
        val value = view.text.toString().toDouble()
        val velocity = Value(value = value, edIzm = EdIzm.NONE, parameter = Parameter.V_db_m_sec)

        vibroCalcByVelocity.setParameters(getSelectedParameters())
        val result = vibroCalcByVelocity.calculate(velocity, getFreq())

        applyResult(result, velocity)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.M_TO_FT
    }

    private fun onEditAccelerationG(view: TextView) {
        if (checkFreqByZero()) return

        val value = view.text.toString().toDouble()
        val acceleration = Value(value = value,
                edIzm = EdIzm.getEdIzm(spinnerAccelerationG.selectedItem as String),
                parameter = Parameter.A_g)

        vibroCalcByAcceleration.setParameters(getSelectedParameters())
        val result = vibroCalcByAcceleration.calculate(acceleration, getFreq())

        applyResult(result, acceleration)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.NONE
    }

    private fun onEditAccelerationMsec2(view: TextView) {
        val value = view.text.toString().toDouble()
        val acceleration = Value(value = value,
                edIzm = EdIzm.getEdIzm(spinnerAccelerationM.selectedItem as String),
                parameter = Parameter.A_m_sec2)

        vibroCalcByAcceleration.setParameters(getSelectedParameters())
        val result = vibroCalcByAcceleration.calculate(acceleration, getFreq())

        applyResult(result, acceleration)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.M_TO_FT
    }

    private fun onEditAccelerationMmSec2(view: TextView) {
        val value = view.text.toString().toDouble()
        val acceleration = Value(value = value,
                edIzm = EdIzm.getEdIzm(spinnerAccelerationMm.selectedItem as String),
                parameter = Parameter.A_mm_sec2)

        vibroCalcByAcceleration.setParameters(getSelectedParameters())
        val result = vibroCalcByAcceleration.calculate(acceleration, getFreq())

        applyResult(result, acceleration)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.MM_TO_INCH
    }

    private fun onEditVelocityMsec(view: TextView) {
        val value = view.text.toString().toDouble()
        val velocity = Value(value = value,
                edIzm = EdIzm.getEdIzm(spinnerVelocityM.selectedItem as String),
                parameter = Parameter.V_m_sec)

        vibroCalcByVelocity.setParameters(getSelectedParameters())
        val result = vibroCalcByVelocity.calculate(velocity, getFreq())

        applyResult(result, velocity)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.M_TO_FT
    }

    private fun onEditVelocityMmSec(view: TextView) {
        val value = view.text.toString().toDouble()
        val velocity = Value(value = value,
                edIzm = EdIzm.getEdIzm(spinnerVelocityMm.selectedItem as String),
                parameter = Parameter.V_mm_sec)

        vibroCalcByVelocity.setParameters(getSelectedParameters())
        val result = vibroCalcByVelocity.calculate(velocity, getFreq())

        applyResult(result, velocity)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.MM_TO_INCH
    }

    private fun onEditDisplacementM(view: TextView) {
        val value = view.text.toString().toDouble()
        val displacement = Value(value = value,
                edIzm = EdIzm.getEdIzm(spinnerDisplacementM.selectedItem as String),
                parameter = Parameter.D_m)

        vibroCalcByDisplacement.setParameters(getSelectedParameters())
        val result = vibroCalcByDisplacement.calculate(displacement, getFreq())

        applyResult(result, displacement)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.M_TO_INCH
    }


    private fun onEditDisplacementMm(view: TextView) {
        val value = view.text.toString().toDouble()
        val displacement = Value(value = value,
                edIzm = EdIzm.getEdIzm(spinnerDisplacementMm.selectedItem as String),
                parameter = Parameter.D_mm)

        vibroCalcByDisplacement.setParameters(getSelectedParameters())
        val result = vibroCalcByDisplacement.calculate(displacement, getFreq())

        applyResult(result, displacement)

        lastView = view
        fromMetricKoeff = MetricToEnglishKoeff.MM_TO_MILS
    }


    //  --------
    private fun onChangeAccelerationGEdIzm() {
        val text = editAccelerationG.text.toString()

        val edIzm = EdIzm.getEdIzm(spinnerAccelerationG.selectedItem as String)

        val value = java.lang.Double.parseDouble(text)
        val acceleration = Value(value = value,
                edIzm = accelerationGSelectEdIzmLastValue,
                parameter = Parameter.A_g)

        editAccelerationG.setText(vibroCalcByAcceleration.recalculateValue(acceleration, edIzm))
        accelerationGSelectEdIzmLastValue = edIzm
    }

    fun onChangeAccelerationMsec2EdIzm() {
        val text = editAccelerationM.text.toString()

        val edIzm = EdIzm.getEdIzm(spinnerAccelerationM.selectedItem as String)

        val value = java.lang.Double.parseDouble(text)
        val acceleration = Value(value = value,
                edIzm = accelerationMsec2SelectEdIzmLastValue,
                parameter = Parameter.A_m_sec2)

        editAccelerationM.setText(vibroCalcByAcceleration.recalculateValue(acceleration, edIzm))
        accelerationMsec2SelectEdIzmLastValue = edIzm
    }

    fun onChangeAccelerationMmSec2EdIzm() {
        val text = editAccelerationMm.text.toString()

        val edIzm = EdIzm.getEdIzm(spinnerAccelerationMm.selectedItem as String)

        val value = java.lang.Double.parseDouble(text)
        val acceleration = Value(value = value,
                edIzm = accelerationMmSec2SelectEdIzmLastValue,
                parameter = Parameter.A_mm_sec2)

        editAccelerationMm.setText(vibroCalcByAcceleration.recalculateValue(acceleration, edIzm))
        accelerationMmSec2SelectEdIzmLastValue = edIzm
    }

    fun onChangeVelocityMsecEdIzm() {
        val text = editVelocityM.text.toString()

        val edIzm = EdIzm.getEdIzm(spinnerVelocityM.selectedItem as String)

        val value = java.lang.Double.parseDouble(text)
        val velocity = Value(value = value,
                edIzm = velocityMsecSelectEdIzmLastValue,
                parameter = Parameter.V_m_sec)

        editVelocityM.setText(vibroCalcByVelocity.recalculateValue(velocity, edIzm))
        velocityMsecSelectEdIzmLastValue = edIzm
    }

    fun onChangeVelocityMmSecEdIzm() {
        val text = editVelocityMm.text.toString()

        val edIzm = EdIzm.getEdIzm(spinnerVelocityMm.selectedItem as String)

        val value = java.lang.Double.parseDouble(text)
        val velocity = Value(value = value,
                edIzm = velocityMmSecSelectEdIzmLastValue,
                parameter = Parameter.V_mm_sec)

        editVelocityMm.setText(vibroCalcByVelocity.recalculateValue(velocity, edIzm))
        velocityMmSecSelectEdIzmLastValue = edIzm
    }

    fun onChangeDisplacementMEdIzm() {
        val text = editDisplacementM.text.toString()

        val edIzm = EdIzm.getEdIzm(spinnerDisplacementM.selectedItem as String)

        val value = java.lang.Double.parseDouble(text)
        val displacement = Value(value = value,
                edIzm = displacementMSelectEdIzmLastValue,
                parameter = Parameter.D_mm)

        editDisplacementM.setText(vibroCalcByDisplacement.recalculateValue(displacement, edIzm))
        displacementMSelectEdIzmLastValue = edIzm
    }

    fun onChangeDisplacementMmEdIzm() {
        val text = editDisplacementMm.text.toString()

        val edIzm = EdIzm.getEdIzm(spinnerDisplacementMm.selectedItem as String)

        val value = java.lang.Double.parseDouble(text)
        val displacement = Value(value = value,
                edIzm = displacementMmSelectEdIzmLastValue,
                parameter = Parameter.D_mm)

        editDisplacementMm.setText(vibroCalcByDisplacement.recalculateValue(displacement, edIzm))
        displacementMmSelectEdIzmLastValue = edIzm
    }
}
