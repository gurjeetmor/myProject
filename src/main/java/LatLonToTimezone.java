import com.google.maps.GeoApiContext;
import com.google.maps.TimeZoneApi;
import com.google.maps.model.LatLng;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Function;

public class LatLonToTimezone implements Function<LatLng,Optional<TimeZone>> {

    private static final String API_KEY = "Your Google API Key";
    private static GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);

    @Override
    public Optional<TimeZone> apply(LatLng latLng) {
        Optional<TimeZone> timezone = Optional.empty();
        try {
            timezone = Optional.of(TimeZoneApi.getTimeZone(context,latLng).await());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timezone;
    }
}
