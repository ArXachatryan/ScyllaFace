package ai.face.liva.sdk.presentation.screens.consent

import ai.face.liva.sdk.ScyllaAiDetectionType
import ai.face.liva.sdk.appHelper.global.AppContextHolder.globalContext
import ai.face.liva.sdk.ext.openLink
import ai.face.liva.sdk.presentation.component.MainButton
import ai.face.liva.sdk.presentation.routing.ScreensRouting
import ai.face.liva.sdk.ui.theme.appLightColor
import ai.face.liva.sdk.ui.theme.appSecondaryDarkColor
import ai.face.liva.sdk.ui.theme.appSecondaryStrokeColor
import ai.face.liva.sdk.ui.theme.appStyleColor
import ai.face.liva.sdk.ui.theme.appWhiteColor
import ai.face.liva.sdk.ui.theme.inputBorderShape
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ConsentScreen(
    navController: NavController,
    detectionType: ScyllaAiDetectionType
) {
    var isAgreed by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Personal Data Processing",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 23.sp,
                modifier = Modifier.padding(bottom = 25.dp)
            )

            Column(
                modifier = Modifier.padding(start = 10.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row {
                    Text(
                        text = "\uD83D\uDD18  ",
                        fontSize = 15.sp,
                        style = TextStyle(
                            color = appWhiteColor,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            lineHeight = 25.sp
                        ),
                    )

                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "I declare that I am at least 16 years of age",
                        fontSize = 15.sp,
                        style = TextStyle(
                            color = appWhiteColor,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            lineHeight = 25.sp
                        ),
                    )
                }

                Row {
                    Text(
                        text = "\uD83D\uDD18  ",
                        fontSize = 15.sp,
                        style = TextStyle(
                            color = appWhiteColor,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            lineHeight = 25.sp
                        ),
                    )

                    Text(
                        fontSize = 15.sp,
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "I shall be fully responsible in case I provide wrong information or any proof I use is fake, forged, counterfeit, etc.",
                        style = TextStyle(
                            color = appWhiteColor,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            lineHeight = 25.sp
                        ),
                    )
                }

                Row {
                    Text(
                        text = "\uD83D\uDD18  ",
                        fontSize = 15.sp,
                        style = TextStyle(
                            color = appWhiteColor,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            lineHeight = 25.sp
                        ),
                    )

                    Text(
                        style = TextStyle(
                            color = appWhiteColor,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            lineHeight = 25.sp
                        ),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "I agree to the collection, processing, and storage of my personal information, including biometric data, by facial for the purpose(s) of verifying that the information I provide is true and accurate to the best of my knowledge"
                    )
                }


            }


        }

        Spacer(modifier = Modifier.weight(1f))

        PrivacyPolicyInfoBox(
            modifier = Modifier.padding(bottom = 24.dp),
            isChecked = isAgreed
        ) {
            isAgreed = it
        }


        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Column {
            Text(
                text = "Powered By SCYLLA",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            MainButton(
                enabled = isAgreed,
                modifier = Modifier,
                name = "Continue",

                ) {

                if (detectionType == ScyllaAiDetectionType.LIVENESS){

                    navController.navigate(ScreensRouting.LiveDetection.route)

                }else{

                    navController.navigate(ScreensRouting.UploadDocument.route)
                }


            }

        }
    }
}

@Composable
private fun PrivacyPolicyInfoBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onTermsChecked: (Boolean) -> Unit = {},
) {

    Row(
        modifier = modifier
            .background(
                color = appSecondaryDarkColor,
                shape = inputBorderShape
            )
            .heightIn(60.dp)
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = appSecondaryStrokeColor,
                ), shape = inputBorderShape
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onTermsChecked
        )

        val context = LocalContext.current
        var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
        val annotatedText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = appLightColor, fontSize = 15.sp)) {
                append("I agree with ")
            }
            pushStringAnnotation(
                tag = "privacy_policy",
                annotation = "https://example.com/privacy"
            )
            withStyle(style = SpanStyle(color = appStyleColor, fontSize = 15.sp)) {
                append("Privacy Policy")
            }
            pop()
        }

        BasicText(
            text = annotatedText,
            modifier = Modifier
                .padding(end = 8.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures { offsetPosition ->
                        layoutResult?.let { layout ->
                            val offset = layout.getOffsetForPosition(offsetPosition)
                            annotatedText.getStringAnnotations("privacy_policy", offset, offset)
                                .firstOrNull()?.let { annotation ->
                                    context.openLink(link = annotation.item)

                                }
                        }
                    }
                },
            onTextLayout = { layoutResult = it }
        )


    }
}

