package br.ufrn.dimap.dim0863.webserver;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@SpringBootApplication
public class WebserverApplication {

	public static void main(String[] args) throws IOException {
		FileInputStream serviceAccount = new FileInputStream("ufrndrivers-firebase-adminsdk-qzsyj-6897067720.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://ufrndrivers.firebaseio.com")
				.build();

		if (FirebaseApp.getApps().size() <= 0) {
			FirebaseApp.initializeApp(options);
		}

		SpringApplication.run(WebserverApplication.class, args);
	}
}
