package ar.com.ada.api.pagada.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.repos.ServicioRepository;
import ar.com.ada.api.pagada.repos.TipoServicioRepository;

@Service
public class ServicioService {

    @Autowired
    TipoServicioRepository tipoRepo;
    @Autowired
    ServicioRepository servicioRepo;

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
}