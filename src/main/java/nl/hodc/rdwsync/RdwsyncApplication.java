package nl.hodc.rdwsync;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.json.JSONObject;

@SpringBootApplication
@RequestMapping("app")

public class RdwsyncApplication implements CommandLineRunner {

	@Autowired
	AppProperties myAppProperties;

	@GetMapping("/startplate")
	public String getStartplate() {
		return myAppProperties.getStartplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(RdwsyncApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String startPlate = this.getStartplate();
		Boolean startPoint1 = false;
		Boolean startPoint2 = false;
		Boolean startPoint3 = false;
		Boolean startPoint4 = false;
		List<String> letterLijst = ListCreator.getList();
		System.out.println("Start Plate: " + startPlate);
		for (String deel1 : letterLijst) {
			if (startPoint1 | deel1.equalsIgnoreCase(startPlate.substring(0, 1))) {
				startPoint1 = true;
				for (String deel2 : letterLijst) {
					if (startPoint2 | deel2.equalsIgnoreCase(startPlate.substring(1, 2))) {
						startPoint2 = true;
						for (String deel3 : letterLijst) {
							if (startPoint3 | deel3.equalsIgnoreCase(startPlate.substring(2, 3))) {
								startPoint3 = true;
								for (String deel4 : letterLijst) {
									if (startPoint4 | deel4.equalsIgnoreCase(startPlate.substring(3, 4))) {
										startPoint4 = true;
										String kenteken = deel1 + deel2 + deel3 + deel4 + "%";
										if (DutchLicensePlateFormatChecker.matchValid(kenteken)) {
											System.out.println("Zoek string naar RDW: " + kenteken);
											ResponseEntity<String> response = RdwRequestData.getData(kenteken);
											if (response.getStatusCode() == HttpStatus.OK) {
												RdwResponse[] voertuigen = Converter.fromJsonString(response.getBody());
												System.out.println(response.getBody());
												for (final RdwResponse voertuig : voertuigen) {
													String jsonString = new JSONObject()
															.put("Kenteken", voertuig.getKenteken())
															.put("Merk", voertuig.getMerk())
															.put("Handelsbenaming", voertuig.getHandelsbenaming())
															.put("EersteKleur", voertuig.getEersteKleur())
															.put("TweedeKleur", voertuig.getTweedeKleur())
															.put("DatumEersteToelating",
																	voertuig.getDatumEersteToelating())
															.put("Voertuigsoort", voertuig.getVoertuigsoort())
															.put("Inrichting", voertuig.getInrichting())
															.put("message",
																	voertuig.getKenteken() + " " + voertuig.getMerk()
																			+ " " + voertuig.getHandelsbenaming() + " "
																			+ voertuig.getEersteKleur() + " "
																			+ voertuig.getTweedeKleur() + " "
																			+ voertuig.getDatumEersteToelating() + " "
																			+ voertuig.getVoertuigsoort() + " "
																			+ voertuig.getInrichting())
															.toString();
													IndexDelete.deleteRdwEntry(voertuig.getKenteken());
													IndexPost.postRdwEntry(jsonString, voertuig.getKenteken());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
