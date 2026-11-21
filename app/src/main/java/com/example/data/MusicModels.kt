package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
  @PrimaryKey val id: String,
  val title: String,
  val artist: String,
  val album: String,
  val durationSeconds: Long,
  val coverUrl: String,
  val audioUrl: String,
  val isLiked: Boolean = false,
  val isDownloaded: Boolean = false,
  val localPath: String? = null,
  val lyricsJson: String = "" // JSON or newline separated timestamped lyrics like "[0:12] Hello world"
)

@Entity(tableName = "playlists")
data class PlaylistEntity(
  @PrimaryKey val id: String,
  val name: String,
  val description: String,
  val coverUrl: String,
  val songIdsCsv: String
)

data class LyricLine(
  val timeMs: Long,
  val text: String
)
