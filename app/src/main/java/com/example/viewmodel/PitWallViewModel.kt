package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class PitWallUiState(
    val currentRole: UserRole = UserRole.COMMENTATOR,
    val selectedCamera: CameraSource = CameraSource.MAIN_FEED,
    val selectedAudio: AudioSource = AudioSource.P4_NINE_LIVE,
    val audioSyncDelaySeconds: Float = 0.20f, // ±0.5s controls
    val isMicActive: Boolean = true,
    val commentatorNote: String = "Watch NOR vs VER into Turn 10 DRS zone. Brake point 85m.",
    val drivers: List<DriverTiming> = initialDrivers(),
    val fiaNotices: List<FiaNotice> = initialFiaNotices(),
    val newsArticles: List<NewsArticle> = initialNewsArticles(),
    val newsFilter: NewsCategory = NewsCategory.ALL,
    val systemServices: List<ServiceBadge> = initialSystemServices(),
    val telemetry: TelemetryGauge = TelemetryGauge(),
    val grandPrixTitle: String = "SINGAPORE GP",
    val currentLap: Int = 44,
    val totalLaps: Int = 62,
    val raceCondition: String = "GREEN FLAG",
    val trackTempCelsius: Int = 38,
    val airTempCelsius: Int = 29,
    val rainProbabilityPct: Int = 12,
    val isSimulationRunning: Boolean = true
)

class PitWallViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PitWallUiState())
    val uiState: StateFlow<PitWallUiState> = _uiState.asStateFlow()

    init {
        startTelemetrySimulation()
    }

    private fun startTelemetrySimulation() {
        viewModelScope.launch {
            while (true) {
                delay(1200)
                if (_uiState.value.isSimulationRunning) {
                    tickTelemetryData()
                }
            }
        }
    }

    private fun tickTelemetryData() {
        _uiState.update { currentState ->
            // Update telemetry gauges with subtle dynamic fluctuations
            val currentSpd = (currentState.telemetry.speedKmh + Random.nextInt(-4, 5)).coerceIn(120, 345)
            val currentRpm = (currentState.telemetry.rpm + Random.nextInt(-200, 250)).coerceIn(8000, 13800)
            val newProgress = (currentState.telemetry.lapProgress + 0.02f).let { if (it > 1.0f) 0.0f else it }
            
            val updatedTelemetry = currentState.telemetry.copy(
                speedKmh = currentSpd,
                rpm = currentRpm,
                lapProgress = newProgress,
                gForceX = (Random.nextFloat() * 1.2f - 0.6f),
                gForceY = (1.2f + Random.nextFloat() * 0.8f)
            )

            // Randomly update sector time ticks for realism
            val updatedDrivers = currentState.drivers.mapIndexed { idx, driver ->
                val randomSectorTick = Random.nextInt(0, 10) == 0
                if (randomSectorTick && idx < 10) {
                    val s1Val = String.format("%.3f", 27.100 + Random.nextDouble(-0.3, 0.4))
                    val s2Val = String.format("%.3f", 38.450 + Random.nextDouble(-0.4, 0.5))
                    val s3Val = String.format("%.3f", 21.320 + Random.nextDouble(-0.2, 0.3))
                    
                    val secStatus = when (Random.nextInt(0, 4)) {
                        0 -> SectorStatus.PURPLE
                        1 -> SectorStatus.GREEN
                        else -> SectorStatus.YELLOW
                    }

                    driver.copy(
                        speedKmh = (300 + Random.nextInt(-20, 30)),
                        s1 = s1Val,
                        s1Status = if (Random.nextBoolean()) secStatus else driver.s1Status,
                        s2 = s2Val,
                        s2Status = if (Random.nextBoolean()) secStatus else driver.s2Status,
                        s3 = s3Val
                    )
                } else {
                    driver
                }
            }

            currentState.copy(
                telemetry = updatedTelemetry,
                drivers = updatedDrivers
            )
        }
    }

    fun setRole(role: UserRole) {
        _uiState.update { it.copy(currentRole = role) }
    }

    fun setCamera(camera: CameraSource) {
        _uiState.update { it.copy(selectedCamera = camera) }
    }

    fun setAudioSource(audio: AudioSource) {
        _uiState.update { it.copy(selectedAudio = audio) }
    }

    fun adjustAudioSync(delta: Float) {
        _uiState.update {
            val newSync = (it.audioSyncDelaySeconds + delta).coerceIn(-2.0f, 2.0f)
            it.copy(audioSyncDelaySeconds = (newSync * 100).toInt() / 100f)
        }
    }

    fun toggleMic() {
        _uiState.update { it.copy(isMicActive = !it.isMicActive) }
    }

    fun updateCommentatorNote(note: String) {
        _uiState.update { it.copy(commentatorNote = note) }
    }

    fun setNewsFilter(category: NewsCategory) {
        _uiState.update { it.copy(newsFilter = category) }
    }

    fun acknowledgeNotice(noticeId: String) {
        _uiState.update { state ->
            val updatedNotices = state.fiaNotices.map {
                if (it.id == noticeId) it.copy(acknowledged = true) else it
            }
            state.copy(fiaNotices = updatedNotices)
        }
    }

    fun toggleServiceStatus(serviceName: String) {
        _uiState.update { state ->
            val updatedServices = state.systemServices.map {
                if (it.name == serviceName) it.copy(status = !it.status) else it
            }
            state.copy(systemServices = updatedServices)
        }
    }

    fun triggerSafetyCar() {
        val newNotice = FiaNotice(
            id = "FIA-${System.currentTimeMillis() % 10000}",
            timestamp = "14:42:01",
            category = FiaCategory.SAFETY_CAR,
            message = "SAFETY CAR DEPLOYED - DEBRIS TURN 7 - NO OVERTAKING",
            driverInvolved = "ALL"
        )
        _uiState.update { state ->
            state.copy(
                raceCondition = "SAFETY CAR",
                fiaNotices = listOf(newNotice) + state.fiaNotices
            )
        }
    }

    fun triggerYellowFlagSector2() {
        val newNotice = FiaNotice(
            id = "FIA-${System.currentTimeMillis() % 10000}",
            timestamp = "14:41:15",
            category = FiaCategory.FLAG,
            message = "YELLOW FLAG SECTOR 2 - CAR 18 (STR) SPUN OFF AT TURN 14",
            driverInvolved = "18 STR"
        )
        _uiState.update { state ->
            state.copy(
                raceCondition = "YELLOW FLAG S2",
                fiaNotices = listOf(newNotice) + state.fiaNotices
            )
        }
    }

    fun triggerGreenFlag() {
        val newNotice = FiaNotice(
            id = "FIA-${System.currentTimeMillis() % 10000}",
            timestamp = "14:43:00",
            category = FiaCategory.FLAG,
            message = "GREEN FLAG - TRACK CLEAR - RACE RESUMED",
            driverInvolved = null
        )
        _uiState.update { state ->
            state.copy(
                raceCondition = "GREEN FLAG",
                fiaNotices = listOf(newNotice) + state.fiaNotices
            )
        }
    }

    fun toggleSimulation() {
        _uiState.update { it.copy(isSimulationRunning = !it.isSimulationRunning) }
    }
}

private fun initialDrivers(): List<DriverTiming> = listOf(
    DriverTiming(1, "1", "VER", "Max Verstappen", "Red Bull Racing", TeamRedBull, "LEADER", "--", TyreCompound.HARD, 18, "26.891", SectorStatus.PURPLE, "38.112", SectorStatus.PURPLE, "21.104", SectorStatus.PURPLE, "1:26.107", 1, 324),
    DriverTiming(2, "4", "NOR", "Lando Norris", "McLaren F1 Team", TeamMcLaren, "+1.482", "+1.482", TyreCompound.MEDIUM, 12, "27.014", SectorStatus.GREEN, "38.204", SectorStatus.GREEN, "21.210", SectorStatus.GREEN, "1:26.428", 1, 321, drsActive = true),
    DriverTiming(3, "16", "LEC", "Charles Leclerc", "Scuderia Ferrari", TeamFerrari, "+4.105", "+2.623", TyreCompound.MEDIUM, 14, "27.120", SectorStatus.GREEN, "38.310", SectorStatus.YELLOW, "21.280", SectorStatus.GREEN, "1:26.710", 1, 319),
    DriverTiming(4, "44", "HAM", "Lewis Hamilton", "Mercedes-AMG F1", TeamMercedes, "+8.920", "+4.815", TyreCompound.HARD, 22, "27.201", SectorStatus.YELLOW, "38.450", SectorStatus.GREEN, "21.340", SectorStatus.YELLOW, "1:26.991", 1, 318),
    DriverTiming(5, "81", "PIA", "Oscar Piastri", "McLaren F1 Team", TeamMcLaren, "+11.340", "+2.420", TyreCompound.SOFT, 6, "26.950", SectorStatus.PURPLE, "38.290", SectorStatus.YELLOW, "21.190", SectorStatus.PURPLE, "1:26.430", 2, 325),
    DriverTiming(6, "55", "SAI", "Carlos Sainz", "Scuderia Ferrari", TeamFerrari, "+15.810", "+4.470", TyreCompound.HARD, 20, "27.310", SectorStatus.YELLOW, "38.510", SectorStatus.YELLOW, "21.410", SectorStatus.YELLOW, "1:27.230", 1, 316),
    DriverTiming(7, "63", "RUS", "George Russell", "Mercedes-AMG F1", TeamMercedes, "+18.200", "+2.390", TyreCompound.MEDIUM, 15, "27.280", SectorStatus.GREEN, "38.480", SectorStatus.YELLOW, "21.380", SectorStatus.GREEN, "1:27.140", 1, 317),
    DriverTiming(8, "14", "ALO", "Fernando Alonso", "Aston Martin F1", TeamAstonMartin, "+24.610", "+6.410", TyreCompound.HARD, 26, "27.420", SectorStatus.YELLOW, "38.690", SectorStatus.YELLOW, "21.520", SectorStatus.YELLOW, "1:27.630", 1, 314),
    DriverTiming(9, "10", "GAS", "Pierre Gasly", "Alpine F1 Team", TeamAlpine, "+31.050", "+6.440", TyreCompound.MEDIUM, 11, "27.501", SectorStatus.YELLOW, "38.740", SectorStatus.YELLOW, "21.610", SectorStatus.YELLOW, "1:27.850", 1, 312),
    DriverTiming(10, "23", "ALB", "Alexander Albon", "Williams Racing", TeamWilliams, "+35.210", "+4.160", TyreCompound.SOFT, 8, "27.380", SectorStatus.GREEN, "38.610", SectorStatus.YELLOW, "21.480", SectorStatus.GREEN, "1:27.470", 2, 322)
)

private fun initialFiaNotices(): List<FiaNotice> = listOf(
    FiaNotice("FIA-104", "14:38:12", FiaCategory.INVESTIGATION, "CAR 44 (HAM) UNDER INVESTIGATION - ALLEGED TRACK LIMITS TURN 9", "44 HAM"),
    FiaNotice("FIA-103", "14:37:05", FiaCategory.DRS, "DRS ENABLED BY RACE CONTROL - LAP 3", null, true),
    FiaNotice("FIA-102", "14:35:40", FiaCategory.INFO, "TRACK TEMPERATURE 38.2°C - AMBIENT 29.1°C", null, true),
    FiaNotice("FIA-101", "14:35:00", FiaCategory.FLAG, "GREEN FLAG - RACE STARTED", null, true)
)

private fun initialNewsArticles(): List<NewsArticle> = listOf(
    NewsArticle("NEWS-1", "Red Bull Introduces High-Downforce Floor Upgrade for Singapore Street Circuit", "RedRace Tech", NewsCategory.TECHNICAL, "8m ago", "Verstappen reports significantly improved turn-in response following the midnight aerodynamic tweak."),
    NewsArticle("NEWS-2", "Norris Closes Gap to Verstappen as Pit Strategy Window Opens", "F1 Pit Wall", NewsCategory.URGENT, "14m ago", "McLaren telemetry confirms Norris running +0.3s per lap faster on the medium compound."),
    NewsArticle("NEWS-3", "Tyre Degradation Analysis: Hard Compound Holding Up Better Than Expected", "Pirelli Race Lab", NewsCategory.ANALYSIS, "22m ago", "Thermal degradation on rear left tyres remains under 0.08s/lap, enabling a potential 1-stop strategy.")
)

private fun initialSystemServices(): List<ServiceBadge> = listOf(
    ServiceBadge("Railway Endpoint", true, 24, "https://api.railway.app/v1/f1-control"),
    ServiceBadge("OpenF1 Telemetry API", true, 18, "https://api.openf1.org/v1/pitwall"),
    ServiceBadge("F1 News RSS Feed", true, 112, "https://f1news.live/feed.rss"),
    ServiceBadge("Stream Proxy Relay", true, 38, "https://stream-proxy.libertyformula.io/live")
)
