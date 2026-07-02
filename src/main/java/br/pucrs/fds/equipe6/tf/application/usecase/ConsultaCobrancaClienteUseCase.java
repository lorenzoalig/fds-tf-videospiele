package br.pucrs.fds.equipe6.tf.application.usecase;


import br.pucrs.fds.equipe6.tf.domain.entity.Contrato;
import br.pucrs.fds.equipe6.tf.domain.entity.Uso;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import br.pucrs.fds.equipe6.tf.domain.service.CalculaCobrancaClienteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaCobrancaClienteUseCase {

    private final CalculaCobrancaClienteService service;

    public ConsultaCobrancaClienteUseCase(CalculaCobrancaClienteService service) {
        this.service = service;
    }

    public double executar(String cpf) {
        return service.calculaCobrancaByCpf(cpf);
    }
}