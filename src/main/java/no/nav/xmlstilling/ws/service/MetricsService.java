package no.nav.xmlstilling.ws.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class MetricsService {

    private static final String metricBaseName = "xmlstilling.mottatt.";
    private static final String metricNameOk = metricBaseName + "ok";
    private static final String metricNameFeil = metricBaseName + "feil";

    private final MeterRegistry meterRegistry;

    @Inject
    MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void registerFor(String leverandor, Boolean receivedOk) {
        String[] tags = {"leverandor", leverandor};

        meterRegistry.counter(metricNameOk, tags).increment(receivedOk ? 1 : 0);
        meterRegistry.counter(metricNameFeil, tags).increment(receivedOk ? 0 : 1);
    }

}
