package eu.tutorials.a7minutesworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import eu.tutorials.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flStart?.setOnClickListener{
            //make an intent that goes the exercise activity
            val intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }
        binding?.flBMI?.setOnClickListener{
            //make an intent that goes the exercise activity
            val intent = Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }
        binding?.flHistory?.setOnClickListener{
            //make an intent that goes the exercise activity
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}