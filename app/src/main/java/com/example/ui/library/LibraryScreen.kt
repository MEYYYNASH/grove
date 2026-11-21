package com.example.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.PlaylistEntity
import com.example.data.SongEntity
import com.example.ui.components.GlassCard

@Composable
fun LibraryScreen(
  songs: List<SongEntity>,
  playlists: List<PlaylistEntity>,
  onSongClick: (SongEntity) -> Unit,
  onPlaylistClick: (PlaylistEntity) -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Playlists", "Songs", "Liked", "Albums")
  var isGrid by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 24.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Your Library",
        style = MaterialTheme.typography.titleLarge
      )
      IconButton(onClick = { isGrid = !isGrid }) {
        Icon(
          imageVector = if (isGrid) Icons.Default.ViewList else Icons.Default.GridView,
          contentDescription = "Toggle view",
          tint = MaterialTheme.colorScheme.onBackground
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Tabs Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      tabs.forEachIndexed { index, tab ->
        val selected = selectedTab == index
        GlassCard(
          modifier = Modifier.weight(1f),
          cornerRadius = 16.dp,
          backgroundColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
          onClick = { selectedTab = index }
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = tab,
              style = MaterialTheme.typography.labelSmall,
              color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    when (selectedTab) {
      0 -> {
        // Playlists
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
          playlists.forEach { playlist ->
            GlassCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 20.dp,
              onClick = { onPlaylistClick(playlist) }
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                AsyncImage(
                  model = playlist.coverUrl,
                  contentDescription = playlist.name,
                  modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp)),
                  contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = playlist.name,
                    style = MaterialTheme.typography.titleMedium
                  )
                  Text(
                    text = playlist.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }
          }
        }
      }
      1 -> {
        // Songs
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          songs.forEach { song ->
            GlassCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 16.dp,
              onClick = { onSongClick(song) }
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                AsyncImage(
                  model = song.coverUrl,
                  contentDescription = song.title,
                  modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp)),
                  contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = song.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }
          }
        }
      }
      2 -> {
        // Liked Songs
        val liked = songs.filter { it.isLiked }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          liked.forEach { song ->
            GlassCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 16.dp,
              onClick = { onSongClick(song) }
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                AsyncImage(
                  model = song.coverUrl,
                  contentDescription = song.title,
                  modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp)),
                  contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = song.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
                Icon(
                  imageVector = Icons.Default.Favorite,
                  contentDescription = "Liked",
                  tint = MaterialTheme.colorScheme.secondary
                )
              }
            }
          }
        }
      }
      3 -> {
        // Albums
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          songs.distinctBy { it.album }.forEach { song ->
            GlassCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 16.dp,
              onClick = { onSongClick(song) }
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                AsyncImage(
                  model = song.coverUrl,
                  contentDescription = song.album,
                  modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp)),
                  contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = song.album,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(140.dp))
  }
}
