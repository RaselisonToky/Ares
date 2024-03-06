package com.iris.ares;



import com.iris.ares.reactGenerator.security.LicenceManager;
import com.iris.ares.reactGenerator.security.exception.LicenceInvalideException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AresApplication {

    public static void main(String[] args) {
        SpringApplication.run(AresApplication.class, args);
    }

    public static void initialize(){
        try {
            LicenceManager licenceManager = new LicenceManager();
            licenceManager.init();
        }catch (LicenceInvalideException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.exit(1);
        }

    }

}
