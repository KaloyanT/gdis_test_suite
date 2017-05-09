package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;


@SpringBootApplication
@RestController
@RequestMapping("/{entryId}/action")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

	@RequestMapping(method = RequestMethod.POST)
	public String userIdInput(@PathVariable Integer entryId, @RequestBody String method) {
		return Integer.toString(entryId) + " with action " + method;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String userId(@PathVariable Integer entryId) {
		return Integer.toString(entryId);
	}

}