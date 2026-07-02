package media.grab.os.ui.downloads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import media.grab.os.data.model.DownloadStatus
import media.grab.os.ui.AppViewModel
import media.grab.os.ui.components.DownloadRow

@Composable
fun DownloadsScreen(vm: AppViewModel) {
    val downloads by vm.downloads.collectAsState()
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf<DownloadStatus?>(null) }

    val filtered = downloads.filter { d ->
        (filter == null || d.status == filter) &&
            (query.isBlank() || d.title.contains(query, true) || d.url.contains(query, true) ||
                d.platform.displayName.contains(query, true))
    }

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            label = { Text("Search downloads") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true
        )
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(selected = filter == null, onClick = { filter = null }, label = { Text("All") })
            DownloadStatus.entries.forEach { s ->
                FilterChip(
                    selected = filter == s,
                    onClick = { filter = if (filter == s) null else s },
                    label = { Text(s.name.lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { vm.clearCompleted() }) { Text("Clear completed") }
            TextButton(onClick = { vm.clearAll() }) { Text("Clear all") }
        }
        if (filtered.isEmpty()) {
            Text(
                "Nothing here yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(24.dp)
            )
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                items(filtered, key = { it.id }) { DownloadRow(it, vm::retry, vm::delete) }
            }
        }
    }
}
