package dev.jordanleeper.mylists.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.jordanleeper.mylists.data.ParentList
import dev.jordanleeper.mylists.ui.button.ListColorButton
import dev.jordanleeper.mylists.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditListDialog(
    showAddListDialog: MutableState<Boolean>,
    label: String,
    colors: List<String>,
    textColors: List<String>,
    parentList: ParentList? = null,
    addNewList: (newListName: String, newListColor: String, myTextColor: String) -> Unit
) {
    var newListName by remember { mutableStateOf(parentList?.name ?: "") }
    val focusRequester = remember { FocusRequester() }
    var newListColor by remember {
        mutableStateOf(
            parentList?.color ?: Blue
        )
    }

    fun resetDialogFields() {
        newListName = ""
        newListColor = Blue
    }

    if (showAddListDialog.value) {
        Dialog(
            onDismissRequest = { showAddListDialog.value = false },
            content = {
                Card() {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(
                            label,
                            modifier = Modifier
                                .padding(bottom = 5.dp),
                            fontSize = 25.sp
                        )
                        OutlinedTextField(
                            value = newListName,
                            onValueChange = { newListName = it },
                            label = { Text("Name") },
                            modifier = Modifier
                                .focusRequester(focusRequester),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    addNewList(
                                        newListName,
                                        newListColor,
                                        textColors[colors.indexOf(newListColor)]
                                    )
                                    resetDialogFields()
                                }
                            ),
                            colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        )
                        LaunchedEffect(true) {
                            focusRequester.requestFocus()
                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            colors.forEach() { color ->
                                ListColorButton(
                                    color = color,
                                    isChecked = newListColor == color,
                                    onChange = { newListColor = color }
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    showAddListDialog.value = false
                                    newListName = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                            ) {
                                Text(
                                    "Cancel",
                                    modifier = Modifier.background(MaterialTheme.colorScheme.tertiary),
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                            Button(
                                onClick = {
                                    addNewList(
                                        newListName,
                                        newListColor,
                                        textColors[colors.indexOf(newListColor)]
                                    )
                                    resetDialogFields()
                                }, modifier = Modifier.padding(start = 15.dp)
                            ) {
                                Text("Add")
                            }
                        }
                    }
                }
            })
    }
}