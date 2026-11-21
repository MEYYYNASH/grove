package com.example.ui.player

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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.SongEntity
import com.example.ui.components.GlassCard

@Composable
fun FullPlayerScreen(
  song: SongEntity?,
  isPlaying: Boolean,
  positionMs: Long,
  durationMs: Long,
  playbackSpeed: Float,
  onCloseClick: () -> Unit,
  onPlayPauseClick: () -> Unit,
  onNextClick: () -> Unit,
  onPrevClick: () -> Unit,
  onSeek: (Long) -> Unit,
  onLikeClick: (String, Boolean) -> Unit,
  onSpeedChange: (Float) -> Unit
) {
  if (song == null) return

  var selectedTab by remember { mutableStateOf(0) } // 0: Player, 1: Lyrics

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onCloseClick) {
        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Close player", tint = MaterialTheme.colorScheme.onBackground)
      }

      // Segmented Tabs Player / Lyrics
      GlassCard(cornerRadius = 24.dp) {
        Row(modifier = Modifier.padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent)
              .clickable { selectedTab = 0 }
              .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Player",
              style = MaterialTheme.typography.labelSmall,
              color = if (selectedTab == 0) Color.White else MaterialTheme.colorScheme.onSurface
            )
          }
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent)
              .clickable { selectedTab = 1 }
              .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Lyrics",
              style = MaterialTheme.typography.labelSmall,
              color = if (selectedTab == 1) Color.White else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      IconButton(onClick = {}) {
        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = MaterialTheme.colorScheme.onBackground)
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    if (selectedTab == 0) {
      // Large Cover Art (80% width)
      AsyncImage(
        model = song.coverUrl,
        contentDescription = song.title,
        modifier = Modifier
          .size(320.dp)
          .clip(RoundedCornerShape(28.dp)),
        contentScale = ContentScale.Crop
      )

      Spacer(modifier = Modifier.height(32.dp))

      // Title & Artist with Like Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = song.title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = song.artist,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        GlassCard(
          cornerRadius = 24.dp,
          onClick = { onLikeClick(song.id, song.isLiked) }
        ) {
          Box(modifier = Modifier.padding(12.dp)) {
            Icon(
              imageVector = if (song.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = "Like",
              tint = if (song.isLiked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(28.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Thin Seek Bar
      val sliderPosition = if (durationMs > 0) positionMs.toFloat() else 0f
      val sliderMax = if (durationMs > 0) durationMs.toFloat() else 1f

      Slider(
        value = sliderPosition,
        onValueChange = { onSeek(it.toLong()) },
        valueRange = 0f..sliderMax,
        modifier = Modifier.fillMaxWidth()
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = formatTime(positionMs),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = formatTime(durationMs),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Controls Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = {}) {
          Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = onPrevClick) {
          Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(36.dp))
        }

        // Big Round Play Button (76px)
        Box(
          modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onPlayPauseClick),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "Play/Pause",
            tint = Color.White,
            modifier = Modifier.size(40.dp)
          )
        }

        IconButton(onClick = onNextClick) {
          Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(36.dp))
        }
        IconButton(onClick = {}) {
          Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Glass Pills (Sleep timer & Speed)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        GlassCard(cornerRadius = 20.dp, onClick = {}) {
          Text(
            text = "Sleep Timer",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
          )
        }
        Spacer(modifier = Modifier.width(16.dp))
        GlassCard(
          cornerRadius = 20.dp,
          onClick = {
            val nextSpeed = if (playbackSpeed == 1.0f) 1.5f else 1.0f
            onSpeedChange(nextSpeed)
          }
        ) {
          Text(
            text = "Speed ${playbackSpeed}x",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
          )
        }
      }
    } else {
      // Lyrics View
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        val lines = song.lyricsJson.split("\n")
        lines.forEachIndexed { idx, line ->
          val isCurrent = idx == 1 // highlight active line
          Text(
            text = line.replace(Regex("\\[.*?\\]"), "").trim(),
            style = if (isCurrent) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(vertical = 12.dp)
          )
        }
      }
    }
  }
}

fun formatTime(ms: Long): String {
  val totalSeconds = ms / 1000
  val minutes = totalSeconds / 60
  val seconds = totalSeconds % 60
  return String.format("%d:%02d", minutes, seconds)
}
