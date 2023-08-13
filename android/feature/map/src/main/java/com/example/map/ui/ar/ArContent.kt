package com.example.map.ui.ar

import android.opengl.Matrix
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.map.ui.ar.helpers.ArHelper
import com.example.map.ui.ar.helpers.ViewRenderData
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import io.github.sceneview.math.toNewQuaternion

@Composable
fun ArContent(
    modifier: Modifier = Modifier,
    viewsRendersData: StateFlow<List<ViewRenderData>> = MutableStateFlow(emptyList())
){
    val scope = rememberCoroutineScope()
    val arView = remember {
        mutableStateOf<ArSceneView?>(null)
    }

    val nodes = remember {
        mutableListOf<ArNode>()
    }

    DisposableEffect(key1 = Unit, effect = {
        onDispose {
            arView.value = null
        }
    })

    LaunchedEffect(key1 = arView.value, block = {
        val view = arView.value
        if (view != null){
            ArHelper.createNodes(
                viewsRendersData,
                arView.value!!
            ).collectLatest { nodesList ->
                nodes.forEach { arView.value!!.removeChild(it) }
                nodes.clear()
                nodesList.forEach { node ->
                    nodes.add(node)
                    arView.value!!.addChild(node)
                    node.isVisible = true
                }
            }
        }
    })

    Box(modifier = modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            planeRenderer = true,
            onCreate = { arSceneView ->
                // Apply your configuration
                arView.value = arSceneView
                arSceneView.geospatialEnabled = true

                arSceneView.onArSessionFailed = { e: Exception ->
                    Toast.makeText(arSceneView.context, e.message, Toast.LENGTH_LONG).show()
                }
            },
            onFrame = { arFrame ->
                // Retrieve ARCore frame update
                if (nodes.isEmpty()) return@ARScene
                val arCamera = arFrame.camera // Get the ARCamera from the ARFrame

                // Get the view matrix from the ARCamera (indicates the camera's position and orientation in the world)
                val viewMatrix = FloatArray(16)
                arCamera.getViewMatrix(viewMatrix, 0)

                // Calculate the rotation quaternion to make the view node face the camera
                val rotationMatrix = FloatArray(16)
                Matrix.invertM(rotationMatrix, 0, viewMatrix, 0)
                val direction = FloatArray(4)
                Matrix.multiplyMV(direction, 0, rotationMatrix, 0, floatArrayOf(0f, 0f, -1f, 0f), 0)
                val forwardInWorld = Vector3(direction[0], direction[1], direction[2])
                val desiredUpInWorld = Vector3(0f, 1f, 0f)

                val rotation = Quaternion.lookRotation(forwardInWorld, desiredUpInWorld)

                nodes.forEach { node ->
                    if (node.isVisible){
                        node.isVisible = true
                    }
                    node.rotation = rotation.toNewQuaternion().toEulerAngles()
                }
            },
            onTap = { hitResult ->
                scope.launch {
                    ArHelper.createNodes(
                        viewsRendersData,
                        arView.value!!
                    ).collectLatest { nodesList ->
                        nodes.forEach { arView.value!!.removeChild(it) }
                        nodes.clear()
                        nodesList.forEach { node ->
                            nodes.add(node)
                            arView.value!!.addChild(node)
                            node.isVisible = true
                        }
                    }
                }
                // User tapped in the AR view
            },
        )
    }
}