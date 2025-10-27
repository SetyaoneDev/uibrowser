/*
 * Copyright © 2020-2021 Jamal Rothfuchs
 * Copyright © 2020-2021 Stéphane Lenclud
 * Copyright © 2015 Anthony Restaino
 */

package newuibrowser.search.engine

import newuibrowser.R
import newuibrowser.settings.preferences.UserPreferences

/**
 * A custom search engine.
 */
class CustomSearch(queryUrl: String, userPreferences: UserPreferences) : BaseSearchEngine(
    userPreferences.imageUrlString,
    queryUrl,
    R.string.search_engine_custom
)
