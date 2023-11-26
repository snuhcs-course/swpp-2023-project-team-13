package com.team13.fooriend.ui.component

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.team13.fooriend.R
import com.team13.fooriend.ui.theme.FooriendColor
import com.team13.fooriend.ui.util.AbstractUser
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.createRetrofit
import kotlinx.coroutines.launch

@Composable
fun ProfileSection(
    context: Context,
    userId: Int,
    onFollowClick : () -> Unit = {},
    isMyPage : Boolean = false,
    clickedFollower : () -> Unit = {},
    clickedFollowing : () -> Unit = {},
) {
    val retrofit = createRetrofit(context)
    val apiService = retrofit.create(ApiService::class.java)
    var username by remember { mutableStateOf("")}
    var followerCount by remember { mutableStateOf(0) }
    var followingCount by remember { mutableStateOf(0) }
    var userProfileImageUrl by remember { mutableStateOf("") }
    var isFollowing by remember { mutableStateOf(false) }
    var myId by remember { mutableStateOf(0) }


    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isFollowing){
        myId = apiService.getMyInfo().id
        val myFollowings = apiService.getFollows(myId).followings
        var user = apiService.getUserDetail(userId = userId)
        username = user.name
        followerCount = user.followerCount
        followingCount = user.followingCount
        userProfileImageUrl = user.profileImage
        isFollowing = myFollowings.contains(AbstractUser(userId, username))
        Log.d("ProfileSection", "isFollowing: $isFollowing")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier
                .size(105.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = rememberImagePainter(
                    data = userProfileImageUrl,
                    builder = {
                        crossfade(true)
                    }),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = clickedFollower, modifier = Modifier.testTag("followersButton")) {
                    Text(
                        text = "$followerCount \n Followers",
                        color = Color.Black,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                    )
                }
                TextButton(onClick = clickedFollowing, modifier = Modifier.testTag("followingButton")) {
                    Text(
                        text = "$followingCount \n Following",
                        color = Color.Black,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // 팔로우 버튼이 null이 아닌경우 == mypage에서 호출하지 않은 경우
            if(userId != myId){
                if(!isFollowing){
                    Button(
                        onClick = {
                        coroutineScope.launch {
                            val res = apiService.follow(userId)
                            isFollowing = true
                        } },
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FooriendColor.FooriendGreen,
                            contentColor = Color.White,
                        )
                    ) {
                        Text(text = "Follow")
                    }

                }
                else{
                    Button(
                        onClick = {
                            coroutineScope.launch {
                            val res = apiService.follow(userId)
                            isFollowing = false
                            } },
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FooriendColor.FooriendGray,
                            contentColor = Color.White,
                        )) {
                        Text(text = "Unfollow")
                    }
                }
            }
        }
    }
}

//@Composable
//@Preview(showSystemUi = true, showBackground = true)
//fun ProfileSectionPreview(){
//    ProfileSection("조용찬", 10, 20, "",
//        isFooriend = false, onFollowClick = {}, isMyPage = false)
//}