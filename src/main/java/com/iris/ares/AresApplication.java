package com.iris.ares;



import com.iris.ares.reactGenerator.security.LicenceManager;
import com.iris.ares.reactGenerator.security.exception.LicenceInvalideException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Class for Ares application
 */
@SpringBootApplication
public class AresApplication {

    /**
     * Default Constructor
     */
    public AresApplication(){

    }


    /**
     * Nothing to comment
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(AresApplication.class, args);
    }


    /**
     * Method who initialize AresApplication
     */
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
