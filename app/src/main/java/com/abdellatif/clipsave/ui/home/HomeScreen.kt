package com.abdellatif.clipsave.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdellatif.clipsave.R
import com.abdellatif.clipsave.data.model.DownloadStatus
import com.abdellatif.clipsave.ui.AppViewModel
import com.abdellatif.clipsave.ui.components.DownloadRow
import com.abdellatif.clipsave.ui.components.EmptyState
import com.abdellatif.clipsave.ui.components.SectionLabel

@Composable
fun HomeScreen(vm: AppViewModel, onGoToPaste: () -> Unit) {
    val downloads by vm.downloads.collectAsStateWithLifecycle()
    val active = remember(downloads) {
        downloads.filter {
            it.status == DownloadStatus.DOWNLOADING ||
                it.status == DownloadStatus.EXTRACTING ||
                it.status == DownloadStatus.QUEUED
        }
    }
    val recent = remember(downloads) {
        downloads.filter { it.status == DownloadStatus.COMPLETED }.take(10)
    }
    val completed = remember(downloads) {
        downloads.count { it.status == DownloadStatus.COMPLETED }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = { NewDownloadButton(onGoToPaste) }
    ) { padding ->
        if (downloads.isEmpty()) {
            EmptyState(
                title = "Nothing saved yet",
                hint = "Paste a link on the New tab, or share one from any app.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            item {
                Header()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatTile(Modifier.weight(1f), "Total", downloads.size.toString())
                    StatTile(Modifier.weight(1f), "Saved", completed.toString())
                    StatTile(Modifier.weight(1f), "Active", active.size.toString())
                }
            }
            if (active.isNotEmpty()) {
                item {
                    SectionLabel(
                        "In progress",
                        Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
                    )
                }
                items(active, key = { it.id }) { DownloadRow(it, vm::retry, vm::delete) }
            }
            if (recent.isNotEmpty()) {
                item {
                    SectionLabel(
                        "Recent",
                        Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
                    )
                }
                items(recent, key = { it.id }) { DownloadRow(it, vm::retry, vm::delete) }
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        Modifier.padding(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text("ClipSave", style = MaterialTheme.typography.headlineMedium)
        Text(
            ".",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun StatTile(modifier: Modifier, label: String, value: String) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(Modifier.padding(vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(2.dp))
            SectionLabel(label)
        }
    }
}

@Composable
private fun NewDownloadButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        shadowElevation = 6.dp
    ) {
        Row(
            Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.plus),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text("New download", style = MaterialTheme.typography.labelLarge)
        }
    }
}
