package ssf.vttp.miniproject.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    private String name;
    private String description;
    private Float amount;
    private String currencyCode;
    private String pictures;
    private String bookingLink;
    
}
