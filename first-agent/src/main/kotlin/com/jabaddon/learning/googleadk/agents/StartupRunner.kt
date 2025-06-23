package com.jabaddon.learning.googleadk.agents

import com.anthropic.client.AnthropicClient
import com.anthropic.models.messages.Model
import com.google.adk.agents.BaseAgent
import com.google.adk.agents.LlmAgent
import com.google.adk.events.Event
import com.google.adk.models.Claude
import com.google.adk.runner.InMemoryRunner
import com.google.genai.types.Content
import com.google.genai.types.Part
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Consumer
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*


@Component
class StartupRunner(val openAIModel: OpenAIModel) : CommandLineRunner {

    override fun run(vararg args: String?) {
        println("=============================================")
        println("Application has started! Running custom code.")
        println("Current time: ${java.time.LocalDateTime.now()}")
        println("=============================================")

        // Add your custom logic here that should execute after Spring Boot starts
        val runner = InMemoryRunner(buildBaseAgent())

        val sessionService = runner.sessionService()
        val session = sessionService
            .createSession("multi_tool_agent", "student")
            .blockingGet()
        println("Session created with ID: ${session.id()}")

        Scanner(System.`in`, StandardCharsets.UTF_8).use { scanner ->
            // Use the scanner resource here
            // When the block exits, the scanner will be closed automatically
            while (true) {
                print("\nYou > ")
                val userInput = scanner.nextLine()

                if ("quit".equals(userInput, ignoreCase = true)) {
                    break
                }

                val userMsg: Content = Content.fromParts(Part.fromText(userInput))
                val events: Flowable<Event> = runner.runAsync("student", session.id(), userMsg)

                print("\nAgent > ")
                events.blockingForEach(Consumer { event: Event? -> println(event?.stringifyContent()) })
            }
        }

    }



    private fun buildBaseAgent(): BaseAgent =
        LlmAgent.builder()
            .name("HelloWorldAgent")
            .model("gemini-2.0-flash")
            .description("An agent that prints 'Hello, World!' and a greeting message.")
            .instruction(
                "You are a helpful agent who can answer user questions about the " +
                        "time and weather in a city.")
            .build()
}
