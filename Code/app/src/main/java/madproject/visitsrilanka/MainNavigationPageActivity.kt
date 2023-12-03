package madproject.visitsrilanka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main_navigation_page.*

class MainNavigationPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation_page)

        createAccountAsTouristButton.setOnClickListener {

            val moveToCreateAccountAsTouristActivity= Intent(this,CreateAccountAsTouristActivity::class.java)
            startActivity(moveToCreateAccountAsTouristActivity)

        }//end method setOnClickListener

        signInButton.setOnClickListener {

            val moveToSignInActivity=Intent(this,SignInActivity::class.java)
            startActivity(moveToSignInActivity)

        }//end method setOnClickListener


    }//end method onCreate
}