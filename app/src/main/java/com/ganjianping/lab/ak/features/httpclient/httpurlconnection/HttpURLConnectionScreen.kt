package com.ganjianping.lab.ak.features.httpclient.httpurlconnection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme
import kotlinx.coroutines.launch

@Composable
fun HttpURLConnectionScreen(
    repository: HttpURLConnectionRepository,
    onResponse: (HttpResponse) -> Unit
) {
    var method by rememberSaveable { mutableStateOf(HttpMethod.GET) }
    var url by rememberSaveable {
        mutableStateOf("https://www.ganjianping.com/api/open/websites?channel=AI&page=0&size=500&lang=EN")
    }
    var payload by rememberSaveable { mutableStateOf("{\n  \"example\": \"value\"\n}") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    HttpURLConnectionForm(
        method = method,
        url = url,
        payload = payload,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onMethodChange = { method = it; errorMessage = null },
        onUrlChange = { url = it; errorMessage = null },
        onPayloadChange = { payload = it; errorMessage = null },
        onDismissError = { errorMessage = null },
        onSend = {
            scope.launch {
                isLoading = true
                errorMessage = null
                try {
                    onResponse(repository.execute(method, url, payload))
                } catch (exception: Exception) {
                    errorMessage = exception.message ?: "Request failed"
                } finally {
                    isLoading = false
                }
            }
        }
    )
}

@Composable
private fun HttpURLConnectionForm(
    method: HttpMethod,
    url: String,
    payload: String,
    isLoading: Boolean,
    errorMessage: String?,
    onMethodChange: (HttpMethod) -> Unit,
    onUrlChange: (String) -> Unit,
    onPayloadChange: (String) -> Unit,
    onDismissError: () -> Unit,
    onSend: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Text(
            "Build and send an HTTP request with the native Android API.",
            modifier = Modifier.padding(top = 4.dp, bottom = 22.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text("Method", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HttpMethod.entries.forEach { option ->
                FilterChip(
                    selected = method == option,
                    onClick = { onMethodChange(option) },
                    label = { Text(option.name) }
                )
            }
        }

        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
            label = { Text("URL") },
            placeholder = { Text("https://example.com/api") },
            singleLine = false
        )

        if (method.supportsPayload) {
            OutlinedTextField(
                value = payload,
                onValueChange = onPayloadChange,
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                label = { Text("Request payload") },
                placeholder = { Text("JSON payload") },
                minLines = 6
            )
        }

        errorMessage?.let { message ->
            ErrorBanner(message = message, onDismiss = onDismissError)
        }

        Button(
            onClick = onSend,
            enabled = !isLoading && url.isNotBlank(),
            modifier = Modifier.fillMaxWidth().padding(top = 22.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.height(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Send request")
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ErrorBanner(message: String, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = message, modifier = Modifier.weight(1f))
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    }
}

@Composable
private fun FormPreview(method: HttpMethod, errorMessage: String?) {
    GJPLabTheme {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            HttpURLConnectionForm(
                method = method,
                url = "https://example.com/api",
                payload = "{\n  \"example\": \"value\"\n}",
                isLoading = false,
                errorMessage = errorMessage,
                onMethodChange = {},
                onUrlChange = {},
                onPayloadChange = {},
                onDismissError = {},
                onSend = {}
            )
        }
    }
}

@Preview(name = "HttpURLConnection - light", showBackground = true, widthDp = 360)
@Composable
private fun HttpURLConnectionPreview() = FormPreview(HttpMethod.GET, errorMessage = null)

@Preview(name = "HttpURLConnection - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HttpURLConnectionDarkPreview() = FormPreview(HttpMethod.GET, errorMessage = null)

@Preview(name = "POST with error - light", showBackground = true, widthDp = 360)
@Composable
private fun HttpURLConnectionErrorPreview() = FormPreview(HttpMethod.POST, errorMessage = "Unable to resolve host")

@Preview(name = "POST with error - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HttpURLConnectionErrorDarkPreview() = FormPreview(HttpMethod.POST, errorMessage = "Unable to resolve host")
