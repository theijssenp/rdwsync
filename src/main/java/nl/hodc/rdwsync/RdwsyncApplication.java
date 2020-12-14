package nl.hodc.rdwsync;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@RequestMapping("app")

public class RdwsyncApplication implements CommandLineRunner {

	@Autowired
	AppProperties myAppProperties;

	@GetMapping("/startplate")
	public String getStartplate() {
		return myAppProperties.getStartplate();
	}

	@GetMapping("/csv")
	public String getCsv() {
		return myAppProperties.getCsv();
	}

	@GetMapping("/uitsluiten")
	public String getUitsluiten() {
		return myAppProperties.getUitsluiten();
	}

	@GetMapping("/esserverip")
	public String getEsserverip() {
		return myAppProperties.getEsserverip();
	}

	private static final String COMMA_DELIMITER = ",";

	public static boolean mod(int a, int b) {
		if (a < 0) {
			return false;
		} else if (a == b) {
			return true;
		} else {
			return mod(a - b, b);
		}
	}

	private static void putRecordToIndex(String line, String esserverip) {
		RdwResponse voertuig = new RdwResponse();
		// System.out.println("Start rowscanner: ");
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(COMMA_DELIMITER);
			int i = 0;
			while (rowScanner.hasNext()) {
				String waarde = rowScanner.next();
				i++;
				if (i == 1) {
					// System.out.println("Set kenteken: ");
					voertuig.setKenteken(waarde);
					// System.out.println("Get kenteken: " + voertuig.getKenteken());
				}

				if (i == 3) {
					// System.out.println("Set merk: ");
					voertuig.setMerk(waarde);
					// System.out.println("Get merk: " + voertuig.getMerk());
				}

				if (i == 4) {
					// System.out.println("Set handelsbenaming: ");
					voertuig.setHandelsbenaming(waarde);
					// System.out.println("Get Handelsbenaming: " + voertuig.getHandelsbenaming());
				}

				if (i == 10) {
					// System.out.println("Set eerste kleur: ");
					voertuig.setEersteKleur(waarde);
					// System.out.println("Get EersteKleur: " + voertuig.getEersteKleur());
				}

				if (i == 11) {
					voertuig.setTweedeKleur(waarde);
				}
				if (i == 20) {
					voertuig.setDatumEersteToelating(waarde);
					// System.out.println("Get DatumEersteToelating: " +
					// voertuig.getDatumEersteToelating());
				}

				if (i == 2) {
					voertuig.setVoertuigsoort(waarde);
					// System.out.println("Get Voertuigsoort: " + voertuig.getVoertuigsoort());
				}
				if (i == 8) {
					voertuig.setInrichting(waarde);
					// System.out.println("Get Inrichting: " + voertuig.getInrichting());
				}

			}

			IndexPost.postRdwEntry(voertuig, voertuig.getKenteken(), esserverip);

		}
	}

	public static void main(String[] args) {
		SpringApplication.run(RdwsyncApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String yes = "Y";
		String esserverip = this.getEsserverip();
		if (this.getCsv().equalsIgnoreCase(yes)) {
			System.out.println("Start CSV: " + this.getCsv());
			try (Scanner scanner = new Scanner(new File("Open_Data_RDW__Gekentekende_voertuigen.csv"));) {
				int i = 0;
				while (scanner.hasNextLine()) {
					i++;
					String line = scanner.nextLine();
					// Header overslaan
					if (i > 260) {
						try {
							putRecordToIndex(line, esserverip);
						} catch (Exception e) {
							System.out.println("Probleem met " + line.substring(0, 6));
						}

					}
					if (mod(i, 10000)) {
						System.out.println("Aantal verwerkt: " + i);
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
						Date date = new Date(System.currentTimeMillis());
						System.out.println(formatter.format(date));
					}
				}
				System.out.println("Done!");
			}
		} else {
			String startPlate = this.getStartplate();
			Boolean startPoint1 = false;
			Boolean startPoint2 = false;
			Boolean startPoint3 = false;
			Boolean startPoint4 = false;
			List<String> letterLijst = ListCreator.getList(this.getUitsluiten());
			System.out.println("Start Plate: " + startPlate);
			for (String deel1 : letterLijst) {
				String kenteken = "";
				try {
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
												kenteken = deel1 + deel2 + deel3 + deel4 + "%";
												if (DutchLicensePlateFormatChecker.matchValid(kenteken)) {
													System.out.println("Zoek string naar RDW: " + kenteken);
													ResponseEntity<String> response = RdwRequestData.getData(kenteken);
													if (response.getStatusCode() == HttpStatus.OK) {
														RdwResponse[] voertuigen = Converter
																.fromJsonString(response.getBody());
														System.out.println("Aantal gevonden: " + voertuigen.length
																+ " met zoekvraag: " + kenteken);
														for (final RdwResponse voertuig : voertuigen) {

															// IndexDelete.deleteRdwEntry(voertuig.getKenteken(),
															// esserverip
															// );
															IndexPost.postRdwEntry(voertuig, voertuig.getKenteken(),
																	esserverip);
															// String jsonString2 = new JSONObject()
															// .put("herkomst", "initieel")
															// .put("zoekterm",
															// voertuig.getMerk()).toString().toLowerCase();

															// ZoektermPost.postZoektermEntry(jsonString2,
															// voertuig.getMerk().toLowerCase(),
															// esserverip);
															// String jsonString3 = new JSONObject()
															// .put("herkomst", "initieel")
															// .put("zoekterm",
															// voertuig.getEersteKleur()).toString().toLowerCase();

															// ZoektermPost.postZoektermEntry(jsonString3,
															// voertuig.getEersteKleur().toLowerCase(), esserverip);

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
				} catch (Exception e) {
					System.out.println("Error: " + kenteken + " " + e);
				}
			}
		}
	}
}
