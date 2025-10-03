package com.example.page.di

import android.content.Context
import android.net.Uri.Builder
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.page.Utils.Constants.BASE_URL
import com.example.page.api.API
import com.example.page.api.AuthInterceptor
//import com.example.page.api.NotesAPI
//import com.example.page.api.UserAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context, authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(

                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250_000L)
                    .alwaysReadResponseBody(true)
                    .build()


            )
            .build()

    }


    @Singleton
    @Provides
    fun providesRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)

    }
//    @Singleton
//    @Provides
//    fun provideUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI{
//        return retrofitBuilder.build()
//            .create(UserAPI::class.java)
//
//
//    }

    @Singleton
    @Provides
    fun provideNoteAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient):API{
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(API::class.java)
    }
}