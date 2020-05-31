package com.balves42.daterangepickerdemo.mainactivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.balves42.daterangepickerdemo.R
import com.balves42.daterangepickerdemo.mainactivity.utils.CustomMaterialDatePicker
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.core.util.Pair


class MainActivity : AppCompatActivity(), MainActivityView {

    private val maxDays: Long = 7
    private lateinit var mCustomMaterialDatePicker: CustomMaterialDatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCustomMaterialDatePicker = CustomMaterialDatePicker(
            this,
            btnOpenCalendar,
            TimeUnit.DAYS.toMillis(maxDays),
            "Selecionar intervalo",
            "Guardar",
            Pair("Inicio", "Fim"),
            "pt"
        )

        btnOpenCalendar.setOnClickListener {
            mCustomMaterialDatePicker.show(supportFragmentManager)
        }
    }

    override fun maxDateRangeBehaviour() {
        Toast.makeText(this, "Impossible to select more than $maxDays days!", Toast.LENGTH_SHORT)
            .show()
    }

    override fun selectedBehavior(range: Pair<Long, Long>?) {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        tvStartDate.text = formatter.format(Date(range!!.first!!))
        tvEndDate.text = formatter.format(Date(range.second!!))
    }

    override fun onDestroy() {
        super.onDestroy()
        mCustomMaterialDatePicker.removeListeners()
    }
}
