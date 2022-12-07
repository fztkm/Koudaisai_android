package com.nit.koudaisai.presentation.home.screen.map_view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.nit.koudaisai.R
import com.nit.koudaisai.domain.model.Event
import com.nit.koudaisai.domain.model.LocationId
import com.nit.koudaisai.local_database.CommonLatLng
import com.nit.koudaisai.local_database.EventDatabase
import com.nit.koudaisai.local_database.FoodDatabase


@Composable
fun MapViewScreen(
    onClickDetail: (String) -> Unit
) {
    val context = LocalContext.current
    var hasAccessFineLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasAccessCoarseLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcherFine = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasAccessFineLocationPermission = granted
        }
    )
    val launcherCoarse = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasAccessFineLocationPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcherFine.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    val nitech = LatLng(35.15705836614306, 136.92511389786347)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(nitech, 17.5f)

    }
    var showDetail by remember { mutableStateOf(false) }
    var focusedLocation: LocationId? by remember {
        mutableStateOf(null)
    }

    var properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = hasAccessFineLocationPermission || hasAccessCoarseLocationPermission,
                minZoomPreference = 12f,
                maxZoomPreference = 20f
            )
        )
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
    ) {
        Marker(
            state = MarkerState(position = CommonLatLng.stage),
            title = "２号館前ステージ",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_stage),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.STAGE
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.fiftyTwo),
            title = "52号館",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_52),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.FIFTYTWO
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.nineteen),
            title = "19号館",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_19),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.NINTEEN
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.fiftyOne),
            title = "51号館",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_51),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.FIFTYONE
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.nitechHall),
            title = "NITech Hall",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_nit),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.NIT
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.nitechHallFront),
            title = "雨天時 整理券配布場所",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_ticket),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.TICKET_LAIN
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.ticket),
            title = "晴天時 整理券配布場所",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_ticket),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.TICKET_SUN
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.gym),
            title = "体育館",
            icon = bitmapDescriptor(vectorResId = R.drawable.pn_gym),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.GYM
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.four),
            title = "4号館",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_4),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.FOUR
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.twelve),
            title = "12号館",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_12),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.TWELVE
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.seimon),
            title = "正門",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_home)
        )
        Marker(
            state = MarkerState(position = CommonLatLng.eatEntry),
            title = "模擬店受付",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_eat_entry)
        )
        Marker(
            state = MarkerState(position = CommonLatLng.eatShops),
            title = "模擬店",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_eat),
            onClick = {
                showDetail = true
                focusedLocation = LocationId.EAT
                true
            }
        )
        Marker(
            state = MarkerState(position = CommonLatLng.eatingSpace),
            title = "飲食スペース",
            icon = bitmapDescriptor(vectorResId = R.drawable.pin_eat)
        )
    }
    if (showDetail) {
        var events: List<Event> = mutableListOf()
        if (focusedLocation == LocationId.TICKET_SUN || focusedLocation == LocationId.TICKET_LAIN) {
            events = mutableListOf(EventDatabase.specialEventList.first {
                it.name == "中夜祭"
            }).apply {
                add(EventDatabase.generalEventList.first {
                    it.name == "後夜祭"
                })
            }
        } else if (focusedLocation != LocationId.EAT) {
            events = EventDatabase.generalEventList.filter {
                it.locationId == focusedLocation
            }.plus(EventDatabase.specialEventList.filter {
                it.locationId == focusedLocation
            })
        }
        AlertDialog(
            onDismissRequest = {
                showDetail = false
            },
            buttons = {},
            title = {
                Text(focusedLocation?.label ?: "")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (focusedLocation != LocationId.EAT) {
                        events.forEach {
                            EventCard(text = it.name, onClickDetail = onClickDetail)
                        }
                    } else {
                        FoodDatabase.list.forEach {
                            FoodCard(text = it.name, producer = it.producer)
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun EventCard(
    text: String,
    onClickDetail: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, modifier = Modifier.weight(1f))
        TextButton(
            onClick = { onClickDetail(text) }
        ) {
            Text(text = "詳細", color = Color.Gray)
        }
    }
}

@Composable
fun FoodCard(
    text: String,
    producer: String
) {
    Row(
        modifier = Modifier
            .padding(bottom = 1.dp)
            .verticalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text, modifier = Modifier
                .weight(3f)
                .padding(end = 8.dp)
        )
        Text(
            producer, modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun bitmapDescriptor(
    vectorResId: Int
): BitmapDescriptor? {
    val width = 125
    // retrieve the actual drawable
    val drawable = LocalContext.current.getDrawable(vectorResId) ?: return null
    drawable.setBounds(0, 0, width, width)
    val bm = Bitmap.createBitmap(
        width,
        width,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}