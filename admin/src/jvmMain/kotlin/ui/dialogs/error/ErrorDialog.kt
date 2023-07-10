package ui.dialogs.error

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.rememberDialogState

@Composable
fun ErrorDialog(
    msg:String,
    onDismiss:()->Unit = {},
    showDialog:Boolean = true,
    dialogState: DialogState = rememberDialogState(height = 200.dp, width = 350.dp),
    title:String = ""
){
    Dialog(
        onCloseRequest = { onDismiss() },
        state = dialogState,
        visible = showDialog,
        title = title
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = msg
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    onDismiss()
                }) {
                Text("Ok".uppercase())
            }

        }
    }
}