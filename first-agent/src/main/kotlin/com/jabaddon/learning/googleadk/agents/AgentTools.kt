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


        @JvmStatic // this is important to make the tool discoverable by the agent as static method
        fun getCapitalCity(
            @Annotations.Schema(description = "The name of the country for which to retrieve the capital city")
            country: String?
        ): Map<String, String> {
            return if (country.isNullOrBlank()) {
                mapOf(
                    "status" to "error",
                    "report" to "Country name cannot be empty."
                )
            } else {
                // For simplicity, let's assume we have a predefined map of countries and their capitals
                val capitals = mapOf(
                    "france".lowercase() to "Paris",
                    "germany".lowercase() to "Berlin",
                    "japan".lowercase() to "Tokyo",
                    "united states".lowercase() to "Washington, D.C.",
                    "canada".lowercase() to "Ottawa",
                    "india".lowercase() to "New Delhi",
                    "brazil".lowercase() to "Brasília",
                    "australia".lowercase() to "Canberra",
                    "south africa".lowercase() to "Pretoria",
                    "china".lowercase() to "Beijing",
                    "mexico".lowercase() to "Mexico City",
                )

                capitals[country.lowercase()]?.let { capital ->
                    mapOf(
                        "status" to "success",
                        "report" to "The capital city of $country is $capital."
                    )
                } ?: mapOf(
                    "status" to "error",
                    "report" to "Sorry, I don't have information about the capital city of $country."
                )
            }
        }
    }
}
