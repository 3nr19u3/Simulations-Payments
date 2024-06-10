package com.balance.balanceservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {
    @Nullable
    private long id;
    @NonNull
    private String name;
    @NonNull
    private double value;
    @Builder.Default
    private boolean isLocal = false;

}
