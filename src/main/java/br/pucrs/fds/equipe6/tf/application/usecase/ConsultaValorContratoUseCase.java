package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.Contrato;
import br.pucrs.fds.equipe6.tf.domain.entity.Uso;

import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import br.pucrs.fds.equipe6.tf.domain.service.CalculaValorContratoService;
import org.springframework.stereotype.Service;

@Service
public class ConsultaValorContratoUseCase {
    private final CalculaValorContratoService service;

    public ConsultaValorContratoUseCase(CalculaValorContratoService service) {
        this.service = service;
    }

    public double executar(int id) {
        return service.calculaValorById(id);
    }
}