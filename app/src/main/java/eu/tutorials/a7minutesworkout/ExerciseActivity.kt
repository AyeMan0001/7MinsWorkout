package eu.tutorials.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import eu.tutorials.a7minutesworkout.databinding.ActivityExerciseBinding
import eu.tutorials.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

//for binding follow the steps
//0-in the gradle
class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    //1-
    private var binding : ActivityExerciseBinding? = null
    //rest time
    private var restTimer : CountDownTimer? =null
    private var restProgress =0
    private var restTimerDuration:Long = 10
    private var exerciseTimerDuration:Long = 30


    private var exerciseTimer : CountDownTimer? =null
    private var exerciseProgress =0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
//text to speech first 1-declare the var
    private var tts : TextToSpeech? = null
    private var player: MediaPlayer? = null

    //creating the adapter var
    private var exerciseAdapter : ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //2-
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        //3-
        setContentView(binding?.root)
        //set the actionbar to our activity
        setSupportActionBar(binding?.toolbarExercise)
        //set the back function when clicked
        //it makes it back when u click in the back button in the mobile
        ///add the back button
        //setDisplayHomeAsUpEnabled backs you one level only

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }

        exerciseList = Constants.defaultExerciseList()
        //text to speech 2-initialise the var then overdrive the interface
        tts = TextToSpeech(this,this)
       //as we get to the second activity the timer starts to begin
        setRestView()
        setupExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }
    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()

    }
    //we can link the adapter with the recyclerview in the onCreate method
    //but to not overload it we will make it a seperate fucntion
    private fun setupExerciseStatusRecyclerView(){
        //here we difine the layout type
        binding?.rvExerciseStatus?.layoutManager =
           LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }
    //if we returned from the activity the timer will begin from the start
    private fun setRestView(){
        try{
            val soundURI = Uri.parse("android.resource://eu.tutorials.a7minutesworkout/"
                    + R.raw.app_src_main_res_raw_press_start)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping = false
            player?.start()

        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE


        if(restTimer !=null){
            restTimer?.cancel()
            restProgress =0
        }
        speakOut("relax")
        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition+1].getName()
        setRestProgressBar()
    }
    private fun setUpExerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }
    private fun setRestProgressBar(){
        //before clicking the progress will be 0
        binding?.progressBar?.progress = restProgress
        //after clicking two function will begin
        //onTick then onFinish
        //the onTick what it does every 1000mls happens something

        restTimer = object : CountDownTimer(restTimerDuration*1000,1000){
            override fun onTick(p0: Long) {
                //every second the rest progress increments
                restProgress++
                //the progressbar will be this and also the text in it
                binding?.progressBar?.progress = 10-restProgress
                binding?.tvTimer?.text = (10-restProgress).toString()

            }
        //after that the onFinish begins
            override fun onFinish() {
                currentExercisePosition++

            exerciseList!![currentExercisePosition].setIsSelected(true)
            exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView()
            }

        }.start()
    }
    private fun setExerciseProgressBar(){
        //before clicking the progress will be 0
        binding?.progressBarExercise?.progress = exerciseProgress
        //after clicking two function will begin
        //onTick then onFinish
        //the onTick what it does every 1000mls happens something

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration*1000,1000){
            override fun onTick(p0: Long) {
                //every second the rest progress increments
                exerciseProgress++
                //the progressbar will be this and also the text in it
                binding?.progressBarExercise?.progress = 30-exerciseProgress
                binding?.tvTimerExercise?.text = (30-exerciseProgress).toString()

            }
            //after that the onFinish begins
            override fun onFinish() {

                if(currentExercisePosition <exerciseList?.size!! -1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }

        }.start()
    }
    //as things like that cause leakages:
    //as we did the binding we now should destroy it by the destroy function
    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress=0
        }
        if(exerciseTimer !=null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
//text to speech we do the onDestroy part
        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if(player!=null){
            player!!.stop()
        }
        binding = null
    }
//text to speech 4-here we choose the language spoken
    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","Language not supported")
            }
        }else{
            Log.e("TTS","Initialisaiton Failed")
        }
    }
    //text to speech 3-make the speakout function all it does is make it speak
    private fun speakOut(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

}