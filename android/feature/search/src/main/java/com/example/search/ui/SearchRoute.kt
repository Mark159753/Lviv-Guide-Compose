package com.example.search.ui

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.PlaceModel
import com.example.search.R
import com.example.ui.modifiers.drawColoredShadow
import com.example.ui.theme.bgColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SearchRoute(
    onNavBack:()->Unit,
    onPlaceClick:(id:Int, color:Int) -> Unit = {_,_ ->},
    viewModel: SearchViewModel = hiltViewModel()
){
    SearchScreen(
        onNavBack = onNavBack,
        query = viewModel.query,
        onQueryUpdate = viewModel::onUpdateQuery,
        searchResult = viewModel.searchResult,
        onPlaceClick = onPlaceClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchScreen(
    onNavBack:()->Unit = {},
    query: State<String>,
    onQueryUpdate:(q:String)->Unit,
    onPlaceClick:(id:Int, color:Int) -> Unit = {_,_ ->},
    searchResult:StateFlow<List<PlaceModel>> = MutableStateFlow(emptyList())
){

    val searchResultState by searchResult.collectAsStateWithLifecycle()

    val navigationBarHeight = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    val listState = rememberLazyListState()

    val drawShadow by remember {
        derivedStateOf{ listState.firstVisibleItemScrollOffset > 0 }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        SearchToolbar(
            onBackClick = onNavBack,
            query = query,
            onQueryUpdate = onQueryUpdate,
            modifier = Modifier,
            drawShadow = drawShadow
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = navigationBarHeight),
            state = listState
        ){
            items(
                count = searchResultState.size,
                key = { index -> searchResultState[index].id }
            ){ index ->
                val dataItem = searchResultState[index]
                SearchItem(
                    item = dataItem,
                    modifier = Modifier
                        .animateItemPlacement(),
                    onClick = {
                        onPlaceClick(
                            it.id,
                            bgColors.random().toArgb()
                        )
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))

                Divider(
                    modifier = Modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchToolbar(
    modifier: Modifier = Modifier,
    onBackClick:()->Unit = {},
    query:State<String>,
    onQueryUpdate:(q:String)->Unit,
    drawShadow:Boolean = false
){

    val shadowDp by animateDpAsState(if (drawShadow) 6.dp else 0.dp)

    Surface(modifier = modifier
        .statusBarsPadding()
        .fillMaxWidth()
        .drawColoredShadow(
            offsetY = shadowDp,
            blurRadius = shadowDp
        )
        .height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier,
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                )
            }

            SearchField(
                query = query.value,
                onQueryChanged = onQueryUpdate,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(
    query:String,
    onQueryChanged:(String)->Unit,
    focusManager: FocusManager = LocalFocusManager.current,
    keyboard: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
){
    val interactionSource = remember { MutableInteractionSource() }

    val windowInfo = LocalWindowInfo.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = windowInfo){
        snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
            if (isWindowFocused) {
                focusRequester.requestFocus()
            }
        }
    }

    BasicTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
        textStyle = MaterialTheme.typography.bodyMedium,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            keyboard?.hide()
        })
    ) { innerTextField ->
        TextFieldDefaults.TextFieldDecorationBox(
            value = query,
            innerTextField = innerTextField,
            singleLine = true,
            enabled = true,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
            visualTransformation = VisualTransformation.None,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_hint),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )
    }
}

@Preview
@Composable
private fun SearchScreenPreview(){
    val query = remember {
        mutableStateOf<String>("")
    }
    Surface {
        SearchScreen(
            query = query,
            onQueryUpdate = { query.value = it }
        )
    }
}