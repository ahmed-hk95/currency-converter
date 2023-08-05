package ahmed.hk.currencyconverter.utils

import ahmed.hk.currencyconverter.data.remote.APIs
import ahmed.hk.currencyconverter.data.repository.DataInterface
import ahmed.hk.currencyconverter.data.repository.DataRepository
import ahmed.hk.currencyconverter.ui.base.BaseApplication

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoggingInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class HeadersInterceptor

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(Json.asConverterFactory(Constants.CONTENT_TYPE.toMediaType()))
            .build()
    }

    private val READ_TIMEOUT = 30L
    private val WRITE_TIMEOUT = 30L
    private val CONNECTION_TIMEOUT = 10L
    private val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @HeadersInterceptor headerInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient().newBuilder()
        with(okHttpClientBuilder){
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            cache(cache)
            addInterceptor(headerInterceptor)
            addInterceptor(loggingInterceptor)

        }


        return okHttpClientBuilder.build()
    }


    @Provides
    @HeadersInterceptor
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            requestBuilder.addHeader("Content-Type","application/json")
            it.proceed(requestBuilder.build())
        }
    }

    @Provides
    @LoggingInterceptor
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        fun RequestBody?.format():String {
            return try {
                val buffer = Buffer()
                this?.writeTo(buffer)
                buffer.readUtf8()
            }catch (e:Exception){
                "DID NOT WORK"
            }
        }

        return Interceptor {
            with(it.request()){
                val reqBody = body.format()
                Log.d("RQST","Request Method: $method")
                Log.d("RQST","Request Url: $url")
                Log.d("RQST","Request Headers: $headers")
                Log.d("RQST","Request Body: $reqBody")
            }
            it.proceed(it.request())
        }


    }


    @Provides
    @Singleton
    internal fun provideCache(context: Context): Cache {
//        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(context.cacheDir, CACHE_SIZE_BYTES)
    }


    @Provides
//    @Singleton
    fun provideContext(application: BaseApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): APIs {
        return retrofit.create(APIs::class.java)
    }

    @FlowPreview
    @Provides
    @Singleton
    fun provideDataRepository(apIs: APIs): DataRepository {
        return DataRepository(apIs)
    }

    @Provides
    fun provideEventChannel():Channel<Event> = Channel(Channel.BUFFERED)
}


@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelsModule{

    @Binds
    abstract fun provideDataInterface(repository: DataRepository): DataInterface

}