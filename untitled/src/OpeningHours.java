import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

public class OpeningHours {
    private final int dayNumber;
    private final String dayName;
    private final LocalTime openingTime;
    private final LocalTime closingTime;

    public OpeningHours(int dayNumber, String dayName, LocalTime openingTime, LocalTime closingTime) {
        this.dayNumber = dayNumber;
        this.dayName = dayName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public String getDayName() {
        return dayName;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }
}
