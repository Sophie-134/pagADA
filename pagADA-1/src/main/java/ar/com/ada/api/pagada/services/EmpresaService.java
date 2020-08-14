package ar.com.ada.api.pagada.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import ar.com.ada.api.pagada.entities.Empresa;
import ar.com.ada.api.pagada.repos.EmpresaRepository;

@Service
public class EmpresaService {

    @Autowired
    EmpresaRepository empresaRepository;

    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    public void crearEmpresa(Empresa emp) {

        empresaRepository.save(emp);
    }

    public EmpresaValidacionEnum validarEmpresa(Empresa emp) {

        // si es nulo, error
        if (emp.getIdImpositivo() == null)
            return EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO;

        // ID impositivo al menos de 11 digitos y maximo 20
        if (!(emp.getIdImpositivo().length() >= 11 && emp.getIdImpositivo().length() <= 20))
            return EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO;
        // 1er forma: rrecorrer foreach
        // 2da forma: transformar el string a un array de chara y recorrer el array
        // 3ra forma: recorrer el string utilizando charIndex(posicion)
        // 4ta forma: stream

        String idImpositivo = emp.getIdImpositivo();
        // 1forma: "A3939393"
        for (char caracter : idImpositivo.toCharArray()) {
            if (!Character.isDigit(caracter))
                return EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO;

        }
        // 2 forma: usando for i
        char[] idImpositivoComoArray = idImpositivo.toCharArray();
        for (int i = 0; i < idImpositivoComoArray.length; i++) {
            if (!Character.isDigit(idImpositivoComoArray[i])) {
                return EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO;
            }
        }
        // 3 forma: un for i en el string usando char index
        for (int i = 0; i < idImpositivo.length(); i++) {
            if (!Character.isDigit(idImpositivo.charAt(i))) {
                return EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO;
            }
        }

        // 4Forma: usando stream
        // idImpositivo.chars().filter(caracter -> condicion) te devuelve aqeullos que
        // cumplan la condicion
        // !Character.isDigit(c) : significa que NO sea un digito
        // con el count() nos devuelve la cantidad de items.
        if (idImpositivo.chars().filter(c -> !Character.isDigit(c)).count() > 0)
            return EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO;

        if (emp.getNombre() == null)
            return EmpresaValidacionEnum.NOMBRE_INVALIDO;

        if (emp.getNombre().length() > 100)
            return EmpresaValidacionEnum.NOMBRE_INVALIDO;

        // Si llego hassta aqui, es que todo lo de arriba, era valido
        return EmpresaValidacionEnum.OK;
    }

    public enum EmpresaValidacionEnum {
        OK, // cuando esta todo validado ok
        NOMBRE_INVALIDO, // nombre tenga algun problema
        ID_IMPOSITIVO_INVALIDO// IdImpositivo tanga un problema
    }
    public Empresa buscarEmpresaPorId(Integer empresaId) {
        // en este caso para reusar el findById que no devuelva optional
        // tenemos qeu castear el Integer a int
        return empresaRepository.findById((int) empresaId);
    }
}