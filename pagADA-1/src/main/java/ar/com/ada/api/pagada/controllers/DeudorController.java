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
        Deudor deudor = new Deudor();

        // to do: hacer validaciones y crear la empresa a traves del servic
        deudorService.crearDeudor(deudorReq.paisId, deudorReq.tipoIdImpositivo, deudorReq.idImpositivo,
                deudorReq.nombre);

        // O haciendo esto
        // Empresa empresa = empresaService.crearEmpresa(empR.paisId,
        // empR.tipoIdImpositivo, empR.idImpositivo, empR.nombre);

        if (deudor.getDeudorId() != null) {
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