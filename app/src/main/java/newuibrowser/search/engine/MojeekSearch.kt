/*
 * Copyright © 2020-2021 Jamal Rothfuchs
 * Copyright © 2020-2021 Stéphane Lenclud
 * Copyright © 2015 Anthony Restaino
 */

package newuibrowser.search.engine

import newuibrowser.R

/**
 * The Mojeek search engine.
 */
class MojeekSearch : BaseSearchEngine(
    "file:///android_asset/mojeek.webp",
    "https://www.mojeek.com/search?q=",
    R.string.search_engine_mojeek
)
