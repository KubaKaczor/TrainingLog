package com.example.traininglog.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traininglog.Constants
import com.example.traininglog.activities.HistoryDetailsActivity
import com.example.traininglog.adapters.FinishedTrainingsAdapter
import com.example.traininglog.database.HistoryWithSets
import com.example.traininglog.database.TrainingLogApp
import com.example.traininglog.databinding.FragmentHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFinishedTrainings()
    }

    private fun loadFinishedTrainings(){
        val historyDao = (requireActivity().application as TrainingLogApp).db.historyDao()

        lifecycleScope.launch {

            historyDao.getTrainingsHistory().collect{

                val finishedTrainings = it

                val adapter = FinishedTrainingsAdapter(finishedTrainings, requireContext())

                binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
                binding.rvHistory.adapter = adapter

                adapter.onClickListener = object: FinishedTrainingsAdapter.OnClick{
                    override fun onClick(training: HistoryWithSets) {
                        val intent = Intent(requireContext(), HistoryDetailsActivity::class.java)
                        intent.putExtra(Constants.HISTORY_DETAILS, training)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}