package com.team13.fooriend.ui.component

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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team13.fooriend.R

@Composable
fun ProfileSection(
    // default value는 추후에 모두 지우자
    username : String = "ozeeeno",
    followersCount : Int = 100,
    followingCount : Int = 50,
    userProfileImageUrl : Int = R.drawable.profile_cat,
    isFooried : Boolean = false,
    onFollowClick : () -> Unit = {},
    isMyPage : Boolean = false,
) {
    var isFollowed by remember { mutableStateOf(isFooried) } // 팔로우 여부 <- 서버에서 관리해야 함
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
                painter = painterResource(id = userProfileImageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(){
            Text(
                text = username,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Followers \n $followersCount", color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Following \n $followingCount", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(8.dp))
            // 팔로우 버튼이 null이 아닌경우 == mypage에서 호출하지 않은 경우
            if(!isMyPage){
                if(!isFollowed){
                    Button(onClick = onFollowClick) {// 팔로우 되어 있지 않은 친구의 경우
                        Text(text = "Follow")
                    }
                }
                else{
                    Button(onClick = onFollowClick) {// 팔로우 되어 있던 친구의 경우
                        Text(text = "Unfollow")
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ProfileSectionPreview(){
    ProfileSection("조용찬", 10, 20, R.drawable.profile_cat,
        isFooried = false, onFollowClick = {}, isMyPage = false)
}