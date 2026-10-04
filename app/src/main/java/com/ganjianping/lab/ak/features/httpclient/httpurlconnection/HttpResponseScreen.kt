package com.ganjianping.lab.ak.features.httpclient.httpurlconnection

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun HttpResponseScreen(response: HttpResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Text(
            "HttpURLConnection response details",
            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("HTTP status", fontWeight = FontWeight.Bold)
                Text(
                    response.statusCode.toString(),
                    modifier = Modifier.padding(top = 6.dp),
                    color = statusColor(response.statusCode)
                )
            }
        }
        ResponseBlock("Response JSON", response.body)
        if (response.headers.isNotEmpty()) {
            ResponseBlock("Headers", response.headers.entries.joinToString("\n") { "${it.key}: ${it.value}" })
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ResponseBlock(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(top = 14.dp)) {
        Column(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(18.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            SelectionContainer {
                Text(value.ifBlank { "(empty response)" }, modifier = Modifier.padding(top = 10.dp), fontFamily = FontFamily.Monospace)
            }
        }
    }
}

private fun statusColor(statusCode: Int) = when {
    statusCode in 200..299 -> androidx.compose.ui.graphics.Color(0xFF2E7D32)
    statusCode in 400..599 -> androidx.compose.ui.graphics.Color(0xFFC62828)
    else -> androidx.compose.ui.graphics.Color.Unspecified
}

private val previewSuccess = HttpResponse(
    statusCode = 200,
    body = "{\n  \"message\": \"Hello\"\n}",
    headers = mapOf("Content-Type" to "application/json", "Server" to "nginx")
)
private val previewError = HttpResponse(statusCode = 404, body = "", headers = emptyMap())

@Composable
private fun ResponsePreview(response: HttpResponse) {
    GJPLabTheme {
        Column(Modifier.background(MaterialTheme.colorScheme.background)) { HttpResponseScreen(response) }
    }
}

@Preview(name = "Success - light", showBackground = true, widthDp = 360)
@Composable
private fun SuccessPreview() = ResponsePreview(previewSuccess)

@Preview(name = "Success - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SuccessDarkPreview() = ResponsePreview(previewSuccess)

@Preview(name = "Error - light", showBackground = true, widthDp = 360)
@Composable
private fun ErrorPreview() = ResponsePreview(previewError)

@Preview(name = "Error - dark", showBackground = true, widthDp = 360, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ErrorDarkPreview() = ResponsePreview(previewError)
