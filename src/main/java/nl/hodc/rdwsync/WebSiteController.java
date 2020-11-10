package nl.hodc.rdwsync;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class WebSiteController {

	@RequestMapping("/")
	public String index() {
		return "Indexer working!";
	}

}