package com.example.map.ui.ar.helpers

import com.google.ar.core.Anchor
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.node.ViewNode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object ArHelper {

    fun createNodes(viewNodesData: StateFlow<List<ViewRenderData>>, arSceneView:ArSceneView): Flow<List<ArNode>> {
        return viewNodesData.map { list ->
            list.mapNotNull { item ->
                createAnchor(arSceneView, item.lat, item.lon)?.let { anchor ->
                    ArNode(
                        engine = arSceneView.engine,
                        anchor = anchor
                    ).also {
                        val viewNode = ViewNode(engine = arSceneView.engine).also {
                            it.setRenderable(item.viewRender)
                        }
                        it.addChild(viewNode)
                    }
                }
            }
        }
    }

    private suspend fun createAnchor(arSceneView:ArSceneView, lat:Double, lon:Double):Anchor?{
        val earth = arSceneView.arSession?.earth ?: return null
        return suspendCoroutine { continuation ->
            earth.resolveAnchorOnTerrainAsync(
                lat, lon, 2.5, 0f, 0f, 0f, 1f){ resolvedAnchor: Anchor?, state: Anchor.TerrainAnchorState? ->
                if (resolvedAnchor != null && state != null) {
                    continuation.resume(resolvedAnchor)
                } else {
                    val altitude = earth.cameraGeospatialPose.altitude
                    val anchor =  try {
                        earth.createAnchor(lat, lon, altitude, 0f, 0f, 0f, 1f)
                    }catch (e:IllegalStateException){
                        null
                    }
                    continuation.resume(anchor)
                }
            }
        }
    }
}