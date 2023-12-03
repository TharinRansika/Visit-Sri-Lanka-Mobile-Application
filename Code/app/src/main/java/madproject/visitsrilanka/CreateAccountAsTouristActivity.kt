package madproject.visitsrilanka

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account_as_tourist.*
import java.io.ByteArrayOutputStream

class CreateAccountAsTouristActivity : AppCompatActivity() {

    private var touristImage:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account_as_tourist)

        lateinit var dataBase:DatabaseReference
        lateinit var dataBase2:DatabaseReference


        createAccountButton.setOnClickListener {

            if(validateInputs()) {

                //check whether confirmed password and created password are same
                val touristCreatedPassword = createdPassword.text.toString()
                val touristConfirmedPassword = confirmedPassword.text.toString()

                if (touristCreatedPassword == touristConfirmedPassword) {

                    //get Values From Input Fields
                    val userTouristName = touristName.text.toString()
                    val userTouristEmail = touristEmail.text.toString()
                    val userTouristContactNumber = touristContactNumber.text.toString()
                    val userTouristCountry = touristCountry.text.toString()


                    //formatting 'userTouristEmail' to 'formattedEmail'
                    //this 'formattedEmail' use as primary key
                    val formattedEmail = userTouristEmail.split(".").toTypedArray()

                    //create Tourist object
                    val tourist = Tourist(
                        userTouristName,
                        userTouristEmail,
                        userTouristContactNumber,
                        userTouristCountry,
                        touristConfirmedPassword,
                        touristImage
                    )

                    //create AppUsers object
                    val appUser = AppUsers(userTouristEmail, touristConfirmedPassword, "Tourist")

                    //pass data to 'Tourist' in 'Visit Sri Lanka'
                    dataBase = FirebaseDatabase.getInstance().getReference("Tourist")
                    dataBase.child(formattedEmail[0]).setValue(tourist).addOnSuccessListener {
                        //this code block will execute if data successfully submitted

                        //pass data  to 'AppUsers' in 'Visit Sri Lanka'
                        dataBase2 = FirebaseDatabase.getInstance().getReference("AppUsers")
                        dataBase2.child(formattedEmail[0]).setValue(appUser).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "You Created Tourist Account Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            resetInputFieldsAfterSubmission()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Tourist Account Creation is Unsuccessful",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }.addOnFailureListener {
                        //this code block will execute if data not successfully submitted
                        Toast.makeText(
                            this,
                            "Tourist Account Creation is Unsuccessful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }//end if
                else {
                    Toast.makeText(this, "Please Check Password Again", Toast.LENGTH_SHORT).show()
                }//else

            }//end if

        }//end method setOnClickListener

        uploadProfilePictureButton.setOnClickListener {

            var myFileIntent=Intent(Intent.ACTION_GET_CONTENT)
            myFileIntent.setType("image/*")
            ActivityResultLauncher.launch(myFileIntent)
        }//end method setOnClickListener

    }//end method onCreate


    private val ActivityResultLauncher=registerForActivityResult<Intent,ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){result:ActivityResult->
        if(result.resultCode== RESULT_OK){
            val uri=result.data!!.data
            try{
                val inputStream=contentResolver.openInputStream(uri!!)
                val myBitmap=BitmapFactory.decodeStream(inputStream)
                val stream=ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                val bytes=stream.toByteArray()
                touristImage=android.util.Base64.encodeToString(bytes,android.util.Base64.DEFAULT)
                uploadProfilePictureImageView.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(this,"Image Selected",Toast.LENGTH_SHORT).show()

            }catch (ex:Exception){
                Toast.makeText(this,ex.message.toString(),Toast.LENGTH_SHORT).show()
            }//end try-catch block
        }//end if


    }//end val ActivityResultLauncher


    private fun resetInputFieldsAfterSubmission(){
        touristName.text.clear()
        touristEmail.text.clear()
        touristContactNumber.text.clear()
        touristCountry.text.clear()
        createdPassword.text.clear()
        confirmedPassword.text.clear()
        uploadProfilePictureImageView.setImageResource(R.drawable.baseline_account_box_24)
    }//end method resetInputFieldsAfterSubmission

    private fun validateInputs():Boolean{

        //validate name
        if(touristName.text.isEmpty()){
            touristName.error="Name is Required!!!"
            return false;
        }//end if


        val nameValidator = "^[\\p{L}\\s]+(?:[\\p{Zs}\\-][\\p{L}\\s]+)*\$".toRegex()
        if(!nameValidator.matches(touristName.text.toString())){
            touristName.error="Name has invalid characters!!!"
            return false;
        }//end if

        //validate email
        if(touristEmail.text.isEmpty()){
            touristEmail.error="Email is Required!!!"
            return false
        }//end if

        val emailValidator="^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))\$".toRegex()
        if(!emailValidator.matches(touristEmail.text.toString())){
            touristEmail.error="Enter valid Email!!!"
            return false
        }//end if

        //validate contact number
        if(touristContactNumber.text.isEmpty()){
            touristContactNumber.error="Contact Number is Required!!!"
            return false
        }//end if

        val phoneNumberPattern = Regex("^0\\d{9,10}$")
        if(!phoneNumberPattern.matches(touristContactNumber.text.toString())){
            touristContactNumber.error="Enter Valid Contact Number"
            return false
        }//end if

        //validate tourist country
        if(touristCountry.text.isEmpty()){
            touristCountry.error="Country is Required!!!"
            return false
        }//end if

        val countryValidator = "^[\\p{L}\\s]+(?:[\\p{Zs}\\-][\\p{L}\\s]+)*\$".toRegex()
        if(!countryValidator.matches(touristCountry.text.toString())){
            touristCountry.error="Enter valid Country Name"
            return false
        }//end if

        //validate password
        if(createdPassword.text.isEmpty()){
            createdPassword.error="Password Creation is Required!!!"
            return false
        }//end if

        val createdPasswordValidator=Regex("^.{5,}$")
        if(!createdPasswordValidator.matches(createdPassword.text.toString())){
            createdPassword.error="Password should has At least 5 characters, At least one uppercase letter, At least one lowercase letter, At least one digit and At least one special character(e.g. !@#\$%^&*)"
            return false
        }//end if

        //validate confirmed password
        if(confirmedPassword.text.isEmpty()){
            confirmedPassword.error="Confirmed Password is Required!!!"
            return false;
        }//end if

        if(!(createdPassword.text.toString()==confirmedPassword.text.toString())){
            confirmedPassword.error="Recheck Confirmed Password!!!"
            return false;
        }//end if



        return true
    }//end function validateInputs()
}