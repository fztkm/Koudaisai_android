package com.nit.koudaisai.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.R
import com.nit.koudaisai.graph.*
import com.nit.koudaisai.theme.KoudaisaiTheme

@Composable
fun HomeBottomNavigationBar(
    items: List<HomeDestination>,
    currentRoute: String?,
    onClickIcon: (route: String) -> Unit = {}
) {
    val primaryColor = MaterialTheme.colors.primary
    BottomNavigation(
        modifier = Modifier.drawBehind {
            val strokeWidth = 8f
            val x = size.width - strokeWidth
            drawLine(
                color = primaryColor,
                start = Offset(0f, 0f),
                end = Offset(x, 0f),
                strokeWidth = strokeWidth
            )
        },
        backgroundColor = MaterialTheme.colors.surface
    ) {

        items.forEach { screen ->
            val isInfoSelected = currentRoute == Info.route
            if (screen == Info)
                BottomNavigationItem(
                    icon = {
                        if (isInfoSelected)
                            Image(
                                modifier = Modifier
                                    .padding(vertical = 13.dp)
                                    .fillMaxHeight(),
                                painter = painterResource(id = R.drawable.kohunman_face_color),
                                contentScale = ContentScale.FillHeight,
                                contentDescription = "古墳マン_アイコン"
                            )
                        else
                            Image(
                                modifier = Modifier
                                    .padding(vertical = 15.dp)
                                    .fillMaxHeight(),
                                painter = painterResource(id = R.drawable.kohunman_face_gray),
                                contentScale = ContentScale.FillHeight,
                                contentDescription = "古墳マン_アイコン"
                            )
                    },
                    selected = isInfoSelected,
                    onClick = { onClickIcon(screen.route) }
                )
            else
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = screen.icon),
                            contentDescription = screen.route,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    selected = currentRoute == screen.route,
                    unselectedContentColor = MaterialTheme.colors.onSurface,
                    selectedContentColor = MaterialTheme.colors.primary,
                    onClick = { onClickIcon(screen.route) }
                )
        }
    }
}

@Preview
@Composable
private fun BottomNavPreview() {
    KoudaisaiTheme {
        HomeBottomNavigationBar(
            items = listOf(Overview, Events, Mapview, Schedule),
            currentRoute = Overview.route
        )
    }
}

@Preview
@Composable
private fun BottomNavPreview2() {
    KoudaisaiTheme {
        HomeBottomNavigationBar(
            items = listOf(Overview, Events, Mapview, Schedule),
            currentRoute = Events.route
        )
    }
}


