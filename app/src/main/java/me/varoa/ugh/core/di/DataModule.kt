package me.varoa.ugh.core.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import logcat.logcat
import me.varoa.ugh.BuildConfig
import me.varoa.ugh.core.data.local.AppDatabase
import me.varoa.ugh.core.data.prefs.AppPreferences
import me.varoa.ugh.core.data.remote.NoConnectionInterceptor
import me.varoa.ugh.core.data.remote.api.GithubApiService
import me.varoa.ugh.core.data.remote.api.GithubAuthInterceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    companion object {
        const val DEFAULT_TIMEOUT = 5L

        private const val GITHUB_BASE_URL = "https://api.github.com/"

        // https://www.dicoding.com/blog/apa-itu-rate-limit-pada-github-api/
        private const val GITHUB_PERSONAL_KEY = BuildConfig.githubToken
        private val GITHUB_AUTH_KEY get() = "token $GITHUB_PERSONAL_KEY"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Singleton
    @Provides
    fun provideNoConnectionInterceptor(
        @ApplicationContext appContext: Context
    ): NoConnectionInterceptor =
        NoConnectionInterceptor(appContext)

    @Singleton
    @Provides
    fun provideHttpClient(netConn: NoConnectionInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)

        // logging
        if (BuildConfig.DEBUG) {
            logcat { "Applying HTTP Logging" }
            val logger = HttpLoggingInterceptor { message ->
                logcat("API") { message }
            }.apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            builder.addInterceptor(logger)
        }

        // detect internet connection
        builder.addInterceptor(netConn)
        // add auth
        builder.addInterceptor(
            GithubAuthInterceptor(GITHUB_AUTH_KEY)
        )
        return builder
            .build()
    }

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideJsonConverterFactory(): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Singleton
    @Provides
    fun provideGithubApiService(client: OkHttpClient, factory: Converter.Factory): GithubApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()
        return retrofit.create(GithubApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "ugh"
        ).build()

    @Singleton
    @Provides
    fun provideFavoriteDao(db: AppDatabase) = db.favoriteDao

    @Singleton
    @Provides
    fun provideAppPreferences(@ApplicationContext appContext: Context): AppPreferences =
        AppPreferences(appContext)
}
