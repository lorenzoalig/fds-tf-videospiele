package br.pucrs.fds.equipe6.trab1.usecase;

import br.pucrs.fds.equipe6.trab1.Jogo;
import br.pucrs.fds.equipe6.trab1.Situacao;
import br.pucrs.fds.equipe6.trab1.repository.JogoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AtualizaSituacaoJogoUseCase {

    private final JogoRepository jogoRepository;

    public AtualizaSituacaoJogoUseCase(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    public Optional<Jogo> executar(int codigo, String status) {
        Jogo jogo = jogoRepository.findById(codigo).orElse(null);
        if (jogo == null) return Optional.empty();

        Situacao situacao = Situacao.buscaPorNome(status);
        if (situacao == null) return Optional.empty();

        jogo.setSituacao(situacao);
        jogo.setSituacaoManual(true);

        return Optional.of(jogoRepository.save(jogo));
    }
}