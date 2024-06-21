package dev.stas.testflowxml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.stas.testflowxml.databinding.ItemResultBinding

class ResultsAdapter : ListAdapter<CalculationResult, ResultsAdapter.ResultViewHolder>(ResultsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }

    class ResultViewHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: CalculationResult) {
            binding.inputValueTextView.text = "Введенное количество: ${result.inputValue}"
            binding.resultTextView.text = "Результат: ${result.resultList.joinToString(" ")}"
        }
    }

    class ResultsDiffCallback : DiffUtil.ItemCallback<CalculationResult>() {

        override fun areItemsTheSame(oldItem: CalculationResult, newItem: CalculationResult): Boolean {
            return oldItem.inputValue == newItem.inputValue
        }

        override fun areContentsTheSame(oldItem: CalculationResult, newItem: CalculationResult): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: CalculationResult, newItem: CalculationResult): Any? {
            return if (oldItem.resultList != newItem.resultList) newItem.resultList else null
        }
    }
}