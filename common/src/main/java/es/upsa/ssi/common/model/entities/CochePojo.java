package es.upsa.ssi.common.model.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
@Builder(setterPrefix = "with")
public class CochePojo {

    private String marca;
    private String modelo;
    private int anno;

}
