package newuibrowser.search.suggestions

import newuibrowser.database.SearchSuggestion
import io.reactivex.Single

/**
 * A search suggestions repository that doesn't fetch any results.
 */
class NoOpSuggestionsRepository :
    SuggestionsRepository {

    private val emptySingle: Single<List<SearchSuggestion>> = Single.just(emptyList())

    override fun resultsForSearch(rawQuery: String) = emptySingle
}
