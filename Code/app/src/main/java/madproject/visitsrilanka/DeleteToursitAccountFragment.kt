package madproject.visitsrilanka

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_delete_toursit_account.*
import madproject.visitsrilanka.databinding.FragmentDeleteToursitAccountBinding

class DeleteToursitAccountFragment : Fragment() {

    //fragment binding
    private lateinit var binding: FragmentDeleteToursitAccountBinding

    private lateinit var database:DatabaseReference
    private lateinit var database2:DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        //fragment binding
        binding= FragmentDeleteToursitAccountBinding.inflate(inflater)
        return binding.root
    }//end method onCreateVieew

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //disable deleteaccount button
        binding.deleteAccountButton.setEnabled(false)

        //get values pass with bundle object
        var myBundle=arguments
        var touristEmail= myBundle?.getString("touristEmail").toString()

        binding.confirmButton.setOnClickListener {

            var enteredEmail=binding.touristEmailEditText.text.toString()

            if(enteredEmail==touristEmail){
                binding.deleteAccountButton.setEnabled(true)
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //how to access drawable folder from fragment
                var myDrawable= activity?.resources?.getDrawable(R.drawable.delete_account_button_after)
                binding.deleteAccountButton.background=myDrawable
                Toast.makeText(activity,"Delete Account Button Enabled!!!",Toast.LENGTH_SHORT).show()

                binding.deleteAccountButton.setOnClickListener {

                    deleteTouristAccount(touristEmail)
                }
            }//end if
            else{
                Toast.makeText(activity,"Wrong Email Address",Toast.LENGTH_SHORT).show()
            }//end else

        }

    }//end function onViewCreated

    private fun deleteTouristAccount(email:String){

        //formatting email
        var formattedEmail=email.split(".").toTypedArray()

        database=FirebaseDatabase.getInstance().getReference("Tourist")
        database2=FirebaseDatabase.getInstance().getReference("AppUsers")
        database.child(formattedEmail[0]).removeValue().addOnSuccessListener {


            database2.child(formattedEmail[0]).removeValue().addOnSuccessListener {

                Toast.makeText(activity,"Your Account Deleted Successfully!!!",Toast.LENGTH_SHORT).show()
                binding.touristEmailEditText.text.clear()
                moveToMainNavigationActivity()
            }.addOnFailureListener {
                Toast.makeText(activity,"Account Deleting Failed!!!",Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(activity,"Account Deleting Failed!!!",Toast.LENGTH_SHORT).show()
        }
    }//end function deleteTouristAccount

    private fun moveToMainNavigationActivity(){
        var intent=Intent(activity,MainNavigationPageActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }//end function moveToMainNavigationActivity
}