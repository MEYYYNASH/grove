package com.example


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.playback.PlayerViewModel
import com.example.ui.components.GlassBottomBar
import com.example.ui.components.MiniPlayer
import com.example.ui.components.Screen
import com.example.ui.downloads.DownloadsScreen
import com.example.ui.home.HomeScreen
import com.example.ui.library.LibraryScreen
import com.example.ui.player.FullPlayerScreen
import com.example.ui.playlist.PlaylistDetailScreen
import com.example.ui.search.SearchScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.MeuMusicTheme
import com.example.update.UpdateManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  private val playerViewModel: PlayerViewModel by viewModels()
  private lateinit var updateManager: UpdateManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    updateManager = UpdateManager(this)

    lifecycleScope.launch {
      updateManager.checkForUpdates(BuildConfig.VERSION_NAME, "owner", "repo")
    }

    setContent {
      MeuMusicTheme {
        MainAppContent(playerViewModel, updateManager)
      }
    }
  }
}

@Composable
fun MainAppContent(viewModel: PlayerViewModel, updateManager: UpdateManager) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

  val currentSong by viewModel.currentSong.collectAsState()
  val isPlaying by viewModel.isPlaying.collectAsState()
  val position by viewModel.playbackPosition.collectAsState()
  val duration by viewModel.playbackDuration.collectAsState()
  val speed by viewModel.playbackSpeed.collectAsState()
  val songs by viewModel.allSongs.collectAsState()
  val playlists by viewModel.allPlaylists.collectAsState()
  val searchResults by viewModel.searchResults.collectAsState()
  val downloaded by viewModel.downloadedSongs.collectAsState()

  val updateState by updateManager.state.collectAsState()
  val latestVersion by updateManager.latestVersion.collectAsState()
  val releaseNotes by updateManager.releaseNotes.collectAsState()
  val progress by updateManager.progress.collectAsState()

  var showFullPlayer by remember { mutableStateOf(false) }
  var selectedPlaylistId by remember { mutableStateOf<String?>(null) }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      if (!showFullPlayer) {
        Box(modifier = Modifier.fillMaxWidth()) {
          androidx.compose.foundation.layout.Column(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.BottomCenter)
          ) {
            MiniPlayer(
              song = currentSong,
              isPlaying = isPlaying,
              positionMs = position,
              durationMs = duration,
              onPlayPauseClick = { viewModel.togglePlayPause() },
              onNextClick = { viewModel.next() },
              onClick = { showFullPlayer = true }
            )
            GlassBottomBar(
              currentRoute = currentRoute,
              onItemSelected = { screen ->
                navController.navigate(screen.route) {
                  popUpTo(navController.graph.startDestinationId) { saveState = true }
                  launchSingleTop = true
                  restoreState = true
                }
              }
            )
          }
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(innerPadding)
    ) {
      NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.fillMaxSize()
      ) {
        composable(Screen.Home.route) {
          HomeScreen(
            songs = songs,
            playlists = playlists,
            onSongClick = { song -> viewModel.playSong(song) },
            onPlaylistClick = { playlist ->
              selectedPlaylistId = playlist.id
              navController.navigate("playlist_detail")
            }
          )
        }
        composable(Screen.Search.route) {
          SearchScreen(
            searchResults = searchResults,
            onSearchQueryChanged = { q -> viewModel.searchOnlineOrLocal(q) },
            onSongClick = { song -> viewModel.playSong(song) },
            onDownloadClick = { song -> viewModel.downloadSong(song) }
          )
        }
        composable(Screen.Library.route) {
          LibraryScreen(
            songs = songs,
            playlists = playlists,
            onSongClick = { song -> viewModel.playSong(song) },
            onPlaylistClick = { playlist ->
              selectedPlaylistId = playlist.id
              navController.navigate("playlist_detail")
            }
          )
        }
        composable(Screen.Settings.route) {
          SettingsScreen(
            currentVersion = BuildConfig.VERSION_NAME,
            updateState = updateState,
            latestVersion = latestVersion,
            releaseNotes = releaseNotes,
            downloadProgress = progress,
            onCheckUpdate = {
              kotlinx.coroutines.GlobalScope.launch {
                updateManager.checkForUpdates(BuildConfig.VERSION_NAME, "owner", "repo")
              }
            },
            onDownloadAndUpdate = {
              kotlinx.coroutines.GlobalScope.launch {
                updateManager.downloadAndUpdate("https://github.com/owner/repo/releases/download/v$latestVersion/MeuMusic-$latestVersion.apk")
              }
            },
            onInstall = { updateManager.installApk() }
          )
        }
        composable("playlist_detail") {
          val playlist = playlists.find { it.id == selectedPlaylistId } ?: playlists.firstOrNull()
          if (playlist != null) {
            PlaylistDetailScreen(
              playlist = playlist,
              songs = songs,
              onBackClick = { navController.popBackStack() },
              onSongClick = { song -> viewModel.playSong(song) },
              onPlayAll = {
                val pSongs = songs.filter { playlist.songIdsCsv.contains(it.id) }
                if (pSongs.isNotEmpty()) viewModel.playSong(pSongs.first(), pSongs)
              },
              onShuffleClick = {
                val pSongs = songs.filter { playlist.songIdsCsv.contains(it.id) }.shuffled()
                if (pSongs.isNotEmpty()) viewModel.playSong(pSongs.first(), pSongs)
              },
              onLikeClick = { id, liked -> viewModel.toggleLike(id, liked) }
            )
          }
        }
      }

      // Full Player Overlay
      AnimatedVisibility(
        visible = showFullPlayer,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
      ) {
        FullPlayerScreen(
          song = currentSong,
          isPlaying = isPlaying,
          positionMs = position,
          durationMs = duration,
          playbackSpeed = speed,
          onCloseClick = { showFullPlayer = false },
          onPlayPauseClick = { viewModel.togglePlayPause() },
          onNextClick = { viewModel.next() },
          onPrevClick = { viewModel.previous() },
          onSeek = { pos -> viewModel.seekTo(pos) },
          onLikeClick = { id, liked -> viewModel.toggleLike(id, liked) },
          onSpeedChange = { s -> viewModel.setSpeed(s) }
        )
      }
    }
  }
}
