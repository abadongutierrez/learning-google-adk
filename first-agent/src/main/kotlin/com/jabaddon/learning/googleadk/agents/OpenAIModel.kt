package com.jabaddon.learning.googleadk.agents

import com.google.adk.models.BaseLlm
import com.google.adk.models.BaseLlmConnection
import com.google.adk.models.LlmRequest
import com.google.adk.models.LlmResponse
import io.reactivex.rxjava3.core.Flowable
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.stereotype.Component
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.subjects.PublishSubject
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.Generation
import java.util.concurrent.CompletableFuture

@Component
class OpenAIModel(chatClientBuilder: ChatClient.Builder) : BaseLlm("openai") {

    private val chatClient: ChatClient = chatClientBuilder.build()

    override fun generateContent(
        llmRequest: LlmRequest?,
        stream: Boolean
    ): Flowable<LlmResponse>? {
        if (llmRequest == null) {
            return null
        }
        println("Generating content with OpenAI model...")

        val subject = PublishSubject.create<LlmResponse>()

//        CompletableFuture.runAsync {
//            try {
//                val messages = mutableListOf<org.springframework.ai.chat.messages.Message>()
//
//                // Add system message if present
//                llmRequest.contents().stream().map { adkContent ->
//                    println(adkContent.role())
//                }
//                    messages.add(SystemMessage(it))
//                }
//
//                // Add user message
//                messages.add(UserMessage(llmRequest.prompt))
//
//                val response: ChatResponse = if (stream) {
//                    // For streaming, we use the streaming API
//                    val streamingResponse = chatClient.stream(messages)
//                    streamingResponse.forEach { partialResponse ->
//                        val content = partialResponse.content
//                        val llmResponse = LlmResponse(content, false)
//                        subject.onNext(llmResponse)
//                    }
//
//                    // Return the last response
//                    chatClient.prompt().messages(messages).c
//                } else {
//                    // For non-streaming, use regular call
//                    chatClient.call(messages)
//                }
//
//                // If not streaming or after streaming completed, send the final response
//                if (!stream) {
//                    val content = response.result.content
//                    val llmResponse = LlmResponse(content, true)
//                    subject.onNext(llmResponse)
//                } else {
//                    // Signal completion for streaming
//                    subject.onNext(LlmResponse("", true))
//                true}
//
//                subject.onComplete()
//            } catch (e: Exception) {
//                subject.onError(e)
//            }
//        }

        return subject.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun connect(llmRequest: LlmRequest?): BaseLlmConnection? {
        println("Connecting to OpenAI model...")
        return null
    }
}

