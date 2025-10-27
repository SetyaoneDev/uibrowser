package newuibrowser.activity

import android.content.Context
import newuibrowser.R
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.CookieManager
import com.codemybrainsout.ratingdialog.RatingDialog
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Completable
import javax.inject.Inject
import com.google.android.gms.ads.interstitial.InterstitialAd as InterstitialAdAdmob
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback as InterstitialAdLoadCallbackAdmob
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback as FullScreenContentCallbackAdmob
import com.google.android.gms.ads.LoadAdError as LoadAdErrorAdmob
import com.google.android.gms.ads.AdRequest as AdRequestAdmob
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Not used in incognito mode
 */
@AndroidEntryPoint
class MainActivity @Inject constructor(): WebBrowserActivity() {
    private var mInterstitialAdAdmob: InterstitialAdAdmob? = null
    private var intersloaded = 0
    private val INTERSADMOB_ID = "ca-app-pub-3940256099942544/1033173712"
    private val TEST_DEVICE_HASHED_ID = "ABCDEF012345"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("uibrowserads", Context.MODE_PRIVATE)
        intersloaded = sharedPref.getInt("intersloaded", 0)

        //MobileAds.setRequestConfiguration(
        //    RequestConfiguration.Builder().setTestDeviceIds(listOf(TEST_DEVICE_HASHED_ID)).build()
        //)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("iklan...", "Load banner admob1")
            MobileAds.initialize(this@MainActivity) {}
            runOnUiThread {
                Log.d("iklan...", "Load banner admob2")
                if(intersloaded == 0) {
                    loadBannerAdmob()
                    loadIntersAdmob()
                }else{
                    loadBannerAdmob()
                }
            }
        }
        Log.d("iklan...", "Load banner admob3")

        val ratingDialog = RatingDialog.Builder(this)
            .threshold(4)
            .session(2)
            .onRatingBarFormSubmit { feedback -> Log.i("Rating", "onRatingBarFormSubmit: $feedback") }
            .build()
        ratingDialog.show()
    }

    fun loadBannerAdmob(){
        iBinding.adViewAdmob.visibility = View.VISIBLE
        val adRequest = AdRequestAdmob.Builder().build()
        iBinding.adViewAdmob.loadAd(adRequest)

        Log.d("iklan...", "Load banner admob")
    }

    fun loadIntersAdmob(){
        val adRequest = AdRequestAdmob.Builder().build()
        InterstitialAdAdmob.load(this@MainActivity, INTERSADMOB_ID, adRequest, object : InterstitialAdLoadCallbackAdmob() {
            override fun onAdFailedToLoad(adError: LoadAdErrorAdmob) {
                mInterstitialAdAdmob = null
                Timber.tag("iklan...").d("inters admob error")
            }

            override fun onAdLoaded(interstitialAd: InterstitialAdAdmob) {
                Timber.tag("iklan...").d("inters admob loaded")
                mInterstitialAdAdmob = interstitialAd

                mInterstitialAdAdmob?.fullScreenContentCallback = object: FullScreenContentCallbackAdmob() {
                    override fun onAdClicked() {
                        mInterstitialAdAdmob = null
                    }

                    override fun onAdDismissedFullScreenContent() {
                        mInterstitialAdAdmob = null
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        mInterstitialAdAdmob = null
                    }

                    override fun onAdImpression() {
                        mInterstitialAdAdmob = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        mInterstitialAdAdmob = null
                    }
                }
            }
        })
    }

    override fun updateUrl(url: String?, isLoading: Boolean) {
        super.updateUrl(url, isLoading)
        if (url == null) {
            return
        }
        if (url.length > 13){
            if(url.contains("newuibrowser://home", false)){
                Timber.tag("iklan...").d("show banner")
                iBinding.adViewAdmob.visibility = View.VISIBLE
            }else{
                iBinding.adViewAdmob.visibility = View.GONE

                Timber.tag("iklan...").d("gone banner")

                val sharedPref = getSharedPreferences("uibrowserads", Context.MODE_PRIVATE)
                var countInterstitial = sharedPref.getInt("interstitial", 0)

                if (countInterstitial > 6) {
                    if (mInterstitialAdAdmob != null) {
                        mInterstitialAdAdmob?.show(this)
                        Timber.tag("iklan...").d("Show inters admob")
                    }
                    countInterstitial = -1
                }

                with (sharedPref.edit()) {
                    putInt("interstitial", (countInterstitial+1))
                    apply()
                }
            }
        }
    }

    public override fun updateCookiePreference(): Completable = Completable.fromAction {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(userPreferences.cookiesEnabled)
    }

    override fun onNewIntent(intent: Intent) =
        if (intent.action == INTENT_PANIC_TRIGGER) {
            panicClean()
        } else {
            handleNewIntent(intent)
            super.onNewIntent(intent)
        }

    override fun updateHistory(title: String?, url: String) = addItemToHistory(title, url)

    override fun isIncognito() = false

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.isCtrlPressed) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_P ->
                    // Open a new private window
                    if (event.isShiftPressed) {
                        startActivity(IncognitoActivity.createIntent(this))
                        overridePendingTransition(R.anim.slide_up_in, R.anim.fade_out_scale)
                        return true
                    }
            }
        }
        return super.dispatchKeyEvent(event)
    }

}
