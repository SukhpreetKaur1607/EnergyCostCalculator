package com.codingtask.energycostcalculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codingtask.energycostcalculator.databinding.RecyclerItemBinding



class LastConsumptionAdapter(val list: List<LastConsumptionData>) : RecyclerView.Adapter<LastConsumptionAdapter.LastConsumptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastConsumptionViewHolder {
        val binding =
            RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LastConsumptionViewHolder(binding)
    }


    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: LastConsumptionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class LastConsumptionViewHolder(val itemTodoBinding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(itemTodoBinding.root) {
        fun bind(lastConsumptionData: LastConsumptionData) {
            itemTodoBinding.unitValue.text = "Units = "+lastConsumptionData.units.toString()
            itemTodoBinding.costValue.text = "Cost = "+lastConsumptionData.cost.toString()


        }
    }


}


