package root.proproquzigame;

import java.math.BigDecimal;

public enum DamageValue {
    LOW(new BigDecimal("10")),
    MEDIUM(new BigDecimal("20")),
    HIGH(new BigDecimal("30"));

    private BigDecimal damageValue;

    // Constructor now accepts BigDecimal
    DamageValue(BigDecimal damageValue) {
        this.damageValue = damageValue;
    }

    // Getter method now returns BigDecimal
    public BigDecimal getDamageValue() {
        return damageValue;
    }
}
