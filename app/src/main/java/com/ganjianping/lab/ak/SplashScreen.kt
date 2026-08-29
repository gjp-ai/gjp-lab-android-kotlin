package com.ganjianping.lab.ak

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ganjianping.lab.ak.common.theme.GJPLabTheme

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BrandMark(isDarkTheme = isSystemInDarkTheme())
            Spacer(Modifier.height(20.dp))
            Text(
                text = "GJP Lab",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
        }
    }
}

@Composable
private fun BrandMark(isDarkTheme: Boolean) {
    val markResource = if (isDarkTheme) {
        R.drawable.ic_lab_mark_dark
    } else {
        R.drawable.ic_launcher_foreground
    }

    Box(
        modifier = Modifier
            .size(112.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(markResource),
            contentDescription = null,
            modifier = Modifier.size(88.dp),
            tint = Color.Unspecified
        )
    }
}

@Preview(name = "Splash screen - light", showBackground = true)
@Composable
private fun SplashScreenPreview() {
    GJPLabTheme {
        SplashScreen()
    }
}

@Preview(
    name = "Splash screen - dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SplashScreenDarkPreview() {
    GJPLabTheme {
        SplashScreen()
    }
}
