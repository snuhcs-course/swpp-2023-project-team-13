package com.team13.fooriend.ui.screen.signup

import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team13.fooriend.ui.theme.BaseGreen
import com.team13.fooriend.ui.theme.BaseGray
import com.team13.fooriend.ui.theme.CDarkGreen
import com.team13.fooriend.ui.theme.CIvory
import com.team13.fooriend.ui.theme.CLightGreen
import com.team13.fooriend.ui.theme.CMidGreen
import com.team13.fooriend.ui.theme.CRed
import java.time.format.TextStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpClick : () -> Unit = {}
){
    val (name, nameValue) = remember { mutableStateOf("") }
    val (id, idValue) = remember { mutableStateOf("") }
    val (password, passwordValue) = remember { mutableStateOf("") }
    val (passwordCheck, passwordCheckValue) = remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color = if (isPressed) CRed else Color.DarkGray//CDarkGreen

//    val (phoneNumber, phoneNumberValue) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),//CMidGreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        //Text(text = "Sign Up Page")
        Spacer(modifier = Modifier.height(20.dp))
        //val containerColor = FilledTextFieldTokens.ContainerColor.toColor()
        TextField(
            value = name,
            onValueChange = nameValue,
            colors = TextFieldDefaults.colors(
                Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
//                focusedTextColor = Color.DarkGray,
                focusedContainerColor = BaseGray,//CLightGreen,
                unfocusedContainerColor = BaseGray,//CIvory,
            ),
            placeholder = { Text("NAME", fontWeight = FontWeight.SemiBold)},
            shape = RoundedCornerShape(15.dp),
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = id,
            onValueChange = idValue,
            colors = TextFieldDefaults.colors(
                Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
//                focusedTextColor = Color.DarkGray,
                focusedContainerColor = BaseGray,//CLightGreen,
                unfocusedContainerColor = BaseGray,//CIvory,
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
                focusedContainerColor = BaseGray,//CLightGreen,
                unfocusedContainerColor = BaseGray,//CIvory,
            ),
            placeholder = { Text("PASSWORD", fontWeight = FontWeight.SemiBold)},
            shape = RoundedCornerShape(15.dp),
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = passwordCheck,
            onValueChange = passwordCheckValue,
            colors = TextFieldDefaults.colors(
                Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
//                focusedTextColor = Color.DarkGray,
                focusedContainerColor = BaseGray,//CLightGreen,
                unfocusedContainerColor = BaseGray,//CIvory,
            ),
            placeholder = { Text("PASSWORD CONFIRM", fontWeight = FontWeight.SemiBold)},
            shape = RoundedCornerShape(15.dp),
        )
//        TODO("keyboard hide")
//        TODO("password confirm check")
        Spacer(modifier = Modifier.height(30.dp))
//        TextField(value = nickname, onValueChange = nicknameValue)
//        TextField(value = phoneNumber, onValueChange = phoneNumberValue)
        Button(
            onClick = { onSignUpClick() },
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                BaseGreen,//CMidGreen,
                contentColor = color )) {
            Text(
                "SIGN UP",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
                )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpScreenPreview(){
    SignUpScreen()
}