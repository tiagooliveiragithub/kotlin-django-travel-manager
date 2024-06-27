package online.tripguru.tripguruapp.views.adapters

import online.tripguru.tripguruapp.localstorage.models.Local

interface OnLocalClickListener {
    fun onLocalClick(local : Local)
}