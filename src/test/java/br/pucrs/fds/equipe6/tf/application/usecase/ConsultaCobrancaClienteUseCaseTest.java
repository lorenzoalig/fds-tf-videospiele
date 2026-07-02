package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.*;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import br.pucrs.fds.equipe6.tf.domain.service.CalculaCobrancaClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConsultaCobrancaClienteUseCaseTest {

    private static final String CPF = "111.111.111-11";

    // repo falso
    static class ContratoRepositorioFalso implements IContratoRepository {
        private final List<Contrato> contratos = new ArrayList<>();

        void adicionar(Contrato c) { contratos.add(c); }

        @Override public Contrato save(Contrato contrato) { contratos.add(contrato); return contrato; }
        @Override public Contrato findById(Integer id) { return null; }
        @Override public List<Contrato> findAll() { return contratos; }
        @Override public List<Contrato> findByJogo(Jogo jogo) { return new ArrayList<>(); }
        @Override public List<Contrato> findByClienteCPF(String cpf) {
            return contratos.stream().filter(c -> c.getCliente().getCPF().equals(cpf)).toList();
        }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int id) { return false; }
    }

    private ContratoRepositorioFalso contratoRepository;
    private ConsultaCobrancaClienteUseCase useCase;

    @BeforeEach
    void setUp() {
        contratoRepository = new ContratoRepositorioFalso();
        CalculaCobrancaClienteService service = new CalculaCobrancaClienteService(contratoRepository);
        useCase = new ConsultaCobrancaClienteUseCase(service);
    }

    @Test
    void deveRetornarZeroQuandoClienteNaoTemContratos() {
        assertThat(useCase.executar(CPF)).isZero();
    }

    @Test
    void deveRetornarCobrancaCalculadaDoCliente() {
        Categoria categoria = new Categoria(1, "Ação", 10.0);
        Moeda moeda = new Moeda(1, "Real", "R$", 1.0);
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.5, categoria, moeda);
        Cliente cliente = new Cliente(1L, "L", CPF, "l@t.com", null, "s");
        Contrato contrato = new Contrato(1, new Date(), 30, cliente, jogo);

        Date data = new Date();
        contrato.addUso(new Uso(1, data, data, 10, 12)); // 120 min -> 10 + 60 = 70

        contratoRepository.adicionar(contrato);

        assertThat(useCase.executar(CPF)).isEqualTo(70.0);
    }
}
