package madproject.visitsrilanka

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId="notification_channel"
const val channelName="visit_Sri_Lanka_Alerts"

class MyFirebaseMessagingService:FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if(remoteMessage.getNotification()!=null){
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }//end if

    }//end function onMessageReceived

    fun getRemoteView(notificationTitle:String,notificationContext:String):RemoteViews{

        val remoteView=RemoteViews("madproject.visitsrilanka",R.layout.notification)

        remoteView.setTextViewText(R.id.notificationTitle,notificationTitle)
        remoteView.setTextViewText(R.id.notificationContext,notificationContext)

        return remoteView
    }//end function getRemoteView

    fun generateNotification(notificationTitle:String,notificationContext:String){

        var builder:NotificationCompat.Builder= NotificationCompat.Builder(applicationContext, channelId).setSmallIcon(R.drawable.sri_lanka_icon).setAutoCancel(true).setVibrate(
                longArrayOf(1000,1000,1000,1000)
            ).setOnlyAlertOnce(true)

        builder=builder.setContent(getRemoteView(notificationTitle,notificationContext))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0,builder.build())
    }//end function generateNotification

}