package ar.com.ada.api.pagada.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import ar.com.ada.api.pagada.entities.*;
import ar.com.ada.api.pagada.models.request.ActualizarServicioRequest;
import ar.com.ada.api.pagada.models.request.InfoPagoRequest;
import ar.com.ada.api.pagada.models.request.ServicioRequest;
import ar.com.ada.api.pagada.models.response.GenericResponse;
import ar.com.ada.api.pagada.services.*;
import ar.com.ada.api.pagada.services.ServicioService.ServicioValidacionEnum;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicioController {

    @Autowired
    EmpresaService empresaService;

    @Autowired
    DeudorService deudorService;

    @Autowired
    ServicioService servicioService;

    @PostMapping("/api/servicios")
    public ResponseEntity<GenericResponse> crearServicio(@RequestBody ServicioRequest servicioReq) {

        GenericResponse genericResponse = new GenericResponse();
        // 1) instanciar el objeto(en este caso el servicio)
        // 2) validar servicio ,si no es todo ok q devuleva un 400
        // 3) crear en la base de datos

        // Comenzamos con el 1) instanciar el objeto(en este caso el servicio)
        Servicio servicio = new Servicio();

        Empresa empresa = empresaService.buscarEmpresaPorId(servicioReq.empresaId);
        servicio.setEmpresa(empresa);

        Deudor deudorEncontrado = deudorService.buscarDeudorPorId(servicioReq.deudorId);
        servicio.setDeudor(deudorEncontrado);

        TipoServicio tipoServicio = servicioService.buscarTipoServicioPorId(servicioReq.tipoServicioId);
        servicio.setTipoServicio(tipoServicio);

        servicio.setTipoComprobante(servicioReq.tipoComprobanteId);
        servicio.setNumero(servicioReq.numero);
        servicio.setFechaEmision(servicioReq.fechaEmision);
        servicio.setFechaVencimiento(servicioReq.fechaVencimiento);
        servicio.setImporte(servicioReq.importe);
        servicio.setMoneda(servicioReq.moneda);
        servicio.setCodigoBarras(servicioReq.codigoBarras);
        servicio.setEstadoId(servicioReq.estadoId);

        // Comenzamos on la seccion 2) validar servicio ,si no es todo ok q devuleva un
        // 400
        ServicioValidacionEnum servicioVResultado;
        servicioVResultado = servicioService.validarServicio(servicio);

        if (servicioVResultado != ServicioValidacionEnum.OK) {
            genericResponse.isOk = false;
            genericResponse.message = "Hubo un error en la validacion del servicio" + servicioVResultado;

            return ResponseEntity.badRequest().body(genericResponse); // Error http 400
        }
        // Comenzamos con la 3) crear en la base de datos
        servicioService.crearServicio(servicio);

        if (servicio.getServicioId() == null) {
            genericResponse.isOk = false;
            genericResponse.message = "No se pudo crear el servicio";

            return ResponseEntity.badRequest().body(genericResponse);

        } else {
            genericResponse.isOk = true;
            genericResponse.id = servicio.getServicioId();
            genericResponse.message = "Se creó el servicio éxitosamente.";

            return ResponseEntity.ok(genericResponse);
        }

    }
    /*
     * 7) Obtener servicios: todos devuelven el formato json Lista de Servicio GET
     * /api/servicios : obtiene todos los servicios. GET /api/servicios?empresa=999
     * : obtiene todos los servicios PENDIENTES de una empresa especifica Formato
     * JSon Esperado: List<Servicio> GET /api/servicios?empresa=999&deudor=888:
     * obtiene todos los servicios PENDIENTES de una empresa y un deudor Formato
     * JSon Esperado: List<Servicio> GET
     * /api/servicios?empresa=999&deudor=888&historico=true: obtiene todos los
     * servicios de una empresa y un deudor(pagados, anulados o pendientes, o sea
     * todos) GET /api/servicios?codigo=AAAAAAA : obtiene un servicio en particular
     * usando el codigo de Barras.
     */

    // LISTA los que son de la empresa y son pendientes.
    @GetMapping("/api/servicios")
    public ResponseEntity<List<Servicio>> listarServicios(
            @RequestParam(name = "empresa", required = false) Integer empresa,
            @RequestParam(name = "deudor", required = false) Integer deudor,
            @RequestParam(name = "historico", required = false) boolean historico,
            @RequestParam(name = "codigoBarras", required = false) String codigoBarras) {
        List<Servicio> servicios = new ArrayList<>();

        if (empresa == null)
            servicios = servicioService.listarServicios();

        else if (deudor == null) {
            // LISTA los que son de la empresa y son pendientes.
            servicios = servicioService.listarServiciosPendientesPorEmpresaId(empresa);

        } else if (!historico) {
            servicios = servicioService.listarServiciosPendientesPorEmpresaIdDeudorId(empresa, deudor);

        } else if (historico) {
            servicios = servicioService.listarHistoricoPorEmpresaIdDeudorId(empresa, deudor);

        } else {
            servicios = servicioService.listarDeudorIdPorCodigoBarras(codigoBarras);
        }

        return ResponseEntity.ok(servicios);
    }
    /*
     * OTRA FORMA if(codigo != null){ return
     * ResponseEntity.ok(servicioService.listarPorCodigoBarras(codigo)); if(empresa
     * == null){ return ResponseEntity.ok(servicioService.listarServicios());
     * if(deudor == null){ return
     * ResponseEntity.ok(servicioService.listarServiciosPendientesPorEmpresaId(
     * empresa)); if(historico){ return
     * ResponseEntity.ok(servicioService.historicoPorEmpresaIdYDeudorId(empresa,
     * deudor)); } return
     * ResponseEntity.ok(servicioService.PendientesPorEmpresaIdYDeudorId(empresa,
     * deudor));
     * 
     * return ResponseEntity.ok(servicios); }
     */
    // Version Ailin
    /*
     * Integer empresaId = empresa; Integer deudorId = deudor; String codigoBarras =
     * codigo; if(codigoBarras != null){ return
     * ResponseEntity.ok(servicioService.listarPorCodigoBarras(codigoBarras)); }else
     * if(empresaId == null){ return
     * ResponseEntity.ok(servicioService.listarServicios()); }else if(deudorId ==
     * null){ return
     * ResponseEntity.ok(servicioService.listarServiciosPendientesPorEmpresaId(
     * empresaId)); }else if(historico){ return
     * ResponseEntity.ok(servicioService.historicoPorEmpresaIdYDeudorId(empresaId,
     * deudorId)); } return
     * ResponseEntity.ok(servicioService.PendientesPorEmpresaIdYDeudorId(empresaId,
     * deudorId));
     */
        //Version Alexmary
        // Version Alexmary
        /*if (codigo != null) {
            servicios = servicioService.listarPorCodigoBarras(codigo);
        } else if (empresa != null && deudor == null) {
            servicios = servicioService.listarServiciosPendientesPorEmpresaId(empresa);
        } else if (empresa != null && deudor != null && !historico) {
            servicios = servicioService.PendientesPorEmpresaIdYDeudorId(empresa, deudor);
        } else if (empresa != null && deudor != null && historico) {
            servicios = servicioService.historicoPorEmpresaIdYDeudorId(empresa, deudor);
        } else {
            servicios = servicioService.listarServicios();
        }*/

    @PostMapping("/api/servicios/{id}")
    public ResponseEntity<GenericResponse> pagarServicio(@PathVariable Integer id, @RequestBody InfoPagoRequest pago) {

        GenericResponse r = new GenericResponse();

        // To do:
        // buscar servicio por id
        // verficar que este pendiente de pago
        // instanciar pago
        // validar si se esta pagando el total o solo una parte
        // si se se pago pasarlo de PENDIENTE a PAGADO (al Servicio)
        // mandar mail si tenemos la info(SI tuviesemos info del que paga)
        // grabarlo en la db
        // hacer el metodo para hacer el pago,
        // todo lo de arriba va en "realizarPago"
        OperacionPago pagoResult = servicioService.realizarPago(id, pago.importePagado, pago.moneda, pago.fechaPago,
                pago.medioPago, pago.infoMedioPago);

        // En este caso en forma generica y con pocas ganas de hacer algo particular.
        /*
         * if (pagoResult.getResultado() != OperacionPagoEnum.REALIZADO) { r.isOk =
         * false; r.message = "El pago fue rechazado, motivo : " +
         * pagoResult.getResultado(); return ResponseEntity.badRequest().body(r); } else
         * { r.isOk = true; r.id = pagoResult.getPago().getPagoId(); r.message =
         * "se realizo el pago con exito"; return ResponseEntity.ok(r); }
         */

        // En este caso customizo las respuestas
        switch(pagoResult.getResultado()) {
            case RECHAZADO_NO_ACEPTA_PAGO_PARCIAL:

                r.isOk = false;
                r.message = "No acepta pago parcial";

                return ResponseEntity.badRequest().body(r);

            case RECHAZADO_SERVICIO_INEXISTENTE:

                r.isOk = false;
                r.message = "Servicio inexistente";

                return ResponseEntity.badRequest().body(r);

            case RECHAZADO_SERVICIO_YA_PAGO:

                r.isOk = false;
                r.message = "Servicio ya pago";

                return ResponseEntity.badRequest().body(r);

            case ERROR_INESPERADO:

                r.isOk = false;
                r.message = "Error inesperado";

                return ResponseEntity.badRequest().body(r);

            case REALIZADO:

                r.isOk = true;
                r.id = pagoResult.getPago().getPagoId();
                r.message = "se realizo el pago con exito";
                return ResponseEntity.ok(r);
        }
        // error sin nada.
        return ResponseEntity.badRequest().build();
    }

    // UNA CREACION ES DIFERENTE
    // @PostMapping("/api/pagos")
    // public ResponseEntity<GenericResponse> crearPago(@RequestBody InfoPagoRequest
    // pago){

    // GenericResponse r = new GenericResponse();

    // // To do: instanciarlo
    // // grabarlo en la db a traves del service.

    // return ResponseEntity.ok(r);
    // }
    @PutMapping("/api/servicios/{id}")
    public ResponseEntity<GenericResponse> actualizarServicio(@PathVariable Integer id,
            @RequestBody ActualizarServicioRequest actualizarS) {
        // Buscar el servicio
        // actualizar propiedades
        // grabarlo

        //    GenericResponse r = new GenericResponse();
        Servicio servicio = servicioService.buscarServicioPorId(id);
        
        //    // To do: instanciarlo
        //    // grabarlo en la db a traves del service.
        servicio.setImporte(actualizarS.importe);
        servicio.setFechaVencimiento(actualizarS.vencimiento);
         
        servicioService.grabar(servicio);

        //    return ResponseEntity.ok(r);
        //}
        GenericResponse response = new GenericResponse();
        response.isOk = true;
        response.message = "Servicio actualizado!";
        response.id = servicio.getServicioId();

        return ResponseEntity.ok(response);
    }
}