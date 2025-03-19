package com.dooribun.vreal.handler

import org.springframework.web.socket.*

class SignalingHandler : WebSocketHandler {

    private val sessions = mutableSetOf<WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
        println("새로운 WebSocket 연결: ${session.id}")
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        val messagePayload = message.payload.toString()
        when {
            messagePayload.startsWith("offer") -> {
                // offer 메시지를 다른 클라이언트로 전달
                forwardMessageToOtherClients(session, "offer", messagePayload)
            }
            messagePayload.startsWith("answer") -> {
                // answer 메시지를 다른 클라이언트로 전달
                forwardMessageToOtherClients(session, "answer", messagePayload)
            }
            messagePayload.startsWith("candidate") -> {
                // ICE 후보를 다른 클라이언트로 전달
                forwardMessageToOtherClients(session, "candidate", messagePayload)
            }
            else -> {
                println("Unknown message type: $messagePayload")
            }
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        println("WebSocket 오류 발생: ${exception.message}")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
        println("WebSocket 연결 종료: ${session.id}")
    }
    private fun forwardMessageToOtherClients(session: WebSocketSession, type: String, message: String) {
        for (clientSession in sessions) {
            if (clientSession != session) {
                println("$type:$message");
                clientSession.sendMessage(TextMessage("$type:$message"))
            }
        }
    }

    override fun supportsPartialMessages(): Boolean = false
}