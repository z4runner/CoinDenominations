import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/*
 * Represents the coin denominations and quantities for a given amount
 * Example: 95 cents is comprised of Qty 1 50c, Qty 1 25c, and Qty 2 10c
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DenomBreakdown implements Serializable {

    // Coin denomination
    @JsonProperty("Coin")
    private float coin;

    // Qty of coins needed
    @JsonProperty("quantity")
    private long quantity;

}
