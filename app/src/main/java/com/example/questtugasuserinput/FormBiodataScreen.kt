package com.example.questtugasuserinput

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormBiodataScreen() {
    var namaDepan by remember { mutableStateOf("") }
    var namaBelakang by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var tglLahir by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }

    var isAndroidChecked by remember { mutableStateOf(false) }
    var isUiUxChecked by remember { mutableStateOf(false) }
    var isWebChecked by remember { mutableStateOf(false) }

    // State untuk DatePicker Dialog
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // State untuk Dropdown Experience
    var expandedExperience by remember { mutableStateOf(false) }
    val experienceOptions = listOf("Fresh Graduate", "1-2 Tahun", "3-5 Tahun", "> 5 Tahun")

    // State untuk Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Validasi Lengkap: Tombol Proceed aktif & berubah warna HANYA ketika SELURUH data terisi
    val isFormValid = namaDepan.isNotBlank() &&
            namaBelakang.isNotBlank() &&
            email.isNotBlank() &&
            tglLahir.isNotBlank() &&
            experience.isNotBlank() &&
            (isAndroidChecked || isUiUxChecked || isWebChecked)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colorResource(id = R.color.white_bg))
        ) {
            // 1. Gambar Header Bagian Atas
            Image(
                painter = painterResource(id = R.drawable.taehyung),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.header_height))
            )

            // 2. Card/Box Form Mengapung diatas Image & Menyisakan ~1/4 bagian layar di bawah
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.card_top_overlap),
                        start = dimensionResource(id = R.dimen.padding_medium),
                        end = dimensionResource(id = R.dimen.padding_medium),
                        bottom = 100.dp // Mengambang menyisakan bagian bawah
                    ),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_large)),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.white_bg)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_large))
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.title_form),
                        color = colorResource(id = R.color.purple_title),
                        fontSize = dimensionResource(id = R.dimen.text_size_title).value.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

                    Text(
                        text = stringResource(id = R.string.subtitle_form),
                        color = colorResource(id = R.color.grey_subtitle),
                        fontSize = dimensionResource(id = R.dimen.text_size_subtitle).value.sp
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

                    // Row 1: Nama Depan & Nama Belakang (Presisi Seimbang)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        OutlinedTextField(
                            value = namaDepan,
                            onValueChange = { namaDepan = it },
                            label = { Text(stringResource(id = R.string.label_nama_depan)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium))
                        )
                        OutlinedTextField(
                            value = namaBelakang,
                            onValueChange = { namaBelakang = it },
                            label = { Text(stringResource(id = R.string.label_nama_belakang)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium))
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

                    // Input Email (Full Width)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(id = R.string.label_email)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium))
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

                    // Row 2: Tgl Lahir & Experience
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        // Tgl Lahir
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = tglLahir,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text(stringResource(id = R.string.label_tgl_lahir)) },
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(Icons.Default.DateRange, contentDescription = null)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDatePicker = true },
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium))
                            )
                        }

                        // Experience Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = experience,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text(stringResource(id = R.string.label_experience)) },
                                trailingIcon = {
                                    IconButton(onClick = { expandedExperience = true }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expandedExperience = true },
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium))
                            )

                            DropdownMenu(
                                expanded = expandedExperience,
                                onDismissRequest = { expandedExperience = false }
                            ) {
                                experienceOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            experience = option
                                            expandedExperience = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

                    // Label Skill
                    Text(
                        text = stringResource(id = R.string.label_pilih_skill),
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.text_size_section).value.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
























