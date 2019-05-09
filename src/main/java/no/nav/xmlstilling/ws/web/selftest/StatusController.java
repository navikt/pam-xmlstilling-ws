package no.nav.xmlstilling.ws.web.selftest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@RestController
public class StatusController {

    @Autowired
    private DataSource sixDb;

    @GetMapping(path = "/isAlive")
    public String isAlive() {
        return "OK";
    }

    @GetMapping("/internal/selftest")
    public ResponseEntity selftest () {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(sixDb);
        boolean ok = true;
        try {
            jdbcTemplate.execute("select now()");
        } catch (Exception e) {
            ok = false;
        }

        Map<String, String> statusMap = new HashMap();
        statusMap.put("Database", ok ? "OK" : "NOT OK");
        return ResponseEntity.ok(statusMap);
    }
}
