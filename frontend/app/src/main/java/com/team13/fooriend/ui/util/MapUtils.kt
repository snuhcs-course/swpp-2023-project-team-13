package com.team13.fooriend.ui.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.team13.fooriend.ui.screen.home.MyItem

fun checkForPermission(context: Context): Boolean {
    return !(ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED)
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationFetched: (location: LatLng) -> Unit) {
    var loc: LatLng
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                loc = LatLng(latitude,longitude)
                onLocationFetched(loc)
            }
        }
        .addOnFailureListener { exception: Exception ->
            Log.d("MAP-EXCEPTION",exception.message.toString())
        }

}

fun getMarkerIconFromDrawable(context: Context, drawableId: Int, width: Int, height: Int): BitmapDescriptor {
    // drawable 리소스를 비트맵으로 변환
    val originalBitmap = BitmapFactory.decodeResource(context.resources, drawableId)

    // 비트맵 크기 조절
    val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false)

    // BitmapDescriptor로 변환
    return BitmapDescriptorFactory.fromBitmap(resizedBitmap)
}

val restaurants = listOf(
    MyItem(LatLng(37.4773666, 126.9531265), "Taco Bueno"),
    MyItem(LatLng(37.4784436, 126.9565545), "Tendong Yotsuya"),
    MyItem(LatLng(37.4790055, 126.9537665), "순이네밥상"),
    MyItem(LatLng(37.4790948, 126.95556), "멘쇼우라멘"),
    MyItem(LatLng(37.4777708, 126.9573454), "누들하우스"),
    MyItem(LatLng(37.4791105, 126.9537991), "Jeongsugseong"),
    MyItem(LatLng(37.477904, 126.9524293), "Frank Burger"),
    MyItem(LatLng(37.480378, 126.9534373), "Samcha"),
    MyItem(LatLng(37.4788032, 126.9545337), "모다 모다"),
    MyItem(LatLng(37.480606, 126.9515086), "Hanam Pig House Seoul National University Station"),
    MyItem(LatLng(37.4806412, 126.9529295), "七里香刀削面"),
    MyItem(LatLng(37.4798268, 126.9539257), "부엌우동집"),
    MyItem(LatLng(37.4781838, 126.9530122), "Chamjag"),
    MyItem(LatLng(37.47861054481183, 127.00040582567452), "Marker 1"),
    MyItem(LatLng(37.47921478418604, 127.00007289648055), "Marker 2"),
    MyItem(LatLng(37.477953218751, 127.0015639782424), "Marker 3"),
    MyItem(LatLng(37.4801124123123, 127.002785610199), "Marker 4"),
    MyItem(LatLng(37.4789341238123, 127.003456782312), "Marker 5"),
    MyItem(LatLng(37.477123912312, 127.002312315678), "Marker 6")
)