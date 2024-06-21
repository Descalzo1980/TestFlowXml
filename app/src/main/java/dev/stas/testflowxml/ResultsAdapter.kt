package dev.stas.testflowxml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.stas.testflowxml.databinding.ItemResultBinding

class ResultsAdapter : ListAdapter<Pair<Int, List<Int>>, ResultsAdapter.ResultViewHolder>(ResultsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val (inputValue, resultList) = getItem(position)
        holder.bind(inputValue, resultList)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val payload = payloads[0] as List<Int>
            holder.updateResults(payload)
        }
    }

    class ResultViewHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(inputValue: Int, resultList: List<Int>) {
            binding.inputValueTextView.text = "Введенное количество: $inputValue"
            binding.resultTextView.text = "Результат: ${resultList.joinToString(" ")}"
        }

        fun updateResults(updatedResultList: List<Int>) {
            binding.resultTextView.text = "Результат: ${updatedResultList.joinToString(" ")}"
        }
    }

    class ResultsDiffCallback : DiffUtil.ItemCallback<Pair<Int, List<Int>>>() {

        override fun areItemsTheSame(oldItem: Pair<Int, List<Int>>, newItem: Pair<Int, List<Int>>): Boolean {
            return oldItem.first == newItem.first
        }

        override fun areContentsTheSame(oldItem: Pair<Int, List<Int>>, newItem: Pair<Int, List<Int>>): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Pair<Int, List<Int>>, newItem: Pair<Int, List<Int>>): Any? {
            return if (oldItem.second != newItem.second) newItem.second else null
        }
    }
}