package com.dev.usbdigitalcommunityplatform.ui.home.common

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
import java.text.SimpleDateFormat
import java.util.*

// ── Data class ────────────────────────────────────────────────
// Ye screen REUSABLE hai — Employer, Lawyer, CA teeno isi se
// apni incoming requests dekhenge. Koi role-specific logic nahi,
// kyunki query sirf "toUserId == currentUser.uid" pe based hai.

data class WorkRequest(
    val requestId: String = "",
    val fromUserId: String = "",
    val fromUserName: String = "",   // joined from users collection
    val toUserId: String = "",
    val toRole: String = "",
    val message: String = "",
    val status: String = "pending",  // pending | accepted | rejected
    val createdAt: Timestamp? = null
)

// ── ViewModel ─────────────────────────────────────────────────

class RequestsInboxViewModel : ViewModel() {

    private val db   = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var requests by mutableStateOf<List<WorkRequest>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMsg by mutableStateOf<String?>(null)
        private set

    // requestId -> "processing" | "done"
    var actionStatus by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    fun fetchRequests() {
        val myUid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            isLoading = true
            errorMsg  = null
            try {
                val snapshot = db.collection("requests")
                    .whereEqualTo("toUserId", myUid)
                    .whereEqualTo("status", "pending")
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val list = snapshot.documents.mapNotNull { doc ->
                    val fromUid = doc.getString("fromUserId") ?: return@mapNotNull null

                    // sender ka naam fetch karo
                    val senderDoc = db.collection("users").document(fromUid).get().await()
                    val senderName = senderDoc.getString("name") ?: "Unknown User"

                    WorkRequest(
                        requestId    = doc.id,
                        fromUserId   = fromUid,
                        fromUserName = senderName,
                        toUserId     = doc.getString("toUserId") ?: "",
                        toRole       = doc.getString("toRole") ?: "",
                        message      = doc.getString("message") ?: "",
                        status       = doc.getString("status") ?: "pending",
                        createdAt    = doc.getTimestamp("createdAt")
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

    fun acceptRequest(request: WorkRequest) {
        viewModelScope.launch {
            actionStatus = actionStatus + (request.requestId to "processing")
            try {
                db.collection("requests").document(request.requestId)
                    .update("status", "accepted")
                    .await()

                val chatId = listOf(request.fromUserId, request.toUserId).sorted().joinToString("_")
                val chatData = hashMapOf(
                    "participants"  to listOf(request.fromUserId, request.toUserId),
                    "requestId"     to request.requestId,
                    "createdAt"     to Timestamp.now(),
                    "lastMessage"   to "",
                    "lastMessageAt" to Timestamp.now()
                )
                db.collection("chats").document(chatId).set(chatData).await()

                requests = requests.filter { it.requestId != request.requestId }
                actionStatus = actionStatus + (request.requestId to "done")
            } catch (e: Exception) {
                actionStatus = actionStatus + (request.requestId to "error")
            }
        }
    }

    fun rejectRequest(request: WorkRequest) {
        viewModelScope.launch {
            actionStatus = actionStatus + (request.requestId to "processing")
            try {
                db.collection("requests").document(request.requestId)
                    .update("status", "rejected")
                    .await()

                requests = requests.filter { it.requestId != request.requestId }
                actionStatus = actionStatus + (request.requestId to "done")
            } catch (e: Exception) {
                actionStatus = actionStatus + (request.requestId to "error")
            }
        }
    }
}

// ── Screen ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsInboxScreen(
    onBack: () -> Unit = {},
    onOpenChat: (chatId: String) -> Unit = {},
    viewModel: RequestsInboxViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchRequests()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Requests",
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
                        Text("Loading requests...", fontSize = 14.sp, color = Color(0xFF8E8E93))
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
                        Button(onClick = { viewModel.fetchRequests() }) {
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
                        Text("📨", fontSize = 40.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Koi pending request nahi hai",
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
                            RequestCard(
                                request      = request,
                                actionStatus = viewModel.actionStatus[request.requestId],
                                onAccept     = { viewModel.acceptRequest(request) },
                                onReject     = { viewModel.rejectRequest(request) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Request Card ──────────────────────────────────────────────

@Composable
fun RequestCard(
    request: WorkRequest,
    actionStatus: String?,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val isProcessing = actionStatus == "processing"

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
                        .background(Color(0xFF007AFF).copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = request.fromUserName
                        .split(" ")
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .take(2)
                        .joinToString("")
                        .ifEmpty { "?" }
                    Text(
                        text       = initials,
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF007AFF)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = request.fromUserName,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF000000)
                    )
                    Text(
                        text     = formatTimestamp(request.createdAt),
                        fontSize = 11.sp,
                        color    = Color(0xFF8E8E93)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            if (request.message.isNotBlank()) {
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
                Spacer(Modifier.height(12.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick  = onReject,
                    enabled  = !isProcessing,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE24B4A)
                    )
                ) {
                    Text("Reject", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick  = onAccept,
                    enabled  = !isProcessing,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1D9E75)
                    )
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(16.dp),
                            color       = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Accept", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ── Helper ────────────────────────────────────────────────────

fun formatTimestamp(timestamp: Timestamp?): String {
    if (timestamp == null) return ""
    val now  = System.currentTimeMillis()
    val time = timestamp.toDate().time
    val diffMinutes = (now - time) / (1000 * 60)

    return when {
        diffMinutes < 1    -> "Just now"
        diffMinutes < 60   -> "${diffMinutes}m ago"
        diffMinutes < 1440 -> "${diffMinutes / 60}h ago"
        else -> SimpleDateFormat("dd MMM", Locale.getDefault()).format(timestamp.toDate())
    }
}

// ── Preview ───────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
fun RequestsInboxScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        Column(modifier = Modifier.background(Color(0xFFF9F9F9))) {
            RequestCard(
                request = WorkRequest(
                    requestId    = "1",
                    fromUserName = "Sahil Maske",
                    message      = "Mujhe packaging ka kaam chahiye, kal se start kar sakta hoon",
                    status       = "pending"
                ),
                actionStatus = null,
                onAccept     = {},
                onReject     = {}
            )
        }
    }
}