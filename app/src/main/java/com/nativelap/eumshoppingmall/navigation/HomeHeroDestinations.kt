package com.nativelap.eumshoppingmall.navigation

import com.nativelap.eumshoppingmall.ui.home.HomeHeroActionId

fun destinationForHomeHeroAction(action: HomeHeroActionId): EumDestination = when (action) {
    HomeHeroActionId.BrowseProducts -> EumDestination.ProductList(categoryId = null)
    HomeHeroActionId.RequestQuote -> EumDestination.QuoteRequest()
    HomeHeroActionId.ViewCompany -> EumDestination.CompanyIntro
}

fun destinationForCategory(categoryId: String?): EumDestination = EumDestination.ProductList(categoryId)
