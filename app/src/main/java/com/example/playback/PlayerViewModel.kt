package com.example.playback

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MusicRepository
import com.example.data.SongEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = MusicRepository(application)

  val allSongs: StateFlow<List<SongEntity>> = repository.allSongs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val likedSongs: StateFlow<List<SongEntity>> = repository.likedSongs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val downloadedSongs: StateFlow<List<SongEntity>> = repository.downloadedSongs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allPlaylists = repository.allPlaylists
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _currentSong = MutableStateFlow<SongEntity?>(null)
  val currentSong: StateFlow<SongEntity?> = _currentSong.asStateFlow()

  private val _isPlaying = MutableStateFlow(false)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _playbackPosition = MutableStateFlow(0L)
  val playbackPosition: StateFlow<Long> = _playbackPosition.asStateFlow()

  private val _playbackDuration = MutableStateFlow(0L)
  val playbackDuration: StateFlow<Long> = _playbackDuration.asStateFlow()

  private val _queue = MutableStateFlow<List<SongEntity>>(emptyList())
  val queue: StateFlow<List<SongEntity>> = _queue.asStateFlow()

  private val _playbackSpeed = MutableStateFlow(1.0f)
  val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

  private val _searchResults = MutableStateFlow<List<SongEntity>>(emptyList())
  val searchResults: StateFlow<List<SongEntity>> = _searchResults.asStateFlow()

  init {
    viewModelScope.launch(Dispatchers.IO) {
      repository.seedInitialData()
    }
  }

  fun playSong(song: SongEntity, playlist: List<SongEntity> = allSongs.value) {
    _currentSong.value = song
    _queue.value = playlist
    _isPlaying.value = true
    _playbackDuration.value = song.durationSeconds * 1000L
    _playbackPosition.value = 0L
  }

  fun togglePlayPause() {
    _isPlaying.value = !_isPlaying.value
  }

  fun seekTo(positionMs: Long) {
    _playbackPosition.value = positionMs
  }

  fun next() {
    val current = _currentSong.value ?: return
    val currentQueue = _queue.value
    val idx = currentQueue.indexOfFirst { it.id == current.id }
    if (idx != -1 && idx < currentQueue.size - 1) {
      playSong(currentQueue[idx + 1], currentQueue)
    } else if (currentQueue.isNotEmpty()) {
      playSong(currentQueue[0], currentQueue)
    }
  }

  fun previous() {
    val current = _currentSong.value ?: return
    val currentQueue = _queue.value
    val idx = currentQueue.indexOfFirst { it.id == current.id }
    if (idx > 0) {
      playSong(currentQueue[idx - 1], currentQueue)
    }
  }

  fun toggleLike(songId: String, currentLiked: Boolean) {
    viewModelScope.launch(Dispatchers.IO) {
      repository.setLiked(songId, !currentLiked)
    }
  }

  fun downloadSong(song: SongEntity) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val fileDir = File(getApplication<Application>().filesDir, "downloads")
        if (!fileDir.exists()) fileDir.mkdirs()
        val targetFile = File(fileDir, "${song.id}.mp3")
        
        // Download audio stream using OkHttp or URL connection
        val url = URL(song.audioUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()
        val input = connection.inputStream
        val output = targetFile.outputStream()
        input.copyTo(output)
        output.close()
        input.close()

        repository.setDownloaded(song.id, true, targetFile.absolutePath)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun setSpeed(speed: Float) {
    _playbackSpeed.value = speed
  }

  fun searchOnlineOrLocal(query: String) {
    if (query.isBlank()) {
      _searchResults.value = emptyList()
      return
    }
    viewModelScope.launch(Dispatchers.IO) {
      // Search local database first
      val localMatches = allSongs.value.filter {
        it.title.contains(query, true) || it.artist.contains(query, true)
      }.toMutableList()

      // Add dynamic YouTube Music search style mock results for any query
      val dynamicResults = listOf(
        SongEntity(
          id = "yt_${query.hashCode()}_1",
          title = "$query (Live Acoustic Mix)",
          artist = "Global Artist",
          album = "YouTube Music Stream",
          durationSeconds = 210,
          coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80",
          audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
          lyricsJson = "[0:00] Live session for $query\n[0:30] Playing live in high definition\n[1:00] Feel the energy flow"
        ),
        SongEntity(
          id = "yt_${query.hashCode()}_2",
          title = "Remix: $query Club Edit",
          artist = "DJ Liquid Glass",
          album = "Remix Vault",
          durationSeconds = 195,
          coverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&auto=format&fit=crop&q=80",
          audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
          lyricsJson = "[0:00] Club edit remix\n[0:35] Heavy bass and crystal synth\n[1:10] Dancing through the night"
        )
      )
      localMatches.addAll(dynamicResults)
      _searchResults.value = localMatches
    }
  }
}
