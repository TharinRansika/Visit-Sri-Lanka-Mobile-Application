package madproject.visitsrilanka

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account_as_tourist.*
import madproject.visitsrilanka.databinding.FragmentShareYourExperiencesBinding
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ShareYourExperiencesFragment : Fragment() {

    //variables
    //get values pass with bundle
    lateinit var touristName:String
    lateinit var nameOfThePlace:String
    lateinit var location:String
    lateinit var description:String
    lateinit var upload:String

    //formatting date
    var date=Calendar.getInstance().time
    val dateFormatter=SimpleDateFormat.getDateInstance()
    val timeFormatter=SimpleDateFormat.getTimeInstance()
    var postingDay=dateFormatter.format(date)
    var postingTime=timeFormatter.format(date)

    //database reference
    lateinit var dataBase: DatabaseReference

    //fragment binding
    private lateinit var binding: FragmentShareYourExperiencesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //fragment binding
        binding= FragmentShareYourExperiencesBinding.inflate(layoutInflater)
        return binding.root
    }//end method onCreateView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get values pass with bundle
        var myBundle=arguments
        touristName= myBundle?.getString("touristName").toString()

        binding.shareExperienceButton.setOnClickListener {

            if(validate()) {

                nameOfThePlace = binding.nameOfThePlace.text.toString()
                location = binding.localtion.text.toString()
                description = binding.experience.text.toString()

                //passing data to save inside database
                saveExperienceDataInDatabase(
                    touristName,
                    nameOfThePlace,
                    location,
                    description,
                    upload,
                    postingDay,
                    postingTime
                )

            }//end if
        }//end method setOnClickListener

        binding.uploadSomePicturesButton.setOnClickListener {

            var myFileIntent= Intent(Intent.ACTION_GET_CONTENT)
            myFileIntent.setType("image/*")
            ActivityResultLauncher.launch(myFileIntent)
        }//end method setOnClickListener

    }//end method onViewCreated

    private fun saveExperienceDataInDatabase(touristName:String,nameOfThePlace:String,location:String,description:String,uploadParameter:String,date:String,time:String){


        //create Experience object
        val experience=Experience(touristName,nameOfThePlace,location,description,uploadParameter,date,time)

        //passing data to database
        dataBase=FirebaseDatabase.getInstance().getReference("Tourists_Experience")
        //create unique value for child
        var childValue=touristName+"_"+date+"_"+time
        dataBase.child(childValue).setValue(experience).addOnSuccessListener {
            Toast.makeText(activity,"You share your experience successfully",Toast.LENGTH_SHORT).show()
            resetInputFieldsAfterSubmission()
        }.addOnFailureListener {
            Toast.makeText(activity,"Sharing your experience is unsuccessful",Toast.LENGTH_SHORT).show()
        }
    }//end function saveExperienceDataInDatabase

    private fun resetInputFieldsAfterSubmission(){

        binding.nameOfThePlace.text.clear()
        binding.localtion.text.clear()
        binding.experience.text.clear()

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //how to access drawable folder from fragment
        var myDrawable= activity?.resources?.getDrawable(R.drawable.baseline_upload_file_24)
        binding.experiencesUploadsImageView.setImageDrawable(myDrawable)
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }//end function resetInputFieldsAfterSubmission


    private val ActivityResultLauncher=registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){result: ActivityResult ->
        if(result.resultCode== AppCompatActivity.RESULT_OK){
            val uri=result.data!!.data
            try{
                val inputStream= activity?.contentResolver?.openInputStream(uri!!)
                val myBitmap= BitmapFactory.decodeStream(inputStream)
                val stream= ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                val bytes=stream.toByteArray()
                upload=android.util.Base64.encodeToString(bytes,android.util.Base64.DEFAULT)
                binding.experiencesUploadsImageView.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(activity,"Image Selected",Toast.LENGTH_SHORT).show()

            }catch (ex:Exception){
                Toast.makeText(activity,ex.message.toString(),Toast.LENGTH_SHORT).show()
            }//end try-catch block
        }//end if


    }//end val ActivityResultLauncher

    private fun validate():Boolean{

        if(binding.nameOfThePlace.text.isEmpty()){
            binding.nameOfThePlace.error="Name of the Place is Required!!!"
            return false
        }//end if

        if(binding.localtion.text.isEmpty()){
            binding.localtion.error="Location is Required!!!"
            return false
        }//end if

        if(binding.experience.text.isEmpty()){
            binding.experience.error="Experience is Required!!!"
            return false
        }//end if

        return true
    }//end function validate

}
