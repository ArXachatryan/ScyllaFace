package ai.face.liva.sdk.presentation.component

import ai.face.liva.sdk.ui.theme.appBackgroundColor
import ai.face.liva.sdk.ui.theme.appLightColor
import ai.face.liva.sdk.ui.theme.appStyleColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainButton(
    enabled:Boolean = true,
    modifier: Modifier,
    name:String,
    startIcon:Int?=null,
    click:()->Unit){
    Button(
        onClick = {
            click.invoke()
        },
        modifier = modifier.fillMaxWidth().height(50.dp),
        enabled = enabled
    ) {
        startIcon?.let {
            Image(
                modifier = modifier.size(27.dp),
                painter = painterResource(it),
                colorFilter = ColorFilter.tint(appBackgroundColor),
                contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            style = TextStyle(color = appBackgroundColor, fontWeight = FontWeight.Bold),
            text = name, fontSize = 16.sp)
    }

}

@Composable
fun MainOutlinedButton(
    modifier: Modifier,
    name:String,
    startIcon:Int?=null,
    click:()->Unit){
    OutlinedButton(
        border = BorderStroke(width = 0.7.dp, color = appStyleColor),
        onClick = {
            click.invoke()
        },
        modifier = modifier.fillMaxWidth().height(50.dp)
    ) {
        startIcon?.let {
            Image(
                modifier = modifier.size(27.dp),
                painter = painterResource(it),
                colorFilter = ColorFilter.tint(appStyleColor),
                contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            style = TextStyle(color = appStyleColor, fontWeight = FontWeight.Bold),
            text = name, fontSize = 16.sp)
    }

}