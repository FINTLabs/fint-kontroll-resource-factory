package no.fintlabs.utils;

import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fintlabs.applicationResource.GyldighetsperiodeService;

import java.util.Date;

public class PeriodeUtils {
    private static final GyldighetsperiodeService gyldighetsperiodeService = new GyldighetsperiodeService();
    public static String getStatus(Periode gyldighetsperiode, Date currentTime) {
        return gyldighetsperiodeService.isValid(gyldighetsperiode, currentTime)
                ? "ACTIVE"
                : "INACTIVE";
    }

    public static Date getStatusChanged(Periode gyldighetsperiode, Date currentTime) {
        boolean valid = gyldighetsperiodeService.isValid(gyldighetsperiode, currentTime);
        if (valid) {
            return gyldighetsperiode.getStart();
        }
        return currentTime.before(gyldighetsperiode.getStart())
                ? gyldighetsperiode.getStart()
                : gyldighetsperiode.getSlutt();
    }
}
