/*
 * Copyright © 2020-2021 Jamal Rothfuchs
 * Copyright © 2020-2021 Stéphane Lenclud
 * Copyright © 2015 Anthony Restaino
 */

package newuibrowser.html.incognito

import android.app.Application
import newuibrowser.App
import newuibrowser.R
import newuibrowser.constant.FILE
import newuibrowser.constant.UTF8
import newuibrowser.html.HtmlPageFactory
import newuibrowser.search.SearchEngineProvider
import newuibrowser.utils.ThemeUtils
import newuibrowser.utils.htmlColor
import dagger.Reusable
import newuibrowser.html.jsoup.andBuild
import newuibrowser.html.jsoup.body
import newuibrowser.html.jsoup.charset
import newuibrowser.html.jsoup.parse
import newuibrowser.html.jsoup.tag
import io.reactivex.Single
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

/**
 * A factory for the incognito page.
 */
@Reusable
class IncognitoPageFactory @Inject constructor(
    private val application: Application,
    private val searchEngineProvider: SearchEngineProvider,
    private val incognitoPageReader: IncognitoPageReader
) : HtmlPageFactory {

    override fun buildPage(): Single<String> = Single
            .just(searchEngineProvider.provideSearchEngine())
            .map { (_, queryUrl, _) ->
                parse(incognitoPageReader.provideHtml()
                        .replace("\${TITLE}", application.getString(R.string.incognito))
                        .replace("\${backgroundColor}", htmlColor(ThemeUtils.getSurfaceColor(App.currentContext())))
                        .replace("\${searchBarColor}", htmlColor(ThemeUtils.getSearchBarColor(ThemeUtils.getSurfaceColor(App.currentContext()))))
                        .replace("\${searchBarTextColor}", htmlColor(ThemeUtils.getColor(App.currentContext(),R.attr.colorOnSurface)))
                        .replace("\${borderColor}", htmlColor(ThemeUtils.getColor(App.currentContext(),R.attr.colorOnSecondary)))
                        .replace("\${accent}", htmlColor(ThemeUtils.getColor(App.currentContext(),R.attr.colorSecondary)))
                        .replace("\${search}", application.getString(R.string.search_incognito))
                ) andBuild {
                    charset { UTF8 }
                    body {
                        tag("script") {
                            html(
                                html()
                                    .replace("\${BASE_URL}", queryUrl)
                                    .replace("&", "\\u0026")
                            )
                        }
                    }
                }
            }
            .map { content -> Pair(createIncognitoPage(), content) }
            .doOnSuccess { (page, content) ->
                FileWriter(page, false).use {
                    it.write(content)
                }
            }
    .map { (page, _) -> "$FILE$page" }

    /**
     * Create the incognito page file.
     */
    fun createIncognitoPage() = File(application.filesDir, FILENAME)

    companion object {

        const val FILENAME = "private.html"

    }

}
