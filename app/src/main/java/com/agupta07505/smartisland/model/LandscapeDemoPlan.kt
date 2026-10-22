/*
 * Smart Island (2026)
 * © Animesh Gupta — github.com/agupta07505
 * Licensed under the GNU GPL v3 License
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.agupta07505.smartisland.model

import androidx.annotation.StringRes
import com.agupta07505.smartisland.R
import com.agupta07505.smartisland.data.LandscapeCamera
import com.agupta07505.smartisland.data.OrientationState
import com.agupta07505.smartisland.data.SmartIslandSettings

/** Landscape Lab: the matrix of landscape situations and the script played in each. */
enum class LabRotation(@StringRes val labelRes: Int, val reversed: Boolean) {
    NORMAL(R.string.lab_rotation_normal, false),
    REVERSE(R.string.lab_rotation_reverse, true)
}

enum class LabIdle(@StringRes val labelRes: Int, val position: String, val cameraReported: Boolean) {
    AUTO_CAMERA(R.string.lab_idle_camera, SmartIslandSettings.LANDSCAPE_POSITION_AUTO, true),
    AUTO_NO_CAMERA(R.string.lab_idle_no_camera, SmartIslandSettings.LANDSCAPE_POSITION_AUTO, false),
    LEFT(R.string.lab_idle_left, SmartIslandSettings.LANDSCAPE_POSITION_LEFT, false),
    RIGHT(R.string.lab_idle_right, SmartIslandSettings.LANDSCAPE_POSITION_RIGHT, false),
    MIDDLE(R.string.lab_idle_middle, SmartIslandSettings.LANDSCAPE_POSITION_MIDDLE, false)
}

enum class LabPill(@StringRes val labelRes: Int, val position: String) {
    LEFT(R.string.lab_pill_left, SmartIslandSettings.LANDSCAPE_POSITION_LEFT),
    MIDDLE(R.string.lab_pill_middle, SmartIslandSettings.LANDSCAPE_POSITION_MIDDLE),
    RIGHT(R.string.lab_pill_right, SmartIslandSettings.LANDSCAPE_POSITION_RIGHT)
}

data class LabStage(val rotation: LabRotation, val idle: LabIdle, val pill: LabPill) {
    val title: String get() = "${rotation.name.lowercase()} / ${idle.name.lowercase()} / ${pill.name.lowercase()}"
}

/** What the lab forces while it plays: the stage's layout choices plus the merge style of the moment. */
data class LabOverride(
    val stage: LabStage,
    val mergeStyle: String = com.agupta07505.smartisland.util.MergeStyle.ID_SLIDE
) {
    /** Stored settings with the stage's layout applied and everything that could hide or end a demo card switched off. */
    fun applyTo(raw: SmartIslandSettings): SmartIslandSettings = raw.copy(
        showInLandscape = true,
        disabledEvents = emptySet(),
        disabledEventsLandscape = emptySet(),
        noExpandPackages = emptySet(),
        landscapeCirclePosition = stage.idle.position,
        landscapeCircleXAdjust = 0f,
        landscapeCircleYAdjust = 0f,
        landscapePillPosition = stage.pill.position,
        landscapeXOffset = 0f,
        mergeStyle = mergeStyle,
        mergeStyleLandscape = mergeStyle,
        // The lab puts cards away itself; the idle timer must not end a step early.
        idleTimeoutMs = LAB_IDLE_TIMEOUT_MS
    )

    /** Display state with the stage's rotation and camera applied; portrait is left alone. */
    fun applyTo(state: OrientationState): OrientationState {
        if (!state.isLandscape) return state
        val reversed = stage.rotation.reversed
        val camera = if (stage.idle.cameraReported) {
            LandscapeCamera(
                xOffsetDp = (if (reversed) 1f else -1f) * (state.screenWidthDp / 2f - LAB_CAMERA_INSET_DP).coerceAtLeast(0f),
                centerYDp = LAB_CAMERA_CENTER_Y_DP
            )
        } else {
            null
        }
        return state.copy(
            camera = camera,
            rotationSide = if (reversed) SmartIslandSettings.LANDSCAPE_POSITION_RIGHT
            else SmartIslandSettings.LANDSCAPE_POSITION_LEFT,
            reversed = reversed
        )
    }

    companion object {
        const val LAB_IDLE_TIMEOUT_MS = 10 * 60 * 1000
        const val LAB_CAMERA_INSET_DP = 28f
        const val LAB_CAMERA_CENTER_Y_DP = 20f
    }
}

fun allLabStages(): List<LabStage> =
    LabRotation.entries.flatMap { r -> LabIdle.entries.flatMap { i -> LabPill.entries.map { p -> LabStage(r, i, p) } } }

fun labStages(rotation: LabRotation?, idle: LabIdle?, pill: LabPill?): List<LabStage> =
    allLabStages().filter {
        (rotation == null || it.rotation == rotation) &&
            (idle == null || it.idle == idle) &&
            (pill == null || it.pill == pill)
    }

/** Everything a step can ask for; the runner turns these into cards, ViewModel commands and settings changes. */
sealed interface LabAction {
    data class Post(val card: LabCard, val autoExpand: Boolean = false) : LabAction
    data class Remove(val card: LabCard) : LabAction
    data object RemoveAll : LabAction
    data object Expand : LabAction
    data object Collapse : LabAction
    data object OpenPicker : LabAction
    data object ClosePicker : LabAction
    data object TapIdleDot : LabAction
    data class SetMerge(val style: String) : LabAction
    data class Wait(val ms: Long) : LabAction
}

/** Sample cards for the script; apps differ on purpose (one app, one cutout). */
enum class LabCard {
    MUSIC, MESSAGE, DOWNLOAD,
    SIBLING_A, SIBLING_B, SIBLING_C,
    CALL, PLUG
}

data class LabStep(val id: String, @StringRes val titleRes: Int, val actions: List<LabAction>, val holdMs: Long, val quick: Boolean)

private const val BUBBLE = com.agupta07505.smartisland.util.MergeStyle.ID_BUBBLE
private const val SLIDE = com.agupta07505.smartisland.util.MergeStyle.ID_SLIDE

/** The pill exit plays 360 ms (x animation speed); the interrupted exit brings the card back in the middle of it. */
const val LAB_INTERRUPT_AFTER_MS = 170L

/**
 * The script. Each step starts from the state the previous one left; the list begins and ends with nothing on the
 * island. [quick] steps are the short run: entry, expand, exit.
 */
fun labSteps(): List<LabStep> = listOf(
    LabStep("idle", R.string.lab_step_idle, listOf(LabAction.RemoveAll), 1400, quick = true),
    LabStep("enter", R.string.lab_step_enter, listOf(LabAction.Post(LabCard.MUSIC)), 2000, quick = true),
    LabStep("expand", R.string.lab_step_expand, listOf(LabAction.Expand), 2400, quick = true),
    LabStep("collapse", R.string.lab_step_collapse, listOf(LabAction.Collapse), 1400, quick = false),
    LabStep(
        "second_slide", R.string.lab_step_second_slide,
        listOf(LabAction.SetMerge(SLIDE), LabAction.Post(LabCard.MESSAGE)), 2400, quick = false
    ),
    LabStep(
        "second_bubble", R.string.lab_step_second_bubble,
        listOf(
            LabAction.Remove(LabCard.MESSAGE), LabAction.Wait(900), LabAction.SetMerge(BUBBLE),
            LabAction.Post(LabCard.MESSAGE)
        ), 2800, quick = false
    ),
    LabStep("third", R.string.lab_step_third, listOf(LabAction.Post(LabCard.DOWNLOAD)), 2400, quick = false),
    LabStep(
        "expand_many", R.string.lab_step_expand_many,
        listOf(LabAction.Expand), 2600, quick = false
    ),
    LabStep(
        "siblings", R.string.lab_step_siblings,
        listOf(
            LabAction.Collapse, LabAction.Wait(500), LabAction.RemoveAll, LabAction.Wait(900),
            LabAction.Post(LabCard.SIBLING_A), LabAction.Post(LabCard.SIBLING_B), LabAction.Post(LabCard.SIBLING_C),
            LabAction.Wait(900), LabAction.Expand, LabAction.Wait(700), LabAction.OpenPicker
        ), 2800, quick = false
    ),
    LabStep(
        "picker_close", R.string.lab_step_picker_close,
        listOf(LabAction.ClosePicker, LabAction.Wait(500), LabAction.Collapse), 1400, quick = false
    ),
    LabStep(
        "call", R.string.lab_step_call,
        listOf(LabAction.RemoveAll, LabAction.Wait(900), LabAction.Post(LabCard.CALL, autoExpand = true)), 2800, quick = false
    ),
    LabStep(
        "popup", R.string.lab_step_popup,
        listOf(
            LabAction.Collapse, LabAction.Wait(500), LabAction.RemoveAll, LabAction.Wait(900),
            LabAction.Post(LabCard.MUSIC), LabAction.Wait(1400), LabAction.Post(LabCard.PLUG)
        ), 3200, quick = false
    ),
    LabStep("exit", R.string.lab_step_exit, listOf(LabAction.RemoveAll), 2200, quick = true),
    LabStep(
        "interrupt", R.string.lab_step_interrupt,
        listOf(
            LabAction.Post(LabCard.MUSIC), LabAction.Wait(1600), LabAction.RemoveAll,
            LabAction.Wait(LAB_INTERRUPT_AFTER_MS), LabAction.Post(LabCard.MUSIC)
        ), 2400, quick = false
    ),
    LabStep(
        "dot_tap", R.string.lab_step_dot_tap,
        listOf(LabAction.Expand, LabAction.Wait(1500), LabAction.TapIdleDot), 3000, quick = false
    ),
    LabStep("end", R.string.lab_step_end, listOf(LabAction.RemoveAll), 600, quick = true)
)

fun labStepsFor(quick: Boolean): List<LabStep> = labSteps().filter { !quick || it.quick }

fun labStageDurationMs(quick: Boolean): Long = labStepsFor(quick).sumOf { step ->
    step.holdMs + step.actions.filterIsInstance<LabAction.Wait>().sumOf { it.ms }
}

/** Exit direction the pill is expected to take in [stage] on a screen [screenWidthDp] wide (what to watch for). */
fun expectedExitDirection(stage: LabStage, screenWidthDp: Float, pillWidthDp: Float, edgeInsetDp: Float): PillExitDirection {
    val cornerX = (screenWidthDp / 2f - edgeInsetDp - pillWidthDp / 2f).coerceAtLeast(0f)
    val reversed = stage.rotation.reversed
    val side = when (stage.pill.position) {
        SmartIslandSettings.LANDSCAPE_POSITION_LEFT -> if (reversed) 1f else -1f
        SmartIslandSettings.LANDSCAPE_POSITION_RIGHT -> if (reversed) -1f else 1f
        else -> 0f
    }
    return pillExitDirection(side * cornerX, screenWidthDp)
}

/** Side the sibling picker opens on: away from the camera / the idle circle's edge. True = left. */
fun expectedPickerOnLeft(stage: LabStage): Boolean {
    val cameraOnRight = when (stage.idle.position) {
        SmartIslandSettings.LANDSCAPE_POSITION_LEFT -> stage.rotation.reversed
        SmartIslandSettings.LANDSCAPE_POSITION_RIGHT -> !stage.rotation.reversed
        else -> stage.rotation.reversed
    }
    return cameraOnRight
}
