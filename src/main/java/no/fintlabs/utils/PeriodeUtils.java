package no.fintlabs.utils;

import no.fint.model.felles.kompleksedatatyper.Periode;

import java.util.Date;

public class PeriodeUtils {
    public static boolean periodeIsValid(Periode gyldighetsperiode, Date currentTime) {
        if (gyldighetsperiode == null) {
            throw new NullPeriodeException();
        }
        if (gyldighetsperiode.getStart() == null) {
            throw new NullPeriodeStartDatoException();
        }
        return !currentTime.before(gyldighetsperiode.getStart())
                && isEndValid(gyldighetsperiode.getSlutt(), currentTime);
    }
    public static String getStatus(Periode gyldighetsperiode, Date currentTime) {
        return periodeIsValid(gyldighetsperiode, currentTime)
                ? "ACTIVE"
                : "INACTIVE";
    }

    public static Date getStatusChanged(Periode gyldighetsperiode, Date currentTime) {
        boolean valid = periodeIsValid(gyldighetsperiode, currentTime);
        if (valid) {
            return gyldighetsperiode.getStart();
        }
        return currentTime.before(gyldighetsperiode.getStart())
                ? gyldighetsperiode.getStart()
                : gyldighetsperiode.getSlutt();
    }
    private static boolean isEndValid(Date end, Date currentTime) {
        return end == null || currentTime.before(end);
    }

    public static class NullPeriodeException extends IllegalArgumentException  {
        public NullPeriodeException() {
            super("Gyldighetsperiode is null");
        }
    }

    public static class NullPeriodeStartDatoException extends IllegalArgumentException  {
        public NullPeriodeStartDatoException() {
            super("Gyldighetsperiode.start is null");
        }
    }
}
