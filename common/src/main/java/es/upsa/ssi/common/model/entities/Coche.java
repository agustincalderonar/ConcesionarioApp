package es.upsa.ssi.common.model.entities;



import lombok.*;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@With
@Builder(setterPrefix = "with")
public class Coche implements Serializable {

    private String matricula;
    private String marca;
    private String modelo;
    private int anno;

}
