package com.jabaddon.learning.googleadk.agents

import com.google.adk.events.Event
import com.google.adk.runner.InMemoryRunner
import com.google.adk.sessions.Session
import com.google.genai.types.Content
import com.google.genai.types.Part
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Consumer
import java.nio.charset.StandardCharsets
import java.util.Scanner


fun main() {
    val agent = Config().currentTimeAtCityAgent()
    val runner = InMemoryRunner(agent)

    val session: Session =
        runner
            .sessionService()
            .createSession(agent.name(), AgentConstants.USER_ID)
            .blockingGet()

    Scanner(System.`in`, StandardCharsets.UTF_8).use { scanner ->
        while (true) {
            print("\nYou > ")
            val userInput: String? = scanner.nextLine()

            if ("quit".equals(userInput, ignoreCase = true)) {
                break
            }

            val userMsg: Content? = Content.fromParts(Part.fromText(userInput))
            val events: Flowable<Event> = runner.runAsync(AgentConstants.USER_ID, session.id(), userMsg)

            print("\nAgent > ")
            events.blockingForEach(Consumer { event: Event? -> System.out.println(event?.stringifyContent()) })
        }
    }
}

