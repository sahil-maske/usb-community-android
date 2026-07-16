package com.dev.usbdigitalcommunityplatform.ui.home.member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ── Extended data class — ServiceUser se zyada detail ────────

data class ServiceUserDetail(
    val uid: String = "",
    val name: String = "",
    val role: String = "",
    val city: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val description: String = "",

    // CA/Lawyer specific fields
    val experienceYears: Int = 0,
    val specializations: List<String> = emptyList(),   // ["Labour Law", "GST", "ITR"]
    val education: List<String> = emptyList(),          // ["LLB - Mumbai University"]
    val certifications: List<String> = emptyList(),     // ["Bar Council Registered"]
    val casesHandled: Int = 0,                          // Lawyer ke liye
    val clientsServed: Int = 0                          // CA ke liye
)

// ── ViewModel ─────────────────────────────────────────────────

class ServiceDetailViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var userDetail by mutableStateOf<ServiceUserDetail?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMsg by mutableStateOf<String?>(null)
        private set

    @Suppress("UNCHECKED_CAST")
    fun fetchUserDetail(uid: String) {
        viewModelScope.launch {
            isLoading = true
            errorMsg  = null
            try {
                val doc = db.collection("users").document(uid).get().await()

                userDetail = ServiceUserDetail(
                    uid              = doc.id,
                    name             = doc.getString("name") ?: "",
                    role             = doc.getString("role") ?: "",
                    city             = doc.getString("city") ?: "",
                    rating           = (doc.getDouble("rating") ?: 0.0).toFloat(),
                    reviewCount      = (doc.getLong("reviewCount") ?: 0L).toInt(),
                    description      = doc.getString("description") ?: "",
                    experienceYears  = (doc.getLong("experienceYears") ?: 0L).toInt(),
                    specializations  = (doc.get("specializations") as? List<String>) ?: emptyList(),
                    education        = (doc.get("education") as? List<String>) ?: emptyList(),
                    certifications   = (doc.get("certifications") as? List<String>) ?: emptyList(),
                    casesHandled     = (doc.getLong("casesHandled") ?: 0L).toInt(),
                    clientsServed    = (doc.getLong("clientsServed") ?: 0L).toInt()
                )
            } catch (e: Exception) {
                errorMsg = "Profile load nahi hua. Dobara try karo."
            } finally {
                isLoading = false
            }
        }
    }
}

// ── Screen (Stateful) ──────────────────────────────────────────

@Composable
fun ServiceDetailScreen(
    uid: String,                          // kaun sa user
    role: String,                         // "lawyer" ya "ca"
    onBack: () -> Unit = {},
    viewModel: ServiceDetailViewModel = viewModel()
) {
    // ViewModel logic is separated from UI content to allow Previews to work
    // without instantiating Firebase-dependent ViewModels.
    LaunchedEffect(uid) { viewModel.fetchUserDetail(uid) }

    ServiceDetailContent(
        userDetail = viewModel.userDetail,
        isLoading  = viewModel.isLoading,
        errorMsg   = viewModel.errorMsg,
        role       = role,
        onBack     = onBack,
        onRetry    = { viewModel.fetchUserDetail(uid) }
    )
}

// ── Screen Content (Stateless) ───────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailContent(
    userDetail: ServiceUserDetail?,
    isLoading: Boolean,
    errorMsg: String?,
    role: String,
    onBack: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
    // This stateless version can be easily previewed or tested with mock data.
    val roleColor = when (role.lowercase()) {
        "lawyer" -> Color(0xFF1D9E75)
        "ca"     -> Color(0xFFBA7517)
        else     -> Color(0xFF007AFF)
    }

    // SendRequestDialog state
    var showDialog by remember { mutableStateOf(false) }
    var requestSent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
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

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = roleColor)
                }
            }

            errorMsg != null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚠️", fontSize = 40.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(errorMsg, color = Color(0xFF8E8E93))
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = onRetry) {
                            Text("Dobara Try Karo")
                        }
                    }
                }
            }

            userDetail != null -> {
                val user = userDetail

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {

                    // ── Header — Avatar + Name + Rating ───────
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(80.dp)
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
                                fontSize   = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color      = roleColor
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text       = user.name,
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFF000000)
                        )

                        Spacer(Modifier.height(6.dp))

                        // Role badge
                        Box(
                            modifier = Modifier
                                .background(roleColor.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text       = user.role.replaceFirstChar { it.uppercaseChar() },
                                fontSize   = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = roleColor
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        // Rating row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            StarRatingLarge(rating = user.rating)
                            Text(
                                text     = "${user.rating} • ${user.reviewCount} reviews",
                                fontSize = 13.sp,
                                color    = Color(0xFF8E8E93)
                            )
                        }

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text     = "📍 ${user.city}",
                            fontSize = 13.sp,
                            color    = Color(0xFF8E8E93)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // ── Stats row ─────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            value    = "${user.experienceYears}+",
                            label    = "Years Exp.",
                            color    = roleColor,
                            modifier = Modifier.weight(1f)
                        )
                        // Lawyer → cases, CA → clients
                        if (role.lowercase() == "lawyer") {
                            StatCard(
                                value    = "${user.casesHandled}+",
                                label    = "Cases",
                                color    = roleColor,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            StatCard(
                                value    = "${user.clientsServed}+",
                                label    = "Clients",
                                color    = roleColor,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        StatCard(
                            value    = "${user.reviewCount}",
                            label    = "Reviews",
                            color    = roleColor,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // ── About ─────────────────────────────────
                    if (user.description.isNotBlank()) {
                        DetailSection(title = "About") {
                            Text(
                                text     = user.description,
                                fontSize = 14.sp,
                                color    = Color(0xFF3C3C43),
                                lineHeight = 22.sp
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                    }

                    // ── Specializations ───────────────────────
                    if (user.specializations.isNotEmpty()) {
                        DetailSection(title = "Specializations") {
                            FlowChips(items = user.specializations, color = roleColor)
                        }
                        Spacer(Modifier.height(10.dp))
                    }

                    // ── Education ─────────────────────────────
                    if (user.education.isNotEmpty()) {
                        DetailSection(title = "Education") {
                            user.education.forEach { item ->
                                BulletItem(text = item)
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                    }

                    // ── Certifications ────────────────────────
                    if (user.certifications.isNotEmpty()) {
                        DetailSection(title = "Certifications") {
                            user.certifications.forEach { item ->
                                BulletItem(text = item)
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                    }

                    Spacer(Modifier.height(24.dp))

                    // ── Send Request Button ───────────────────
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Button(
                            onClick  = { if (!requestSent) showDialog = true },
                            enabled  = !requestSent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape    = RoundedCornerShape(14.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor         = if (requestSent) Color(0xFF1D9E75) else roleColor,
                                disabledContainerColor = Color(0xFF1D9E75)
                            )
                        ) {
                            Text(
                                text       = if (requestSent) "Request Bhej Di ✓" else "Request Bhejo",
                                fontSize   = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))
                }

                // ── SendRequestDialog ─────────────────────────
                if (showDialog) {
                    SendRequestDialog(
                        targetUser = ServiceUser(
                            uid  = user.uid,
                            name = user.name,
                            role = user.role
                        ),
                        roleColor  = roleColor,
                        onDismiss  = { showDialog = false },
                        onConfirm  = { _ ->
                            showDialog   = false
                            requestSent  = true
                        }
                    )
                }
            }
        }
    }
}

// ── Helper Composables ────────────────────────────────────────

@Composable
fun DetailSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text       = title,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color      = Color(0xFF000000)
            )
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun StatCard(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text       = value,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = color
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text     = label,
                fontSize = 11.sp,
                color    = Color(0xFF8E8E93)
            )
        }
    }
}

@Composable
fun BulletItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("• ", fontSize = 14.sp, color = Color(0xFF8E8E93))
        Text(text, fontSize = 14.sp, color = Color(0xFF3C3C43))
    }
}

@Composable
fun FlowChips(items: List<String>, color: Color) {
    // Simple wrap layout — chips ek ke baad ek, wrap karte hain
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.chunked(3).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .background(color.copy(alpha = 0.10f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text     = item,
                            fontSize = 12.sp,
                            color    = color,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StarRatingLarge(rating: Float) {
    val fullStars  = rating.toInt()
    val emptyStars = 5 - fullStars
    Row {
        repeat(fullStars)  { Text("★", fontSize = 16.sp, color = Color(0xFFEF9F27)) }
        repeat(emptyStars) { Text("☆", fontSize = 16.sp, color = Color(0xFFD1D5DB)) }
    }
}

// ── Preview ───────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
fun ServiceDetailScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        ServiceDetailContent(
            userDetail = ServiceUserDetail(
                uid = "preview_uid",
                name = "Sahil Maske",
                role = "Lawyer",
                city = "Mumbai",
                rating = 4.8f,
                reviewCount = 120,
                description = "Expert in Corporate and Criminal Law with 10+ years of experience.",
                experienceYears = 10,
                specializations = listOf("Corporate Law", "Criminal Law", "Civil Litigation"),
                education = listOf("LLB - Mumbai University"),
                certifications = listOf("Bar Council Registered"),
                casesHandled = 500
            ),
            isLoading = false,
            errorMsg = null,
            role = "lawyer",
            onBack = {},
            onRetry = {}
        )
    }
}