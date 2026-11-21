package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
  @Query("SELECT * FROM songs")
  fun getAllSongs(): Flow<List<SongEntity>>

  @Query("SELECT * FROM songs WHERE isLiked = 1")
  fun getLikedSongs(): Flow<List<SongEntity>>

  @Query("SELECT * FROM songs WHERE isDownloaded = 1")
  fun getDownloadedSongs(): Flow<List<SongEntity>>

  @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
  fun searchSongs(query: String): Flow<List<SongEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSongs(songs: List<SongEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSong(song: SongEntity)

  @Query("UPDATE songs SET isLiked = :isLiked WHERE id = :songId")
  suspend fun updateLiked(songId: String, isLiked: Boolean)

  @Query("UPDATE songs SET isDownloaded = :isDownloaded, localPath = :localPath WHERE id = :songId")
  suspend fun updateDownloaded(songId: String, isDownloaded: Boolean, localPath: String?)
}

@Dao
interface PlaylistDao {
  @Query("SELECT * FROM playlists")
  fun getAllPlaylists(): Flow<List<PlaylistEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPlaylists(playlists: List<PlaylistEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPlaylist(playlist: PlaylistEntity)
}

@Database(entities = [SongEntity::class, PlaylistEntity::class], version = 1, exportSchema = false)
abstract class MusicDatabase : RoomDatabase() {
  abstract fun songDao(): SongDao
  abstract fun playlistDao(): PlaylistDao

  companion object {
    @Volatile
    private var INSTANCE: MusicDatabase? = null

    fun getDatabase(context: Context): MusicDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          MusicDatabase::class.java,
          "meu_music_db"
        ).build()
        INSTANCE = instance
        instance
      }
    }
  }
}
