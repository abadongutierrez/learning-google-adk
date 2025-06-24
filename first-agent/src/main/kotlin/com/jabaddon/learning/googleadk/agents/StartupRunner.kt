package com.jabaddon.learning.googleadk.agents

import com.anthropic.client.AnthropicClient
import com.anthropic.models.messages.Model
import com.google.adk.agents.BaseAgent
import com.google.adk.agents.LlmAgent
import com.google.adk.events.Event
import com.google.adk.models.Claude
import com.google.adk.runner.InMemoryRunner
import com.google.adk.sessions.Session
import com.google.genai.types.Content
import com.google.genai.types.Part
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Consumer
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*


@Component
class StartupRunner(val googleAkdAgent: BaseAgent) : CommandLineRunner {

    override fun run(vararg args: String?) {
        println("=============================================")
        println("Application has started! Running custom code.")
        println("Current time: ${java.time.LocalDateTime.now()}")
        println("=============================================")

        val runner = InMemoryRunner(googleAkdAgent)

        val session: Session =
            runner
                .sessionService()
                .createSession(AgentConstants.NAME, AgentConstants.USER_ID)
                .blockingGet()

        Scanner(System.`in`, StandardCharsets.UTF_8).use { scanner ->
            while (true) {
                print("\nYou > ")
                val userInput: String? = scanner.nextLine()

                if ("quit".equals(userInput, ignoreCase = true) || "exit".equals(userInput, ignoreCase = true)) {
                    break
                }

                val userMsg: Content? = Content.fromParts(Part.fromText(userInput))
                val events: Flowable<Event> = runner.runAsync(AgentConstants.USER_ID, session.id(), userMsg)

                print("\nAgent > ")
                events.blockingForEach(Consumer { event: Event? -> System.out.println(event?.stringifyContent()) })
            }
        }

    }
}
