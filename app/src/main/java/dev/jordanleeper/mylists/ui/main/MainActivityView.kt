package dev.jordanleeper.mylists.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.jordanleeper.mylists.data.MainActivityViewModel
import dev.jordanleeper.mylists.data.ParentList
import dev.jordanleeper.mylists.ui.button.AddListFloatingActionButton
import dev.jordanleeper.mylists.ui.dialog.AddEditListDialog
import dev.jordanleeper.mylists.ui.theme.MyListsTheme
import dev.jordanleeper.mylists.ui.theme.White
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityView(viewModel: MainActivityViewModel) {
    val parentListWithSubLists by viewModel.readAllData.observeAsState(initial = listOf())
    val showAddListDialog = remember { mutableStateOf(false) }

    MyListsTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            "List Keeper",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
            )
        }, floatingActionButton = {
            AddListFloatingActionButton(showAddListDialog)
        }, content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                AddEditListDialog(
                    showAddListDialog,
                    label = "Add List",
                ) { newListName, newListColor, newTextColor ->
                    viewModel.addList(
                        ParentList(
                            name = newListName,
                            color = newListColor,
                            textColor = newTextColor ?: White,
                            isComplete = false,
                            dateCreated = Date().time
                        )
                    )
                    showAddListDialog.value = false
                }

                LazyColumn(
                    Modifier
                        .fillMaxSize()
                ) {
                    items(
                        parentListWithSubLists,
                        key = { it.parentList.hashCode() }) { it ->
                        MainListItem(it, viewModel)
                    }

                }

            }
        }, containerColor = MaterialTheme.colorScheme.background)
    }
}