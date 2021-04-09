package knu.capston.returnhomesafely;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class APIApplication {
    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class, args);
    }
}

/*

* APIApplication Process
*   - the CCTV of Police GPS Location requested by front-end is read from MariaDB stored through BatchApplication and return List<GPS>
*   - receives and stored the current location data sent from front-end

       Todo List :
            RESTAPI format design
            MVC Pattern design
            CI/CD construction

*
*/

// Let's consider about docker / kubernetes for both batch and api modules