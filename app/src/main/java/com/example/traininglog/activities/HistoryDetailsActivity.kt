package com.example.traininglog.activities

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traininglog.Constants
import com.example.traininglog.R
import com.example.traininglog.adapters.FinishedSetsAdapter
import com.example.traininglog.adapters.HistoryDetailsAdapter
import com.example.traininglog.database.HistoryWithSets
import com.example.traininglog.database.TrainingLogApp
import com.example.traininglog.databinding.ActivityHistoryDetailsBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailsBinding

    private var mHistoryDetails: HistoryWithSets? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.HISTORY_DETAILS)){
            mHistoryDetails = intent.getParcelableExtra(Constants.HISTORY_DETAILS)
            loadDetails()
        }

        setUpActionBar()
    }

    private fun loadDetails(){

        binding.tvNameTrainingDetails.text = mHistoryDetails!!.historyTraining.trainingName

        val date = getDateFromMils(mHistoryDetails!!.historyTraining.date)
        binding.tvDate.text = "Data treningu: $date"

        val list = mHistoryDetails!!.sets.groupBy { s -> s.exerciseName }

        val adapter = HistoryDetailsAdapter(list, this)
        binding.rvExercisesTrainingDetails.layoutManager = LinearLayoutManager(this)
        binding.rvExercisesTrainingDetails.adapter = adapter

    }

    private fun setUpActionBar(){

        val toolbar = binding.toolbarHistoryDetails

        if(toolbar != null){
            setSupportActionBar(toolbar)

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_arrow_back_24))

            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun getDateFromMils(timeInMils: Long): String {

        val formatter = SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(Date(timeInMils))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.history_delete ->{
                showDeleteDialog()
                return true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDeleteDialog(){
        val historyDao = (application as TrainingLogApp).db.historyDao()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Na pewno chcesz usunąć trening?")
            .setPositiveButton("Tak",
                DialogInterface.OnClickListener { _, _ ->
                    lifecycleScope.launch {
                        historyDao.delete(mHistoryDetails!!.historyTraining)
                        Toast.makeText(this@HistoryDetailsActivity, "Usunięto trening", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })
            .setNegativeButton("Nie",
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
        val alertDialog = builder.create()
        alertDialog.show()
    }

}