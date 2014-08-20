package be.smarttelecom.audioenforcer;

public class AudioEnforcerSettings {

	private static AudioProfiles _setAudioValue;

	public static AudioProfiles getAudioSetting() {
		return _setAudioValue;
	}

	public static void setAudioSetting(AudioProfiles audioSetting) {
		_setAudioValue = audioSetting;
	}
}
