package newuibrowser.settings.fragment

import newuibrowser.BuildConfig
import newuibrowser.R
import android.os.Bundle
import slions.pref.PreferenceFragmentBase

/**
 * TODO: Derive from [AbstractSettingsFragment]
 */
class RootSettingsFragment : PreferenceFragmentBase() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_root, rootKey)

        if (BuildConfig.BUILD_TYPE!="debug") {
            // Hide debug page in release builds
            //findPreference<Preference>(getString(R.string.pref_key_debug))?.isVisible = false
        }
    }

    override fun titleResourceId(): Int {
        // TODO: Remove possible redundant usage of R.string.settings in places as we now provide it from here
        return R.string.settings
    }
}
