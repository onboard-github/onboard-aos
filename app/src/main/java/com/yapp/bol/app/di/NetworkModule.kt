package com.yapp.bol.app.di

import com.yapp.bol.app.BuildConfig
import com.yapp.bol.data.remote.AuthApi
import com.yapp.bol.data.remote.FileApi
import com.yapp.bol.data.remote.GameApi
import com.yapp.bol.data.remote.GroupApi
import com.yapp.bol.data.remote.MatchApi
import com.yapp.bol.data.remote.MemberApi
import com.yapp.bol.data.remote.SettingApi
import com.yapp.bol.data.remote.TermsApi
import com.yapp.bol.data.remote.UserApi
import com.yapp.bol.data.utils.Utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideGroupApiService(retrofit: Retrofit): GroupApi {
        return retrofit.create(GroupApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMatchApiService(retrofit: Retrofit): MatchApi {
        return retrofit.create(MatchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSettingApiService(retrofit: Retrofit): SettingApi {
        return retrofit.create(SettingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFileApiService(retrofit: Retrofit): FileApi {
        return retrofit.create(FileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGameApiService(retrofit: Retrofit): GameApi {
        return retrofit.create(GameApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberApiService(retrofit: Retrofit): MemberApi {
        return retrofit.create(MemberApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTermsApiService(retrofit: Retrofit): TermsApi {
        return retrofit.create(TermsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}
