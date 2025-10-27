/*
 * The contents of this file are subject to the Common Public Attribution License Version 1.0.
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * https://github.com/Slion/Fulguris/blob/main/LICENSE.CPAL-1.0.
 * The License is based on the Mozilla Public License Version 1.1, but Sections 14 and 15 have been
 * added to cover use of software over a computer network and provide for limited attribution for
 * the Original Developer. In addition, Exhibit A has been modified to be consistent with Exhibit B.
 *
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF
 * ANY KIND, either express or implied. See the License for the specific language governing rights
 * and limitations under the License.
 *
 * The Original Code is Fulguris.
 *
 * The Original Developer is the Initial Developer.
 * The Initial Developer of the Original Code is Stéphane Lenclud.
 *
 * All portions of the code written by Stéphane Lenclud are Copyright © 2020 Stéphane Lenclud.
 * All Rights Reserved.
 */
 
package newuibrowser.activity

import newuibrowser.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import android.os.Handler
import android.widget.TextView

/**
 * Still needed a splash screen activity as the SplashScreen API would not play well with our themed activity.
 * We just could not get our theme override to work then.
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity @Inject constructor(): LocaleAwareActivity() {

    val mHandler = Handler()
    var queue: RequestQueue? = null
    val reqTAG = "MyTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        findViewById<TextView>(R.id.textView).setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply{
                data = Uri.parse(getString(R.string.url_fulguris_home_page))
                putExtra("SOURCE", "SELF")
                startActivity(this)
            }
        }

        val sharedPref = getSharedPreferences("uibrowserads", Context.MODE_PRIVATE)
        queue = Volley.newRequestQueue(this)
        val url = "https://rosydahdev.my.id/data/uibrowser.json"

        val editor = sharedPref.edit()
        editor.putInt("intersloaded", 0)
        editor.apply()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                editor.putBoolean("fan", response.getBoolean("fan"))
                editor.putBoolean("admob", response.getBoolean("admob"))
                editor.apply()

                if(response.getBoolean("redirect")){
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(
                            "https://play.google.com/store/apps/details?id=" + response.getString("redirect_package"))
                        setPackage("com.android.vending")
                    }
                    startActivity(intent)
                    finish()
                }else {
                    startMain()
                }
            },
            { error ->
                startMain()
            }
        )
        jsonObjectRequest.tag = reqTAG
        jsonObjectRequest.setShouldCache(false)
        queue?.add(jsonObjectRequest)
    }

    override fun onLocaleChanged() {
        //TODO("Not yet implemented")
    }

    fun startMain(){
        mHandler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }

    override fun onStop() {
        super.onStop()
        queue?.cancelAll(reqTAG)
    }
}
