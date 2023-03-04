package me.varoa.ugh.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import me.varoa.ugh.core.data.UserRepositoryImpl
import me.varoa.ugh.core.domain.repository.UserRepository

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {
    @Binds
    fun userRepository(repo: UserRepositoryImpl): UserRepository
}
