@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.ganjianping.lab.ak.features.compose.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

/** Only today and later can be picked; the date picker works in UTC milliseconds. */
private object FutureDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
        !Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneOffset.UTC).toLocalDate().isBefore(LocalDate.now())

    override fun isSelectableYear(year: Int): Boolean = year >= LocalDate.now().year
}

@Composable
fun SelectionScreen() {
    var order by remember { mutableStateOf(CoffeeOrder()) }
    var isTemperatureMenuOpen by rememberSaveable { mutableStateOf(false) }
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    var pickupDate by rememberSaveable { mutableStateOf<Long?>(null) }

    LabDemoPage(
        intro = "Selection controls change state that the rest of the screen reads. Every control below feeds " +
            "the order summary at the bottom."
    ) {
        LabDemoSection(
            title = "Single choice",
            caption = "Segmented buttons for a few visible options; a dropdown menu when the options can stay hidden."
        ) {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                CoffeeSize.entries.forEachIndexed { index, size ->
                    SegmentedButton(
                        selected = order.size == size,
                        onClick = { order = order.copy(size = size) },
                        shape = SegmentedButtonDefaults.itemShape(index, CoffeeSize.entries.size)
                    ) { Text(size.title) }
                }
            }
            Box {
                OutlinedButton(onClick = { isTemperatureMenuOpen = true }) { Text("Temperature: ${order.temperature.title}") }
                DropdownMenu(expanded = isTemperatureMenuOpen, onDismissRequest = { isTemperatureMenuOpen = false }) {
                    Temperature.entries.forEach { temperature ->
                        DropdownMenuItem(
                            text = { Text(temperature.title) },
                            onClick = { order = order.copy(temperature = temperature); isTemperatureMenuOpen = false },
                            trailingIcon = { if (order.temperature == temperature) Icon(Icons.Outlined.Check, contentDescription = "Selected") }
                        )
                    }
                }
            }
        }

        LabDemoSection(
            title = "On/off and numbers",
            caption = "Switch for on/off, a stepper built from two icon buttons for small counts, and a slider for a range."
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Decaf", modifier = Modifier.weight(1f))
                Switch(checked = order.isDecaf, onCheckedChange = { order = order.copy(isDecaf = it) })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Espresso shots: ${order.shots}", modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { order = order.copy(shots = order.shots - 1) },
                    enabled = order.shots > CoffeeOrder.ShotRange.first
                ) { Icon(Icons.Outlined.Remove, contentDescription = "Fewer shots") }
                IconButton(
                    onClick = { order = order.copy(shots = order.shots + 1) },
                    enabled = order.shots < CoffeeOrder.ShotRange.last
                ) { Icon(Icons.Outlined.Add, contentDescription = "More shots") }
            }
            Text("Sweetness: ${order.sweetness}%")
            Slider(
                value = order.sweetness.toFloat(),
                onValueChange = { order = order.copy(sweetness = it.roundToInt()) },
                valueRange = CoffeeOrder.SweetnessRange.first.toFloat()..CoffeeOrder.SweetnessRange.last.toFloat(),
                steps = 9,
                modifier = Modifier.semantics { contentDescription = "Sweetness" }
            )
        }

        LabDemoSection(
            title = "Multiple choice",
            caption = "FilterChips toggle extras in and out of a Set; any number can be on."
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Extra.entries.forEach { extra ->
                    val selected = extra in order.extras
                    FilterChip(
                        selected = selected,
                        onClick = { order = order.copy(extras = if (selected) order.extras - extra else order.extras + extra) },
                        label = { Text(extra.title) },
                        leadingIcon = if (selected) {
                            { Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                        } else null
                    )
                }
            }
        }

        LabDemoSection(
            title = "Date",
            caption = "DatePickerDialog with SelectableDates, so days before today cannot be chosen."
        ) {
            OutlinedButton(onClick = { isDatePickerOpen = true }) {
                Text("Pickup: ${pickupDate?.let(::formatUtcDate) ?: "Choose a date"}")
            }
            if (isDatePickerOpen) {
                val state = rememberDatePickerState(initialSelectedDateMillis = pickupDate, selectableDates = FutureDates)
                DatePickerDialog(
                    onDismissRequest = { isDatePickerOpen = false },
                    confirmButton = {
                        TextButton(onClick = { pickupDate = state.selectedDateMillis; isDatePickerOpen = false }) { Text("OK") }
                    },
                    dismissButton = { TextButton(onClick = { isDatePickerOpen = false }) { Text("Cancel") } }
                ) {
                    DatePicker(state = state)
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(18.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (order.temperature == Temperature.Iced) Icons.Outlined.LocalDrink else Icons.Outlined.LocalCafe,
                    contentDescription = null
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Your order", fontWeight = FontWeight.SemiBold)
                    Text(order.summary)
                    pickupDate?.let { Text("Pickup on ${formatUtcDate(it)}") }
                }
            }
        }
        Button(onClick = { order = CoffeeOrder(); pickupDate = null }, modifier = Modifier.fillMaxWidth()) { Text("Reset order") }
    }
}

private fun formatUtcDate(utcMillis: Long): String =
    Instant.ofEpochMilli(utcMillis).atZone(ZoneOffset.UTC).toLocalDate()
        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

@Composable
private fun SelectionPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { SelectionScreen() } }
}

@Preview(name = "Selection - light", showBackground = true, widthDp = 360, heightDp = 1400)
@Composable
private fun SelectionLightPreview() = SelectionPreview()

@Preview(name = "Selection - dark", showBackground = true, widthDp = 360, heightDp = 1400, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SelectionDarkPreview() = SelectionPreview()
