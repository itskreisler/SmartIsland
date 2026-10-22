/*
 * Smart Island (2026)
 * © Animesh Gupta — github.com/agupta07505
 * Licensed under the GNU GPL v3 License
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.agupta07505.smartisland.ui.sections
import androidx.compose.ui.res.stringResource
import com.agupta07505.smartisland.R

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agupta07505.smartisland.data.SmartIslandSettings
import com.agupta07505.smartisland.data.SmartIslandSettingsRepository
import com.agupta07505.smartisland.ui.glass.Card
import com.agupta07505.smartisland.util.FaceUnlockLog
import com.agupta07505.smartisland.util.FaceUnlockLogEvent
import com.agupta07505.smartisland.util.FaceUnlockMode
import com.agupta07505.smartisland.util.ShizukuManager
import com.agupta07505.smartisland.util.UnlockType
import com.agupta07505.smartisland.util.UnlockTypeTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FaceUnlockSection(
    settings: SmartIslandSettings,
    repository: SmartIslandSettingsRepository
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var capturing by remember { mutableStateOf(false) }
    var busy by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf<String?>(null) }
    var captureText by remember { mutableStateOf<String?>(null) }
    val mode = FaceUnlockMode.fromId(settings.faceUnlockMode)
    val lastUnlock by UnlockTypeTracker.last.collectAsState()

    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri: Uri? ->
        val text = captureText
        if (uri == null || text == null) return@rememberLauncherForActivityResult
        scope.launch {
            val ok = withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openOutputStream(uri, "wt")?.use { it.write(text.toByteArray(Charsets.UTF_8)) } != null
                }.getOrDefault(false)
            }
            Toast.makeText(context, if (ok) context.getString(R.string.toast_capture_saved) else context.getString(R.string.toast_saving_failed), Toast.LENGTH_LONG).show()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.face_section_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.face_section_desc),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = lastUnlock?.let {
                    stringResource(
                        R.string.face_last_unlock,
                        stringResource(it.type.labelRes),
                        java.text.DateFormat.getTimeInstance().format(java.util.Date(it.atMillis))
                    )
                } ?: stringResource(R.string.face_last_unlock_none),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = mode == FaceUnlockMode.FaceOnly,
                    onClick = { scope.launch { repository.setFaceUnlockMode(FaceUnlockMode.FaceOnly.id) } },
                    label = { Text(stringResource(R.string.face_unlock_only)) }
                )
                FilterChip(
                    selected = mode == FaceUnlockMode.Any,
                    onClick = { scope.launch { repository.setFaceUnlockMode(FaceUnlockMode.Any.id) } },
                    label = { Text(stringResource(R.string.any_unlock)) }
                )
            }
            Text(
                text = stringResource(R.string.face_capture_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.face_capture_steps),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (!capturing) {
                    Button(
                        enabled = !busy,
                        onClick = {
                            if (!ShizukuManager.hasPermission()) {
                                status = context.getString(R.string.face_capture_shizuku_missing)
                                return@Button
                            }
                            busy = true
                            scope.launch {
                                val result = ShizukuManager.runShellForOutput(
                                    "log -t ${FaceUnlockLog.CAPTURE_TAG} ${FaceUnlockLog.CAPTURE_MARKER_TEXT}"
                                )
                                busy = false
                                if (result.isSuccess) {
                                    capturing = true
                                    captureText = null
                                    status = context.getString(R.string.face_capture_recording)
                                } else {
                                    status = context.getString(
                                        R.string.face_capture_start_failed,
                                        result.exceptionOrNull()?.localizedMessage
                                            ?: context.getString(R.string.face_capture_shell_error)
                                    )
                                }
                            }
                        }
                    ) { Text(stringResource(R.string.btn_start)) }
                } else {
                    Button(
                        enabled = !busy,
                        onClick = {
                            busy = true
                            scope.launch {
                                val result = ShizukuManager.runShellForOutput("logcat -d -v threadtime -b main,system -t 30000")
                                busy = false
                                capturing = false
                                val dump = result.getOrNull()
                                if (dump == null) {
                                    status = context.getString(
                                        R.string.face_capture_read_failed,
                                        result.exceptionOrNull()?.localizedMessage
                                            ?: context.getString(R.string.face_capture_shell_error)
                                    )
                                    return@launch
                                }
                                val lines = FaceUnlockLog.extractCapture(dump)
                                val found = FaceUnlockLog.summarize(lines)
                                val unlocks = FaceUnlockLog.summarizeUnlocks(lines)
                                val unlockSummary = UnlockType.entries.joinToString(" ") {
                                    "${it.name.lowercase()}=${unlocks[it] ?: 0}"
                                }
                                captureText = buildString {
                                    appendLine("Smart Island face unlock capture")
                                    appendLine("Device: ${Build.MANUFACTURER} ${Build.MODEL}, Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})")
                                    appendLine("Build: ${Build.DISPLAY}")
                                    appendLine(
                                        "Detected: start=${found[FaceUnlockLogEvent.Start] ?: 0} " +
                                            "success=${found[FaceUnlockLogEvent.Success] ?: 0} " +
                                            "failure=${found[FaceUnlockLogEvent.Failure] ?: 0} " +
                                            "ended=${found[FaceUnlockLogEvent.End] ?: 0}"
                                    )
                                    appendLine("Unlock types: $unlockSummary")
                                    appendLine("Lines: ${lines.size} (lines the detector recognised end with <<< what it found)")
                                    appendLine()
                                    lines.forEach { line ->
                                        val note = FaceUnlockLog.annotation(line)
                                        appendLine(if (note == null) line else "$line   <<< $note")
                                    }
                                }
                                status = context.getString(
                                    R.string.face_capture_done,
                                    lines.size,
                                    found[FaceUnlockLogEvent.Start] ?: 0,
                                    found[FaceUnlockLogEvent.Success] ?: 0,
                                    found[FaceUnlockLogEvent.Failure] ?: 0,
                                    found[FaceUnlockLogEvent.End] ?: 0,
                                    unlockSummary
                                )
                            }
                        }
                    ) { Text(stringResource(R.string.btn_stop)) }
                }
                OutlinedButton(
                    enabled = captureText != null && !capturing,
                    onClick = { saveLauncher.launch("smartisland_face_unlock_capture.txt") }
                ) { Text(stringResource(R.string.btn_save_capture)) }
            }
            status?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
internal fun FaceStyleChips(selected: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = selected == "square_tick",
            onClick = { onSelect("square_tick") },
            label = { Text(stringResource(R.string.face_unlock_mode_expanded)) }
        )
        FilterChip(
            selected = selected != "cutout" && selected != "square_tick",
            onClick = { onSelect("square") },
            label = { Text(stringResource(R.string.face_unlock_mode_mix)) }
        )
        FilterChip(
            selected = selected == "cutout",
            onClick = { onSelect("cutout") },
            label = { Text(stringResource(R.string.face_unlock_mode_cutout)) }
        )
    }
}
