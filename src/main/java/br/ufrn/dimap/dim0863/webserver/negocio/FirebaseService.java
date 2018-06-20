package br.ufrn.dimap.dim0863.webserver.negocio;

import org.springframework.stereotype.Component;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import br.ufrn.dimap.dim0863.webserver.repositorio.UserTokenRepository;
import br.ufrn.dimap.dim0863.webserver.ssm.AppNotification;


@Component
public class FirebaseService {

	UserTokenRepository repository;

	public FirebaseService(UserTokenRepository repositorio) {
		this.repository = repositorio;
	}

	public void updateToken(String login, String token)  throws Exception  {
		repository.update(login, token);
	}

	public void notifyUser(String login, AppNotification notification)  throws Exception  {
		//This registration token comes from the client FCM SDKs.
		String registrationToken = repository.find(login);

		// See documentation on defining a message payload.
		Message message = Message.builder()
		    .putData("action", notification.name())
		    .setToken(registrationToken)
		    .build();

		// Send a message to the device corresponding to the provided registration token.
		ApiFuture<String> future = FirebaseMessaging.getInstance().sendAsync(message);

		ApiFutures.addCallback(future, new ApiFutureCallback<String>() {

			@Override
			public void onSuccess(String messageId) {
				// Response is a message ID string.
				//System.out.println("Successfully sent message. Response: " + messageId);
			}

			@Override
			public void onFailure(Throwable throwable) {
				if (throwable instanceof ApiException) {
					ApiException apiException = ((ApiException) throwable);
					// details on the API exception
					System.out.println(apiException.getStatusCode().getCode());
					System.out.println(apiException.isRetryable());
				}
				System.out.println("Error publishing message : " + message);
			}

		});
	}

}
