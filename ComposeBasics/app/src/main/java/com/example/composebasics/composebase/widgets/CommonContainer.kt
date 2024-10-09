package com.example.composebasics.composebase.widgets

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInputField(
    modifier: Modifier = Modifier,
    endIcon: Int? = null,
    placeholderText: String? = null,
    onInputSave: (String) -> Unit,
//    inputType: InputType = InputType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester? = null,
    isError: Boolean? = false,
    errorMessage: String? = null,
    onErrorChange: (Boolean) -> Unit,
    savedText : String = ""
) {
    var errorMessagev1 by remember { mutableStateOf<String?>(null) }
    var localError by remember {
        mutableStateOf(isError)
    }
    var isFocused by remember { mutableStateOf(false) }

//    fun validateAndUpdateError(value: String) {
//        val isInputValid = if (value.isNotEmpty()) validateInput(value, inputType) else true
//        errorMessagev1 = if (!isInputValid) {
//            when (inputType) {
//                InputType.Email -> "Please enter a valid email address!"
//                InputType.Phone -> "Please enter a valid phone number!"
//                InputType.Text -> "Please enter a valid input!"
//            }
//        } else null
//        localError = errorMessagev1!=null
//        onErrorChange(errorMessagev1 != null)
//    }

//    if (isError == true && savedText.isEmpty()) {
//        localError = true
//        errorMessagev1 = when (inputType) {
//            InputType.Email -> "Please enter a valid email address!"
//            InputType.Phone -> "Please enter a valid phone number!"
//            InputType.Text -> "Please enter a valid input!"
//        }
//    }
//
//    val backgroundColor = when {
//        localError == true -> BaseRed25
//        else -> BaseDark50
//    }
//
//    val borderColor = when {
//        localError == true -> BaseRed500
//        else -> BaseDark50
//    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 10.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = if(isFocused) { Color.White} else {Color.Blue},
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .focusable()
                .onFocusChanged { focusState->
                    isFocused = focusState.isFocused
                }
                .border(
                    width = 1.dp,
                    color = if(isFocused) { Color.White} else {Color.Blue},
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .padding(horizontal = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .then(focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier),
                value = savedText,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(8.dp),
                onValueChange = { value: String ->
//                    savedText = value
                    onInputSave(value)
//                    validateAndUpdateError(value)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = when (inputType) {
//                        InputType.Email -> KeyboardType.Email
//                        InputType.Phone -> KeyboardType.Phone
//                        InputType.Text -> KeyboardType.Text
//                    },
                    imeAction = ImeAction.Next
                ),
                keyboardActions = keyboardActions,
                trailingIcon = {
                    if (endIcon != null) {
                        Image(
                            modifier = Modifier
                                .padding(1.dp)
                                .width(20.dp)
                                .height(20.dp)
                                .clickable {
                                    Log.e("Icon Clicked", "Trailing Icon Clicked!")
                                    // Handle icon click if needed
                                },
                            painter = painterResource(id = endIcon),
                            contentDescription = "image description",
                            contentScale = ContentScale.None
                        )
                    }
                },
                placeholder = {
                    placeholderText?.let {
//                        CustomTextView(text = it)
                    }
                }
            )
        }
        if (errorMessagev1 != null && !isFocused) {
            Text(
                text = errorMessagev1 ?: "",
//                style = stBaseTextStyle.label.lg.regular,
//                color = BaseRed500
            )
        }
        if (errorMessage != null && errorMessagev1.isNullOrEmpty()) {
            Text(
                text = errorMessage ?: "",
//                style = stBaseTextStyle.label.lg.regular,
//                color = BaseRed500
            )
        }

    }
}