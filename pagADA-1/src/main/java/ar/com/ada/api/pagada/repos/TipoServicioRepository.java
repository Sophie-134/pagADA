package ar.com.ada.api.pagada.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.ada.api.pagada.entities.TipoServicio;

public interface TipoServicioRepository extends JpaRepository<TipoServicio, Integer> {
    
}