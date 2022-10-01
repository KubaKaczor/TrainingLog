package com.example.traininglog.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traininglog.Constants
import com.example.traininglog.activities.AddExerciseActivity
import com.example.traininglog.adapters.PartListAdapter
import com.example.traininglog.database.TrainingLogApp
import com.example.traininglog.databinding.FragmentExercisesBinding
import kotlinx.coroutines.launch

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPartsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadPartsList(){

        val partDao = (requireActivity().application as TrainingLogApp).db.partDao()

        lifecycleScope.launch {

            partDao.getPartsList().collect{
                val parts = it

                val adapter = PartListAdapter(parts, requireContext())
                binding.rvExerciseCategories.layoutManager = LinearLayoutManager(requireContext())
                binding.rvExerciseCategories.adapter = adapter

                adapter.onClickListener = object: PartListAdapter.OnClickListener{
                    override fun onClick(partId: Int, partName: String) {
                        val intent = Intent(requireContext(), AddExerciseActivity::class.java)
                        intent.putExtra(Constants.BODY_PART_ID, partId)
                        intent.putExtra(Constants.PART_NAME, partName)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}