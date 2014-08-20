package be.smarttelecom.audioenforcer;

import be.smarttelecom.audioenforcer.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;

public class AudioEnforcerIntentReceiver extends BroadcastReceiver {

	private static int clickCount = 2;
	
	

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("AudioEnforcer", "AudioEnforcerIntentReceiver received something");
		if (intent.getAction().equals("be.smarttelecom.intent.action.CHANGE_PICTURE")) {
			Log.i("AudioEnforcer", "updating WidgetPictureAndButtonListener");
			updateWidgetPictureAndButtonListener(context);
		}
	}
	

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_audioenforcer);

		clickCount = (clickCount + 1) % 3;
		Log.i("audioenforcer", "clickcount: " + clickCount);
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		int image;
		switch (clickCount) {
		case 1:
			AudioEnforcerSettings.setAudioSetting(AudioProfiles.Low);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			audioManager.setStreamVolume(AudioManager.STREAM_RING, 2, 0);
			Log.i("audioenforcer", "setting audio to LOW");
			image = R.drawable.low;
			break;
		case 0:
			AudioEnforcerSettings.setAudioSetting(AudioProfiles.Vibrate);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			Log.i("audioenforcer", "setting audio to VIBRATE");
			image = R.drawable.vibrate;
			break;
		case 2:
		default:
			AudioEnforcerSettings.setAudioSetting(AudioProfiles.Loud);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			Log.i("audioenforcer", "setting audio to LOUD");
			audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
			image = R.drawable.loud;
		}
		remoteViews.setImageViewResource(R.id.widget_image, image);

		//REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		remoteViews.setOnClickPendingIntent(R.id.widget_image, AudioEnforcerProvider.buildButtonPendingIntent(context));

		AudioEnforcerProvider.pushWidgetUpdate(context, remoteViews);
	}
}
