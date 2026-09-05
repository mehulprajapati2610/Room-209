package com.room209.app.data.websocket

import android.util.Log
import com.room209.app.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.*
import java.util.concurrent.atomic.AtomicInteger

data class StompMessage(
    val destination: String,
    val body: String
)

class RoomStompClient(
    private val client: OkHttpClient = OkHttpClient()
) {
    private val tag = "RoomStompClient"
    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private val subscriptionCounter = AtomicInteger(1)

    private val _messages = MutableSharedFlow<StompMessage>(extraBufferCapacity = 64)
    val messages: SharedFlow<StompMessage> = _messages

    private val subscriptions = mutableMapOf<String, String>() // subId -> destination

    fun connect(wsUrl: String = BuildConfig.WS_BASE_URL) {
        val request = Request.Builder().url(wsUrl).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(tag, "WebSocket opened, sending STOMP CONNECT...")
                val connectFrame = "CONNECT\naccept-version:1.1,1.2\nheart-beat:10000,10000\n\n\u0000"
                webSocket.send(connectFrame)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleFrame(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(tag, "WebSocket closed: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(tag, "WebSocket failure: ${t.message}", t)
            }
        })
    }

    fun subscribe(destination: String) {
        val subId = "sub-" + subscriptionCounter.getAndIncrement()
        subscriptions[subId] = destination
        val frame = "SUBSCRIBE\nid:$subId\ndestination:$destination\n\n\u0000"
        webSocket?.send(frame)
        Log.d(tag, "Sent STOMP SUBSCRIBE for $destination (id: $subId)")
    }

    fun send(destination: String, payload: String) {
        val frame = "SEND\ndestination:$destination\ncontent-type:application/json\n\n$payload\u0000"
        webSocket?.send(frame)
    }

    private fun handleFrame(raw: String) {
        if (raw.startsWith("CONNECTED")) {
            Log.d(tag, "STOMP Connected successfully!")
            // Re-subscribe to any registered destinations
            subscriptions.forEach { (subId, dest) ->
                val frame = "SUBSCRIBE\nid:$subId\ndestination:$dest\n\n\u0000"
                webSocket?.send(frame)
            }
            return
        }

        if (raw.startsWith("MESSAGE")) {
            val lines = raw.split("\n")
            var destination = ""
            var bodyIndex = -1

            for (i in lines.indices) {
                val line = lines[i]
                if (line.startsWith("destination:")) {
                    destination = line.substring("destination:".length).trim()
                }
                if (line.isEmpty()) {
                    bodyIndex = i + 1
                    break
                }
            }

            if (bodyIndex != -1 && bodyIndex < lines.size) {
                val body = lines.subList(bodyIndex, lines.size).joinToString("\n").trimEnd('\u0000')
                scope.launch {
                    _messages.emit(StompMessage(destination, body))
                }
            }
        }
    }

    fun disconnect() {
        try {
            webSocket?.send("DISCONNECT\n\n\u0000")
            webSocket?.close(1000, "Normal closure")
        } catch (ignored: Exception) {}
    }
}
