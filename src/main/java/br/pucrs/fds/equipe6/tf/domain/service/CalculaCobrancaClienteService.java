package br.pucrs.fds.equipe6.tf.domain.service;

import br.pucrs.fds.equipe6.tf.domain.entity.Contrato;
import br.pucrs.fds.equipe6.tf.domain.entity.Uso;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculaCobrancaClienteService {
    private final IContratoRepository contratoRepository;

    public CalculaCobrancaClienteService(IContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    public double calculaCobrancaByCpf(String cpf) {
        double valorTotal = 0;
        List<Contrato> contratosCliente = contratoRepository.findByClienteCPF(cpf);

        for (Contrato c : contratosCliente) {
            double valorBase = c.getJogo().getCategoria().getValorMinimo();
            double valorMinuto = c.getJogo().getValorMinuto();
            double taxaParaReal = c.getJogo().getMoeda().getTaxaParaReal();

            for (Uso u : c.getUsos()) {
                long minutos = u.getDuracaoMinutos();
                double valorEmMoeda = valorBase + (minutos * valorMinuto);
                double valorEmReal = valorEmMoeda * taxaParaReal;

                valorTotal += valorEmReal > 500 ? valorEmReal * 0.97 : valorEmReal;
            }
        }
        return valorTotal;
    }
}
