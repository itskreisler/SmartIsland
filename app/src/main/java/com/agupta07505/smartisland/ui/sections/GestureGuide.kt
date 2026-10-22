/*
 * Smart Island (2026)
 * © Animesh Gupta — github.com/agupta07505
 * Licensed under the GNU GPL v3 License
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.agupta07505.smartisland.ui.sections
import androidx.compose.ui.res.stringResource
import com.agupta07505.smartisland.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Gesture
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Swipe
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Button
import com.agupta07505.smartisland.ui.glass.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.agupta07505.smartisland.data.SmartIslandSettings
import com.agupta07505.smartisland.model.SwipeAction

// What each gesture does right now.

private data class GestureLine(val icon: ImageVector, val gesture: String, val result: String)

private data class GestureGroup(val title: String, val note: String?, val lines: List<GestureLine>)

@Composable
private fun actionText(id: String, default: SwipeAction, enabled: Boolean): String =
    if (!enabled) stringResource(R.string.guide_off_switched_off) else getSwipeActionTitle(SwipeAction.fromId(id, default))

@Composable
private fun gestureGroups(settings: SmartIslandSettings): List<GestureGroup> {
    val pill = settings.enablePillSwipeActions
    val card = settings.enableSwipeActions
    val longPress = SwipeAction.fromId(settings.longPressAction, SwipeAction.TogglePill)
    val longPressTitle = getSwipeActionTitle(longPress)
    val longPressText = if (settings.enableAppShortcuts) {
        stringResource(R.string.guide_long_press_apps, longPressTitle)
    } else {
        stringResource(R.string.guide_long_press_plain, longPressTitle)
    }
    return listOf(
        GestureGroup(
            title = stringResource(R.string.guide_collapsed_island),
            note = stringResource(R.string.guide_collapsed_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_glyph),
                    stringResource(R.string.guide_tap_glyph_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_middle_band),
                    stringResource(R.string.guide_tap_middle_band_result)
                ),
                GestureLine(Icons.Rounded.Swipe, stringResource(R.string.guide_swipe_left), actionText(settings.pillSwipeLeftAction, SwipeAction.PreviousNotification, pill)),
                GestureLine(Icons.Rounded.Swipe, stringResource(R.string.guide_swipe_right), actionText(settings.pillSwipeRightAction, SwipeAction.NextNotification, pill)),
                GestureLine(Icons.Rounded.ArrowUpward, stringResource(R.string.guide_swipe_up), actionText(settings.pillSwipeUpAction, SwipeAction.DismissCurrent, pill)),
                GestureLine(Icons.Rounded.ArrowDownward, stringResource(R.string.guide_swipe_down), stringResource(R.string.guide_swipe_down_result)),
                GestureLine(Icons.Rounded.TouchApp, stringResource(R.string.guide_long_press), longPressText),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_camera_hole),
            note = stringResource(R.string.guide_camera_hole_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_camera_hole),
                    stringResource(R.string.guide_tap_camera_hole_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_long_press_camera_hole),
                    stringResource(R.string.guide_long_press_camera_hole_result, longPressTitle)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_or_hold_band),
                    stringResource(R.string.guide_tap_or_hold_band_result, longPressTitle)
                ),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_second_third_pill),
            note = stringResource(R.string.guide_second_third_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_second_pill),
                    stringResource(R.string.guide_tap_second_pill_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_second_pill_expanded),
                    stringResource(R.string.guide_tap_second_pill_expanded_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_third_pill),
                    stringResource(R.string.guide_tap_third_pill_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_landscape_pill_open_card),
                    stringResource(R.string.guide_landscape_pill_open_card_result)
                ),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_idle_circle),
            note = stringResource(R.string.guide_idle_circle_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap),
                    stringResource(R.string.guide_tap_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_long_press),
                    stringResource(R.string.guide_idle_circle_long_press_result, longPressTitle)
                ),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_idle_widgets),
            note = stringResource(R.string.guide_idle_widgets_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_glyph_or_value),
                    stringResource(R.string.guide_tap_glyph_or_value_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_middle_band),
                    stringResource(R.string.guide_idle_tap_middle_band_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_outside_widget),
                    stringResource(R.string.guide_tap_outside_widget_result)
                ),
                GestureLine(
                    Icons.Rounded.Swipe, stringResource(R.string.guide_swipe_widget_pill),
                    stringResource(R.string.guide_swipe_widget_pill_result)
                ),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_hidden_pill),
            note = stringResource(R.string.guide_hidden_pill_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_its_spot),
                    stringResource(R.string.guide_tap_its_spot_result)
                ),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_expanded_card),
            note = stringResource(R.string.guide_expanded_card_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_card),
                    stringResource(R.string.guide_tap_card_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_top_edge),
                    stringResource(R.string.guide_tap_top_edge_result, longPressTitle)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_outside_card),
                    stringResource(R.string.guide_tap_outside_card_result)
                ),
                GestureLine(
                    Icons.Rounded.ArrowUpward, stringResource(R.string.guide_swipe_up),
                    actionText(settings.swipeUpAction, SwipeAction.DismissCurrent, card) + stringResource(R.string.guide_card_swipe_up_suffix)
                ),
                GestureLine(
                    Icons.Rounded.DeleteSweep, stringResource(R.string.guide_hold_then_swipe_up),
                    actionText(settings.swipeHoldUpAction, SwipeAction.DismissAll, card) + stringResource(R.string.guide_hold_swipe_up_suffix)
                ),
                GestureLine(
                    Icons.Rounded.ArrowDownward, stringResource(R.string.guide_swipe_down),
                    stringResource(R.string.guide_card_swipe_down_result)
                ),
                GestureLine(Icons.Rounded.Swipe, stringResource(R.string.guide_swipe_left), actionText(settings.swipeLeftAction, SwipeAction.None, card)),
                GestureLine(Icons.Rounded.Swipe, stringResource(R.string.guide_swipe_right), actionText(settings.swipeRightAction, SwipeAction.None, card)),
                GestureLine(
                    Icons.Rounded.ArrowDownward, stringResource(R.string.guide_pull_handle),
                    stringResource(R.string.guide_pull_handle_result)
                ),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_card_steps),
            note = stringResource(R.string.guide_card_steps_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_arrow),
                    stringResource(R.string.guide_tap_arrow_result)
                ),
                GestureLine(
                    Icons.Rounded.ArrowDownward, stringResource(R.string.guide_swipe_down),
                    stringResource(R.string.guide_steps_swipe_down_result)
                ),
                GestureLine(
                    Icons.Rounded.ArrowUpward, stringResource(R.string.guide_swipe_up),
                    stringResource(R.string.guide_steps_swipe_up_result)
                ),
                GestureLine(
                    Icons.Rounded.Swipe, stringResource(R.string.guide_swipe_left_or_right),
                    stringResource(R.string.guide_swipe_left_or_right_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_picture_picker),
                    stringResource(R.string.guide_tap_picture_picker_result)
                ),
                GestureLine(
                    Icons.Rounded.ArrowUpward, stringResource(R.string.guide_scroll_reader_text),
                    stringResource(R.string.guide_scroll_reader_text_result)
                ),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_sibling_list),
            note = stringResource(R.string.guide_sibling_list_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_row),
                    stringResource(R.string.guide_tap_row_result)
                ),
                GestureLine(
                    Icons.Rounded.Swipe, stringResource(R.string.guide_drag_rows),
                    stringResource(R.string.guide_drag_rows_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_second_pill_again),
                    stringResource(R.string.guide_tap_second_pill_again_result)
                ),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_quick_launch),
            note = stringResource(R.string.guide_quick_launch_note),
            lines = listOf(
                GestureLine(
                    Icons.Rounded.ArrowUpward, stringResource(R.string.guide_swipe_up_or_down),
                    stringResource(R.string.guide_swipe_up_or_down_result)
                ),
                GestureLine(
                    Icons.Rounded.Swipe, stringResource(R.string.guide_swipe_left_or_right),
                    stringResource(R.string.guide_quick_launch_swipe_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_app),
                    stringResource(R.string.guide_tap_app_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_long_press_app),
                    stringResource(R.string.guide_long_press_app_result)
                ),
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_outside),
                    stringResource(R.string.guide_tap_outside_result)
                ),
            )
        ),
        GestureGroup(
            title = stringResource(R.string.guide_utilities_page),
            note = null,
            lines = listOf(
                GestureLine(
                    Icons.Rounded.TouchApp, stringResource(R.string.guide_tap_tile),
                    stringResource(R.string.guide_tap_tile_result)
                ),
                GestureLine(
                    Icons.Rounded.ArrowUpward, stringResource(R.string.guide_swipe_widget_edge),
                    stringResource(R.string.guide_swipe_widget_edge_result)
                ),
            )
        ),
    )
}

/** The full list of gestures, grouped by where they happen. Shows the actions as they are set right now. */
@Composable
fun GestureReferenceCard(settings: SmartIslandSettings) {
    val groups = gestureGroups(settings)
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconBadge(Icons.Rounded.Gesture, size = 36)
                Column {
                    Text(stringResource(R.string.guide_all_gestures), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        stringResource(R.string.guide_all_gestures_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            groups.forEachIndexed { index, group ->
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                Text(group.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                group.note?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                group.lines.forEach { line ->
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        IconBadge(line.icon, size = 30)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(line.gesture, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text(line.result, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 17.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IconBadge(icon: ImageVector, size: Int) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size((size * 0.55f).dp))
    }
}

// The tour for new users: a few steps, one idea each, in the order a person meets them.

private data class TourStep(val icon: ImageVector, val title: String, val points: List<String>, val tip: String)

@Composable
private fun tourSteps(settings: SmartIslandSettings): List<TourStep> {
    val left = getSwipeActionTitle(SwipeAction.fromId(settings.pillSwipeLeftAction, SwipeAction.PreviousNotification))
    val right = getSwipeActionTitle(SwipeAction.fromId(settings.pillSwipeRightAction, SwipeAction.NextNotification))
    val up = getSwipeActionTitle(SwipeAction.fromId(settings.pillSwipeUpAction, SwipeAction.DismissCurrent))
    val cardUp = getSwipeActionTitle(SwipeAction.fromId(settings.swipeUpAction, SwipeAction.DismissCurrent))
    val cardHold = getSwipeActionTitle(SwipeAction.fromId(settings.swipeHoldUpAction, SwipeAction.DismissAll))
    return listOf(
        TourStep(
            Icons.Rounded.Gesture, stringResource(R.string.tour_meet_title),
            listOf(
                stringResource(R.string.tour_meet_point_1),
                stringResource(R.string.tour_meet_point_2),
                stringResource(R.string.tour_meet_point_3)
            ),
            stringResource(R.string.tour_meet_tip)
        ),
        TourStep(
            Icons.Rounded.TouchApp, stringResource(R.string.tour_tap_title),
            listOf(
                stringResource(R.string.tour_tap_point_1),
                stringResource(R.string.tour_tap_point_2),
                stringResource(R.string.tour_tap_point_3),
                stringResource(R.string.tour_tap_point_4),
                stringResource(R.string.tour_tap_point_5)
            ),
            stringResource(R.string.tour_tap_tip)
        ),
        TourStep(
            Icons.Rounded.Swipe, stringResource(R.string.tour_swipe_title),
            listOf(
                stringResource(R.string.tour_swipe_left, left),
                stringResource(R.string.tour_swipe_right, right),
                stringResource(R.string.tour_swipe_up, up),
                stringResource(R.string.tour_swipe_down)
            ),
            stringResource(R.string.tour_swipe_tip)
        ),
        TourStep(
            Icons.Rounded.TouchApp, stringResource(R.string.guide_long_press),
            listOf(
                if (settings.enableAppShortcuts) {
                    stringResource(R.string.tour_long_press_apps)
                } else {
                    stringResource(R.string.tour_long_press_hide)
                },
                stringResource(R.string.tour_long_press_point_2),
                stringResource(R.string.tour_long_press_point_3)
            ),
            stringResource(R.string.tour_long_press_tip)
        ),
        TourStep(
            Icons.Rounded.ArrowUpward, stringResource(R.string.tour_card_title),
            listOf(
                stringResource(R.string.tour_card_swipe_up, cardUp),
                stringResource(R.string.tour_card_hold, cardHold),
                stringResource(R.string.tour_card_point_3),
                stringResource(R.string.tour_card_point_4)
            ),
            stringResource(R.string.tour_card_tip)
        ),
        TourStep(
            Icons.Rounded.Apps, stringResource(R.string.tour_quick_title),
            listOf(
                stringResource(R.string.tour_quick_point_1),
                stringResource(R.string.tour_quick_point_2),
                stringResource(R.string.tour_quick_point_3),
                stringResource(R.string.tour_quick_point_4)
            ),
            stringResource(R.string.tour_quick_tip)
        ),
        TourStep(
            Icons.Rounded.Tune, stringResource(R.string.tour_yours_title),
            listOf(
                stringResource(R.string.tour_yours_point_1),
                stringResource(R.string.tour_yours_point_2),
                stringResource(R.string.tour_yours_point_3)
            ),
            stringResource(R.string.tour_yours_tip)
        ),
    )
}

/**
 * The new-user tour: a dialog of steps with Back / Next. Reached from the welcome dialog and from the top of the
 * Gestures screen. [onFinish] is called by Done, by Skip and by tapping outside.
 */
@Composable
fun NewUserTour(settings: SmartIslandSettings, onFinish: () -> Unit) {
    val steps = tourSteps(settings)
    var index by rememberSaveable { mutableIntStateOf(0) }
    val step = steps[index.coerceIn(0, steps.lastIndex)]
    val last = index >= steps.lastIndex
    Dialog(onDismissRequest = onFinish, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(12.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.tour_step_of, index + 1, steps.size),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    if (!last) TextButton(onClick = onFinish) { Text(stringResource(R.string.btn_skip)) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    steps.indices.forEach { i ->
                        Box(
                            Modifier
                                .height(4.dp)
                                .weight(1f)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (i <= index) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconBadge(step.icon, size = 48)
                    Text(step.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    step.points.forEach { point ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                            Box(
                                Modifier
                                    .padding(top = 7.dp)
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Text(point, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp, modifier = Modifier.weight(1f))
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Rounded.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            Text(step.tip, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 17.sp)
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (index > 0) {
                        TextButton(onClick = { index -= 1 }) { Text(stringResource(R.string.btn_back)) }
                    }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = { if (last) onFinish() else index += 1 }, shape = RoundedCornerShape(14.dp)) {
                        Text(if (last) stringResource(R.string.idle_select_done) else stringResource(R.string.btn_next), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/** The card at the top of the Gestures screen that opens the tour. */
@Composable
fun NewUserTourCard(onStart: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onStart),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconBadge(Icons.Rounded.Lightbulb, size = 40)
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.guide_new_to_si), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    stringResource(R.string.tour_card_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(stringResource(R.string.btn_start), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}
