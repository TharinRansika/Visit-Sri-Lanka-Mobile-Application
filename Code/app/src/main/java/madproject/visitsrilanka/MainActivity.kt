package madproject.visitsrilanka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN

        //splash screen activity
        val handler=Handler(Looper.getMainLooper())
        handler.postDelayed({
            val moveToMainNavigationPageActivity= Intent(this,MainNavigationPageActivity::class.java)
            startActivity(moveToMainNavigationPageActivity)
            finish()
        },5000)

    }//end method onCreate()
}