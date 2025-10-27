/*
 * Copyright © 2020-2021 Jamal Rothfuchs
 * Copyright © 2020-2021 Stéphane Lenclud
 * Copyright © 2015 Anthony Restaino
 */

package newuibrowser.search.engine

import newuibrowser.R

/**
 * The Ekoru search engine.
 */
class EkoruSearch : BaseSearchEngine(
    "file:///android_asset/ekoru.webp",
    "https://www.ekoru.org/?ext=uibrowser&q=",
    R.string.search_engine_ekoru
)
