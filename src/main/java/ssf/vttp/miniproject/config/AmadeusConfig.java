package ssf.vttp.miniproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amadeus.Amadeus;

@Configuration
public class AmadeusConfig {
    
    @Value("${api.key}")
    private String API_KEY;
 
    @Value("${api.secret}")
    private String API_SECRET;

    @Bean
    public Amadeus amadeus() {
        return Amadeus.builder(API_KEY, API_SECRET).build();
    }
}
