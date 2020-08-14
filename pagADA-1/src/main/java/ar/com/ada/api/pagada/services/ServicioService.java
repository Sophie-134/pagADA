package ar.com.ada.api.pagada.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.repos.TipoServicioRepository;

@Service
public class ServicioService {

    @Autowired
    TipoServicioRepository tipoRepo;

    public List<TipoServicio> listarTipoServicios() {
        return tipoRepo.findAll();
    }

    public boolean grabar(TipoServicio tipoServicio) {
        if (tipoRepo.existsById(tipoServicio.getTipoServicioId())){
            return false;
        
        }        
        tipoRepo.save(tipoServicio);

        return true;
    }
}