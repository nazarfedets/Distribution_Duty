package org.example;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Duty {
    private String description;
    private int quantity;
    private LocalDate date;

    public Duty(String description, int quantity) {
        this.description = description;
        this.quantity = quantity;
        this.date = null;
    }


}
