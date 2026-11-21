package com.example.ui.playlist

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.PlaylistEntity
import com.example.data.SongEntity
import com.example.ui.components.GlassCard

@Composable
fun PlaylistDetailScreen(
  playlist: PlaylistEntity,
  songs: List<SongEntity>,
  onBackClick: () -> Unit,
  onSongClick: (SongEntity) -> Unit,
  onPlayAll: () -> Unit,
  onShuffleClick: () -> Unit,
  onLikeClick: (String, Boolean) -> Unit
) {
  val playlistSongs = songs.filter { playlist.songIdsCsv.contains(it.id) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(20.dp)
  ) {
    IconButton(onClick = onBackClick) {
      Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Big Cover Art
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(280.dp)
        .clip(RoundedCornerShape(28.dp))
    ) {
      AsyncImage(
        model = playlist.coverUrl,
        contentDescription = playlist.name,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = playlist.name,
      style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = playlist.description,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Play & Shuffle buttons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Button(
        onClick = onPlayAll,
        modifier = Modifier.weight(1f).height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
      ) {
        Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Play", color = Color.White)
      }

      Button(
        onClick = onShuffleClick,
        modifier = Modifier.weight(1f).height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Shuffle", color = MaterialTheme.colorScheme.onSurface)
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Song List
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      playlistSongs.forEach { song ->
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
            IconButton(onClick = { onLikeClick(song.id, song.isLiked) }) {
              Icon(
                imageVector = if (song.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (song.isLiked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(140.dp))
  }
}
