package newuibrowser.di

import newuibrowser.browser.cleanup.DelegatingExitCleanup
import newuibrowser.browser.cleanup.ExitCleanup
import newuibrowser.database.adblock.UserRulesDatabase
import newuibrowser.database.adblock.UserRulesRepository
import newuibrowser.database.bookmark.BookmarkDatabase
import newuibrowser.database.bookmark.BookmarkRepository
import newuibrowser.database.downloads.DownloadsDatabase
import newuibrowser.database.downloads.DownloadsRepository
import newuibrowser.database.history.HistoryDatabase
import newuibrowser.database.history.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dependency injection module used to bind implementations to interfaces.
 * SL: Looks like those are still actually needed.
 */
@Module
@InstallIn(SingletonComponent::class)
interface AppBindsModule {

    @Binds
    fun bindsExitCleanup(delegatingExitCleanup: DelegatingExitCleanup): ExitCleanup

    @Binds
    fun bindsBookmarkModel(bookmarkDatabase: BookmarkDatabase): BookmarkRepository

    @Binds
    fun bindsDownloadsModel(downloadsDatabase: DownloadsDatabase): DownloadsRepository

    @Binds
    fun bindsHistoryModel(historyDatabase: HistoryDatabase): HistoryRepository

    @Binds
    fun bindsAbpRulesRepository(apbRulesDatabase: UserRulesDatabase): UserRulesRepository

}
