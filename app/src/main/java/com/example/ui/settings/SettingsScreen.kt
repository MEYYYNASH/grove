package com.example.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.update.UpdateState
import com.example.ui.components.GlassCard

@Composable
fun SettingsScreen(
  currentVersion: String,
  updateState: UpdateState,
  latestVersion: String,
  releaseNotes: String,
  downloadProgress: Float,
  onCheckUpdate: () -> Unit,
  onDownloadAndUpdate: () -> Unit,
  onInstall: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 24.dp)
  ) {
    Text(
      text = "Settings",
      style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(20.dp))

    // App Update & Release Card (as requested)
    GlassCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 24.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "App Update & Release",
          style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Current Version: v$currentVersion",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        when (updateState) {
          UpdateState.UP_TO_DATE -> {
            Text(
              text = "You are running the latest version.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = onCheckUpdate,
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Text("Check Now")
            }
          }
          UpdateState.CHECKING -> {
            Text(
              text = "Checking GitHub releases...",
              style = MaterialTheme.typography.bodyMedium
            )
          }
          UpdateState.UPDATE_AVAILABLE -> {
            Text(
              text = "Update Available: v$latestVersion",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = releaseNotes,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = onDownloadAndUpdate,
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Text("Download Update")
            }
          }
          UpdateState.DOWNLOADING -> {
            Text(
              text = "Downloading update... ${(downloadProgress * 100).toInt()}%",
              style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
              progress = { downloadProgress },
              modifier = Modifier.fillMaxWidth(),
              color = MaterialTheme.colorScheme.primary
            )
          }
          UpdateState.READY_TO_INSTALL -> {
            Text(
              text = "Ready to install v$latestVersion",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = onInstall,
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Text("Install Now")
            }
          }
          UpdateState.ERROR -> {
            Text(
              text = "Failed to check or download update.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = onCheckUpdate,
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
              Text("Retry")
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Appearance Card
    GlassCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 24.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "Appearance & Theme",
          style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Soft Liquid Glass UI with dynamic cover blur.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Sound & Audio Card
    GlassCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 24.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "Sound & Audio Engine",
          style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "ExoPlayer Media3 background playback & audio focus.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Spacer(modifier = Modifier.height(140.dp))
  }
}
