package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.SongEntity

@Composable
fun MiniPlayer(
  song: SongEntity?,
  isPlaying: Boolean,
  positionMs: Long,
  durationMs: Long,
  onPlayPauseClick: () -> Unit,
  onNextClick: () -> Unit,
  onClick: () -> Unit
) {
  if (song == null) return

  val progress = if (durationMs > 0) (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f

  GlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .clickable(onClick = onClick),
    cornerRadius = 20.dp,
    elevation = 12.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp)),
          contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
          modifier = Modifier
            .weight(1f)
            .padding(end = 4.dp)
        ) {
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

        IconButton(onClick = onPlayPauseClick) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "Play/Pause",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
          )
        }

        IconButton(onClick = onNextClick) {
          Icon(
            imageVector = Icons.Default.SkipNext,
            contentDescription = "Next",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      // Thin progress line at the bottom
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(3.dp)
          .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
      )
    }
  }
}
