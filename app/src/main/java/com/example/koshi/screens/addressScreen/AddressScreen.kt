package com.example.koshi.screens.addressScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.koshi.model.Address
import com.example.koshi.utils.bouncyClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    navigateBack: () -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isEditorOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleEditor() },
            containerColor = MaterialTheme.colorScheme.surface,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            AddressEditorSheet(
                label = uiState.label,
                houseNumber = uiState.houseNumber,
                street = uiState.street,
                city = uiState.city,
                postalCode = uiState.postalCode,
                isAutoFilling = uiState.isAutoFilling,
                error = uiState.error,
                onUpdate = viewModel::updateField,
                onUseLocation = viewModel::useCurrentLocation,
                onSave = viewModel::saveAddress
            )
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("My Addresses", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { viewModel.toggleEditor() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Add, "Add", modifier = Modifier.size(36.dp))
            }
        }
    ) { padding ->
        if (uiState.isLoading && uiState.savedAddresses.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = 100.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.savedAddresses) { address ->
                    ExpressiveAddressCard(
                        address = address,
                        onDelete = { viewModel.deleteAddress(address.id) },
                        onClick = { viewModel.toggleEditor(address) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExpressiveAddressCard(
    address: Address,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val icon = if (address.label == "Work") Icons.Default.Work else Icons.Default.Home

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth().bouncyClick(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    address.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${address.street}, ${address.city}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error.copy(0.7f))
            }
        }
    }
}

@Composable
fun AddressEditorSheet(
    label: String,
    houseNumber: String,
    street: String,
    city: String,
    postalCode: String,
    isAutoFilling: Boolean,
    error: String?,
    onUpdate: (String?, String?, String?, String?, String?) -> Unit,
    onUseLocation: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp).padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Address Details", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Button(
            onClick = onUseLocation,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            if (isAutoFilling) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onTertiaryContainer)
            } else {
                Icon(Icons.Default.MyLocation, null)
                Spacer(Modifier.width(12.dp))
                Text("Use Current Location", fontSize = 16.sp)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Home", "Work", "Other").forEach { tag ->
                FilterChip(
                    selected = label == tag,
                    onClick = { onUpdate(tag, null, null, null, null) },
                    label = { Text(tag) },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Precise Fields Split
        CleanTextField(
            value = houseNumber,
            onValueChange = { onUpdate(null, it, null, null, null) },
            label = "House / Flat / Block No. *"
        )

        CleanTextField(
            value = street,
            onValueChange = { onUpdate(null, null, it, null, null) },
            label = "Area / Street / Sector *"
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CleanTextField(value = city, onValueChange = { onUpdate(null, null, null, it, null) }, label = "City", modifier = Modifier.weight(1f))
            CleanTextField(value = postalCode, onValueChange = { onUpdate(null, null, null, null, it) }, label = "Zip", modifier = Modifier.weight(0.5f), isNumber = true)
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Save Address", fontSize = 18.sp)
        }
    }
}

@Composable
fun CleanTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isNumber: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = if (isNumber) KeyboardType.Number else KeyboardType.Text
        )
    )
}