package com.example.foodpalv1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow

data class Friend(
    val id: Int,
    val name: String,
    val streakDays: Int,
    val avatarUrl: String? = null,
    val lastActive: String = "Today"
)

data class LeaderboardEntry(
    val rank: Int,
    val userId: Int,
    val name: String,
    val score: Int,
    val avatarUrl: String? = null
)

@Composable
fun CommunityScreen() {
    // Sample data
    val friends = remember {
        listOf(
            Friend(1, "Kaushik", 12),
            Friend(2, "Jamas", 30),
            Friend(3, "Smeet", 7),
            Friend(4, "Donald Trump", 21),
            Friend(5, "Ryan Reynolds", 5)
        )
    }
    
    val leaderboard = remember {
        listOf(
            LeaderboardEntry(1, 101, "Purav", 1250),
            LeaderboardEntry(2, 102, "Smeet", 1120),
            LeaderboardEntry(3, 103, "Kaushik", 980),
            LeaderboardEntry(4, 104, "Jamas", 870),
            LeaderboardEntry(5, 105, "Lisa Wong", 810)
        )
    }
    
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Friends", "Leaderboard", "Challenges")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp)
    ) {
        Text(
            "Community",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    height = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (selectedTab) {
            0 -> FriendsTab(friends)
            1 -> LeaderboardTab(leaderboard)
            2 -> ChallengesTab()
        }
    }
}

@Composable
fun FriendsTab(friends: List<Friend>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Your Friends",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(friends) { friend ->
                FriendItem(friend)
            }
        }
    }
}

@Composable
fun FriendItem(friend: Friend) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Avatar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Name and streak info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    friend.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Last active: ${friend.lastActive}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            // Streak badge
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Streak",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${friend.streakDays} days",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardTab(entries: List<LeaderboardEntry>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Weekly Nutrition Champions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Rank",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp)
            )
            Text(
                "User",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Score",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.width(60.dp)
            )
        }
        
        Divider()
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(entries) { entry ->
                LeaderboardEntryItem(entry)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Your Ranking",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "#14",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Keep going! You're 120 points away from the top 10",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun LeaderboardEntryItem(entry: LeaderboardEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank
        Text(
            "#${entry.rank}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = when (entry.rank) {
                1 -> Color(0xFFFFD700) // Gold
                2 -> Color(0xFFC0C0C0) // Silver
                3 -> Color(0xFFCD7F32) // Bronze
                else -> MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.width(40.dp)
        )
        
        // Avatar and name
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    entry.name.first().toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                entry.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        // Score
        Text(
            "${entry.score}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.width(60.dp)
        )
    }
}

@Composable
fun ChallengesTab() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Weekly Challenges",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Active challenge
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "ACTIVE CHALLENGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    "Protein Champion",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Text(
                    "Meet your protein goals for 5 consecutive days",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LinearProgressIndicator(
                    progress = 0.6f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    "3/5 days completed",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "Upcoming Challenges",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Upcoming challenges
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Balanced Diet Master",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    "Maintain a balanced macronutrient ratio for a full week",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                Text(
                    "Starts in 2 days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Calorie Deficit Champion",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    "Maintain a healthy calorie deficit for 10 days",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                Text(
                    "Starts in 9 days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
} 