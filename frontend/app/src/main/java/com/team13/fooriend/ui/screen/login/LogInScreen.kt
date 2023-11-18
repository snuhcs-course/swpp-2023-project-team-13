package com.team13.fooriend.ui.screen.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team13.fooriend.ui.theme.CRed
import com.team13.fooriend.ui.theme.FooriendColor
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.LoginBody
import com.team13.fooriend.ui.util.LoginResponse
import com.team13.fooriend.ui.util.saveAccessToken
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    context : Context,
    onClick : () -> Unit = {},
    onSignUpClick : () -> Unit = {},
    onForgotClick : () -> Unit = {}
){
    val (id, idValue) = remember { mutableStateOf("") }
    val (password, passwordValue) = remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val color = if (isPressed) CRed else Color.DarkGray//CDarkGreen
    val coroutineScope = rememberCoroutineScope()
    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-101-207.ap-northeast-2.compute.amazonaws.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
    val (isPasswordVisible, setPasswordVisibility) = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),//CMidGreen),
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
                focusedContainerColor = FooriendColor.FooriendLightGreen,
                unfocusedContainerColor = FooriendColor.FooriendLightGray
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
                focusedContainerColor = FooriendColor.FooriendLightGreen,
                unfocusedContainerColor = FooriendColor.FooriendLightGray,
            ),
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            placeholder = { Text("PASSWORD", fontWeight = FontWeight.SemiBold) },
            trailingIcon = {
                val icon = if (isPasswordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }
                IconButton(
                    onClick = { setPasswordVisibility(!isPasswordVisible) },
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Color.DarkGray
                    )
                }
            },
            shape = RoundedCornerShape(15.dp),
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = { if(id.isNotEmpty() && password.isNotEmpty()){
                coroutineScope.launch{
                    var response: LoginResponse? = null
                    try{
                        response = apiService.login(LoginBody(id, password))
                    } catch(e: Exception){
                        Toast.makeText(context, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }
                    saveAccessToken(context = context, token = response.accessToken)
                    onClick()
                }
            } },
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                FooriendColor.FooriendGreen,
                contentColor = color )) {
            Text(
                "LOGIN",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            onSignUpClick()
        },
            colors = ButtonDefaults.buttonColors(
                Color.Transparent,
                contentColor = FooriendColor.FooriendGreen,
            )) {
            Text(
                "SIGN UP",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LogInScreenPreview(){
    LogInScreen( TODO())
}