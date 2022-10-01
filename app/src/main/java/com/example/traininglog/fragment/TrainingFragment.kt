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
import com.example.traininglog.activities.AddTrainingActivity
import com.example.traininglog.activities.TrainingDetailsActivity
import com.example.traininglog.adapters.TrainingsListAdapter
import com.example.traininglog.database.TrainingLogApp
import com.example.traininglog.database.TrainingWithExercises
import com.example.traininglog.databinding.FragmentTrainingBinding
import kotlinx.coroutines.launch

class TrainingFragment : Fragment() {

    private lateinit var binding: FragmentTrainingBinding

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTrainingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadTrainings()

        binding.btnAddTraining.setOnClickListener {
            val intent = Intent(requireContext(), AddTrainingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadTrainings(){

        val trainingDao = (requireActivity().application as TrainingLogApp).db.trainingDao()

        lifecycleScope.launch {

            trainingDao.getTrainingsWithExercises().collect{

                val trainingsList = it

                if(trainingsList.isNotEmpty()){

                    val adapter = TrainingsListAdapter(trainingsList, requireContext())

                    binding.rvTrainings.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvTrainings.adapter = adapter

                    adapter.onClickListener = object: TrainingsListAdapter.OnClickListener{
                        override fun onClick(training: TrainingWithExercises) {
                            val intent = Intent(requireContext(), TrainingDetailsActivity::class.java)
                            intent.putExtra(Constants.EXERCISE_DETAILS, training)
                            startActivity(intent)
                        }
                    }

                    binding.rvTrainings.visibility = View.VISIBLE
                    binding.tvNoTrainings.visibility = View.GONE
                }else{

                    binding.rvTrainings.visibility = View.GONE
                    binding.tvNoTrainings.visibility = View.VISIBLE
                }
            }
        }
    }
}