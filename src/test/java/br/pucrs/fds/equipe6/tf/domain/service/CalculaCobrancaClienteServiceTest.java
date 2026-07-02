package br.pucrs.fds.equipe6.tf.domain.service;

import br.pucrs.fds.equipe6.tf.domain.entity.*;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class CalculaCobrancaClienteServiceTest {

    // repositório falso para nao precisar do banco
    static class ContratoRepositorioFalso implements IContratoRepository {
        private final List<Contrato> contratos = new ArrayList<>();

        void adicionar(Contrato c) { contratos.add(c); }

        @Override public Contrato save(Contrato contrato) { contratos.add(contrato); return contrato; }
        @Override public Contrato findById(Integer id) {
            return contratos.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        }
        @Override public List<Contrato> findAll() { return contratos; }
        @Override public List<Contrato> findByJogo(Jogo jogo) { return new ArrayList<>(); }
        @Override public List<Contrato> findByClienteCPF(String cpf) {
            return contratos.stream().filter(c -> c.getCliente().getCPF().equals(cpf)).toList();
        }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int id) { return false; }
    }

    private ContratoRepositorioFalso contratoRepository;
    private CalculaCobrancaClienteService service;

    private static final String CPF = "111.111.111-11";

    @BeforeEach
    void setUp() {
        contratoRepository = new ContratoRepositorioFalso();
        service = new CalculaCobrancaClienteService(contratoRepository);
    }

    private Date dataDe(int ano, int mes, int dia) {
        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes, dia, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Cliente cliente() {
        return new Cliente(1L, "Lorenzo", CPF, "l@t.com", null, "s");
    }

    @Test
    void deveRetornarZeroQuandoClienteNaoTemContratos() {
        assertThat(service.calculaCobrancaByCpf(CPF)).isZero();
    }

    @Test
    void deveCalcularCobrancaSemDescontoQuandoValorMenorOuIgualA500() {
        Categoria categoria = new Categoria(1, "Ação", 10.0);
        Moeda moeda = new Moeda(1, "Real", "R$", 1.0);
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.5, categoria, moeda);
        Contrato contrato = new Contrato(1, new Date(), 30, cliente(), jogo);

        Date data = dataDe(2026, Calendar.JANUARY, 1);
        Uso uso = new Uso(1, data, data, 10, 12); // 120 min -> 10 + 60 = 70 (<= 500, sem desconto)
        contrato.addUso(uso);

        contratoRepository.adicionar(contrato);

        assertThat(service.calculaCobrancaByCpf(CPF)).isEqualTo(70.0);
    }

    @Test
    void deveAplicarDescontoDe3PorCentoQuandoValorSuperiorA500() {
        Categoria categoria = new Categoria(1, "Ação", 10.0);
        Moeda moeda = new Moeda(1, "Dólar", "$", 5.0);
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 1.0, categoria, moeda);
        Contrato contrato = new Contrato(1, new Date(), 30, cliente(), jogo);

        // duracao grande para ultrapassar 500 em reais
        Date inicio = dataDe(2026, Calendar.JANUARY, 1);
        Date fim = dataDe(2026, Calendar.JANUARY, 2);
        Uso uso = new Uso(1, inicio, fim, 0, 0); // 24h = 1440 min

        contrato.addUso(uso);
        contratoRepository.adicionar(contrato);

        // valorEmMoeda = 10 + 1440*1 = 1450 ; valorEmReal = 1450*5 = 7250 (>500) -> *0.97
        double esperado = 7250 * 0.97;
        assertThat(service.calculaCobrancaByCpf(CPF)).isCloseTo(esperado, within(0.001));
    }

    @Test
    void deveSomarCobrancaDeMultiplosContratosEUsos() {
        Categoria categoria = new Categoria(1, "Ação", 10.0);
        Moeda moeda = new Moeda(1, "Real", "R$", 1.0);
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.5, categoria, moeda);

        Contrato contrato1 = new Contrato(1, new Date(), 30, cliente(), jogo);
        Contrato contrato2 = new Contrato(2, new Date(), 30, cliente(), jogo);

        Date data = dataDe(2026, Calendar.JANUARY, 1);
        contrato1.addUso(new Uso(1, data, data, 10, 12)); // 70
        contrato2.addUso(new Uso(2, data, data, 8, 9));   // 40

        contratoRepository.adicionar(contrato1);
        contratoRepository.adicionar(contrato2);

        assertThat(service.calculaCobrancaByCpf(CPF)).isEqualTo(110.0);
    }
}
