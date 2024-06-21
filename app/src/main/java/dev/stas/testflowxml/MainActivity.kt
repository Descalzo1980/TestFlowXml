package dev.stas.testflowxml

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dev.stas.testflowxml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.startButton.setOnClickListener {
            val inputText = binding.inputField.text.toString()
            if (inputText.isNotBlank() && inputText.all { it.isDigit() }) {
                val n = inputText.toInt()
                binding.inputField.setText("")
                viewModel.startSummation(n)
            } else {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.results.observe(this, Observer { results ->
            updateResultsUI(results)
        })
    }

    private fun updateResultsUI(results: List<Pair<Int, List<Int>>>) {
        binding.resultsContainer.removeAllViews()
        results.forEach { (inputValue, resultList) ->
            val inputTextView = TextView(this).apply {
                text = "Введенное количество: $inputValue"
            }
            binding.resultsContainer.addView(inputTextView)

            val resultTextView = TextView(this).apply {
                text = "Результат: ${resultList.joinToString(" ")}"
            }
            binding.resultsContainer.addView(resultTextView)

            binding.resultsContainer.addView(Space(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    8.dpToPx()
                )
            })
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}