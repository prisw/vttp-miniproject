package ssf.vttp.miniproject.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recommendation that = (Recommendation) obj;
        return Objects.equals(name, that.name) &&
               Objects.equals(description, that.description) &&
               Objects.equals(amount, that.amount) &&
               Objects.equals(currencyCode, that.currencyCode) &&
               Objects.equals(pictures, that.pictures) &&
               Objects.equals(bookingLink, that.bookingLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, amount, currencyCode, pictures, bookingLink);
    }
    
}
