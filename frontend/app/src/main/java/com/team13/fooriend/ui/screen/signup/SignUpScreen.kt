package com.team13.fooriend.ui.screen.signup

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team13.fooriend.ui.theme.CRed
import com.team13.fooriend.ui.theme.FooriendColor
import com.team13.fooriend.ui.util.ApiService
import com.team13.fooriend.ui.util.RegisterBody
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    context : Context,
    onSignUpClick : () -> Unit = {},
    onBackClick : () -> Unit = {},
){
    val (name, nameValue) = remember { mutableStateOf("") }
    val (id, idValue) = remember { mutableStateOf("") }
    val (password, passwordValue) = remember { mutableStateOf("") }
    val (passwordCheck, passwordCheckValue) = remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color = if (isPressed) CRed else Color.DarkGray//CDarkGreen

//    val (phoneNumber, phoneNumberValue) = remember { mutableStateOf("") }
    val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-101-207.ap-northeast-2.compute.amazonaws.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
    val coroutineScope = rememberCoroutineScope()
    val (isPasswordVisible, setPasswordVisibility) = remember { mutableStateOf(false) }
    val (isPasswordConfirmVisible, setPasswordConfirmVisibility) = remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column(modifier = Modifier.fillMaxSize()){
        Box(){
            IconButton(onClick = onBackClick, modifier = Modifier.padding(top = 7.dp).testTag("backButton")){
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),//CMidGreen),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            //Text(text = "Sign Up Page")
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                modifier = Modifier.testTag("nameTextField"),
                value = name,
                onValueChange = nameValue,
                colors = TextFieldDefaults.colors(
                    Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = FooriendColor.FooriendLightGreen,
                    unfocusedContainerColor = FooriendColor.FooriendLightGray,
                ),
                placeholder = { Text("NAME", fontWeight = FontWeight.SemiBold)},
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextField(
                modifier = Modifier.testTag("idTextField"),
                value = id,
                onValueChange = idValue,
                colors = TextFieldDefaults.colors(
                    Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = FooriendColor.FooriendLightGreen,
                    unfocusedContainerColor = FooriendColor.FooriendLightGray,
                ),
                placeholder = { Text("ID", fontWeight = FontWeight.SemiBold)},
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextField(
                modifier = Modifier.testTag("passwordTextField"),
                value = password,
                onValueChange = passwordValue,
                colors = TextFieldDefaults.colors(
                    Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = FooriendColor.FooriendLightGreen,
                    unfocusedContainerColor = FooriendColor.FooriendLightGray,
                ),visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                placeholder = { Text("PASSWORD", fontWeight = FontWeight.SemiBold)},
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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextField(
                modifier = Modifier.testTag("passwordCheckTextField"),
                value = passwordCheck,
                onValueChange = passwordCheckValue,
                colors = TextFieldDefaults.colors(
                    Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = FooriendColor.FooriendLightGreen,
                    unfocusedContainerColor = FooriendColor.FooriendLightGray,
                ),
                visualTransformation = if (isPasswordConfirmVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                placeholder = { Text("PASSWORD CONFIRM", fontWeight = FontWeight.SemiBold)},
                trailingIcon = {
                    val icon = if (isPasswordConfirmVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    IconButton(
                        onClick = { setPasswordConfirmVisibility(!isPasswordConfirmVisible) },
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = Color.DarkGray
                        )
                    }
                },
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        if(name == "" || id == "" || password == "" || passwordCheck == ""){
                            Toast.makeText(context, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        if(password != passwordCheck){
                            Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        val response = apiService.register(RegisterBody(name,id, password))
                        onSignUpClick()
                    } },
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(
                    FooriendColor.FooriendGreen,
                    contentColor = color )) {
                Text(
                    "SIGN UP",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreenPreview(){
    SignUpScreen(TODO())
}