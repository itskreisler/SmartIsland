/*
 * Smart Island (2026)
 * © Animesh Gupta — github.com/agupta07505
 * Licensed under the GNU GPL v3 License
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.agupta07505.smartisland.model

import androidx.annotation.StringRes
import com.agupta07505.smartisland.R

/**
 * Every activity the island can show, each with its own switch. `id` is persisted in
 * SmartIslandSettings.disabledEvents; `label` is the long description (Demo tab), `shortLabel` fits a Home tile.
 * `label`, `shortLabel` and `group` are the untranslated originals: they are never shown, only used as stable keys
 * for saved state and search. What the user reads comes from `labelRes`, `shortLabelRes` and `groupRes`.
 */
enum class IslandEvent(
    val id: String,
    val label: String,
    val shortLabel: String,
    val group: String,
    @StringRes val labelRes: Int,
    @StringRes val shortLabelRes: Int,
    @StringRes val groupRes: Int
) {
    PLUGGED_IN(
        "battery_plugged_in", "Charger plugged in (brief card)", "Plugged in", "Power",
        R.string.event_battery_plugged_in_label, R.string.event_battery_plugged_in_short,
        R.string.event_group_power
    ),
    PLUGGED_OUT(
        "battery_plugged_out", "Charger unplugged", "Unplugged", "Power",
        R.string.event_battery_plugged_out_label, R.string.battery_unplugged,
        R.string.event_group_power
    ),
    FULLY_CHARGED(
        "battery_full", "Fully charged", "Full", "Power",
        R.string.event_battery_full_label, R.string.event_battery_full_short,
        R.string.event_group_power
    ),
    LOW_BATTERY(
        "battery_low", "Low battery", "Low battery", "Power",
        R.string.event_battery_low_label, R.string.event_battery_low_label,
        R.string.event_group_power
    ),
    BATTERY_SAVER(
        "battery_saver", "Battery saver", "Saver", "Power",
        R.string.utility_battery_saver, R.string.event_battery_saver_short,
        R.string.event_group_power
    ),

    WIFI(
        "wifi", "Wi-Fi connected (shows network name)", "Wi-Fi", "Connectivity",
        R.string.event_wifi_label, R.string.utility_wifi,
        R.string.event_group_connectivity
    ),
    VPN(
        "vpn", "VPN connected (shows location)", "VPN", "Connectivity",
        R.string.event_vpn_label, R.string.event_vpn_short,
        R.string.event_group_connectivity
    ),
    WIFI_DISCONNECTED(
        "wifi_disconnected", "Wi-Fi disconnected", "Wi-Fi lost", "Connectivity",
        R.string.event_wifi_disconnected_label, R.string.event_wifi_disconnected_short,
        R.string.event_group_connectivity
    ),
    VPN_DISCONNECTED(
        "vpn_disconnected", "VPN disconnected (traffic no longer protected)", "VPN lost", "Connectivity",
        R.string.event_vpn_disconnected_label, R.string.event_vpn_disconnected_short,
        R.string.event_group_connectivity
    ),
    BLUETOOTH(
        "bluetooth", "Bluetooth connected", "Bluetooth", "Connectivity",
        R.string.event_bluetooth_label, R.string.utility_bluetooth,
        R.string.event_group_connectivity
    ),
    USB_CONNECTED(
        "usb_connected", "USB device plugged in", "USB in", "Connectivity",
        R.string.event_usb_connected_label, R.string.event_usb_connected_short,
        R.string.event_group_connectivity
    ),
    USB_DISCONNECTED(
        "usb_disconnected", "USB device unplugged", "USB out", "Connectivity",
        R.string.event_usb_disconnected_label, R.string.event_usb_disconnected_short,
        R.string.event_group_connectivity
    ),
    HEADPHONES_CONNECTED(
        "headphones_connected", "Wired Headphone plugged in", "Headphone in", "Connectivity",
        R.string.event_headphones_connected_label, R.string.event_headphones_connected_short,
        R.string.event_group_connectivity
    ),
    HEADPHONES_DISCONNECTED(
        "headphones_disconnected", "Wired Headphone unplugged", "Headphone out", "Connectivity",
        R.string.event_headphones_disconnected_label, R.string.event_headphones_disconnected_short,
        R.string.event_group_connectivity
    ),
    HOTSPOT(
        "hotspot", "Hotspot", "Hotspot", "Connectivity",
        R.string.utility_hotspot, R.string.utility_hotspot,
        R.string.event_group_connectivity
    ),

    CALL(
        "call", "Incoming and ongoing calls", "Calls", "Activities",
        R.string.event_call_label, R.string.event_call_short,
        R.string.event_group_activities
    ),
    MUSIC(
        "music", "Music player", "Music", "Activities",
        R.string.event_music_label, R.string.btn_music,
        R.string.event_group_activities
    ),
    NOTIFICATION(
        "notification", "App notifications", "Notifications", "Activities",
        R.string.event_notification_label, R.string.notifications_master_title,
        R.string.event_group_activities
    ),
    NAVIGATION(
        "navigation", "Navigation", "Navigation", "Activities",
        R.string.event_navigation_label, R.string.event_navigation_label,
        R.string.event_group_activities
    ),
    LIVE_ACTIVITY(
        "live_activity", "Live activities", "Live", "Activities",
        R.string.event_live_activity_label, R.string.mode_live_activity,
        R.string.event_group_activities
    ),
    DOWNLOAD(
        "download", "Downloads", "Downloads", "Activities",
        R.string.event_download_label, R.string.event_download_label,
        R.string.event_group_activities
    ),
    UPLOAD(
        "upload", "Uploads", "Uploads", "Activities",
        R.string.event_upload_label, R.string.event_upload_label,
        R.string.event_group_activities
    ),
    BACKUP(
        "backup", "Backups and restores", "Backups", "Activities",
        R.string.event_backup_label, R.string.event_backup_short,
        R.string.event_group_activities
    ),
    FILE_COPY_MOVE(
        "file_copy_move", "File copy and move", "Copy / move", "Activities",
        R.string.event_file_copy_move_label, R.string.event_file_copy_move_short,
        R.string.event_group_activities
    ),
    FILE_DELETE(
        "file_delete", "File deletion", "Delete", "Activities",
        R.string.event_file_delete_label, R.string.event_file_delete_short,
        R.string.event_group_activities
    ),
    APP_INSTALL(
        "app_install", "App installs and updates", "Installs", "Activities",
        R.string.event_app_install_label, R.string.event_app_install_short,
        R.string.event_group_activities
    ),
    FLASHLIGHT(
        "flashlight", "Flashlight", "Flashlight", "Activities",
        R.string.utility_flashlight, R.string.utility_flashlight,
        R.string.event_group_activities
    ),
    SCREEN_RECORDING(
        "screen_recording", "Screen recording", "Screen rec.", "Activities",
        R.string.event_screen_recording_label, R.string.event_screen_recording_short,
        R.string.event_group_activities
    ),
    AUDIO_RECORDING(
        "audio_recording", "Audio recording", "Audio rec.", "Activities",
        R.string.event_audio_recording_label, R.string.event_audio_recording_short,
        R.string.event_group_activities
    ),
    TIMER(
        "timer", "Timer", "Timer", "Activities",
        R.string.utility_timer, R.string.utility_timer,
        R.string.event_group_activities
    ),
    STOPWATCH(
        "stopwatch", "Stopwatch", "Stopwatch", "Activities",
        R.string.utility_stopwatch, R.string.utility_stopwatch,
        R.string.event_group_activities
    ),
    // drawn by ui/FaceIdLayer.kt over the cutout, not as a card
    FACE_UNLOCK(
        "face_unlock", "Face ID scan (face unlock animation)", "Face ID", "Activities",
        R.string.event_face_unlock_label, R.string.event_face_unlock_short,
        R.string.event_group_activities
    );

    /**
     * True when the event has its own auto-expand switch on the Home tab. Calls always open the island, so they have
     * none.
     */
    val canSwitchAutoExpand: Boolean get() = this != CALL && this != FACE_UNLOCK

    /**
     * True when the event has its own "hide on the lock screen" switch. Calls never do: a ringing call must stay
     * reachable.
     */
    val canHideOnLockScreen: Boolean get() = this != CALL && this != FACE_UNLOCK

    companion object {
        /** Event for real app notifications (not system broadcasts). */
        fun forListenerMode(mode: IslandMode, transferKind: TransferKind = TransferKind.DOWNLOAD): IslandEvent? = when (mode) {
            IslandMode.Notification -> NOTIFICATION
            IslandMode.IncomingCall -> CALL
            IslandMode.Music -> MUSIC
            IslandMode.LiveActivity -> LIVE_ACTIVITY
            IslandMode.Navigation -> NAVIGATION
            IslandMode.DownloadUpload -> forTransfer(transferKind)
            IslandMode.Hotspot -> HOTSPOT
            IslandMode.ScreenRecording -> SCREEN_RECORDING
            IslandMode.AudioRecording -> AUDIO_RECORDING
            IslandMode.Timer -> TIMER
            IslandMode.Stopwatch -> STOPWATCH
            else -> null
        }

        fun forTransfer(kind: TransferKind): IslandEvent = when (kind) {
            TransferKind.DOWNLOAD -> DOWNLOAD
            TransferKind.UPLOAD -> UPLOAD
            TransferKind.BACKUP -> BACKUP
            TransferKind.COPY_MOVE -> FILE_COPY_MOVE
            TransferKind.DELETE -> FILE_DELETE
            TransferKind.INSTALL -> APP_INSTALL
        }

        /**
         * Event a posted island notification belongs to, for filtering against the user's switches; null for modes
         * that are never gated (Empty).
         */
        fun forNotification(n: IslandNotification): IslandEvent? = when (n.mode) {
            IslandMode.Empty -> null
            IslandMode.Notification -> NOTIFICATION
            IslandMode.IncomingCall -> CALL
            IslandMode.Music -> MUSIC
            IslandMode.Battery -> when (n.category) {
                "battery_plugged_in" -> PLUGGED_IN
                "battery_plugged_out" -> PLUGGED_OUT
                "battery_full" -> FULLY_CHARGED
                "battery_saver" -> BATTERY_SAVER
                "battery_low" -> LOW_BATTERY
                else -> PLUGGED_IN
            }
            IslandMode.LiveActivity -> LIVE_ACTIVITY
            IslandMode.Navigation -> NAVIGATION
            IslandMode.DownloadUpload ->
                if (n.transferKind == TransferKind.DOWNLOAD && n.key.contains("upload", ignoreCase = true)) {
                    UPLOAD
                } else {
                    forTransfer(n.transferKind)
                }
            IslandMode.Hotspot -> HOTSPOT
            IslandMode.Bluetooth -> when (n.category) {
                "wifi" -> WIFI
                "vpn" -> VPN
                "wifi_disconnected" -> WIFI_DISCONNECTED
                "vpn_disconnected" -> VPN_DISCONNECTED
                "usb_connected" -> USB_CONNECTED
                "usb_disconnected" -> USB_DISCONNECTED
                "headphones_connected" -> HEADPHONES_CONNECTED
                "headphones_disconnected" -> HEADPHONES_DISCONNECTED
                else -> BLUETOOTH
            }
            IslandMode.Flashlight -> FLASHLIGHT
            IslandMode.ScreenRecording -> SCREEN_RECORDING
            IslandMode.AudioRecording -> AUDIO_RECORDING
            IslandMode.Timer -> TIMER
            IslandMode.Stopwatch -> STOPWATCH
        }
    }
}
