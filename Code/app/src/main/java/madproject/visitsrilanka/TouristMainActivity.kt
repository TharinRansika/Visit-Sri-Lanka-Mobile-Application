package madproject.visitsrilanka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_tourist_main.*

class TouristMainActivity : AppCompatActivity() {

    //create Fragment objects
    var touristAccountFragment=TouristAccountFragment()
    var visitExperiencesFragment=VisitExperiencesFragment()
    var taxiFragment=TaxiFragment()
    var tourGuidesFragment=TourGuidesFragment()
    var hotelsAndRestaurentsFragment=HotelsAndRestaurentsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tourist_main)


        //get value passed by intent in signInActivity
        var touristEmail=intent.getStringExtra("touristEmail")

        //moving fragment to fragment
        bottomNavigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.myAccount->displayTouristAccountFragment(touristAccountFragment,touristEmail)
                R.id.experiences->changeFragment(visitExperiencesFragment)
                R.id.taxies->changeFragment(taxiFragment)
                R.id.tourGuides->changeFragment(tourGuidesFragment)
                R.id.hotelsAndRestaurents->changeFragment(hotelsAndRestaurentsFragment)
                else->{

                }
            }
            true
        }


    }//end method onCreate

    private fun displayTouristAccountFragment(fragment: Fragment,touristEmail:String?){
        //!!! this function is specially used to display TouristAccountFragment

        //passing touristEmail to fragment
        var myBundle=Bundle()
        myBundle.putString("touristEmail",touristEmail)
        touristAccountFragment.arguments=myBundle

        supportFragmentManager.beginTransaction().apply {

            replace(R.id.fragmentContainerView,fragment)
            commit()
        }


    }//end function displayTouristAccountFragment

    private fun changeFragment(fragment: Fragment){

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView,fragment)
            commit()
        }
    }//end function changeFragment
}