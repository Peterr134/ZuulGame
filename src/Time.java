public class Time {
    private static int timeOfDay;

    public static void changeTime(int minutes){
        timeOfDay += minutes;
        if(timeOfDay >= 1440){
            //switches to the next day
            minutes -= 1440;
        }
    }

    public static int inMinutes(){
        return timeOfDay;
    }

    public static double inHours(){
        return roundHundredth(timeOfDay/60.0);
    }

    public static void setTimeOfDay(int timeOfDay) {
        Time.timeOfDay = timeOfDay;
    }

    public static void initializeTimeOfDay() {
        timeOfDay = 0;
    }

    public static double roundHundredth(double thing){
        return (double) Math.round(thing * 100) / 100;
    }

    public static boolean isNight(){
        return Time.inHours() > 21 || Time.inHours() < 6;
    }
}
