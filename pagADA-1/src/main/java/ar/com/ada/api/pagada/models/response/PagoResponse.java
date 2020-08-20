package ar.com.ada.api.pagada.models.response;

import java.math.BigDecimal;
import java.util.Date;

import ar.com.ada.api.pagada.entities.Pago.MedioPagoEnum;

public class PagoResponse {
    
    public Integer empresaId;
    public String nombreEmpresa;
    public Integer deudorId;
    public String nombreDeudor;
    public Integer comprobanteDePago;
    public Date fecha;
    public BigDecimal importePagado;
    public MedioPagoEnum medioPago;
    public String infoMedioPago;
}