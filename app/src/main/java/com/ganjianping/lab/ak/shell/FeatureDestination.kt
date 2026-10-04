package com.ganjianping.lab.ak.shell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ganjianping.lab.ak.common.accessibility.AccessibilitySettings
import com.ganjianping.lab.ak.features.compose.accessibility.AccessibilityScreen
import com.ganjianping.lab.ak.features.compose.animation.AnimationScreen
import com.ganjianping.lab.ak.features.compose.buttons.ButtonsScreen
import com.ganjianping.lab.ak.features.compose.drawing.DrawingScreen
import com.ganjianping.lab.ak.features.compose.layouts.LayoutsScreen
import com.ganjianping.lab.ak.features.compose.lists.ListsGridsScreen
import com.ganjianping.lab.ak.features.compose.material3.Material3Screen
import com.ganjianping.lab.ak.features.compose.navigation.NavigationPatternsScreen
import com.ganjianping.lab.ak.features.compose.selection.SelectionScreen
import com.ganjianping.lab.ak.features.compose.textinput.TextInputScreen
import com.ganjianping.lab.ak.features.kotlin.basics.KotlinBasicsScreen
import com.ganjianping.lab.ak.features.kotlin.nullsafety.NullSafetyScreen
import com.ganjianping.lab.ak.features.kotlin.collections.CollectionsScreen
import com.ganjianping.lab.ak.features.kotlin.functions.FunctionsScreen
import com.ganjianping.lab.ak.features.kotlin.classes.ClassesScreen
import com.ganjianping.lab.ak.features.kotlin.generics.GenericsScreen
import com.ganjianping.lab.ak.features.kotlin.errors.ErrorHandlingScreen
import com.ganjianping.lab.ak.features.kotlin.coroutines.CoroutinesScreen
import com.ganjianping.lab.ak.features.kotlin.extensions.ExtensionsScreen
import com.ganjianping.lab.ak.features.kotlin.strings.StringsRegexScreen
import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpURLConnectionRepository
import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpURLConnectionScreen
import com.ganjianping.lab.ak.features.integration.firebase.FirebaseFeatureScreen
import com.ganjianping.lab.ak.features.integration.firebase.FirebaseIntegration
import com.ganjianping.lab.ak.features.others.deviceinfo.DeviceInfoRepository
import com.ganjianping.lab.ak.features.others.deviceinfo.DeviceInfoScreen
import com.ganjianping.lab.ak.features.security.blockappduringcalls.BlockAppDuringCallsController
import com.ganjianping.lab.ak.features.security.blockappduringcalls.BlockAppDuringCallsScreen
import com.ganjianping.lab.ak.shell.navigation.DetailRoute
import com.ganjianping.lab.ak.shell.navigation.FeatureRoute

/** The Koin-provided objects the feature screens need; `MainActivity` injects them and passes them down. */
class FeatureDependencies(
    val deviceInfoRepository: DeviceInfoRepository,
    val httpURLConnectionRepository: HttpURLConnectionRepository,
    val firebaseIntegration: FirebaseIntegration,
    val callBlocker: BlockAppDuringCallsController,
    val accessibilitySettings: AccessibilitySettings
)

/** The one place that maps a [FeatureRoute] to its screen. */
@Composable
fun FeatureDestination(
    route: FeatureRoute,
    dependencies: FeatureDependencies,
    onPush: (DetailRoute) -> Unit
) {
    val accessibility by dependencies.accessibilitySettings.status
        .collectAsState(initial = dependencies.accessibilitySettings.current())
    when (route) {
        FeatureRoute.KotlinBasics -> KotlinBasicsScreen()
        FeatureRoute.NullSafety -> NullSafetyScreen()
        FeatureRoute.Collections -> CollectionsScreen()
        FeatureRoute.Functions -> FunctionsScreen()
        FeatureRoute.Classes -> ClassesScreen()
        FeatureRoute.Generics -> GenericsScreen()
        FeatureRoute.ErrorHandling -> ErrorHandlingScreen()
        FeatureRoute.Coroutines -> CoroutinesScreen()
        FeatureRoute.Extensions -> ExtensionsScreen()
        FeatureRoute.StringsRegex -> StringsRegexScreen()
        FeatureRoute.Material3 -> Material3Screen()
        FeatureRoute.Layouts -> LayoutsScreen()
        FeatureRoute.TextInput -> TextInputScreen()
        FeatureRoute.ButtonsActions -> ButtonsScreen()
        FeatureRoute.Selection -> SelectionScreen()
        FeatureRoute.ListsGrids -> ListsGridsScreen()
        FeatureRoute.NavigationPatterns -> NavigationPatternsScreen(onPush)
        FeatureRoute.Animation -> AnimationScreen(reduceMotion = accessibility.reduceMotion)
        FeatureRoute.Drawing -> DrawingScreen(reduceMotion = accessibility.reduceMotion)
        FeatureRoute.Accessibility -> AccessibilityScreen(status = accessibility)
        FeatureRoute.DeviceInfo -> DeviceInfoScreen(dependencies.deviceInfoRepository)
        FeatureRoute.HttpURLConnection -> HttpURLConnectionScreen(dependencies.httpURLConnectionRepository) { response ->
            onPush(DetailRoute.Response(response))
        }
        FeatureRoute.Firebase -> FirebaseFeatureScreen(dependencies.firebaseIntegration)
        FeatureRoute.BlockAppDuringCalls -> BlockAppDuringCallsScreen(dependencies.callBlocker)
    }
}
