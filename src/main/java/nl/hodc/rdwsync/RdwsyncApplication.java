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

	public static void main(String[] args) {
		SpringApplication.run(RdwsyncApplication.class, args);
	}

	@Autowired
	AppProperties myAppProperties;

	@GetMapping("/startplate")
	public String getStartplate() {
		return myAppProperties.getStartplate();
	}

	@GetMapping("/csvstartrow")
	public String getCsvStartRow() {
		return myAppProperties.getCsvstartrow();
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

	@Override
	public void run(String... args) throws Exception {
		String yes = "Y";
		String esserverip = this.getEsserverip();
		if (this.getCsv().equalsIgnoreCase(yes)) {
			System.out.println("Start CSV: " + this.getCsv());
			 /// Open_Data_RDW__Gekentekende_voertuigen ///
			try (Scanner scanner = new Scanner(new File("Open_Data_RDW__Gekentekende_voertuigen.csv"));) {
				int i = 0;
				while (scanner.hasNextLine()) {
					i++;
					String line = scanner.nextLine();
					// Bepalen start row
					int csvstartrow;
					try {
						csvstartrow = Integer.parseInt(this.getCsvStartRow());
					} catch (Exception e) {
						csvstartrow = 1;
					}

					if (i > csvstartrow) {
						try {
							putRecordToIndex(line, esserverip);
						} catch (Exception e) {
							System.out.println("WARNING: Probleem met " + line.substring(0, 6));
							String kenteken = line.substring(0, 6);
							ResponseEntity<String> response = RdwRequestData.getData(kenteken);
							if (response.getStatusCode() == HttpStatus.OK) {
								RdwResponse[] voertuigen = Converter.fromJsonString(response.getBody());
								System.out.println(
										"Online gevonden: " + voertuigen.length + " met zoekvraag: " + kenteken);
								for (final RdwResponse voertuig : voertuigen) {
									try {
										IndexPost.postRdwEntry(voertuig, voertuig.getKenteken(), esserverip);
									} catch (Exception e2) {
										System.out
												.println("ERROR: " + line.substring(0, 6) + " Kenteken niet overgezet");
									}

								}
							}
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
		List<String> kenmerken = CSVUtils.parseLine(line);
		voertuig.setKenteken(kenmerken.get(0));
		voertuig.setMerk(kenmerken.get(2));
		voertuig.setHandelsbenaming(kenmerken.get(3));
		voertuig.setEersteKleur(kenmerken.get(9));
		voertuig.setTweedeKleur(kenmerken.get(10));
		voertuig.setDatumEersteToelating(kenmerken.get(19));
		voertuig.setVoertuigsoort(kenmerken.get(1));
		voertuig.setInrichting(kenmerken.get(7));
		IndexPost.postRdwEntry(voertuig, voertuig.getKenteken(), esserverip);
	}
}
