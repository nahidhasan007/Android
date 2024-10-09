package com.example.composebasics.basecomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composebasics.R
import com.example.composebasics.ui.theme.dark900
import com.example.composebasics.ui.theme.white

@Composable
fun InfoViewContainer(
    modifier: Modifier = Modifier,
    startIcon: Int? = null,
    text: String = "Favourite Guest List",
    endIcon: Int? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = white),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        startIcon?.let { painterResource(id = it) }?.let {
            Image(
                modifier = Modifier
                    .padding(start = 12.dp, top = 10.dp, bottom = 10.dp)
                    .weight(0.1f)
                    .width(24.dp)
                    .height(24.dp),
                painter = it,
                contentDescription = "image description",
                contentScale = ContentScale.None
            )
        }
        Text(
            modifier = Modifier.weight(0.8f).padding(start = 16.dp),
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(400),
                color = dark900,
            )
        )
        endIcon?.let { painterResource(id = it) }?.let {
            Image(
                modifier = Modifier
                    .padding(start = 12.dp, top = 10.dp, bottom = 10.dp, end = 12.dp)
                    .weight(0.1f)
                    .width(24.dp)
                    .height(24.dp),
                painter = it,
                contentDescription = "image description",
                contentScale = ContentScale.None
            )
        }
    }

}

@Preview
@Composable

fun ShowInfoView() {
    InfoViewContainer(
        startIcon = R.drawable.base_re_profile_ic,
        text = "Favourite Guest List",
        endIcon = R.drawable.base_re_right_arrow
    )
}