package com.codingtask.energycostcalculator

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codingtask.energycostcalculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var lastConsumptionUnitList = ArrayList<String>()
    var lastConsumptionCostList = ArrayList<String>()
    val list = ArrayList<LastConsumptionData>()
    lateinit var adapter: LastConsumptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val previousReading = getConsumptionFromSharedPreferences("LAST_CONSUMPTION_UNITS")



        setLastConsumptionAdapterData()

        binding.btnSubmit.setOnClickListener {
            if (binding.labelCurrentMeterReading.editText?.text.toString().isNotEmpty()) {
                var recentReading = 0
                if(previousReading.isNotEmpty())
                    recentReading = previousReading.last().toInt()


                println("RecentReading" + recentReading)

                var currentReading = binding.labelCurrentMeterReading.editText?.text.toString().toInt()
                if (currentReading > recentReading) {
                    val bill = calculateBill(currentReading, recentReading)
                    lastConsumptionUnitList.add(currentReading.toString())
                    lastConsumptionCostList.add(bill.toString())
                    saveConsumptionToSharedPreferences("LAST_CONSUMPTION_COST", lastConsumptionCostList)
                    saveConsumptionToSharedPreferences("LAST_CONSUMPTION_UNITS", lastConsumptionUnitList)
                } else {
                    Toast.makeText(
                        this,
                        "Current Reading cannot be less than last consumed units",
                        Toast.LENGTH_LONG
                    ).show()
                }
                setLastConsumptionAdapterData()
            } else {
                Toast.makeText(this, "Please enter Current Meter Reading", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun setLastConsumptionAdapterData() {

        val previousReading = getConsumptionFromSharedPreferences("LAST_CONSUMPTION_UNITS")
        val previousCost = getConsumptionFromSharedPreferences("LAST_CONSUMPTION_COST")

        if(previousReading.isNotEmpty()){
            for (i in previousReading.indices) {
                var lastConsumptionData = LastConsumptionData()
                lastConsumptionData.cost = previousCost[i].toInt()
                lastConsumptionData.units = previousReading[i].toInt()

                list.add(lastConsumptionData)
            }
            adapter = LastConsumptionAdapter(list)
            binding.recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }

    fun calculateBill(units: Int, recentReading: Int): Int {
        var units = units - recentReading
        println("Recent" + units)

        val range = resources.getIntArray(R.array.slabRange)
        val rate = resources.getIntArray(R.array.slabRate)

        var bill = 0
        for (i in range.indices) {
            if (units <= range[i]) {
                bill += rate[i] * units
                break
            } else {
                bill += rate[i] * range[i]
                units -= range[i]
            }
        }

        return bill
    }

    private fun saveConsumptionToSharedPreferences(key: String, arrayList: ArrayList<String>) {
        val mySet = arrayList.toSet()

        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet(key, mySet)

        editor.apply()
    }

    private fun getConsumptionFromSharedPreferences(key: String): ArrayList<String> {
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        val mySet = sharedPreferences.getStringSet(key, emptySet())
        val myArrayList = arrayListOf<String>()
        mySet?.let { myArrayList.addAll(it) }

        return myArrayList
    }
}