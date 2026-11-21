package com.example.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class MusicRepository(context: Context) {
  private val database = MusicDatabase.getDatabase(context)
  private val songDao = database.songDao()
  private val playlistDao = database.playlistDao()

  val allSongs: Flow<List<SongEntity>> = songDao.getAllSongs()
  val likedSongs: Flow<List<SongEntity>> = songDao.getLikedSongs()
  val downloadedSongs: Flow<List<SongEntity>> = songDao.getDownloadedSongs()
  val allPlaylists: Flow<List<PlaylistEntity>> = playlistDao.getAllPlaylists()

  fun search(query: String): Flow<List<SongEntity>> = songDao.searchSongs(query)

  suspend fun setLiked(songId: String, isLiked: Boolean) {
    songDao.updateLiked(songId, isLiked)
  }

  suspend fun setDownloaded(songId: String, isDownloaded: Boolean, localPath: String?) {
    songDao.updateDownloaded(songId, isDownloaded, localPath)
  }

  suspend fun seedInitialData() {
    val initialSongs = listOf(
      SongEntity(
        id = "1",
        title = "Midnight Lofi Glow",
        artist = "Aura Beats",
        album = "Liquid Glass Sessions",
        durationSeconds = 214,
        coverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&auto=format&fit=crop&q=80",
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
        isLiked = true,
        lyricsJson = "[0:00] Midnight lights glowing soft\n[0:15] Breathing rhythm in the dark\n[0:32] Feel every beat inside your soul\n[0:50] Glass reflection takes control"
      ),
      SongEntity(
        id = "2",
        title = "Neon Horizon",
        artist = "Celeste & Nova",
        album = "Synthwave Odyssey",
        durationSeconds = 188,
        coverUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
        isLiked = true,
        lyricsJson = "[0:00] Neon city pulse awake\n[0:18] Every step a new mistake\n[0:35] We ride the wave into the night\n[0:55] Glowing warm and feeling right"
      ),
      SongEntity(
        id = "3",
        title = "Warm Amber Sunset",
        artist = "Solstice Duo",
        album = "Organic Waves",
        durationSeconds = 245,
        coverUrl = "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=800&auto=format&fit=crop&q=80",
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
        isLiked = false,
        lyricsJson = "[0:00] Sunset casting amber light\n[0:20] Stars awakening the night\n[0:40] Calm and quiet peace flows in\n[1:05] Where the magic will begin"
      ),
      SongEntity(
        id = "4",
        title = "Crystal Echoes",
        artist = "Echo Chamber",
        album = "Glass & Steel",
        durationSeconds = 195,
        coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80",
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
        isLiked = false,
        lyricsJson = "[0:00] Echoes bouncing off the glass\n[0:22] Moments that we watch pass\n[0:45] Pure vibration in the air\n[1:10] Music playing everywhere"
      ),
      SongEntity(
        id = "5",
        title = "Velvet Breeze",
        artist = "Luna Park",
        album = "Smooth Velvet",
        durationSeconds = 220,
        coverUrl = "https://images.unsplash.com/photo-1465847899084-d164df4dedc6?w=800&auto=format&fit=crop&q=80",
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
        isLiked = true,
        lyricsJson = "[0:00] Velvet breeze upon the skin\n[0:25] Let the gentle rhythm spin\n[0:50] Smooth and calm, no rush today\n[1:15] Flowing softly far away"
      )
    )

    val initialPlaylists = listOf(
      PlaylistEntity(
        id = "p1",
        name = "Deep Focus Glass",
        description = "Calm electronic & lofi beats for deep focus.",
        coverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&auto=format&fit=crop&q=80",
        songIdsCsv = "1,2,4"
      ),
      PlaylistEntity(
        id = "p2",
        name = "Sunset Chill",
        description = "Warm organic grooves for evening relaxation.",
        coverUrl = "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=800&auto=format&fit=crop&q=80",
        songIdsCsv = "3,5"
      )
    )

    songDao.insertSongs(initialSongs)
    playlistDao.insertPlaylists(initialPlaylists)
  }
}
