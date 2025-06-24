package com.jabaddon.learning.googleadk.agents

import com.google.adk.agents.BaseAgent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

    @Bean
    fun googleAdkAgent(): BaseAgent {
        return AgentBuilder.buildAgent(AgentConstants.NAME)
    }
}