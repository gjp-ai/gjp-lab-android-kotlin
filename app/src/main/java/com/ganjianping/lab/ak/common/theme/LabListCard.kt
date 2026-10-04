package com.ganjianping.lab.ak.common.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A list row drawn as its own rounded card with a hairline border, as used by the dashboard and
 * catalogue. A selected row gets a thicker `primary` border. A row without [onClick] is shown but
 * cannot be tapped (for example a planned topic).
 */
@Composable
fun LabListCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    val interaction = if (onClick != null) {
        Modifier.clickable(onClickLabel = onClickLabel, onClick = onClick)
    } else {
        // Read the row as one element even when it cannot be tapped.
        Modifier.semantics(mergeDescendants = true) {}
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = if (isSelected) 1.dp else 0.5.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                shape = shape
            )
            .then(interaction)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Preview(name = "Lab list card - light", showBackground = true)
@Composable
private fun LabListCardPreview() {
    GJPLabTheme { LabListCardSamples() }
}

@Preview(name = "Lab list card - dark", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LabListCardDarkPreview() {
    GJPLabTheme { LabListCardSamples() }
}

@Composable
private fun LabListCardSamples() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LabListCard(onClick = {}) { Text("Available row") }
        LabListCard(isSelected = true, onClick = {}) { Text("Selected row") }
        LabListCard { Text("Row that cannot be tapped") }
    }
}
