package com.example.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

enum class UpdateState {
  UP_TO_DATE,
  CHECKING,
  UPDATE_AVAILABLE,
  DOWNLOADING,
  READY_TO_INSTALL,
  ERROR
}

class UpdateManager(private val context: Context) {
  private val client = OkHttpClient()

  private val _state = MutableStateFlow(UpdateState.UP_TO_DATE)
  val state: StateFlow<UpdateState> = _state

  private val _progress = MutableStateFlow(0f)
  val progress: StateFlow<Float> = _progress

  private val _latestVersion = MutableStateFlow("1.0")
  val latestVersion: StateFlow<String> = _latestVersion

  private val _releaseNotes = MutableStateFlow("")
  val releaseNotes: StateFlow<String> = _releaseNotes

  private var downloadedApkFile: File? = null

  suspend fun checkForUpdates(currentVersion: String, owner: String = "owner", repo: String = "repo") {
    _state.value = UpdateState.CHECKING
    try {
      withContext(Dispatchers.IO) {
        val request = Request.Builder()
          .url("https://api.github.com/repos/$owner/$repo/releases/latest")
          .header("Accept", "application/vnd.github+json")
          .build()

        client.newCall(request).execute().use { response ->
          if (response.isSuccessful) {
            val bodyString = response.body?.string() ?: "{\"" + "tag_name" + "\":\"v1.0\"}"
            val json = JSONObject(bodyString)
            val tagName = json.optString("tag_name", "v1.0").removePrefix("v")
            val body = json.optString("body", "Bug fixes and performance enhancements.")
            
            _latestVersion.value = tagName
            _releaseNotes.value = body

            if (tagName != currentVersion && tagName > currentVersion) {
              _state.value = UpdateState.UPDATE_AVAILABLE
            } else {
              _state.value = UpdateState.UP_TO_DATE
            }
          } else {
            // Fallback for simulation / offline demo
            _state.value = UpdateState.UP_TO_DATE
          }
        }
      }
    } catch (e: Exception) {
      // If network fails or repo is placeholder, simulate available update for demo if requested
      _state.value = UpdateState.UP_TO_DATE
    }
  }

  suspend fun downloadAndUpdate(apkUrl: String) {
    _state.value = UpdateState.DOWNLOADING
    _progress.value = 0f
    try {
      withContext(Dispatchers.IO) {
        val request = Request.Builder().url(apkUrl).build()
        client.newCall(request).execute().use { response ->
          if (!response.isSuccessful) throw Exception("Failed to download update")
          val body = response.body ?: throw Exception("Empty body")
          val contentLength = body.contentLength()

          val updatesDir = File(context.cacheDir, "updates")
          if (!updatesDir.exists()) updatesDir.mkdirs()
          val apkFile = File(updatesDir, "meu_music_update.apk")

          FileOutputStream(apkFile).use { output ->
            val input = body.byteStream()
            val buffer = ByteArray(8192)
            var bytesCopied = 0L
            var bytes = input.read(buffer)
            while (bytes >= 0) {
              output.write(buffer, 0, bytes)
              bytesCopied += bytes
              if (contentLength > 0) {
                _progress.value = bytesCopied.toFloat() / contentLength.toFloat()
              }
              bytes = input.read(buffer)
            }
          }
          downloadedApkFile = apkFile
          _state.value = UpdateState.READY_TO_INSTALL
        }
      }
    } catch (e: Exception) {
      _state.value = UpdateState.ERROR
    }
  }

  fun installApk() {
    val file = downloadedApkFile ?: return
    try {
      val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
      )
      val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}
