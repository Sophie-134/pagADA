package ar.com.ada.api.pagada.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.models.response.GenericResponse;
import ar.com.ada.api.pagada.services.TipoServicioService;

@RestController
public class TipoServicioController {
    @Autowired
    TipoServicioService tipoService;

    @GetMapping("/api/tipo-servicios")
    public ResponseEntity<List<TipoServicio>> listarTipoServicios() {
        List<TipoServicio> tipoServicios;
        // to do :obtener lista de empresas a atravez del service y lo guardamos en la
        // varable empresas

        tipoServicios = tipoService.listarTipoServicios();
        return ResponseEntity.ok(tipoServicios);
}
@PostMapping("/api/tipo-servicios")
    public ResponseEntity<GenericResponse> crearTipoServicio(@RequestBody Integer TipoServicioId, String nombre) {
        GenericResponse gr = new GenericResponse();
        TipoServicio tipoServicio = new TipoServicio();

        tipoServicio.setTipoServicioId(TipoServicioId);
        tipoServicio.setNombre(nombre);

        tipoService.grabar(tipoServicio);

        if (tipoServicio.getTipoServicioId() != null) {
            gr.isOk = true;
            gr.id = tipoServicio.getTipoServicioId();
            gr.message = "tipo-servicio creado";
            return ResponseEntity.ok(gr);
        }

        gr.isOk = false;
        gr.message = "No se pudo crear  el tipo-servicio";

        return ResponseEntity.badRequest().body(gr); // http 400

    }
}