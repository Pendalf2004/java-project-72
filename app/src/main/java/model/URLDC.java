package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class URLDC {
    private Long    id;
    private String address;
    private Date    created;

    public URLDC(String address) {
        this.address = address;
    }
}
