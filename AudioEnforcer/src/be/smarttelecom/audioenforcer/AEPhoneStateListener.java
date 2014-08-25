package be.smarttelecom.audioenforcer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.content.Context;
import android.media.AudioManager;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AEPhoneStateListener extends PhoneStateListener {

	public Boolean _callBusy = false;
	private Context _context;
	private AudioManager _audioManager;

	public AEPhoneStateListener(Context context) {
		_context = context;
		_audioManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);

		Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {

				PrintWriter pw;
				try {
					pw = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory() + "/audioenforcer.log", true));
					ex.printStackTrace(pw);
					pw.flush();
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void onCallStateChanged(int state, String incomingNumber) {

		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:
			Log.d("DEBUG", "IDLE");

			enforceAudioSettings();

			//			int maxvolume = _audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
			//			int currentVolume = _audioManager.getStreamVolume(AudioManager.STREAM_RING);
			//			Log.d("DEBUG", "volume: " + currentVolume + " " + maxvolume);
			//			
			//			_audioManager.setStreamVolume(AudioManager.STREAM_RING, maxvolume, 0);

			break;
		}
	}

	private void enforceAudioSettings() {
		switch (AudioEnforcerSettings.getAudioSetting()) {
		case Low:
			AudioEnforcerSettings.setAudioSetting(AudioProfiles.Low);
			_audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			_audioManager.setStreamVolume(AudioManager.STREAM_RING, 3, 0);
			break;
		case Vibrate:
			AudioEnforcerSettings.setAudioSetting(AudioProfiles.Vibrate);
			_audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			break;
		case Loud:
		default:
			AudioEnforcerSettings.setAudioSetting(AudioProfiles.Loud);
			_audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			_audioManager.setStreamVolume(AudioManager.STREAM_RING, _audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
		}
	}
}