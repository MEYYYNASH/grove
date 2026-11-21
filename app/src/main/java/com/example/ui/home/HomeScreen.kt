package com.example.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.SongEntity
import com.example.ui.components.GlassCard

@Composable
fun HomeScreen(
  songs: List<SongEntity>,
  playlists: List<com.example.data.PlaylistEntity>,
  onSongClick: (SongEntity) -> Unit,
  onPlaylistClick: (com.example.data.PlaylistEntity) -> Unit
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 20.dp, vertical = 24.dp)
  ) {
    // Greeting & Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Good evening,",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = "Feel every beat.",
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onBackground
        )
      }
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "MM", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Quick Play Chips Row
    val quickChips = listOf("Chill Lofi", "Deep Focus", "Synthwave", "Sunset", "Workout")
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      quickChips.forEach { chip ->
        GlassCard(
          modifier = Modifier.height(40.dp),
          cornerRadius = 20.dp,
          onClick = {}
        ) {
          Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = chip,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    // Continue Listening Card
    if (songs.isNotEmpty()) {
      val featured = songs.first()
      GlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp),
        cornerRadius = 28.dp,
        onClick = { onSongClick(featured) }
      ) {
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          AsyncImage(
            model = featured.coverUrl,
            contentDescription = featured.title,
            modifier = Modifier
              .size(108.dp)
              .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
          )
          Spacer(modifier = Modifier.width(16.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "CONTINUE LISTENING",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = featured.title,
              style = MaterialTheme.typography.titleMedium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = featured.artist,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            androidx.compose.material3.Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = "Play",
              tint = Color.White
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    // Made for You Row
    Text(
      text = "Made for You",
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(14.dp))
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      playlists.forEach { playlist ->
        GlassCard(
          modifier = Modifier
            .width(160.dp)
            .height(210.dp),
          cornerRadius = 20.dp,
          onClick = { onPlaylistClick(playlist) }
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(12.dp)
          ) {
            AsyncImage(
              model = playlist.coverUrl,
              contentDescription = playlist.name,
              modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp)),
              contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = playlist.name,
              style = MaterialTheme.typography.bodyLarge,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = playlist.description,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    // Trending Now
    Text(
      text = "Trending Now",
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(14.dp))
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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

    // Bottom spacing for mini player & nav bar
    Spacer(modifier = Modifier.height(140.dp))
  }
}
