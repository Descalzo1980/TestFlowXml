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

    class ResultViewHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(inputValue: Int, resultList: List<Int>) {
            binding.inputValueTextView.text = "Введенное количество: $inputValue"
            binding.resultTextView.text = "Результат: ${resultList.joinToString(" ")}"
        }
    }

    class ResultsDiffCallback : DiffUtil.ItemCallback<Pair<Int, List<Int>>>() {

        override fun areItemsTheSame(oldItem: Pair<Int, List<Int>>, newItem: Pair<Int, List<Int>>): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Pair<Int, List<Int>>, newItem: Pair<Int, List<Int>>): Boolean {
            return oldItem == newItem
        }
    }
}