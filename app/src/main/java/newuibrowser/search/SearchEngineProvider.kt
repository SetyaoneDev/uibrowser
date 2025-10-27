package newuibrowser.search

import newuibrowser.di.SuggestionsClient
import newuibrowser.settings.preferences.UserPreferences
import android.app.Application
import dagger.Reusable
import newuibrowser.search.engine.AskSearch
import newuibrowser.search.engine.BaiduSearch
import newuibrowser.search.engine.BaseSearchEngine
import newuibrowser.search.engine.BingSearch
import newuibrowser.search.engine.BraveSearch
import newuibrowser.search.engine.CustomSearch
import newuibrowser.search.engine.DuckLiteNoJSSearch
import newuibrowser.search.engine.DuckLiteSearch
import newuibrowser.search.engine.DuckNoJSSearch
import newuibrowser.search.engine.DuckSearch
import newuibrowser.search.engine.EcosiaSearch
import newuibrowser.search.engine.EkoruSearch
import newuibrowser.search.engine.GoogleSearch
import newuibrowser.search.engine.MojeekSearch
import newuibrowser.search.engine.NaverSearch
import newuibrowser.search.engine.QwantLiteSearch
import newuibrowser.search.engine.QwantSearch
import newuibrowser.search.engine.SearxSearch
import newuibrowser.search.engine.StartPageMobileSearch
import newuibrowser.search.engine.StartPageSearch
import newuibrowser.search.engine.YahooNoJSSearch
import newuibrowser.search.engine.YahooSearch
import newuibrowser.search.engine.YandexSearch
import newuibrowser.search.suggestions.BaiduSuggestionsModel
import newuibrowser.search.suggestions.DuckSuggestionsModel
import newuibrowser.search.suggestions.GoogleSuggestionsModel
import newuibrowser.search.suggestions.NaverSuggestionsModel
import newuibrowser.search.suggestions.NoOpSuggestionsRepository
import newuibrowser.search.suggestions.RequestFactory
import newuibrowser.search.suggestions.SuggestionsRepository
import io.reactivex.Single
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * The model that provides the search engine based
 * on the user's preference.
 */
@Reusable
class SearchEngineProvider @Inject constructor(
    private val userPreferences: UserPreferences,
    @SuggestionsClient private val okHttpClient: Single<OkHttpClient>,
    private val requestFactory: RequestFactory,
    private val application: Application
) {

    /**
     * Provide the [SuggestionsRepository] that maps to the user's current preference.
     */
    fun provideSearchSuggestions(): SuggestionsRepository =
        when (userPreferences.searchSuggestionChoice) {
            0 -> NoOpSuggestionsRepository()
            1 -> GoogleSuggestionsModel(okHttpClient, requestFactory, application, userPreferences)
            2 -> DuckSuggestionsModel(okHttpClient, requestFactory, application, userPreferences)
            3 -> BaiduSuggestionsModel(okHttpClient, requestFactory, application, userPreferences)
            4 -> NaverSuggestionsModel(okHttpClient, requestFactory, application, userPreferences)
            else -> GoogleSuggestionsModel(okHttpClient, requestFactory, application, userPreferences)
        }

    /**
     * Provide the [BaseSearchEngine] that maps to the user's current preference.
     */
    fun provideSearchEngine(): BaseSearchEngine =
        when (userPreferences.searchChoice) {
            0 -> CustomSearch(userPreferences.searchUrl, userPreferences)
            1 -> GoogleSearch()
            2 -> AskSearch()
            3 -> BaiduSearch()
            4 -> BingSearch()
            5 -> BraveSearch()
            6 -> DuckSearch()
            7 -> DuckNoJSSearch()
            8 -> DuckLiteSearch()
            9 -> DuckLiteNoJSSearch()
            10 -> EcosiaSearch()
            11 -> EkoruSearch()
            12 -> MojeekSearch()
            13 -> NaverSearch()
            14 -> QwantSearch()
            15 -> QwantLiteSearch()
            16 -> SearxSearch()
            17 -> StartPageSearch()
            18 -> StartPageMobileSearch()
            19 -> YahooSearch()
            20 -> YahooNoJSSearch()
            21 -> YandexSearch()
            else -> GoogleSearch()
        }

    /**
     * Return the serializable index of of the provided [BaseSearchEngine].
     */
    fun mapSearchEngineToPreferenceIndex(searchEngine: BaseSearchEngine): Int =
        when (searchEngine) {
            is CustomSearch -> 0
            is GoogleSearch -> 1
            is AskSearch -> 2
            is BaiduSearch -> 3
            is BingSearch -> 4
            is BraveSearch -> 5
            is DuckSearch -> 6
            is DuckNoJSSearch -> 7
            is DuckLiteSearch -> 8
            is DuckLiteNoJSSearch -> 9
            is EcosiaSearch -> 10
            is EkoruSearch -> 11
            is MojeekSearch -> 12
            is NaverSearch -> 13
            is QwantSearch -> 14
            is QwantLiteSearch -> 15
            is SearxSearch -> 16
            is StartPageSearch -> 17
            is StartPageMobileSearch -> 18
            is YahooSearch -> 19
            is YahooNoJSSearch -> 20
            is YandexSearch -> 21
            else -> throw UnsupportedOperationException("Unknown search engine provided: " + searchEngine.javaClass)
        }

    /**
     * Provide a list of all supported search engines.
     */
    fun provideAllSearchEngines(): List<BaseSearchEngine> = listOf(
        CustomSearch(userPreferences.searchUrl, userPreferences),
        GoogleSearch(),
        AskSearch(),
        BaiduSearch(),
        BingSearch(),
        BraveSearch(),
        DuckSearch(),
        DuckNoJSSearch(),
        DuckLiteSearch(),
        DuckLiteNoJSSearch(),
        EcosiaSearch(),
        EkoruSearch(),
        MojeekSearch(),
        NaverSearch(),
        QwantSearch(),
        QwantLiteSearch(),
        SearxSearch(),
        StartPageSearch(),
        StartPageMobileSearch(),
        YahooSearch(),
        YahooNoJSSearch(),
        YandexSearch()
    )

}
