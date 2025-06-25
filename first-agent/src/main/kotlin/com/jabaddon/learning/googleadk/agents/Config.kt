package com.jabaddon.learning.googleadk.agents

import com.google.adk.agents.BaseAgent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

    @Bean("currentTimeAtCityAgent")
    fun currentTimeAtCityAgent(): BaseAgent {
        return AgentBuilder.buildCurrentTimeAtCityAgent("currentTimeAtCityAgent")
    }

    @Bean("capitalCityAgent")
    fun capitalCityAgent(): BaseAgent {
        return AgentBuilder.buildCapitalCityAgent("capitalCityAgent")
    }

    @Bean("agentManager")
    fun agentManager(
        @Qualifier("currentTimeAtCityAgent") currentTimeAtCityAgent: BaseAgent,
        @Qualifier("capitalCityAgent") capitalCityAgent: BaseAgent
    ): BaseAgent {
        return AgentBuilder.buildMultiToolAgent(
            "agentManager",
            listOf(
                currentTimeAtCityAgent(),
                capitalCityAgent()
            )
        )
    }
}