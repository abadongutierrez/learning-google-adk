package com.jabaddon.learning.googleadk.agents

import com.google.adk.agents.BaseAgent
import com.google.adk.agents.LlmAgent
import com.google.adk.tools.FunctionTool

class AgentBuilder {
    companion object {
        fun buildAgent(name: String): BaseAgent {
            return LlmAgent.builder()
                .name(name)
                .model("gemini-2.0-flash")
                .description("Agent to answer questions about the time and weather in a city.")
                .instruction(
                    """
                    You are a helpful agent who can answer user questions about the time and weather in a city.
                    """
                )
                .tools(
                    FunctionTool.create(AgentTools::class.java, "getCurrentTime"),
                    // FunctionTool.create(MultiToolAgent::class.java, "getWeather")
                )
                .build()
        }
    }
}
