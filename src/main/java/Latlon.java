import com.google.maps.model.LatLng;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Latlon {
    public static void main(String[] args) throws Exception {
        System.out.println("Technical Coding Test");
        Latlon latLng = new Latlon();
        Path filePath = Paths.get(Latlon.class.getResource("input.txt").toURI());
        latLng.processFile(filePath);
    }

    public void processFile(Path filePath) throws IOException {
        List<String> inputData = Files.readAllLines(filePath);
        Function<LatLng,Optional<TimeZone>> latLngToTimezone = new LatLonToTimezone();
        List<String> output = convert(inputData,latLngToTimezone);
        output.forEach(s -> System.out.println(s));
    }

    List<String> convert(List<String> inputData, Function<LatLng, Optional<TimeZone>> LatLonToTimezone) {
        return inputData.stream().map(input -> {
            final String[] split = input.split(",");
            String dateTime = split[0];
            Double lat = Double.valueOf(split[1]);
            Double lng = Double.valueOf(split[2]);
            LatLng latLng = new LatLng(lat,lng);
            Optional<TimeZone> timeZone = LatLonToTimezone.apply(latLng);
            if(!timeZone.isPresent()){
                return "Please provide correct input";
            }
            LocalDateTime localDateTime = getLocalDateFromUtc(dateTime);
            String formattedLocalDateTime = localDateTime.toString().replace("T"," ");
            final String output = input.concat(",").concat(timeZone.get().getID()).concat(",").concat(formattedLocalDateTime);
            return output;
        }).collect(Collectors.toList());
    }

    private DateTimeFormatter getUTCDateTimeFormat(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
    }

    private LocalDateTime getLocalDateFromUtc(String dateTime){
        final DateTimeFormatter UTCTimeFormat = getUTCDateTimeFormat();
        final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime, UTCTimeFormat);
        return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.of("Pacific/Auckland"));
    }
}
