package ssf.vttp.miniproject.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Itinerary {

    @NotNull(message="Mandatory field")
    @Min(value=1, message="Day 1 of travelling not included")
    private Integer day;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message="Departure date must be present or future date")
    private Date date;

    @NotNull(message="Mandatory field")
    @Size(min=3, max=50, message="3 to 50 characters")
    private String location;

    @NotNull(message="Mandatory field")
    private String activity;

    private String remarks;

    private String id;

    



    public Itinerary(Integer day, Date date,String location,String activity, String remarks) {
        this.day = day;
        this.date = date;
        this.location = location;
        this.activity = activity;
        this.remarks = remarks;
    }

}
