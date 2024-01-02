package ssf.vttp.miniproject.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {


    private String name;
    private Float latitude;
    private Float longitude;
    private List<Recommendation> recommendations;
    
    
}
