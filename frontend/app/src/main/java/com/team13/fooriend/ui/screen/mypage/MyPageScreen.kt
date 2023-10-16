package com.team13.fooriend.ui.screen.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team13.fooriend.R
import com.team13.fooriend.ui.component.ScreenChangeBar


@Composable
fun MyPageScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 프로필 섹션
        ProfileSection()

        Spacer(modifier = Modifier.height(16.dp))


        // 중앙의 음식 리스트
        MyPage_foodpicList()

        Spacer(modifier = Modifier.weight(1f))

    }
    ScreenChangeBar(navController)
}


@Composable
fun ProfileSection() {
    val userProfileImageUrl = painterResource(id = R.drawable.profile_cat)
    val username = "ozeeeno"
    val followersCount = 100
    val followingCount = 50

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Image(
                painter = userProfileImageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = username,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$followersCount Followers", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "$followingCount Following", color = MaterialTheme.colorScheme.primary)
        }
    }
}


@Composable
fun MyPage_foodpicList() {
    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(2) { rowIndex ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) { columnIndex ->
                    val index = rowIndex * 3 + columnIndex
                    if (index < 15) {
                        Mypage_ImageListItem(index)
                    } else {
                        Spacer(modifier = Modifier.size(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Mypage_ImageListItem(index: Int) {
    Box(
        modifier = Modifier
            .size(100.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.hamburger),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


    }
}