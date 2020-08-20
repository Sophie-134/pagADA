package ar.com.ada.api.pagada.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.OperacionPago;
import ar.com.ada.api.pagada.entities.Pago;
import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.entities.OperacionPago.OperacionPagoEnum;
import ar.com.ada.api.pagada.entities.Pago.MedioPagoEnum;
import ar.com.ada.api.pagada.entities.Servicio.EstadoEnum;
import ar.com.ada.api.pagada.repos.PagoRepository;
import ar.com.ada.api.pagada.repos.ServicioRepository;
import ar.com.ada.api.pagada.repos.TipoServicioRepository;

@Service
public class ServicioService {

    @Autowired
    TipoServicioRepository tipoRepo;
    @Autowired
    ServicioRepository servicioRepo;
    @Autowired
    PagoRepository pagoRepo;

    public List<TipoServicio> listarTipoServicios() {
        return tipoRepo.findAll();
    }

    public boolean crearTipoServicio(TipoServicio tipoS) {
        if (tipoRepo.existsById(tipoS.getTipoServicioId())) {
            return false;

        }
        tipoRepo.save(tipoS);

        return true;
    }

    public ServicioValidacionEnum validarServicio(Servicio servicio) {

        if (servicio.getImporte().compareTo(new BigDecimal(0)) <= 0) {
            return ServicioValidacionEnum.IMPORTE_INVALIDO;
        }
        return ServicioValidacionEnum.OK;

    }

    public enum ServicioValidacionEnum {
        OK, IMPORTE_INVALIDO
    }

    public Servicio crearServicio(Servicio servicio) {
        // Si agreggo validacion justo antes de la creacion
        if (this.validarServicio(servicio) != ServicioValidacionEnum.OK)
            return servicio;

        return servicioRepo.save(servicio);

    }

    public TipoServicio buscarTipoServicioPorId(Integer tipoServicioId) {
        Optional<TipoServicio> oTipoServicio = tipoRepo.findById(tipoServicioId);
        if (oTipoServicio.isPresent()) {
            return oTipoServicio.get();
        } else
            return null;
    }

    /***
     * Trae todos los servicios
     * 
     * @return
     */
    public List<Servicio> listarServicios() {
        return servicioRepo.findAll();
    }

    /***
     * Trae todos los servicios de una empresa
     * 
     * @param empresaId eeste parametro eel Id de la empresa
     * @return
     */
    public List<Servicio> listarServiciosPorEmpresaId(Integer empresaId) {
        return servicioRepo.findAllEmpresaId(empresaId);
    }

    /**
     * Trae todos los servicios PENDIENTES de una empresa
     * 
     * @param empresaId
     * @return
     */
    public List<Servicio> listarServiciosPendientesPorEmpresaId(Integer empresaId) {
        return servicioRepo.findAllPendientesEmpresaId(empresaId);
    }

    public List<Servicio> listarServiciosPendientesPorEmpresaIdDeudorId(Integer empresaId, Integer deudorId) {
        return servicioRepo.findAllPendientesEmpresaIdDeudorId(empresaId, deudorId);
    }

    public List<Servicio> listarHistoricoPorEmpresaIdDeudorId(Integer empresaId, Integer deudorId) {
        return servicioRepo.findAllHistoricoEmpresaIdDeudorId(empresaId, deudorId);
    }

    public List<Servicio> listarDeudorIdPorCodigoBarras(String codigoBarras) {
        return servicioRepo.findByCodigoBarras(codigoBarras);
    }

    // To do:
    // buscar servicio por id
    // verficar que este pendiente de pago
    // validar si se esta pagando el total o solo una parte
    // instanciar pago

    // si se se pago pasarlo de PENDIENTE a PAGADO (al Servicio)
    // mandar mail si tenemos la info(SI tuviesemos info del que paga)
    // grabarlo en la db
    // hacer el metodo para hacer el pago,
    // todo lo de arriba va en "realizarPago"
    public OperacionPago realizarPago(Integer servicioId, BigDecimal importePagado, String moneda, Date fechaPago,
            MedioPagoEnum medioPago, String infoMedioPago) {

        OperacionPago opePago = new OperacionPago();

        Servicio servicio = buscarServicioPorId(servicioId);

        if (servicio == null) {
            opePago.setResultado(OperacionPagoEnum.RECHAZADO_SERVICIO_INEXISTENTE);
            return opePago;
        }

        if (servicio.getEstadoId() != EstadoEnum.PENDIENTE) {

            opePago.setResultado(OperacionPagoEnum.RECHAZADO_SERVICIO_YA_PAGO);
            return opePago;
        }

        // NO ACEPTAMOS PAGOS DIFERENTES AL TOTAL
        if (servicio.getImporte().compareTo(importePagado) != 0) {

            opePago.setResultado(OperacionPagoEnum.RECHAZADO_NO_ACEPTA_PAGO_PARCIAL);
            return opePago;
        }

        // INSTANCIAMOS EL PAGO
        Pago pago = new Pago();
        pago.setImportePagado(importePagado);
        pago.setMoneda(moneda);
        pago.setFechaPago(fechaPago);
        pago.setMedioPago(medioPago);
        pago.setInfoMedioPago(infoMedioPago);
        // AGREGAMOs el pago al servicio
        servicio.setPago(pago);
        // Cambiamos el estado de Pendiente a Pagado del Servicio
        servicio.setEstadoId(EstadoEnum.PAGADO);
        // Grabamos el servicio, porque en CASCADA, va a grabar el PAGO
        servicioRepo.save(servicio);

        // Devolvemos la estructura OperacionPago con la info Ok
        opePago.setPago(servicio.getPago());
        opePago.setResultado(OperacionPagoEnum.REALIZADO);

        return opePago;
    }

    public Servicio buscarServicioPorId(Integer servicioId) {
        return servicioRepo.findByServicioId(servicioId);
    }

    public Pago buscarPagoPorId(Integer pagoId) {
		return pagoRepo.findByPagoId(pagoId);
	}

	public List<Pago> buscarPagosPorEmpresaId(Integer empresaId) {
		return pagoRepo.findPagosByEmpresaId(empresaId);
	}

	public List<Pago> buscarPagosPorDeudorId(Integer deudorId) {
		return pagoRepo.findPagosByDeudorId(deudorId);
	}

}