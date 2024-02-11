import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/*
 * Represents the denominations and quantities making a given amount
 * Example: A 95 cents is comprised of Qty 1 50c, Qty 1 25c, and Qty 2 10c
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DenomBreakdown implements Serializable {

    // The amount of the gift card
    @JsonProperty("Coin")
    private float coin;

    // How many of this amount is needed
    @JsonProperty("quantity")
    private long quantity;

}
