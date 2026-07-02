package br.pucrs.fds.equipe6.tf.application.usecase;


import br.pucrs.fds.equipe6.tf.domain.entity.Contrato;
import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import br.pucrs.fds.equipe6.tf.domain.entity.Situacao;

import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtualizaSituacaoJogosUseCase {

    private final IJogoRepository jogoRepository;
    private final IContratoRepository contratoRepository;

    public AtualizaSituacaoJogosUseCase(IJogoRepository jogoRepository, IContratoRepository contratoRepository) {
        this.jogoRepository = jogoRepository;
        this.contratoRepository = contratoRepository;
    }

    public void executar() {
        List<Jogo> jogos = jogoRepository.findAll();

        for (Jogo jogo : jogos) {
            if (jogo.isSituacaoManual()) continue;

            List<Contrato> contratosDoJogo = contratoRepository.findByJogo(jogo);

            if (jogo.estaRemovido(contratosDoJogo)) {
                jogo.setSituacao(Situacao.REMOVIDO);
            } else if (jogo.estaObsoleto(contratosDoJogo)) {
                jogo.setSituacao(Situacao.OBSOLETO);
            } else if (jogo.estaContratado(contratosDoJogo)) {
                jogo.setSituacao(Situacao.CONTRATADO);
            } else {
                jogo.setSituacao(Situacao.DISPONIVEL);
            }
        }

        jogoRepository.saveAll(jogos);
    }
}