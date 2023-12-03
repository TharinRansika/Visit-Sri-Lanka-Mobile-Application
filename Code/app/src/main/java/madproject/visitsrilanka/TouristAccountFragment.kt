package madproject.visitsrilanka

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_tourist_account.*
import madproject.visitsrilanka.databinding.FragmentTouristAccountBinding


class TouristAccountFragment : Fragment() {

    //fragment binding
    private lateinit var binding: FragmentTouristAccountBinding

    private lateinit var touristName:String
    private lateinit var touristsEmail:String
    private lateinit var touristCountry:String
    private lateinit var touristContactNumber:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        //fragment binding
        binding=FragmentTouristAccountBinding.inflate(layoutInflater)
        return binding.root
    }//end method onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get values passed with bundle
        var bundle=arguments
        var touristEmail=bundle?.getString("touristEmail")

        if(touristEmail!=null){

            Toast.makeText(activity,"touristEmail $touristEmail recieved by TouristAccountFragment",Toast.LENGTH_SHORT).show()

            //formatting email
            var formattedEmail=touristEmail.split(".").toTypedArray()

            //access to database
            lateinit var database: DatabaseReference
            database=FirebaseDatabase.getInstance().getReference("Tourist")
            database.child(formattedEmail[0]).get().addOnSuccessListener {

                if(it.exists()){
                    touristName=it.child("touristName").value.toString()
                    touristsEmail=it.child("touristEmail").value.toString()
                    touristCountry=it.child("touristCountry").value.toString()
                    touristContactNumber=it.child("touristContactNumber").value.toString()
                    var touristPassword=it.child("touristPassword").value.toString()
                    var touristProfilePicture=it.child("touristProfilePicture").value.toString()

                    //rendering values on account
                    renderingValuesOnAccount(touristName,touristEmail,touristContactNumber,touristCountry,touristProfilePicture)


                }//end if
                else{
                    Toast.makeText(activity,"There is no document available in database called $touristEmail",Toast.LENGTH_SHORT).show()
                }//end else
            }.addOnFailureListener {
                Toast.makeText(activity,"Query execution unsuccessfull!!!",Toast.LENGTH_SHORT).show()
            }

        }//end if
        else{
            Toast.makeText(activity,"touristEmail is not recieved by TouristAccountFragment",Toast.LENGTH_SHORT).show()
        }//end else


        binding.shareYourExperiencesButton.setOnClickListener {

            moveToShareMyExperiencesFragment(touristName)
        }//end method setOnClickListener


        //open edit tourist account fragment when press 'editProfileButton'
        binding.editProfileButton.setOnClickListener {

            moveToEditTouristAccountFragment(touristName,touristsEmail,touristCountry,touristContactNumber)
        }


        binding.moveToDeleteFragmentButton.setOnClickListener {
            moveToDeleteAccountFragment(touristsEmail)
        }


    }//end method onViewCreated

    private fun renderingValuesOnAccount(touristName:String,touristEmail:String,touristContactNumber:String,touristCountry:String,touristProfilePicture:String){
        binding.touristNameInProfile.text=touristName
        binding.touristEmailInProfile.text=touristEmail
        binding.touristCountryInProfile.text=touristCountry
        binding.touristContactNumberInProfile.text=touristContactNumber

        if(touristProfilePicture!=null){
            var bytes=android.util.Base64.decode(touristProfilePicture,android.util.Base64.DEFAULT)
            var bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.size)
            binding.touristProfilePicture.setImageBitmap(bitmap)
        }//end if

        //formatting heading
        var formattedTouristName=touristName.split(" ").toTypedArray()
        binding.touristAccountHeading.text="Welcome ${formattedTouristName[0]}"

    }//end function  renderingValuesOnAccount

    private fun moveToShareMyExperiencesFragment(touristName:String){

        //implementation of moving this fragment to ShareYourExperiencesFragment
        var shareYourExperiencesFragment=ShareYourExperiencesFragment()

        //passing some data using bundle with shareYourExperiences fragment
        var myBundle=Bundle()
        myBundle.putString("touristName",touristName)
        shareYourExperiencesFragment.arguments=myBundle

        //moving function
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragmentContainerView,shareYourExperiencesFragment)
            commit()
        }


    }//end function moveToShareMyExperiencesFragment

    private fun moveToEditTouristAccountFragment(name:String,email:String,country:String,contactNumber:String){

        //create object of EditTouristAccountFragment
        var editTouristAccountFragment=EditTouristAccountFragment()

        //passing some data using bundle with editTouristAccountFragment
        var myBundle=Bundle()
        myBundle.putString("touristName",name)
        myBundle.putString("touristEmail",email)
        myBundle.putString("touristContactNumber",contactNumber)
        myBundle.putString("touristCountry",country)

        editTouristAccountFragment.arguments=myBundle

        //moving function
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragmentContainerView,editTouristAccountFragment)
            commit()
        }


    }//end function moveToEditTouristAccountFragment


    private fun moveToDeleteAccountFragment(email:String){

        //create fragment object
        var deleteToursitAccountFragment=DeleteToursitAccountFragment()

        //create bundle object and attach data
        var myBundle=Bundle()
        myBundle.putString("touristEmail",email)
        deleteToursitAccountFragment.arguments=myBundle

        //moving to deleteTouristAccountFragment
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragmentContainerView,deleteToursitAccountFragment)
            commit()
        }

    }//end function moveToDeleteAccountFragment
}