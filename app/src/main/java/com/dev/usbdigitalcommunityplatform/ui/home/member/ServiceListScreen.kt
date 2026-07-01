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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ── Data class ────────────────────────────────────────────────

data class ServiceUser(
    val uid: String = "",
    val name: String = "",
    val role: String = "",
    val city: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val description: String = ""
)

// ── ViewModel ─────────────────────────────────────────────────

class ServiceViewModel : ViewModel() {

    private val db   = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var users by mutableStateOf<List<ServiceUser>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMsg by mutableStateOf<String?>(null)
        private set

    // key = toUserId, value = "sending" | "sent" | "error"
    var requestStatus by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    fun fetchUsers(role: String) {
        viewModelScope.launch {
            isLoading = true
            errorMsg  = null
            try {
                val snapshot = db.collection("users")
                    .whereEqualTo("role", role)
                    .get()
                    .await()

                users = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ServiceUser::class.java)?.copy(uid = doc.id)
                }
            } catch (e: Exception) {
                errorMsg = "Data load nahi hua. Dobara try karo."
            } finally {
                isLoading = false
            }
        }
    }

    // ab message bhi lega — SendRequestDialog se aayega
    fun sendRequest(toUser: ServiceUser, message: String) {
        val fromUid = auth.currentUser?.uid ?: return

        if (requestStatus[toUser.uid] == "sent") return

        viewModelScope.launch {
            requestStatus = requestStatus + (toUser.uid to "sending")
            try {
                val request = hashMapOf(
                    "fromUserId" to fromUid,
                    "toUserId"   to toUser.uid,
                    "toRole"     to toUser.role,
                    "status"     to "pending",
                    "message"    to message,
                    "createdAt"  to com.google.firebase.Timestamp.now()
                )
                db.collection("requests").add(request).await()
                requestStatus = requestStatus + (toUser.uid to "sent")
            } catch (e: Exception) {
                requestStatus = requestStatus + (toUser.uid to "error")
            }
        }
    }
}

// ── Screen ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListScreen(
    role: String,
    onBack: () -> Unit = {},
    viewModel: ServiceViewModel = viewModel()
) {
    val (screenTitle, roleColor) = when (role.lowercase()) {
        "employer" -> "Jobs — Employers"     to Color(0xFF007AFF)
        "lawyer"   -> "Legal Help — Lawyers" to Color(0xFF1D9E75)
        "ca"       -> "CA / Finance"         to Color(0xFFBA7517)
        "vendor"   -> "Vendors — Business"   to Color(0xFF8B5CF6)
        else       -> "Service Providers"    to Color(0xFF007AFF)
    }

    LaunchedEffect(role) {
        viewModel.fetchUsers(role)
    }

    ServiceListContent(
        screenTitle   = screenTitle,
        roleColor     = roleColor,
        isLoading     = viewModel.isLoading,
        errorMsg      = viewModel.errorMsg,
        users         = viewModel.users,
        requestStatus = viewModel.requestStatus,
        onBack        = onBack,
        onRefresh     = { viewModel.fetchUsers(role) },
        onSendRequest = { user, message -> viewModel.sendRequest(user, message) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListContent(
    screenTitle: String,
    roleColor: Color,
    isLoading: Boolean,
    errorMsg: String?,
    users: List<ServiceUser>,
    requestStatus: Map<String, String>,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onSendRequest: (ServiceUser, String) -> Unit
) {
    // ── Dialog state — kis user ko request bhejni hai abhi ────
    var selectedUser by remember { mutableStateOf<ServiceUser?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = screenTitle,
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
                        CircularProgressIndicator(color = roleColor)
                        Spacer(Modifier.height(12.dp))
                        Text("Loading...", fontSize = 14.sp, color = Color(0xFF8E8E93))
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
                            onClick = onRefresh,
                            colors = ButtonDefaults.buttonColors(containerColor = roleColor)
                        ) {
                            Text("Dobara Try Karo")
                        }
                    }
                }

                users.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", fontSize = 40.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Abhi koi $screenTitle available nahi hai",
                            fontSize = 14.sp,
                            color = Color(0xFF8E8E93),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 40.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = users,
                            key   = { it.uid }
                        ) { user ->
                            ServiceCard(
                                user          = user,
                                roleColor     = roleColor,
                                requestStatus = requestStatus[user.uid],
                                onSendRequest = { selectedUser = user }
                            )
                        }
                    }
                }
            }
        }
    }

    // ── Dialog dikhao jab user ne kisi card pe Request Bhejo dabaya ──
    selectedUser?.let { user ->
        SendRequestDialog(
            targetUser = user,
            roleColor  = roleColor,
            onDismiss  = { selectedUser = null },
            onConfirm  = { message ->
                onSendRequest(user, message)
                selectedUser = null
            }
        )
    }
}

// ── Flip Card ─────────────────────────────────────────────────

@Composable
fun ServiceCard(
    user: ServiceUser,
    roleColor: Color,
    requestStatus: String?,
    onSendRequest: () -> Unit
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue   = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label         = "cardFlip"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .graphicsLayer { rotationY = rotation; cameraDistance = 12f * density }
            .clickable { isFlipped = !isFlipped }
    ) {
        if (rotation <= 90f) {
            CardFront(user = user, roleColor = roleColor)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
            ) {
                CardBack(
                    user          = user,
                    roleColor     = roleColor,
                    requestStatus = requestStatus,
                    onSendRequest = {
                        onSendRequest()
                        isFlipped = false
                    }
                )
            }
        }
    }
}

// ── Card Front ────────────────────────────────────────────────

@Composable
fun CardFront(user: ServiceUser, roleColor: Color) {
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
                        .size(58.dp)
                        .background(roleColor.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = user.name
                        .split(" ")
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .take(2)
                        .joinToString("")
                        .ifEmpty { "?" }
                    Text(
                        text       = initials,
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = roleColor
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text       = user.name,
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = Color(0xFF000000)
                        )
                        RoleBadge(role = user.role, color = roleColor)
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text     = user.description.ifBlank { "No description" },
                        fontSize = 12.sp,
                        color    = Color(0xFF6C6C70),
                        maxLines = 2
                    )

                    Spacer(Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StarRating(rating = user.rating)
                        Text(
                            text     = "${user.rating} • ${user.reviewCount} reviews",
                            fontSize = 11.sp,
                            color    = Color(0xFF8E8E93)
                        )
                    }
                }
            }

            Spacer(Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text     = "📍 ${user.city}",
                    fontSize = 11.sp,
                    color    = Color(0xFF8E8E93)
                )
                Text(
                    text     = "Tap to flip →",
                    fontSize = 11.sp,
                    color    = Color(0xFF8E8E93)
                )
            }
        }
    }
}

// ── Card Back ─────────────────────────────────────────────────

@Composable
fun CardBack(
    user: ServiceUser,
    roleColor: Color,
    requestStatus: String?,
    onSendRequest: () -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxSize(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = roleColor.copy(alpha = 0.06f)),
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
                    text       = user.name,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = roleColor
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text     = user.description.ifBlank { "Koi description nahi." },
                    fontSize = 12.sp,
                    color    = Color(0xFF6C6C70),
                    maxLines = 3
                )
            }

            val btnLabel = when (requestStatus) {
                "sending" -> "Bhej raha hoon..."
                "sent"    -> "Request Bhej Di ✓"
                "error"   -> "Dobara Try Karo"
                else      -> "Request Bhejo"
            }
            val btnEnabled = requestStatus == null || requestStatus == "error"
            val btnColor = when (requestStatus) {
                "sent"  -> Color(0xFF1D9E75)
                "error" -> Color(0xFFE24B4A)
                else    -> roleColor
            }

            Button(
                onClick  = { if (btnEnabled) onSendRequest() },
                enabled  = btnEnabled,
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = btnColor,
                    disabledContainerColor = btnColor.copy(alpha = 0.6f)
                )
            ) {
                if (requestStatus == "sending") {
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

// ── Helper composables ────────────────────────────────────────

@Composable
fun RoleBadge(role: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text       = role.replaceFirstChar { it.uppercaseChar() },
            fontSize   = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color      = color
        )
    }
}

@Composable
fun StarRating(rating: Float) {
    val fullStars  = rating.toInt()
    val emptyStars = 5 - fullStars
    Row {
        repeat(fullStars)  { Text("★", fontSize = 12.sp, color = Color(0xFFEF9F27)) }
        repeat(emptyStars) { Text("☆", fontSize = 12.sp, color = Color(0xFFD1D5DB)) }
    }
}

@Composable
fun SendRequestDialog(
    targetUser: ServiceUser,
    roleColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Request Bhejein?", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Aap ${targetUser.name} ko request bhej rahe hain.", fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("Message likhein (optional)...", fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(message) },
                colors = ButtonDefaults.buttonColors(containerColor = roleColor)
            ) {
                Text("Bhejo")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

// ── Preview ───────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
fun ServiceListScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        // We call ServiceListContent directly to avoid instantiating ServiceViewModel
        // which fails in Preview due to Firebase initialization.
        ServiceListContent(
            screenTitle = "Jobs — Employers",
            roleColor = Color(0xFF007AFF),
            isLoading = false,
            errorMsg = null,
            users = listOf(
                ServiceUser(
                    uid = "1",
                    name = "Sahil Maske",
                    role = "employer",
                    city = "Mumbai",
                    rating = 4.5f,
                    reviewCount = 12,
                    description = "Looking for Android Developers."
                ),
                ServiceUser(
                    uid = "2",
                    name = "Rahul Kumar",
                    role = "employer",
                    city = "Pune",
                    rating = 4.0f,
                    reviewCount = 5,
                    description = "Need a CA for tax filing."
                )
            ),
            requestStatus = emptyMap(),
            onBack = {},
            onRefresh = {},
            onSendRequest = { _, _ -> }
        )
    }
}