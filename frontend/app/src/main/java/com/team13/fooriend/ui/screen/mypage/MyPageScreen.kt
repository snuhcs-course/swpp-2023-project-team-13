package com.team13.fooriend.ui.screen.mypage

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.compose.rememberImagePainter
import com.team13.fooriend.ui.component.ProfileSection
import com.team13.fooriend.ui.component.ReviewLazyGrid
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.Review
import com.team13.fooriend.ui.util.User
import com.team13.fooriend.ui.util.createRetrofit


@Composable
fun MyPageScreen(
    context: Context,
    onMyInfoClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit
){
    val retrofit = createRetrofit(context)
    val apiService = retrofit.create(ApiService::class.java)

    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var myName by remember { mutableStateOf("") }
    var myId by remember { mutableStateOf(0) }
    var followerList by remember { mutableStateOf<List<Int>>(emptyList()) }
    var followingList by remember { mutableStateOf<List<Int>>(emptyList()) }
    var followers by remember { mutableStateOf<List<User>>(emptyList()) }
    var followings by remember { mutableStateOf<List<User>>(emptyList()) }
    var showPopup by remember { mutableStateOf(false) }
    var clickedFollower by remember { mutableStateOf(false) }
    fun changePopupState(bool: Boolean){
        clickedFollower = bool
        showPopup = !showPopup
    }
    Log.d("MyPageScreen", "isLoading: $isLoading")
    LaunchedEffect(Unit) {
        try {
            // API 호출하여 데이터 가져오기
            val response = apiService.getMyReviews()
            val response2 = apiService.getMyInfo()
            myId = response2.id
            myName = response2.name
            reviews = response.reviewList
            followerList = apiService.getFollows(myId).followers.map {
                it.id
            }
            for(id in followerList){
                followers += apiService.getUserDetail(id)
            }
            followingList = apiService.getFollows(myId).followings.map{
                it.id
            }
            for(id in followingList){
                followings += apiService.getUserDetail(id)
            }
        } catch (e: Exception) {
            Log.d("RestaurantDetailScreen", "error: $e")
        }
        isLoading = false
    }
    if(!isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, top = 7.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "face",
                        modifier = Modifier
                            .width(15.dp)
                            .height(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = myName,
                        style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp),
                    )
                }
                IconButton(onClick = onMyInfoClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "My Info",
                        tint = Color.Black,
                    )
                }
            }
            // 프로필 섹션
            ProfileSection(
                context = context,
                userId = myId,
                clickedFollower = { changePopupState(true) },
                clickedFollowing = { changePopupState(false) },
            )
            Log.d("MyPageScreen", "showPopup: $showPopup")
            if (showPopup) {
                Popup(
                    alignment = Alignment.Center,
                    onDismissRequest = { showPopup = false }
                ) {
                    // Your popup content
                    Card(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp),
                    ) {
                        Column {
                            // Tabs for Followers and Following
                            var selectedTab by remember { mutableStateOf(if(clickedFollower) 0 else 1) }
                            TabRow(selectedTabIndex = selectedTab) {
                                Tab(
                                    modifier = Modifier
                                        .shadow(4.dp, RoundedCornerShape(4.dp)),
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    text = { Text("Followers") }
                                )
                                Tab(
                                    modifier = Modifier
                                        .shadow(4.dp, RoundedCornerShape(4.dp)),
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
                                    text = { Text("Following") }
                                )
                            }

                            // List content
                            when (selectedTab) {
                                0 -> UserList(userList = followers, onUserClick = onUserClick)
                                1 -> UserList(userList = followings, onUserClick = onUserClick)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            // 중앙의 음식 리스트
            ReviewLazyGrid(reviews = reviews, onReviewClick = onReviewClick)
        }
    }
}

@Composable
fun UserList(userList: List<User>, onUserClick: (Int) -> Unit) { // Assuming User is a data class with name and photoUrl
    LazyColumn {
        items(userList) { user ->
            UserListItem(user, onUserClick)
        }
    }
}

@Composable
fun UserListItem(user: User, onUserClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onUserClick(user.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(data = user.profileImage),
            contentDescription = "User Photo",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape), // Circle shape for the image
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = user.name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun MyPageScreenPreview(){
//    MyPageScreen(
//        context = TODO(),
//        onMyInfoClick = {  },
////        onReviewClick = {  })
//}