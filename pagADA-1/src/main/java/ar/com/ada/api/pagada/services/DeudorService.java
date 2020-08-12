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

    public DeudorValidacionEnum validarDeudor(Deudor deudor) {
        if (deudor.getIdImpositivo() == null) {
            return DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO;
        }
        if (!(deudor.getIdImpositivo().length() >= 11 && deudor.getIdImpositivo().length() <= 20)) {
            return DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO;
        }
        if (deudor.getIdImpositivo().chars().filter(c -> !Character.isDigit(c)).count() > 0) {
            return DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO;
        }

        return DeudorValidacionEnum.OK;
    }

    public enum DeudorValidacionEnum {
        OK, ID_IMPOSITIVO_INVALIDO;
    }
}