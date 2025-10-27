package newuibrowser.di

import newuibrowser.adblock.AbpBlockerManager
import newuibrowser.adblock.AbpUserRules
import newuibrowser.adblock.NoOpAdBlocker
import newuibrowser.browser.TabsManager
import newuibrowser.database.bookmark.BookmarkRepository
import newuibrowser.database.downloads.DownloadsRepository
import newuibrowser.database.history.HistoryRepository
import newuibrowser.dialog.LightningDialogBuilder
import newuibrowser.download.DownloadHandler
import newuibrowser.favicon.FaviconModel
import newuibrowser.html.homepage.HomePageFactory
import newuibrowser.js.InvertPage
import newuibrowser.js.SetMetaViewport
import newuibrowser.js.TextReflow
import newuibrowser.network.NetworkConnectivityModel
import newuibrowser.search.SearchEngineProvider
import newuibrowser.settings.preferences.UserPreferences
import newuibrowser.view.webrtc.WebRtcPermissionsModel
import android.app.DownloadManager
import android.content.ClipboardManager
import android.content.SharedPreferences
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler

/**
 * Provide access to all our injectable classes.
 * Virtual fields can't resolve qualifiers for some reason.
 * Therefore we use functions where qualifiers are needed.
 *
 * Just add your class here if you need it.
 *
 * TODO: See if and how we can use the 'by' syntax to initialize usage of those.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface HiltEntryPoint {

    val bookmarkRepository: BookmarkRepository
    val userPreferences: UserPreferences
    @UserPrefs
    fun userSharedPreferences(): SharedPreferences
    val historyRepository: HistoryRepository
    @DatabaseScheduler
    fun databaseScheduler(): Scheduler
    @NetworkScheduler
    fun networkScheduler(): Scheduler
    @DiskScheduler
    fun diskScheduler(): Scheduler
    @MainScheduler
    fun mainScheduler(): Scheduler
    val searchEngineProvider: SearchEngineProvider
    val textReflowJs: TextReflow
    val invertPageJs: InvertPage
    val setMetaViewport: SetMetaViewport
    val homePageFactory: HomePageFactory
    val abpBlockerManager: AbpBlockerManager
    val noopBlocker: NoOpAdBlocker
    val dialogBuilder: LightningDialogBuilder
    val networkConnectivityModel: NetworkConnectivityModel
    val faviconModel: FaviconModel
    val webRtcPermissionsModel: WebRtcPermissionsModel
    val abpUserRules: AbpUserRules
    val downloadHandler: DownloadHandler
    val downloadManager: DownloadManager
    val downloadsRepository: DownloadsRepository
    var tabsManager: TabsManager
    var clipboardManager: ClipboardManager

}


