package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.UserRole
import com.example.ui.components.*
import com.example.ui.theme.F1Background
import com.example.viewmodel.PitWallViewModel

@Composable
fun PitWallScreen(
    viewModel: PitWallViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = F1Background,
        topBar = {
            HeaderBar(
                currentRole = uiState.currentRole,
                onRoleSelected = viewModel::setRole,
                grandPrixTitle = uiState.grandPrixTitle,
                currentLap = uiState.currentLap,
                totalLaps = uiState.totalLaps,
                raceCondition = uiState.raceCondition,
                trackTemp = uiState.trackTempCelsius,
                airTemp = uiState.airTempCelsius,
                rainPct = uiState.rainProbabilityPct
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            val isWide = maxWidth >= 800.dp

            if (isWide) {
                // Wide / Tablet / Landscape Full-Screen Left / Right Split Layout
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // LEFT COLUMN (Video Player, Commentator Controls, FIA Ticker)
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Video Section
                        VideoSection(
                            selectedCamera = uiState.selectedCamera,
                            onCameraSelected = viewModel::setCamera,
                            telemetry = uiState.telemetry,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 2. Commentator Controls (Visible in Commentator & Admin roles)
                        if (uiState.currentRole == UserRole.COMMENTATOR || uiState.currentRole == UserRole.ADMIN) {
                            CommentatorControls(
                                selectedAudio = uiState.selectedAudio,
                                onAudioSourceSelected = viewModel::setAudioSource,
                                audioSyncDelay = uiState.audioSyncDelaySeconds,
                                onAdjustAudioSync = viewModel::adjustAudioSync,
                                isMicActive = uiState.isMicActive,
                                onToggleMic = viewModel::toggleMic,
                                commentatorNote = uiState.commentatorNote,
                                onNoteChange = viewModel::updateCommentatorNote,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // 3. FIA Race Control Notices Ticker
                        FiaNoticeTicker(
                            notices = uiState.fiaNotices,
                            onAcknowledgeNotice = viewModel::acknowledgeNotice,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }

                    // RIGHT COLUMN (Podium, Live Timing, News Hub, Admin Diagnostics)
                    Column(
                        modifier = Modifier
                            .weight(1.0f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Pit Wall Podium
                        PodiumView(
                            topThreeDrivers = uiState.drivers,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 2. Live Timing Table
                        LiveTimingTable(
                            drivers = uiState.drivers,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 3. RedRace News Hub
                        NewsHub(
                            articles = uiState.newsArticles,
                            selectedFilter = uiState.newsFilter,
                            onFilterSelected = viewModel::setNewsFilter,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 4. Admin Diagnostics Panel (Visible in Admin role)
                        AnimatedVisibility(
                            visible = uiState.currentRole == UserRole.ADMIN,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            AdminDiagnosticsPanel(
                                services = uiState.systemServices,
                                onToggleService = viewModel::toggleServiceStatus,
                                onTriggerSafetyCar = viewModel::triggerSafetyCar,
                                onTriggerYellowFlag = viewModel::triggerYellowFlagSector2,
                                onTriggerGreenFlag = viewModel::triggerGreenFlag,
                                isSimulationRunning = uiState.isSimulationRunning,
                                onToggleSimulation = viewModel::toggleSimulation,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            } else {
                // Compact / Phone Portrait Adaptive Layout
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Video Section
                    VideoSection(
                        selectedCamera = uiState.selectedCamera,
                        onCameraSelected = viewModel::setCamera,
                        telemetry = uiState.telemetry,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Commentator Controls
                    if (uiState.currentRole == UserRole.COMMENTATOR || uiState.currentRole == UserRole.ADMIN) {
                        CommentatorControls(
                            selectedAudio = uiState.selectedAudio,
                            onAudioSourceSelected = viewModel::setAudioSource,
                            audioSyncDelay = uiState.audioSyncDelaySeconds,
                            onAdjustAudioSync = viewModel::adjustAudioSync,
                            isMicActive = uiState.isMicActive,
                            onToggleMic = viewModel::toggleMic,
                            commentatorNote = uiState.commentatorNote,
                            onNoteChange = viewModel::updateCommentatorNote,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // FIA Notices Ticker
                    FiaNoticeTicker(
                        notices = uiState.fiaNotices,
                        onAcknowledgeNotice = viewModel::acknowledgeNotice,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Pit Wall Podium
                    PodiumView(
                        topThreeDrivers = uiState.drivers,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Live Timing Table
                    LiveTimingTable(
                        drivers = uiState.drivers,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // News Hub
                    NewsHub(
                        articles = uiState.newsArticles,
                        selectedFilter = uiState.newsFilter,
                        onFilterSelected = viewModel::setNewsFilter,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Admin Panel
                    if (uiState.currentRole == UserRole.ADMIN) {
                        AdminDiagnosticsPanel(
                            services = uiState.systemServices,
                            onToggleService = viewModel::toggleServiceStatus,
                            onTriggerSafetyCar = viewModel::triggerSafetyCar,
                            onTriggerYellowFlag = viewModel::triggerYellowFlagSector2,
                            onTriggerGreenFlag = viewModel::triggerGreenFlag,
                            isSimulationRunning = uiState.isSimulationRunning,
                            onToggleSimulation = viewModel::toggleSimulation,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
