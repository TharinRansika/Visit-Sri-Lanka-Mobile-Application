package madproject.visitsrilanka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account_as_tourist.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        lateinit var database: DatabaseReference
        signInButton.setOnClickListener {

            if(validateInput()) {

                var userPassword = signinUserPassword.text.toString()
                var userEmail = signinUserEmail.text.toString()

                var formattedUserEmail = userEmail.split(".").toTypedArray()

                //create database reference
                database = FirebaseDatabase.getInstance().getReference("AppUsers")
                database.child(formattedUserEmail[0]).get().addOnSuccessListener {

                    if (it.exists()) {

                        var appUserEmail = it.child("appUserEmail").value
                        var appUserPassword = it.child("appUserPassword").value
                        var appUserStatus = it.child("appUserStatus").value

                        //moving activities according to user status
                        if (appUserPassword == userPassword) {
                            //check whether user entered password and firebase stored password are same
                            moveActivityAccordingtoUserStatus(
                                appUserStatus.toString(),
                                appUserEmail.toString()
                            )
                            //reset input fields
                            resetEditTextsFieldsAfterSubmission()

                        }//end if
                        else {
                            Toast.makeText(this, "Check Your Password Again", Toast.LENGTH_SHORT)
                                .show()
                        }//end else


                    }//end if
                    else {
                        Toast.makeText(this, "No user exists", Toast.LENGTH_SHORT).show()
                        Toast.makeText(
                            this,
                            "Just check your credentials again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }//end else
                }.addOnFailureListener {
                    Toast.makeText(this, "Query Execution Unsuccessful!!!", Toast.LENGTH_SHORT)
                        .show()
                }

            }//end if
        }//end method setOnClickListener
    }//end method onCreate()

    private fun moveActivityAccordingtoUserStatus(appUserStatus:String,appUserEmail:String){
        if(appUserStatus=="Tourist"){

            val moveToTouristMainActivity= Intent(this,TouristMainActivity::class.java)
            moveToTouristMainActivity.putExtra("touristEmail",appUserEmail.toString())
            startActivity(moveToTouristMainActivity)
        }//end if
        else{

            val moveToOtherUsersMainActivity=Intent(this,OtherUsersMainActivity::class.java)
            startActivity(moveToOtherUsersMainActivity)
        }//end else

    }//end function moveActivityAccordingtoUserStatus

    private fun resetEditTextsFieldsAfterSubmission(){
        signinUserEmail.text.clear()
        signinUserPassword.text.clear()
    }//end function resetEditTextsFieldsAfterSubmission

    private fun validateInput():Boolean{

        //validate email
        if(signinUserEmail.text.isEmpty()){
            signinUserEmail.error="Email is Required!!!"
            return false
        }//end if

        val emailValidator="^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))\$".toRegex()
        if(!emailValidator.matches(signinUserEmail.text.toString())){
            signinUserEmail.error="Enter valid Email!!!"
            return false
        }//end if

        //validate password
        if(signinUserPassword.text.isEmpty()){
            signinUserPassword.error="Password is Required!!!"
            return false
        }//end if

        return true;
    }//end function validateInput()
}