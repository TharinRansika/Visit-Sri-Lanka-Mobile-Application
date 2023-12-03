package madproject.visitsrilanka

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account_as_tourist.*
import madproject.visitsrilanka.databinding.FragmentEditTouristAccountBinding
import madproject.visitsrilanka.databinding.FragmentTouristAccountBinding
import java.io.ByteArrayOutputStream

class EditTouristAccountFragment : Fragment() {

    //fragment binding
    private lateinit var binding:FragmentEditTouristAccountBinding

    //profilePicture
    private lateinit var profilePicture:String

    //database reference
    private lateinit var database:DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //fragment binding
        binding= FragmentEditTouristAccountBinding.inflate(layoutInflater)
        return binding.root
    }//end method onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get values sending with bundle
        var bundle=arguments
        var touristName= bundle?.getString("touristName")
        var touristEmail= bundle?.getString("touristEmail")
        var touristCountry= bundle?.getString("touristCountry")
        var touristContactNumber= bundle?.getString("touristContactNumber")

        //set values to views
        binding.touristEmail.setText(touristEmail)
        binding.editTouristName.setText(touristName)
        binding.editTouristCountry.setText(touristCountry)
        binding.editTouristContactNumber.setText(touristContactNumber)


        binding.editAccountButton.setOnClickListener {

            var name=binding.editTouristName.text.toString()
            var country=binding.editTouristCountry.text.toString()
            var contactNumber=binding.editTouristContactNumber.text.toString()

            editTouristDetails(name,touristEmail.toString(),country,contactNumber,profilePicture)
        }

        //fetching image
        binding.editProfilePictureButton.setOnClickListener {
            var myFileIntent= Intent(Intent.ACTION_GET_CONTENT)
            myFileIntent.setType("image/*")
            ActivityResultLauncher.launch(myFileIntent)
        }
    }//end method onViewCreated


    private fun editTouristDetails(touristName:String,touristEmail:String,touristCountry:String,touristContactNumber:String,touristProfilePicture:String){

        //formatting email
        var formattedEmail=touristEmail.split(".").toTypedArray()

        database=FirebaseDatabase.getInstance().getReference("Tourist")
        val tourist= mapOf<String,String>(
            "touristName" to touristName,
            "touristCountry" to touristCountry,
            "touristContactNumber" to touristContactNumber,
            "touristProfilePicture" to touristProfilePicture
        )

        database.child(formattedEmail[0]).updateChildren(tourist).addOnSuccessListener {
            Toast.makeText(activity,"Your Profile Updated Successfully",Toast.LENGTH_SHORT).show()
            resetEditTextandViews()
        }.addOnFailureListener {
            Toast.makeText(activity,"Profile Updation Failed!!!",Toast.LENGTH_SHORT).show()
        }

    }//end function editTouristDetails

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
                profilePicture=android.util.Base64.encodeToString(bytes,android.util.Base64.DEFAULT)
                binding.editProfilePictureImageView.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(activity,"Image Selected", Toast.LENGTH_SHORT).show()

            }catch (ex:Exception){
                Toast.makeText(activity,ex.message.toString(), Toast.LENGTH_SHORT).show()
            }//end try-catch block
        }//end if


    }//end val ActivityResultLauncher

    private fun resetEditTextandViews(){
        binding.editTouristName.text.clear()
        binding.editTouristContactNumber.text.clear()
        binding.editTouristCountry.text.clear()
        var myDrawable= activity?.resources?.getDrawable(R.drawable.baseline_account_box_24)
        binding.editProfilePictureImageView.setImageDrawable(myDrawable)
    }//end function resetEditTextandViews

}