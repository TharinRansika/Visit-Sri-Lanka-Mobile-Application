package madproject.visitsrilanka

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExperienceAdapter(private val allTouristsExperiencesObject:ArrayList<TouristExperience>):RecyclerView.Adapter<ExperienceAdapter.MyViewHolder>() {
    //this class is used to feed data to recyclerview

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.tourist_experience,parent,false)
        return MyViewHolder(itemView)
    }//end method onCreateViewHolder

    override fun getItemCount(): Int {
        //this function returns number of all objects in 'allTouristsExperiencesObject'

        return allTouristsExperiencesObject.size
    }//end method getItemCount

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentExperience=allTouristsExperiencesObject[position]

        //set values coming from firebase to 'tourist_experience' caed view
        holder.touristName_placeName_location_TextView.text="${currentExperience.touristName} \n@ ${currentExperience.nameOfThePlace}, ${currentExperience.location}."
        holder.postedDateTextView.text = currentExperience.date
        holder.postedTimeTextView.text = currentExperience.time
        if(currentExperience.upload!=null){
            holder.experiencePhotoImageView.setImageBitmap(createImageBitMap((currentExperience.upload)))
        }//end if
        else{
            holder.experiencePhotoImageView.setImageResource(R.drawable.sri_lanka_icon_2)
        }//end else
        holder.descriptionTextView.text=currentExperience.description

    }//end function onBindViewHolder


    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        //accessing all views inside 'tourist_experience' card view
        val touristName_placeName_location_TextView:TextView=itemView.findViewById(R.id.touristName_placeName_location_TextView)
        val postedDateTextView:TextView=itemView.findViewById(R.id.postedDateTextView)
        val postedTimeTextView:TextView=itemView.findViewById(R.id.postedTimeTextView)
        val experiencePhotoImageView:ImageView=itemView.findViewById(R.id.experiencePhotoImageView)
        val descriptionTextView:TextView=itemView.findViewById(R.id.descriptionTextView)

    }//end class MyViewHolder

    private fun createImageBitMap(image: String?):Bitmap{
        var bytes=android.util.Base64.decode(image,android.util.Base64.DEFAULT)
        var bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.size)

        return bitmap
    }//end method createImageBitMap


}//end class ExperienceAdapter