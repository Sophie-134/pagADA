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
}