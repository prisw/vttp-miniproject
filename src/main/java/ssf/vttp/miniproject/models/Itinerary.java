package ssf.vttp.miniproject.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @Min(value=3, message = "Minimal 3 characters")
    @Max(value=50, message="Maximal 50 characters")
    private String location;

    @NotNull(message="Mandatory field")
    private String activity;

    private String remarks;

}
