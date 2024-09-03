package dsm.pick2024.global.config.fcm

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class FcmConfig(
    @Value("\${firebase.accessKey}")
    private val resource: String
) {
    private val firebaseResource = ClassPathResource(resource)

    @Bean
    @Throws(IOException::class)
    fun firebaseApp(): FirebaseApp {
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(firebaseResource.inputStream))
            .build()

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    @Throws(IOException::class)
    fun firebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance(firebaseApp())
    }
}
