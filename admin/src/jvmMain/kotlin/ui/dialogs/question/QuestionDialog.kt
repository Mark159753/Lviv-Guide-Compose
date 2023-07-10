package ui.dialogs.question

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.rememberDialogState


@Composable
fun QuestionDialog(
    msg:String,
    onDismiss:()->Unit = {},
    onConfirm:()->Unit = {},
    showDialog:Boolean = true,
    dialogState: DialogState = rememberDialogState(height = 160.dp, width = 350.dp),
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
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = msg
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onConfirm()
                    }) {
                    Text("Yes".uppercase())
                }
                Spacer(
                    modifier = Modifier
                        .width(6.dp)
                )
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDismiss()
                    }) {
                    Text("Cancel".uppercase())
                }
            }

        }
    }
}