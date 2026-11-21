package com.example.ui.search

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import coil.compose.AsyncImage
import com.example.data.SongEntity
import com.example.ui.components.GlassCard

@Composable
fun SearchScreen(
  searchResults: List<SongEntity>,
  onSearchQueryChanged: (String) -> Unit,
  onSongClick: (SongEntity) -> Unit,
  onDownloadClick: (SongEntity) -> Unit
) {
  var query by remember { mutableStateOf("") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 24.dp)
  ) {
    Text(
      text = "Search Music & YouTube",
      style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Large Glass Search Field
    GlassCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 24.dp
    ) {
      OutlinedTextField(
        value = query,
        onValueChange = {
          query = it
          onSearchQueryChanged(it)
        },
        placeholder = { Text("Search songs, artists, or YouTube music...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color.Transparent,
          unfocusedBorderColor = Color.Transparent,
          disabledBorderColor = Color.Transparent,
          focusedContainerColor = Color.Transparent,
          unfocusedContainerColor = Color.Transparent
        ),
        singleLine = true
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Genre Chips
    Text(
      text = "Explore Genres",
      style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(12.dp))

    val genres = listOf("Lofi Beats", "Synthwave", "Ambient", "Acoustic", "Jazz", "Electronic", "Classical")
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      genres.forEach { genre ->
        GlassCard(
          modifier = Modifier
            .width(130.dp)
            .height(70.dp),
          cornerRadius = 16.dp,
          onClick = {
            query = genre
            onSearchQueryChanged(genre)
          }
        ) {
          Box(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            contentAlignment = Alignment.CenterStart
          ) {
            Text(
              text = genre,
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    Text(
      text = if (query.isBlank()) "Recent & Recommended" else "Search Results",
      style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(14.dp))

    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      searchResults.forEach { song ->
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
                .size(54.dp)
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
            IconButton(onClick = { onDownloadClick(song) }) {
              Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download song",
                tint = if (song.isDownloaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(140.dp))
  }
}
