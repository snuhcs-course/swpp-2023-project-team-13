package com.team13.fooriend.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team13.fooriend.ui.theme.BaseGreen
import com.team13.fooriend.ui.theme.BaseGrey
import com.team13.fooriend.ui.theme.CDarkGreen
import com.team13.fooriend.ui.theme.CIvory
import com.team13.fooriend.ui.theme.CLightGreen
import com.team13.fooriend.ui.theme.CMidGreen
import com.team13.fooriend.ui.theme.CRed


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    onClick : () -> Unit = {},
    onSignUpClick : () -> Unit = {},
    onForgotClick : () -> Unit = {}
){
    val (id, idValue) = remember { mutableStateOf("") }
    val (password, passwordValue) = remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color = if (isPressed) CRed else Color.Black//CDarkGreen

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseGreen),//CMidGreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        //Text(text = "Log In Screen")
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = id,
            onValueChange = idValue,
            colors = TextFieldDefaults.colors(
                Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
//                focusedTextColor = Color.DarkGray,
                focusedContainerColor = BaseGrey,//CLightGreen,
                unfocusedContainerColor = BaseGrey,//CIvory,
            ),
            placeholder = { Text("ID", fontWeight = FontWeight.SemiBold)},
            shape = RoundedCornerShape(15.dp),
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = password,
            onValueChange = passwordValue,
            colors = TextFieldDefaults.colors(
                Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
//                focusedTextColor = Color.DarkGray,
                focusedContainerColor = BaseGrey,//CLightGreen,
                unfocusedContainerColor = BaseGrey,//CIvory,
            ),
            placeholder = { Text("PASSWORD", fontWeight = FontWeight.SemiBold)},
            shape = RoundedCornerShape(15.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { if(id.isNotEmpty() && password.isNotEmpty()){
                onClick()
            } },
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                BaseGreen,//CMidGreen,
                contentColor = color )) {
            Text(
                "LOGIN",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Don't have a Fooriend account?")
        Button(onClick = {
            onSignUpClick()
        },
            colors = ButtonDefaults.buttonColors(
                Color.Transparent,//CMidGreen,
                contentColor = Color.Black//CDarkGreen
                         )) {
            Text(
                "SIGN UP",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LogInScreenPreview(){
    LogInScreen()
}