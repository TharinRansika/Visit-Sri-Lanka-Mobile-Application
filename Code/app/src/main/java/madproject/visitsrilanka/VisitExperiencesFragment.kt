package madproject.visitsrilanka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import madproject.visitsrilanka.databinding.FragmentVisitExperiencesBinding


class VisitExperiencesFragment : Fragment() {

    private lateinit var database:DatabaseReference
    private lateinit var allTouristsExperiencesRecyclerView: RecyclerView
    private lateinit var experiencesArrayList:ArrayList<TouristExperience>

    //fragment binding
    private lateinit var binding:FragmentVisitExperiencesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //fragment binding
        binding=FragmentVisitExperiencesBinding.inflate(layoutInflater)
        return binding.root
    }//end method onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allTouristsExperiencesRecyclerView=binding.allTouristsExperiencesRecyclerView
        allTouristsExperiencesRecyclerView.layoutManager=LinearLayoutManager(activity)
        allTouristsExperiencesRecyclerView.setHasFixedSize(true)

        experiencesArrayList= arrayListOf<TouristExperience>()

        getAllExperiencesDataFromFirebase()

    }//end method onViewCreated

    private fun getAllExperiencesDataFromFirebase(){
        database=FirebaseDatabase.getInstance().getReference("Tourists_Experience")
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for(singleSnapShot in snapshot.children){

                        //get all rows(single_experience/document) from Torists_Experience table
                        val experience=singleSnapShot.getValue(TouristExperience::class.java)
                        //and store it in arrayList
                        experiencesArrayList.add(experience!!)

                    }//end for loop
                }//end if

                //passing arraylist with all experiences to Adapter class and display it on recyclerview
                allTouristsExperiencesRecyclerView.adapter=ExperienceAdapter(experiencesArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }//end function getAllExperiencesDataFromFirebase


}