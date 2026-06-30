package com.dev.usbdigitalcommunityplatform.ui.home.member


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ── Data class ────────────────────────────────────────────────

data class JobPost(
    val jobId: String = "",
    val employerId: String = "",
    val employerName: String = "",
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val salary: String = "",
    val vacancies: Int = 0,
    val postedAt: Timestamp? = null
)

// ── ViewModel ─────────────────────────────────────────────────

class JobListViewModel : ViewModel() {

    private val db   = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var jobs by mutableStateOf<List<JobPost>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMsg by mutableStateOf<String?>(null)
        private set

    // key = jobId, value = "applying" | "applied" | "error"
    var applyStatus by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    fun fetchJobs() {
        viewModelScope.launch {
            isLoading = true
            errorMsg  = null
            try {
                val snapshot = db.collection("jobs")
                    .orderBy("postedAt", Query.Direction.DESCENDING)
                    .get()
                    .await()

                jobs = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(JobPost::class.java)?.copy(jobId = doc.id)
                }

                // pehle se applied jobs check karo
                checkExistingApplications()
            } catch (e: Exception) {
                errorMsg = "Jobs load nahi hui. Dobara try karo."
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun checkExistingApplications() {
        val myUid = auth.currentUser?.uid ?: return
        try {
            val snapshot = db.collection("applications")
                .whereEqualTo("applicantId", myUid)
                .get()
                .await()

            val appliedMap = snapshot.documents.mapNotNull { doc ->
                doc.getString("jobId")
            }.associateWith { "applied" }

            applyStatus = applyStatus + appliedMap
        } catch (e: Exception) {
            // silent fail — important nahi hai blocking ke liye
        }
    }

    // ek tap mein apply — koi message nahi
    fun applyToJob(job: JobPost) {
        val myUid = auth.currentUser?.uid ?: return

        if (applyStatus[job.jobId] == "applied") return

        viewModelScope.launch {
            applyStatus = applyStatus + (job.jobId to "applying")
            try {
                val application = hashMapOf(
                    "jobId"        to job.jobId,
                    "applicantId"  to myUid,
                    "employerId"   to job.employerId,
                    "status"       to "applied",
                    "appliedAt"    to Timestamp.now()
                )
                db.collection("applications").add(application).await()
                applyStatus = applyStatus + (job.jobId to "applied")
            } catch (e: Exception) {
                applyStatus = applyStatus + (job.jobId to "error")
            }
        }
    }
}

// ── Screen ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
    onBack: () -> Unit = {},
    viewModel: JobListViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchJobs()
    }

    JobListContent(
        onBack = onBack,
        jobs = viewModel.jobs,
        isLoading = viewModel.isLoading,
        errorMsg = viewModel.errorMsg,
        applyStatus = viewModel.applyStatus,
        onFetchJobs = { viewModel.fetchJobs() },
        onApplyToJob = { viewModel.applyToJob(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListContent(
    onBack: () -> Unit,
    jobs: List<JobPost>,
    isLoading: Boolean,
    errorMsg: String?,
    applyStatus: Map<String, String>,
    onFetchJobs: () -> Unit,
    onApplyToJob: (JobPost) -> Unit
) {
    val jobColor = Color(0xFF007AFF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Jobs",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF000000)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF000000)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = jobColor)
                        Spacer(Modifier.height(12.dp))
                        Text("Loading jobs...", fontSize = 14.sp, color = Color(0xFF8E8E93))
                    }
                }

                errorMsg != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("⚠️", fontSize = 40.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = errorMsg,
                            fontSize = 14.sp,
                            color = Color(0xFF8E8E93),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onFetchJobs,
                            colors  = ButtonDefaults.buttonColors(containerColor = jobColor)
                        ) {
                            Text("Dobara Try Karo")
                        }
                    }
                }

                jobs.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("💼", fontSize = 40.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Abhi koi job available nahi hai",
                            fontSize = 14.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = jobs,
                            key   = { it.jobId }
                        ) { job ->
                            JobCard(
                                job          = job,
                                jobColor     = jobColor,
                                applyStatus  = applyStatus[job.jobId],
                                onApply      = { onApplyToJob(job) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Flip Card ─────────────────────────────────────────────────

@Composable
fun JobCard(
    job: JobPost,
    jobColor: Color,
    applyStatus: String?,
    onApply: () -> Unit
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue   = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label         = "jobCardFlip"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .graphicsLayer { rotationY = rotation; cameraDistance = 12f * density }
            .clickable { isFlipped = !isFlipped }
    ) {
        if (rotation <= 90f) {
            JobCardFront(job = job, jobColor = jobColor)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
            ) {
                JobCardBack(
                    job         = job,
                    jobColor    = jobColor,
                    applyStatus = applyStatus,
                    onApply     = onApply   // flip nahi karega — apply ke baad bhi back pe rahega, status dikhega
                )
            }
        }
    }
}

// ── Job Card Front ───────────────────────────────────────────

@Composable
fun JobCardFront(job: JobPost, jobColor: Color) {
    Card(
        modifier  = Modifier.fillMaxSize(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(jobColor.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💼", fontSize = 20.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = job.title,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF000000)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text     = job.employerName,
                        fontSize = 12.sp,
                        color    = Color(0xFF6C6C70)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text     = job.salary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color    = jobColor
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("📍 ${job.location}", fontSize = 11.sp, color = Color(0xFF8E8E93))
                    Text("👥 ${job.vacancies} vacancies", fontSize = 11.sp, color = Color(0xFF8E8E93))
                }
                Text("Tap to flip →", fontSize = 11.sp, color = Color(0xFF8E8E93))
            }
        }
    }
}

// ── Job Card Back ────────────────────────────────────────────

@Composable
fun JobCardBack(
    job: JobPost,
    jobColor: Color,
    applyStatus: String?,
    onApply: () -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxSize(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = jobColor.copy(alpha = 0.06f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text       = job.title,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = jobColor
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text     = job.description.ifBlank { "Koi description nahi." },
                    fontSize = 12.sp,
                    color    = Color(0xFF6C6C70),
                    maxLines = 3
                )
            }

            // ── Apply Button — koi message nahi, seedha ek tap ──
            val btnLabel = when (applyStatus) {
                "applying" -> "Apply ho raha hai..."
                "applied"  -> "Applied ✓"
                "error"    -> "Dobara Try Karo"
                else       -> "Apply Karo"
            }
            val btnEnabled = applyStatus == null || applyStatus == "error"
            val btnColor = when (applyStatus) {
                "applied" -> Color(0xFF1D9E75)
                "error"   -> Color(0xFFE24B4A)
                else      -> jobColor
            }

            Button(
                onClick  = { if (btnEnabled) onApply() },
                enabled  = btnEnabled,
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = btnColor,
                    disabledContainerColor = btnColor.copy(alpha = 0.6f)
                )
            ) {
                if (applyStatus == "applying") {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(16.dp),
                        color       = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text       = btnLabel,
                    color      = Color.White,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
fun JobListScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        JobListContent(
            onBack = {},
            jobs = listOf(
                JobPost(
                    jobId = "1",
                    title = "Android Developer",
                    employerName = "USB Tech",
                    salary = "₹50,000 - ₹80,000",
                    location = "Mumbai",
                    vacancies = 2,
                    description = "We are looking for a senior Android developer."
                ),
                JobPost(
                    jobId = "2",
                    title = "Backend Engineer",
                    employerName = "Community Hub",
                    salary = "₹60,000 - ₹90,000",
                    location = "Remote",
                    vacancies = 1,
                    description = "Join our backend team."
                )
            ),
            isLoading = false,
            errorMsg = null,
            applyStatus = emptyMap(),
            onFetchJobs = {},
            onApplyToJob = {}
        )
    }
}