package dev.stas.testflowxml

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.stas.testflowxml.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var resultsAdapter: ResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViews()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        resultsAdapter = ResultsAdapter()
        binding.recyclerView.apply {
            adapter = resultsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            animation = null
        }
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
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.results.collect { results ->
                    resultsAdapter.submitList(results)
                }
            }
        }
    }
}