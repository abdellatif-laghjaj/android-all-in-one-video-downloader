package com.abdellatif.clipsave.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdellatif.clipsave.data.model.DownloadStatus
import com.abdellatif.clipsave.ui.AppViewModel
import com.abdellatif.clipsave.ui.components.DownloadRow

@Composable
fun HomeScreen(vm: AppViewModel, onGoToPaste: () -> Unit) {
    val downloads by vm.downloads.collectAsState()
    val active = downloads.filter {
        it.status == DownloadStatus.DOWNLOADING || it.status == DownloadStatus.EXTRACTING || it.status == DownloadStatus.QUEUED
    }
    val recent = downloads.filter { it.status == DownloadStatus.COMPLETED }.take(10)
    val total = downloads.size
    val completed = downloads.count { it.status == DownloadStatus.COMPLETED }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onGoToPaste,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("New") }
            )
        }
    ) { p ->
        LazyColumn(Modifier.fillMaxSize().padding(p)) {
            item {
                Text(
                    "ClipSave",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
                )
                Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(Modifier.weight(1f), "Total", total.toString())
                    StatCard(Modifier.weight(1f), "Completed", completed.toString())
                    StatCard(Modifier.weight(1f), "Active", active.size.toString())
                }
            }
            if (active.isNotEmpty()) {
                item { SectionHeader("Active") }
                items(active, key = { it.id }) { DownloadRow(it, vm::retry, vm::delete) }
            }
            item { SectionHeader(if (recent.isEmpty()) "No downloads yet" else "Recent") }
            items(recent, key = { it.id }) { DownloadRow(it, vm::retry, vm::delete) }
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, label: String, value: String) {
    Card(modifier) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
    )
}
