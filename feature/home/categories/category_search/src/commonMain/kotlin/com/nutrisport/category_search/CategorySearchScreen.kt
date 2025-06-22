package com.nutrisport.category_search


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.Alpha
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.IconSecondary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceDarker
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.component.ProductCard
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySearchScreen(
    category: ProductCategory,
    navigateToDetails: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<CategoryScreenViewModel>()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var searchBarVisible by mutableStateOf(false)

    val cursorMarkColor = TextSelectionColors(
        handleColor = IconSecondary,
        backgroundColor = Color.Unspecified
    )

    Scaffold(
        containerColor = Surface,
        topBar = {
            AnimatedContent(
                targetState = searchBarVisible
            ) { visible ->
                if (visible) {
                    CompositionLocalProvider(LocalTextSelectionColors provides cursorMarkColor) {
                        SearchBar(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth(),
                            inputField = {
                                SearchBarDefaults.InputField(
                                    modifier = Modifier.fillMaxWidth(),
                                    query = searchQuery,
                                    onQueryChange = viewModel::updateSearchQuery,
                                    colors = TextFieldDefaults.colors(
                                        cursorColor = IconSecondary,
                                        unfocusedContainerColor = SurfaceLighter,
                                        focusedContainerColor = SurfaceLighter,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary,
                                        disabledTextColor = TextPrimary.copy(alpha = Alpha.DISABLED),
                                        focusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
                                        unfocusedPlaceholderColor = TextPrimary.copy(alpha = Alpha.HALF),
                                        disabledPlaceholderColor = TextPrimary.copy(alpha = Alpha.DISABLED),
                                        disabledContainerColor = SurfaceDarker,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        errorIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        selectionColors = cursorMarkColor
                                    ),
                                    expanded = false,
                                    onExpandedChange = {},
                                    onSearch = {},
                                    placeholder = {
                                        Text(
                                            text = "Search here..",
                                            fontSize = FontSize.REGULAR,
                                            color = TextPrimary
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(
                                            modifier = Modifier.size(14.dp),
                                            onClick = {
                                                if (searchQuery.isNotEmpty())
                                                    viewModel.updateSearchQuery("")
                                                else
                                                    searchBarVisible = false
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(Resources.Icon.Close),
                                                contentDescription = "Close icon",
                                                tint = IconPrimary
                                            )
                                        }
                                    },
                                )
                            },
                            colors = SearchBarColors(
                                containerColor = SurfaceLighter,
                                dividerColor = BorderIdle,
                            ),
                            expanded = false,
                            onExpandedChange = {},
                            content = {}
                        )
                    }

                } else {
                    TopAppBar(
                        title = {
                            Text(
                                text = category.title,
                                fontFamily = BebasNeueFont(),
                                fontSize = FontSize.LARGE,
                                color = TextPrimary
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = navigateBack) {
                                Icon(
                                    painter = painterResource(Resources.Icon.BackArrow),
                                    contentDescription = "Back Arrow icon",
                                    tint = IconPrimary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Surface,
                            scrolledContainerColor = Surface,
                            navigationIconContentColor = IconPrimary,
                            titleContentColor = TextPrimary,
                            actionIconContentColor = IconPrimary
                        ),
                        actions = {
                            IconButton(
                                onClick = { searchBarVisible = true }
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Search),
                                    contentDescription = "Search icon",
                                    tint = IconPrimary
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        filteredProducts.DisplayResult(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onSuccess = { categoryProducts ->
                AnimatedContent(
                    targetState = categoryProducts
                ) { products ->
                    if (products.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = products,
                                key = { it.id }
                            ) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = navigateToDetails
                                )
                            }
                        }
                    } else {
                        InfoCard(
                            title = "Nothing Here!",
                            subTitle = "We couldn't find any product.",
                            image = Resources.Image.Cat
                        )
                    }
                }
            },
            onError = { message ->
                InfoCard(
                    title = "Error",
                    subTitle = message,
                    image = Resources.Image.Cat
                )
            },
            transitionSpec = fadeIn() togetherWith fadeOut()
        )
    }
}