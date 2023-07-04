package eu.tutorials.a7minutesworkout

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import eu.tutorials.a7minutesworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class FinishActivity : AppCompatActivity() {
    var binding:ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarFinishActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
        binding?.btnFinish?.setOnClickListener{
            finish()
        }
        val dao = (application as WorkOutApp).db.historyDao()
        addDateToDatabase(dao)

    }

private fun addDateToDatabase(historyDao: HistoryDao){

    val c = Calendar.getInstance()
    val dateTime = c.time
    Log.e("Date:",""+dateTime)

    val sdf = java.text.SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
    val date = sdf.format(dateTime)
    Log.e("Formatted Date : ",""+date)


    lifecycleScope.launch {
        historyDao.insert(HistoryEntity(date))
        Log.e(
            "Date: ",
            "Added..."
        )
    }

}

}