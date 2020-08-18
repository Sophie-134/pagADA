package ar.com.ada.api.pagada.models.request;

import java.math.BigDecimal;
import java.util.Date;
import static ar.com.ada.api.pagada.entities.Pago.MedioPagoEnum;

public class InfoPagoRequest {
    public BigDecimal importePagado;
    public Date fechaPago;
    public MedioPagoEnum medioPago;
    public String infoMedioPago;
    public String moneda;
}