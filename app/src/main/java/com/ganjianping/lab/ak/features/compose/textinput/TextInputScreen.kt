package com.ganjianping.lab.ak.features.compose.textinput

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import com.ganjianping.lab.ak.common.theme.LabDemoPage
import com.ganjianping.lab.ak.common.theme.LabDemoSection
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Currency
import java.util.Locale

/** The bio field stops at this many characters. */
const val BioLimit = 140

private const val LongText = "Compose draws text with the same engine as the rest of the UI, so a long paragraph can be " +
    "limited to a few lines and end with an ellipsis instead of pushing the layout around."

@Composable
fun TextInputScreen() {
    var form by remember { mutableStateOf(SignUpForm()) }
    var confirmation by rememberSaveable { mutableStateOf<String?>(null) }
    var bio by rememberSaveable { mutableStateOf("") }
    var limitLines by rememberSaveable { mutableStateOf(true) }
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun submit() {
        if (!form.isValid) return
        focusManager.clearFocus()
        confirmation = "Welcome, ${form.name.trim()}! Nothing was sent; this form stays on the device."
    }

    LabDemoPage(
        intro = "Text shows styled, formatted content. Text fields hold their value in state, and focus moves " +
            "between them with keyboard actions."
    ) {
        LabDemoSection(
            title = "Styled and formatted text",
            caption = "buildAnnotatedString mixes styles in one Text; java.text and java.time format numbers and dates for the device locale."
        ) {
            Text(
                buildAnnotatedString {
                    append("Text can be ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("bold") }
                    append(", ")
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("italic") }
                    append(", or ")
                    withStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = MaterialTheme.colorScheme.surfaceContainer)) {
                        append("code")
                    }
                    append(".")
                }
            )
            val locale = Locale.getDefault()
            val price = NumberFormat.getCurrencyInstance(locale).apply { currency = Currency.getInstance("SGD") }.format(12.5)
            val date = LocalDate.of(2026, 10, 4).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale))
            Text("Price: $price · Date: $date", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Switch(checked = limitLines, onCheckedChange = { limitLines = it })
                Text("Limit to 2 lines")
            }
            Text(
                LongText,
                maxLines = if (limitLines) 2 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis
            )
        }

        LabDemoSection(
            title = "Sign-up form",
            caption = "Each field's keyboard action moves focus to the next one; the last one submits. The button stays disabled until every rule passes."
        ) {
            OutlinedTextField(
                value = form.name,
                onValueChange = { form = form.copy(name = it); confirmation = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() })
            )
            OutlinedTextField(
                value = form.email,
                onValueChange = { form = form.copy(email = it); confirmation = null },
                modifier = Modifier.fillMaxWidth().focusRequester(emailFocus),
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() })
            )
            OutlinedTextField(
                value = form.password,
                onValueChange = { form = form.copy(password = it); confirmation = null },
                modifier = Modifier.fillMaxWidth().focusRequester(passwordFocus),
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { submit() })
            )
            form.problems.forEach { problem ->
                Text("• $problem", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }
            Button(onClick = ::submit, enabled = form.isValid, modifier = Modifier.fillMaxWidth()) {
                Text("Create account")
            }
            confirmation?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        }

        LabDemoSection(
            title = "Multi-line input with a limit",
            caption = "The field grows with its text. onValueChange trims anything past $BioLimit characters, and the counter shows how many are used."
        ) {
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it.take(BioLimit) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Bio") },
                minLines = 3,
                supportingText = { Text("${bio.length} of $BioLimit") }
            )
        }
    }
}

@Composable
private fun TextInputPreview() {
    GJPLabTheme { Column(Modifier.background(MaterialTheme.colorScheme.background)) { TextInputScreen() } }
}

@Preview(name = "Text & input - light", showBackground = true, widthDp = 360, heightDp = 1400)
@Composable
private fun TextInputLightPreview() = TextInputPreview()

@Preview(name = "Text & input - dark", showBackground = true, widthDp = 360, heightDp = 1400, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TextInputDarkPreview() = TextInputPreview()
