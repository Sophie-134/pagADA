package ar.com.ada.api.pagada.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.Deudor;
import ar.com.ada.api.pagada.models.request.DeudorRequest;
import ar.com.ada.api.pagada.models.response.GenericResponse;
import ar.com.ada.api.pagada.services.DeudorService;
import ar.com.ada.api.pagada.services.DeudorService.DeudorValidacionEnum;

@RestController
public class DeudorController {

    @Autowired
    DeudorService deudorService;

    @GetMapping("/api/deudores")
    public ResponseEntity<List<Deudor>> listarDeudores() {
        List<Deudor> deudores;
        // to do :obtener lista de empresas a atravez del service y lo guardamos en la
        // varable empresas

        deudores = deudorService.listarDeudores();
        return ResponseEntity.ok(deudores);

    }

    @PostMapping("/api/deudores")
    public ResponseEntity<GenericResponse> crearEmpresa(@RequestBody DeudorRequest deudorReq) {
        GenericResponse gr = new GenericResponse();

        // Validacion
        DeudorValidacionEnum resultadoValidacion = deudorService.validarDeudorInfo(deudorReq.paisId,
                deudorReq.tipoIdImpositivo, deudorReq.idImpositivo, deudorReq.nombre);
        if (resultadoValidacion != DeudorValidacionEnum.OK) {
            gr.isOk = false;
            gr.message = "No se pudo validar el deudor " + resultadoValidacion.toString();

            return ResponseEntity.badRequest().body(gr); // http 400
        }
        Deudor deudor = deudorService.crearDeudor(deudorReq.paisId, deudorReq.tipoIdImpositivo, deudorReq.idImpositivo,
                deudorReq.nombre);

        if (deudor != null) {
            gr.isOk = true;
            gr.id = deudor.getDeudorId();
            gr.message = "Deudor creado con exito";
            return ResponseEntity.ok(gr);
        }

        gr.isOk = false;
        gr.message = "No se pudo crear  el Deudor";

        return ResponseEntity.badRequest().body(gr); // http 400

    }
}