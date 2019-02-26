package no.nav.xmlstilling.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
//@EnableJpaRepositories("no.nav.sbl.entity.six")
public class DevApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DevApplication.class);
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(args));
        list.add("--spring.profiles.active=dev");
        //SpringApplication.run(DevApplication.class, list.toArray(new String[0]));
        SpringApplication sa = new SpringApplication(DevApplication.class);
        sa.run(list.toArray(new String[0]));
    }
}
