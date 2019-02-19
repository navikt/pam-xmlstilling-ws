package aetat.six.web.selftest.status;

import no.nav.selftest.status.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;

import javax.sql.DataSource;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Answers.RETURNS_MOCKS;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SixDbStatusTest {

    @Mock(answer=RETURNS_MOCKS)
    private DataSource dataSource;


    @Test
    public void returnsStatusWithExceptionWhenQueryingSixDbFails() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        Status status = new SixDbStatus(dataSource).create();
        assertTrue(Status.HAS_EXCEPTION.evaluate(status));
        assertThat(status.toString(), containsString("_ERROR"));
    }

    @Test
    public void returnsStatusWithOkMessageAndNoException() {
        Status status = new SixDbStatus(dataSource).create();
        assertFalse(Status.HAS_EXCEPTION.evaluate(status));
        assertThat(status.toString(), containsString("_OK"));
    }
}
