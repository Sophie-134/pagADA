package ar.com.ada.api.pagada.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.Deudor;
import ar.com.ada.api.pagada.entities.Pais.TipoIdImpositivoEnum;
import ar.com.ada.api.pagada.repos.DeudorRepository;

@Service
public class DeudorService {

    @Autowired
    DeudorRepository deudorRepo;

    public List<Deudor> listarDeudores() {
        return deudorRepo.findAll();
    }

    public Deudor crearDeudor(Integer paisId, TipoIdImpositivoEnum tipoIdImpositivo, String idImpositivo,
            String nombre) {
        Deudor deudor = new Deudor();

        deudor.setPaisId(paisId);
        deudor.setTipoIdImpositivo(tipoIdImpositivo);
        deudor.setIdImpositivo(idImpositivo);
        deudor.setNombre(nombre);

        deudorRepo.save(deudor);
        return deudor;

    }

    // Evitar pasar el model DeudorRequest que referencia a algo que pasa FRONT o
    // desde afuera, hacia la capa Service
    // Si se hace, usarlo con otro nombre o con otro tipo de funcionalidad.
    // O sea este metodo evitarlo hacerlo asi:
    // public DeudorValidacionEnum validarDeudorInfo(DeudorRequest req) {
    // return validarDeudorInfo(req.paisId, req.tipoIdImpositivo, req.idImpositivo,
    // req.nombre);
    // }
    public DeudorValidacionEnum validarDeudorInfo(Integer paisId, TipoIdImpositivoEnum tipoIdImpositivo,
            String idImpositivo, String nombre) {
        if (idImpositivo == null) {
            return DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO;
        }
        if (!(idImpositivo.length() >= 11 && idImpositivo.length() <= 20)) {
            return DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO;
        }
        if (idImpositivo.chars().filter(c -> !Character.isDigit(c)).count() > 0) {
            return DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO;
        }
        if (nombre == null)
            return DeudorValidacionEnum.NOMBRE_INVALIDO;

        if (nombre.length() > 100)
            return DeudorValidacionEnum.NOMBRE_INVALIDO;

        return DeudorValidacionEnum.OK;
    }

    public enum DeudorValidacionEnum {
        OK, ID_IMPOSITIVO_INVALIDO, NOMBRE_INVALIDO;
    }
}