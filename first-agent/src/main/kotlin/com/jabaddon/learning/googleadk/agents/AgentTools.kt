package com.jabaddon.learning.googleadk.agents

import com.google.adk.tools.Annotations
import java.text.Normalizer
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AgentTools {
    companion object {
        @JvmStatic // this is important to make the tool discoverable by the agent as static method
        fun getCurrentTime(
            @Annotations.Schema(description = "The name of the city for which to retrieve the current time")
            city: String?
        ): Map<String, String> {
            return if (city.isNullOrBlank()) {
                mapOf(
                    "status" to "error",
                    "report" to "City name cannot be empty."
                )
            } else {
                val normalizedCity = Normalizer.normalize(city, Normalizer.Form.NFD)
                    .trim().lowercase()
                    .replace(Regex("(\\p{IsM}+|\\p{IsP}+)"), "")
                    .replace(Regex("\\s+"), "_")

                println("normalizedCity=$normalizedCity")

                return ZoneId.getAvailableZoneIds().stream()
                    .filter({ zid -> zid.lowercase().endsWith("/" + normalizedCity) })
                    .findFirst()
                    .map(
                        { zid ->
                            mapOf(
                                "status" to "success",
                                "report" to
                                        ("The current time in "
                                                + city
                                                + " is "
                                                + ZonedDateTime.now(ZoneId.of(zid))
                                            .format(DateTimeFormatter.ofPattern("HH:mm"))
                                                + ".")
                            )
                        })
                    .orElse(
                        mapOf(
                            "status" to "error",
                            "report" to "Sorry, I don't have timezone information for " + city + "."
                        )
                    )
            }
        }
    }
}
