package aetat.six.web.selftest.status;

import no.nav.selftest.status.Status;

import org.apache.commons.collections15.Factory;

import javax.sql.DataSource;

public class SixDbStatus implements Factory<Status> {

    private final DataSource sixDb;

    public SixDbStatus(DataSource sixDb) {
        this.sixDb = sixDb;
    }

    @Override
    public Status create() {
        String status = "UNI_SIX-DB_";
        try {
            sixDb.getConnection().createStatement().execute("select * from STILLING_LOGG");
            return new Status(status + "OK");
        } catch (Exception e) {
            return new Status(status + "ERROR", e);
        }
    }

}
