package com.nutrisport.manage_product

import ContentWithMessageBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.ButtonPrimary
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceBrand
import com.nutrisport.shared.SurfaceDarker
import com.nutrisport.shared.SurfaceError
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.SurfaceSecondary
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.TextSecondary
import com.nutrisport.shared.TextWhite
import com.nutrisport.shared.component.AlertTextField
import com.nutrisport.shared.component.CustomTextField
import com.nutrisport.shared.component.ErrorCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.component.PrimaryButton
import com.nutrisport.shared.component.dialog.CategoriesDialog
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.DisplayResult
import com.nutrisport.shared.util.RequestState
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    id: String?,
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<ManageProductViewModel>()
    val messageBarState = rememberMessageBarState()
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid
    val thumbnailUploaderState = viewModel.thumbnailUploaderState
    var showCategoriesDialog by remember { mutableStateOf(false) }
    var dropDownMenuOpened by remember { mutableStateOf(false) }

    val photoPicker = koinInject<PhotoPicker>()
    photoPicker.InitializePhotoPicker(
        onImageSelect = { file ->
            viewModel.uploadThumbnailToStorage(
                file = file,
                onSuccess = {
                    messageBarState.addSuccess("Image uploaded successfully.")
                }
            )
        }
    )

    AnimatedVisibility(
        visible = showCategoriesDialog
    ) {
        CategoriesDialog(
            category = screenState.category,
            onDismiss = {
                showCategoriesDialog = false
            },
            onConfirmClick = { selectedCategory ->
                viewModel.updateCategory(selectedCategory)
                showCategoriesDialog = false
            }
        )
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (id == null) "NEW PRODUCT" else "EDIT PRODUCT",
                        fontFamily = BebasNeueFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (screenState.thumbnail.isEmpty()) {
                            messageBarState.addError("You must add an image before proceeding.")
                        } else {
                            navigateBack()
                        }
                    }) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Arrow icon",
                            tint = IconPrimary
                        )
                    }
                },
                actions = {
                    id.takeIf { it != null }?.let {
                        Box {
                            IconButton(onClick = { dropDownMenuOpened = true }) {
                                Icon(
                                    painter = painterResource(Resources.Icon.VerticalMenu),
                                    contentDescription = "Vertical menu icon",
                                    tint = IconPrimary
                                )
                            }
                            DropdownMenu(
                                containerColor = Surface,
                                expanded = dropDownMenuOpened,
                                onDismissRequest = { dropDownMenuOpened = false }
                            ) {
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            modifier = Modifier.size(14.dp),
                                            painter = painterResource(Resources.Icon.Delete),
                                            contentDescription = "Delete icon",
                                            tint = IconPrimary
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = "Delete",
                                            color = TextPrimary,
                                            fontSize = FontSize.REGULAR
                                        )
                                    },
                                    onClick = {
                                        dropDownMenuOpened = false
                                        viewModel.deleteProduct(
                                            onSuccess = navigateBack,
                                            onError = { message -> messageBarState.addError(message) }
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }
    ) { padding ->
        ContentWithMessageBar(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            contentBackgroundColor = Surface,
            messageBarState = messageBarState,
            errorMaxLines = 2,
            errorContainerColor = SurfaceError,
            errorContentColor = TextWhite,
            successContainerColor = SurfaceBrand,
            successContentColor = TextPrimary
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(
                        top = 12.dp,
                        bottom = 24.dp
                    )
                    .imePadding(),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(size = 12.dp))
                            .border(
                                width = 1.dp,
                                color = BorderIdle,
                                shape = RoundedCornerShape(size = 12.dp)
                            )
                            .background(SurfaceLighter)
                            .clickable(
                                enabled = thumbnailUploaderState.isIdle()
                            ) {
                                photoPicker.open()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        thumbnailUploaderState.DisplayResult(
                            onIdle = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(Resources.Icon.Plus),
                                    contentDescription = "Plus icon",
                                    tint = IconPrimary
                                )
                            },
                            onLoading = {
                                LoadingCard(modifier = Modifier.fillMaxSize())
                            },
                            onSuccess = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = ImageRequest.Builder(
                                            LocalPlatformContext.current
                                        ).data(screenState.thumbnail)
                                            .crossfade(enable = true)
                                            .build(),
                                        contentDescription = "Thumbnail image",
                                        contentScale = ContentScale.Crop
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(size = 6.dp))
                                            .padding(
                                                top = 12.dp,
                                                end = 12.dp
                                            )
                                            .background(ButtonPrimary)
                                            .clickable {
                                                viewModel.deleteThumbnailFromStorage(
                                                    onSuccess = {
                                                        messageBarState.addSuccess("Image deleted successfully.")
                                                    },
                                                    onError = { message ->
                                                        messageBarState.addError(message = message)
                                                    }
                                                )
                                            }
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(14.dp),
                                            painter = painterResource(Resources.Icon.Delete),
                                            contentDescription = "Icon Delete"
                                        )
                                    }
                                }

                            },
                            onError = { message ->
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ErrorCard(message = message)
                                    Spacer(Modifier.height(12.dp))
                                    TextButton(
                                        onClick = {
                                            viewModel.updateThumbnailUploaderState(RequestState.Idle)
                                        },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = Color.Transparent
                                        )
                                    ) {
                                        Text(
                                            text = "Try Again",
                                            fontSize = FontSize.SMALL,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        )
                    }
                    CustomTextField(
                        value = screenState.title,
                        onValueChange = viewModel::updateTitle,
                        placeHolder = "Title"
                    )
                    CustomTextField(
                        modifier = Modifier.height(168.dp),
                        value = screenState.description,
                        onValueChange = viewModel::updateDescription,
                        placeHolder = "Description",
                        expanded = true
                    )
                    AlertTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = screenState.category.title,
                        onClick = { showCategoriesDialog = true }
                    )
                    AnimatedVisibility(
                        visible = screenState.category != ProductCategory.Accessories
                    ) {
                        Column {
                            CustomTextField(
                                value = "${screenState.weight ?: ""}",
                                onValueChange = { viewModel.updateWeight(it.toIntOrNull()) },
                                placeHolder = "Weight",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                )
                            )
                            Spacer(Modifier.height(12.dp))
                            CustomTextField(
                                value = screenState.flavors,
                                onValueChange = viewModel::updateFlavors,
                                placeHolder = "Flavors"
                            )
                        }
                    }
                    CustomTextField(
                        value = "${screenState.price}",
                        onValueChange = { value ->
                            if (value.isEmpty() || value.toDoubleOrNull() != null) {
                                viewModel.updatePrice(value.toDoubleOrNull() ?: 0.0)
                            }
                        },
                        placeHolder = "Price",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 12.dp),
                                text = "New?",
                                fontSize = FontSize.REGULAR,
                                color = TextPrimary
                            )
                            Switch(
                                checked = screenState.isNew,
                                onCheckedChange = { checked ->
                                    viewModel.updateNew(checked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = SurfaceSecondary,
                                    checkedThumbColor = Surface,
                                    checkedBorderColor = SurfaceSecondary,
                                    uncheckedTrackColor = SurfaceDarker,
                                    uncheckedThumbColor = Surface,
                                    uncheckedBorderColor = SurfaceDarker
                                )
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 12.dp),
                                text = "Popular?",
                                fontSize = FontSize.REGULAR,
                                color = TextPrimary
                            )
                            Switch(
                                checked = screenState.isPopular,
                                onCheckedChange = { checked ->
                                    viewModel.updatePopular(checked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = SurfaceSecondary,
                                    checkedThumbColor = Surface,
                                    checkedBorderColor = SurfaceSecondary,
                                    uncheckedTrackColor = SurfaceDarker,
                                    uncheckedThumbColor = Surface,
                                    uncheckedBorderColor = SurfaceDarker
                                )
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 12.dp),
                                text = "Discounted?",
                                fontSize = FontSize.REGULAR,
                                color = TextPrimary
                            )
                            Switch(
                                checked = screenState.isDiscounted,
                                onCheckedChange = { checked ->
                                    viewModel.updateDiscounted(checked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = SurfaceSecondary,
                                    checkedThumbColor = Surface,
                                    checkedBorderColor = SurfaceSecondary,
                                    uncheckedTrackColor = SurfaceDarker,
                                    uncheckedThumbColor = Surface,
                                    uncheckedBorderColor = SurfaceDarker
                                )
                            )
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
                PrimaryButton(
                    text = if (id == null) "Add New Product" else "Update",
                    icon = if (id == null) Resources.Icon.Plus else Resources.Icon.Checkmark,
                    enabled = isFormValid,
                    onClick = {
                        if (id != null) {
                            viewModel.updateProduct(
                                onSuccess = {
                                    messageBarState.addSuccess("Product updated successfully.")
                                },
                                onError = { message ->
                                    messageBarState.addError(message)
                                }
                            )
                        } else {
                            viewModel.createNewProduct(
                                onSuccess = {
                                    messageBarState.addSuccess("Product created successfully.")
                                    navigateBack()
                                },
                                onError = { message ->
                                    messageBarState.addError(message)
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}