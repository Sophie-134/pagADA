package ar.com.ada.api.pagada.entities;

import javax.persistence.*;

@Entity
@Table
public class Servicio {

    private Integer servicioId;
    private Empresa empresaId;
    private Deudor deudorId;

}