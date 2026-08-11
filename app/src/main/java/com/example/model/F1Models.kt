package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class UserRole {
    SPECTATOR,
    COMMENTATOR,
    ADMIN
}

enum class AudioSource(val displayName: String, val frequency: String) {
    P4_NINE_LIVE("P4/9 LIVE", "104.8 MHz"),
    RED_RACE_FX("RedRace FX", "Multi-Ch Dolby"),
    PIT_RADIO("Pit Radio Direct", "EHF Ch 12"),
    AMBIENT_TRACK("Ambient Track FX", "Binaural 3D")
}

enum class CameraSource(val displayName: String, val badge: String) {
    MAIN_FEED("MAIN BROADCAST", "1080p60"),
    ONBOARD_HAM("ONBOARD #44 HAM", "4K HDR"),
    ONBOARD_VER("ONBOARD #1 VER", "4K HDR"),
    PIT_LANE("PIT LANE CAM 1", "1080p60"),
    DATA_MATRIX("TELEMETRY HUD", "RAW DATA")
}

enum class SectorStatus {
    NONE,
    PURPLE,  // Overall fastest
    GREEN,   // Personal best
    YELLOW   // Slow / Normal sector
}

enum class TyreCompound(val code: String, val color: Color, val label: String) {
    SOFT("S", Color(0xFFFF1801), "Soft C5"),
    MEDIUM("M", Color(0xFFFFEA00), "Medium C3"),
    HARD("H", Color(0xFFFFFFFF), "Hard C1"),
    INTERMEDIATE("I", Color(0xFF00E676), "Inter")
}

data class DriverTiming(
    val pos: Int,
    val carNumber: String,
    val driverCode: String,
    val driverName: String,
    val teamName: String,
    val teamColor: Color,
    val gap: String,
    val interval: String,
    val tyre: TyreCompound,
    val tyreAgeLaps: Int,
    val s1: String,
    val s1Status: SectorStatus,
    val s2: String,
    val s2Status: SectorStatus,
    val s3: String,
    val s3Status: SectorStatus,
    val bestLap: String,
    val pitStops: Int,
    val speedKmh: Int,
    val drsActive: Boolean = false,
    val inPit: Boolean = false
)

enum class FiaCategory {
    DRS,
    INVESTIGATION,
    PENALTY,
    SAFETY_CAR,
    FLAG,
    INFO
}

data class FiaNotice(
    val id: String,
    val timestamp: String,
    val category: FiaCategory,
    val message: String,
    val driverInvolved: String? = null,
    val acknowledged: Boolean = false
)

enum class NewsCategory {
    ALL,
    URGENT,
    ANALYSIS,
    TECHNICAL
}

data class NewsArticle(
    val id: String,
    val title: String,
    val source: String = "RedRace News",
    val category: NewsCategory,
    val timeAgo: String,
    val summary: String,
    val author: String = "F1 Pit Reporter",
    val read: Boolean = false
)

data class ServiceBadge(
    val name: String,
    val status: Boolean, // true = Online, false = Offline
    val latencyMs: Int,
    val endpoint: String
)

data class TelemetryGauge(
    val speedKmh: Int = 312,
    val rpm: Int = 11800,
    val maxRpm: Int = 13500,
    val gear: Int = 7,
    val throttlePct: Float = 0.94f,
    val brakePct: Float = 0.00f,
    val drsOpen: Boolean = true,
    val gForceX: Float = 0.2f,
    val gForceY: Float = 1.4f,
    val lapProgress: Float = 0.68f
)
