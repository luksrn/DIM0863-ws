package br.ufrn.dimap.dim0863.webserver.web.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufrn.dimap.dim0863.webserver.dominio.CarInfo;
import br.ufrn.dimap.dim0863.webserver.negocio.CarInfoService;
import br.ufrn.dimap.dim0863.webserver.web.dto.CarInfoListResponse;
import br.ufrn.dimap.dim0863.webserver.web.dto.CarInfoRequest;

@Controller
@RequestMapping("/api/v1/car/data")
public class CarInfoController {

	@Autowired
	CarInfoService carInfoService;

	@PostMapping(value="")
	public ResponseEntity<String> postCarInfo(@RequestBody CarInfoRequest request)  throws Exception {
		String licensePlate = request.getLicensePlate();
		CarInfo carInfo = request.getCarInfo();

		carInfoService.sendCarInfo(licensePlate, carInfo);

		JSONObject responseJson = new JSONObject();
		responseJson.put("result", "success");
		return ResponseEntity.ok(responseJson.toString());
	}

	@GetMapping(value="/{licensePlate}")
	public ResponseEntity<?> listCarInfo(@PathVariable("licensePlate") String licensePlate) throws Exception {
		List<CarInfo> carInfoList = carInfoService.findCarInfo(licensePlate);

		if(carInfoList != null) {
			return ResponseEntity.ok(buildCarInfoListResponse(licensePlate, carInfoList));
		}

		return ResponseEntity.unprocessableEntity().build();
	}

	protected CarInfoListResponse buildCarInfoListResponse(String licensePlate, List<CarInfo> carInfoList) {
		return new CarInfoListResponse(licensePlate, carInfoList);
	}

}
