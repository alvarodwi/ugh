package me.varoa.ugh.ui.screen.detail

import dagger.hilt.android.lifecycle.HiltViewModel
import me.varoa.ugh.core.domain.repository.UserRepository
import me.varoa.ugh.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val user: UserRepository
) : BaseViewModel()
