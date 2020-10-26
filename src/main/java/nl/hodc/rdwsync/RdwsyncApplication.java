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
		List<String> letterLijst = ListCreator.getList();
		System.out.println("Start Plate: " + startPlate);
		for (String deel1 : letterLijst) {
			if (deel1.equalsIgnoreCase(startPlate.substring(0, 1))) {
				for (String deel2 : letterLijst) {
					if (deel2.equalsIgnoreCase(startPlate.substring(1, 2))) {
						for (String deel3 : letterLijst) {
							if (deel3.equalsIgnoreCase(startPlate.substring(2, 3))) {
								for (String deel4 : letterLijst) {
									for (String deel5 : letterLijst) {
										String kenteken = deel1 + deel2 + deel3 + deel4 + deel5 + "%";
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
