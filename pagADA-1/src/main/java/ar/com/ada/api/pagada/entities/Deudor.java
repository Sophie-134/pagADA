package ar.com.ada.api.pagada.entities;

import javax.persistence.*;

import ar.com.ada.api.pagada.entities.Pais.TipoIdImpositivoEnum;

@Entity
@Table(name = "deudor")
public class Deudor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deudor_id")
    private Integer deudorId;
    @Column(name = "pais_id")
    private Integer paisId;
    @Column(name = "tipo_id_impositivo_id")
    private TipoIdImpositivoEnum tipoIdImpositivo;
    @Column(name = "id_impositivo")
    private String idImpositivo;
    private String nombre;
}