package br.ufrn.dimap.dim0863.webserver.web.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufrn.dimap.dim0863.webserver.negocio.FirebaseService;
import br.ufrn.dimap.dim0863.webserver.ssm.AppNotification;
import br.ufrn.dimap.dim0863.webserver.web.dto.UpdateTokenRequest;

@Controller
@RequestMapping("/api/v1/firebase")
public class FirebaseController {

	@Autowired
	FirebaseService firebaseService;

	@PostMapping(value="/update-token")
	public ResponseEntity<String> updateFirebaseToken(@RequestBody UpdateTokenRequest request) throws Exception {
		String login = request.getLogin();
		String token = request.getToken();
		firebaseService.updateToken(login, token);

		JSONObject responseJson = new JSONObject();
		responseJson.put("result", "success");
		return ResponseEntity.ok(responseJson.toString());
	}

	//TODO Remove this test method
	@PostMapping(value="/notify/{login}/{action}")
	public ResponseEntity<String> testUserNotification(@PathVariable("login") String login, @PathVariable("action") String action) throws Exception {
		JSONObject responseJson = new JSONObject();

		if(action != null) {
			if (action.equals("start")) {
				firebaseService.notifyUser(login, AppNotification.START_COLLECT);
				responseJson.put("result", "success");

			} else if (action.equals("stop")) {
				firebaseService.notifyUser(login, AppNotification.STOP_COLLECT);
				responseJson.put("result", "success");

			} else {
				responseJson.put("result", "error");
				responseJson.put("error", "Unknown action '" + action + "'");
			}
		} else {
			responseJson.put("result", "error");
			responseJson.put("error", "No action provided");
		}

		return ResponseEntity.ok(responseJson.toString());
	}

}
