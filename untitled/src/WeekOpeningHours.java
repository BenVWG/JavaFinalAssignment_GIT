import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeekOpeningHours {
    private List<OpeningHours> openingHoursList = new ArrayList<>();
    private String[] daysOfTheWeek = new String[]{
            "MONDAY",
            "TUESDAY",
            "WEDNESDAY",
            "THURSDAY",
            "FRIDAY",
            "SATURDAY",
            "SUNDAY"
    };

    public void addOpeningHours(OpeningHours openingHours) {
        openingHoursList.add(openingHours);
    }

    ZoneId zoneId = ZoneId.of("Europe/Amsterdam");

    // Getters and Setters
        // OpeningHours
    public OpeningHours getOpeningHours(String day) {
        for (int i = 0; i < daysOfTheWeek.length; i++) {
            if (daysOfTheWeek[i].equalsIgnoreCase(day)) {
                return openingHoursList.get(i);
            }
        }
        return null;
    }

    public void setOpeningHours(List<OpeningHours> openingHoursList) {
        this.openingHoursList = openingHoursList;
    }

        // OpeningHoursList
    public List<OpeningHours> getOpeningHoursList() {
        return openingHoursList;
    }

    public void setOpeningHoursList(List<OpeningHours> openingHoursList) {
        this.openingHoursList = openingHoursList;
    }

        // DaysOfTheWeek
    public String[] getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    public void setDaysOfTheWeek(String[] daysOfTheWeek) {
        this.daysOfTheWeek = daysOfTheWeek;
    }

    // Recursive Method
    //<editor-fold desc="Description">
    //    public Instant calculateDeliveryDate(Instant time, Duration productionTime, ZoneId timeZone) {
//        OpeningHours openingHours = getOpeningHours(time.atZone(timeZone).getDayOfWeek().toString());
//
//        if (openingHours != null) {
//            LocalTime openingTime = openingHours.getOpeningTime();
//            LocalTime closingTime = openingHours.getClosingTime();
//
//            LocalTime localTime = time.atZone(timeZone).toLocalTime();
//
//            // Calculate the time until the shop closes on the current day
//            Duration timeUntilClosing = Duration.between(localTime, closingTime);
//
//            if (timeUntilClosing.compareTo(productionTime) >= 0) {
//                // The order can be completed within today's opening hours
//                return time;
//            } else {
//                // The order continues into the next opening day
//                Instant nextOpeningDayTime = time.atZone(timeZone).with(TemporalAdjusters.next(DayOfWeek.of(openingHours.getDayNumber())))
//                        .toLocalDate().atTime(openingTime).atZone(timeZone).toInstant();
//
//                // Calculate remaining production time considering shop's opening hours
//                Duration remainingProductionTime = productionTime.minus(timeUntilClosing);
//
//                return calculateDeliveryDate(nextOpeningDayTime, remainingProductionTime, timeZone);
//            }
//        } else {
//            // No valid opening hours for the current day, find the next opening day
//            DayOfWeek nextOpeningDay = findNextOpeningDay(time.atZone(timeZone).getDayOfWeek());
//
//            if (nextOpeningDay != null) {
//                OpeningHours nextOpeningHours = getOpeningHours(nextOpeningDay.toString());
//                LocalTime nextOpeningTime = nextOpeningHours.getOpeningTime();
//
//                Instant nextDayTime = time.atZone(timeZone).with(nextOpeningDay)
//                        .toLocalDate().atTime(nextOpeningTime).atZone(timeZone).toInstant();
//
//                // Calculate time until the next opening
//                Duration timeUntilOpening = Duration.between(time, nextDayTime);
//
//                // Adjust remaining production time
//                Duration adjustedProductionTime = productionTime.plus(timeUntilOpening);
//
//                return calculateDeliveryDate(nextDayTime, adjustedProductionTime, timeZone);
//            }
//        }
//
//        return null; // No valid opening hours found
//    }
    //</editor-fold>

    //TODO 3 options instead of a boolean; open, almost open and closed (two of which will stay on today to do their logic
    public LocalTime adjustLocalTime(LocalTime timeNow, OpeningHours today){
        LocalTime openingTime = today.getOpeningTime();
        LocalTime closingTime = today.getClosingTime();

         if (timeNow.isBefore(openingTime)) {
            return openingTime; // Scenario 1: Time is before opening; thus time should be the openingTime
         } else if (timeNow.isAfter(closingTime)) {
             return closingTime; // Scenario 2: Time is after closing; Time gets set to ClosingTime and through the
                                 // 'if' statement the time gets set to the nextOpeningDayTime
         } else {
             return timeNow;     // Scenario 3: Time is now.
         }
    }

    public int findNextDayOfWeek(int dayNumber) {
        if (dayNumber < 7){
            return dayNumber + 1;
        }else {
            return 1;
        }
    }

    public Instant calculateDeliveryDate(Instant time, Duration productionTime, ZoneId timeZone) {
        OpeningHours openingHours = getOpeningHours(time.atZone(timeZone).getDayOfWeek().toString());

        LocalTime closingTime = openingHours.getClosingTime();
        LocalTime localTime = time.atZone(timeZone).toLocalTime();

        localTime = adjustLocalTime(localTime, openingHours);

        if (localTime.equals(closingTime)) {
            int nextDayNumber = findNextDayOfWeek(openingHours.getDayNumber());
            DayOfWeek nextDayOfWeek = DayOfWeek.of(nextDayNumber);  // Calculate next day's DayOfWeek

            OpeningHours nextOpeningHours = getOpeningHours(nextDayOfWeek.toString());
            LocalTime nextOpeningTime = nextOpeningHours.getOpeningTime();

            LocalDate localDate = LocalDate.now();
            Instant nextOpeningDayTime = nextOpeningTime.atDate(localDate).atZone(timeZone).toInstant();

            return calculateDeliveryDate(nextOpeningDayTime, productionTime, timeZone);
        }

        Duration timeUntilClosing = Duration.between(localTime, closingTime);

        if (timeUntilClosing.compareTo(productionTime) >= 0) {
            return time.plus(productionTime);

        } else {

            // Calculate full opening hours of the next day
            int nextDayNumber = findNextDayOfWeek(openingHours.getDayNumber());
            DayOfWeek nextDayOfWeek = DayOfWeek.of(nextDayNumber);

            OpeningHours nextOpeningHours = getOpeningHours(nextDayOfWeek.toString());
            LocalTime nextOpeningTime = nextOpeningHours.getOpeningTime();
            LocalTime nextClosingTime = nextOpeningHours.getClosingTime();

            Duration openingHoursNextDay = Duration.between(nextOpeningTime, nextClosingTime);
            Duration remainingProductionTime = productionTime.minus(timeUntilClosing);

            Instant instant = nextOpeningTime // LocalTime
                    .atDate(localDate) // LocalDateTime
                    .atZone(zoneId) // ZonedDateTime
                    .toInstant(); // Instant
            LocalDate localDate = LocalDate.ofInstant(instant, timeZone);
            Instant nextOpeningDayTime = nextOpeningTime.atDate(localDate).atZone(timeZone).toInstant();

            if (openingHoursNextDay.compareTo(remainingProductionTime) >= 0) {
                // Remaining production time can be accommodated within the next day's opening hours
                return nextOpeningDayTime.plus(remainingProductionTime);
            } else {
                // Production continues into the following day
                return calculateDeliveryDate(nextOpeningDayTime, remainingProductionTime, timeZone);
            }
        }
    }

    public Instant getValidPickupTime(Instant orderTime, ShoppingCart shoppingCart, ZoneId timeZone) {
        Duration totalManufacturingTime = shoppingCart.getTotalManufacturingTime();

        Instant validPickupInstant = calculateDeliveryDate(orderTime, totalManufacturingTime, timeZone);

        // Debug line
        System.out.println("Calculated Valid Pickup Time: " + validPickupInstant.atZone(timeZone).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return validPickupInstant;
    }

    public boolean openingHoursAreSameDay(Instant time, LocalTime openingTime, LocalTime closingTime, ZoneId timeZone) {
        LocalTime localTime = time.atZone(timeZone).toLocalTime();
        return !localTime.isBefore(openingTime) && !localTime.isAfter(closingTime);
    }
}