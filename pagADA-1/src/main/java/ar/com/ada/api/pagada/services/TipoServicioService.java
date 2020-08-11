package ar.com.ada.api.pagada.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.repos.TipoServicioRepository;

@Service
public class TipoServicioService {

    @Autowired
    TipoServicioRepository tipoRepo;

    public List<TipoServicio> listarTipoServicios() {
        return tipoRepo.findAll();
    }

    public void grabar(TipoServicio tipoServicio) {
      
        tipoRepo.save(tipoServicio);
    }
}