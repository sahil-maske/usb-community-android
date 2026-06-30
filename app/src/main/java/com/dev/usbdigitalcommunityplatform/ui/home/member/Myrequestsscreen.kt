package com.dev.usbdigitalcommunityplatform.ui.home.member




import androidx.compose.foundation.background
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

data class SentRequest(
    val requestId: String = "",
    val toUserId: String = "",
    val toUserName: String = "",   // joined from users collection
    val toRole: String = "",
    val message: String = "",
    val status: String = "pending",  // pending | accepted | rejected
    val createdAt: Timestamp? = null
)

// ── ViewModel ─────────────────────────────────────────────────

class MyRequestsViewModel : ViewModel() {

    private val db   = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var requests by mutableStateOf<List<SentRequest>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMsg by mutableStateOf<String?>(null)
        private set

    fun fetchMyRequests() {
        val myUid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            isLoading = true
            errorMsg  = null
            try {
                val snapshot = db.collection("requests")
                    .whereEqualTo("fromUserId", myUid)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val list = snapshot.documents.mapNotNull { doc ->
                    val toUid = doc.getString("toUserId") ?: return@mapNotNull null

                    // receiver ka naam fetch karo
                    val receiverDoc = db.collection("users").document(toUid).get().await()
                    val receiverName = receiverDoc.getString("name") ?: "Unknown User"

                    SentRequest(
                        requestId  = doc.id,
                        toUserId   = toUid,
                        toUserName = receiverName,
                        toRole     = doc.getString("toRole") ?: "",
                        message    = doc.getString("message") ?: "",
                        status     = doc.getString("status") ?: "pending",
                        createdAt  = doc.getTimestamp("createdAt")
                    )
                }
                requests = list
            } catch (e: Exception) {
                errorMsg = "Requests load nahi hui. Dobara try karo."
            } finally {
                isLoading = false
            }
        }
    }
}

// ── Screen ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRequestsScreen(
    onBack: () -> Unit = {},
    onOpenChat: (toUserId: String) -> Unit = {},
    viewModel: MyRequestsViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchMyRequests()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Meri Requests",
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
                viewModel.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFF007AFF))
                        Spacer(Modifier.height(12.dp))
                        Text("Loading...", fontSize = 14.sp, color = Color(0xFF8E8E93))
                    }
                }

                viewModel.errorMsg != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("⚠️", fontSize = 40.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = viewModel.errorMsg ?: "",
                            fontSize = 14.sp,
                            color = Color(0xFF8E8E93),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchMyRequests() }) {
                            Text("Dobara Try Karo")
                        }
                    }
                }

                viewModel.requests.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📭", fontSize = 40.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Tumne abhi tak koi request nahi bheji",
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
                            items = viewModel.requests,
                            key   = { it.requestId }
                        ) { request ->
                            SentRequestCard(
                                request   = request,
                                onOpenChat = { onOpenChat(request.toUserId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Sent Request Card ────────────────────────────────────────

@Composable
fun SentRequestCard(
    request: SentRequest,
    onOpenChat: () -> Unit
) {
    val (statusLabel, statusColor) = when (request.status) {
        "accepted" -> "Accepted ✓" to Color(0xFF1D9E75)
        "rejected" -> "Rejected"   to Color(0xFFE24B4A)
        else       -> "Pending"    to Color(0xFFBA7517)
    }

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(statusColor.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = request.toUserName
                        .split(" ")
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .take(2)
                        .joinToString("")
                        .ifEmpty { "?" }
                    Text(
                        text       = initials,
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color      = statusColor
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = request.toUserName,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF000000)
                    )
                    Text(
                        text     = request.toRole.replaceFirstChar { it.uppercaseChar() },
                        fontSize = 11.sp,
                        color    = Color(0xFF8E8E93)
                    )
                }

                // Status badge
                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = statusLabel,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = statusColor
                    )
                }
            }

            if (request.message.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text     = request.message,
                        fontSize = 13.sp,
                        color    = Color(0xFF3C3C43)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text     = formatTimestamp(request.createdAt),
                fontSize = 11.sp,
                color    = Color(0xFF8E8E93)
            )

            // Accept ho gaya toh Chat button dikhao
            if (request.status == "accepted") {
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick  = onOpenChat,
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D9E75))
                ) {
                    Text("Chat Kholo", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
fun MyRequestsScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        Column(modifier = Modifier.background(Color(0xFFF9F9F9))) {
            SentRequestCard(
                request = SentRequest(
                    requestId  = "1",
                    toUserName = "Ramesh Kumar",
                    toRole     = "employer",
                    message    = "Mujhe packaging ka kaam chahiye",
                    status     = "accepted"
                ),
                onOpenChat = {}
            )
        }
    }
}