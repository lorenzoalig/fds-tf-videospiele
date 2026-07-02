package br.pucrs.fds.equipe6.trab1;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.pucrs.fds.equipe6.trab1.repository.ClienteRepository;
import br.pucrs.fds.equipe6.trab1.repository.JogoRepository;
import br.pucrs.fds.equipe6.trab1.repository.ContratoRepository;
import br.pucrs.fds.equipe6.trab1.repository.CategoriaRepository;
import br.pucrs.fds.equipe6.trab1.repository.FormaPagamentoRepository;

import br.pucrs.fds.equipe6.trab1.usecase.ConsultarJogosPorSituacaoUseCase;
import br.pucrs.fds.equipe6.trab1.usecase.UploadCategoriasService;
import br.pucrs.fds.equipe6.trab1.usecase.AtualizarSituacaoJogosService;
import br.pucrs.fds.equipe6.trab1.usecase.ConsultaCobrancaClienteUseCase;
import br.pucrs.fds.equipe6.trab1.usecase.CalculaValorContratoUseCase;
import br.pucrs.fds.equipe6.trab1.usecase.AtualizaSituacaoJogoUseCase;
import br.pucrs.fds.equipe6.trab1.usecase.UploadClientesService;
import br.pucrs.fds.equipe6.trab1.usecase.UploadContratosService;
import br.pucrs.fds.equipe6.trab1.usecase.UploadFormasPagamentoService;
import br.pucrs.fds.equipe6.trab1.usecase.UploadJogosService;
import br.pucrs.fds.equipe6.trab1.usecase.UploadMoedasService;
import br.pucrs.fds.equipe6.trab1.usecase.UploadUsosService;


@RestController
@RequestMapping("/acmespiele")
public class Controller {

    //REPOSITORY
    private final ClienteRepository clienteRepository;
    private final JogoRepository jogoRepository;
    private final ContratoRepository contratoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FormaPagamentoRepository formaPagamentoRepository;

    //USECASES
    private final CalculaValorContratoUseCase calculaValorContratoUseCase;
    private final ConsultarJogosPorSituacaoUseCase consultarJogosPorSituacaoUseCase;
    private final AtualizaSituacaoJogoUseCase atualizarSituacaoJogoUseCase;
    private final ConsultaCobrancaClienteUseCase consultaCobrancaClienteUseCase;

    // UPLOADS
    private final UploadClientesService uploadClientesService;
    private final UploadJogosService uploadJogosService;
    private final UploadContratosService uploadContratosService;
    private final UploadMoedasService uploadMoedasService;
    private final UploadUsosService uploadUsosService;
    private final UploadFormasPagamentoService uploadFormasPagamentoService;
    private final UploadCategoriasService uploadCategoriasService;


    public Controller(

        ClienteRepository clienteRepository,
        JogoRepository jogoRepository,
        ContratoRepository contratoRepository,
        CategoriaRepository categoriaRepository,
        FormaPagamentoRepository formaPagamentoRepository,


        ConsultarJogosPorSituacaoUseCase consultarJogosPorSituacaoUseCase,
        AtualizaSituacaoJogoUseCase atualizarSituacaoJogoUseCase,
        CalculaValorContratoUseCase calculaValorContratoUseCase,
        ConsultaCobrancaClienteUseCase consultaCobrancaClienteUseCase,
        UploadClientesService uploadClientesService,
        UploadJogosService uploadJogosService,
        UploadContratosService uploadContratosService,
        UploadMoedasService uploadMoedasService,
        UploadUsosService uploadUsosService,
        UploadFormasPagamentoService uploadFormasPagamentoService,
        UploadCategoriasService uploadCategoriasService
    ){
        this.clienteRepository = clienteRepository;
        this.jogoRepository = jogoRepository;
        this.contratoRepository = contratoRepository;
        this.categoriaRepository = categoriaRepository;
        this.formaPagamentoRepository = formaPagamentoRepository;

        this.consultarJogosPorSituacaoUseCase = consultarJogosPorSituacaoUseCase;
        this.atualizarSituacaoJogoUseCase = atualizarSituacaoJogoUseCase;
        this.calculaValorContratoUseCase = calculaValorContratoUseCase;
        this.consultaCobrancaClienteUseCase = consultaCobrancaClienteUseCase;
        this. uploadClientesService = uploadClientesService;
        this.uploadJogosService = uploadJogosService;
        this.uploadContratosService = uploadContratosService;
        this.uploadMoedasService = uploadMoedasService;
        this.uploadUsosService = uploadUsosService;
        this.uploadFormasPagamentoService = uploadFormasPagamentoService;
        this.uploadCategoriasService = uploadCategoriasService;

    }

    //endpoint 0: Upload de arquivos CSV
    @PostMapping("/upload/clientes")
    public ResponseEntity<Boolean> uploadClientes(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(uploadClientesService.executar(file));
    }

    @PostMapping("/upload/jogos")
    public ResponseEntity<Boolean> uploadJogos(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(uploadJogosService.executar(file));
    }

    @PostMapping("/upload/contratos")
    public ResponseEntity<Boolean> uploadContratos(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(uploadContratosService.executar(file));
    }

    @PostMapping("/upload/moedas")
    public ResponseEntity<Boolean> uploadMoedas(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(uploadMoedasService.executar(file));
    }

    @PostMapping("/upload/usos")
    public ResponseEntity<Boolean> uploadUsos(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(uploadUsosService.executar(file));
    }

    @PostMapping("/upload/formaspagamento")
    public ResponseEntity<Boolean> uploadFormasPagamento(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(uploadFormasPagamentoService.executar(file));
    }

    @PostMapping("/upload/categorias")
    public ResponseEntity<Boolean> uploadCategorias(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(uploadCategoriasService.executar(file));
    }


    // endpoint 1: Consultar todos os clientes
    @GetMapping("/consulta/listaclientes")
    public List<Cliente> getClientes() {
        return clienteRepository.findAll();
    }

    // endpoint 2: Consultar todos os jogos cadastrados
    @GetMapping("/consulta/listajogos")
    public List<Jogo> getJogos() {
        return jogoRepository.findAll();
    }

    // endpoint 3: Consultar todas as formas de pagamento cadastradas
    // não existia no T1 (novo)
    @GetMapping("/consulta/listaformaspagamentos")
    public List<FormaPagamento> getFormasPagamento() {
        return formaPagamentoRepository.findAll();
    }

    // endpoint 5: Consultar jogos por situação
    @GetMapping("/consulta/jogossituacao/{situacao}")

    public List<Jogo> consultaJogoPorSituacao(@PathVariable String situacao) {
        return consultarJogosPorSituacaoUseCase.executar(situacao);
    }

    // endpoint 6: Cadastrar novo contrato
    // agora recebe FormasPagamento e valida forma de pagamento + jogo exclusivo (tf)
    @PostMapping("/cadastro/cadcontrato")
    public List<Contrato> getContratosPorCpf(@PathVariable String cpf) {
        return contratoRepository.findByCliente_CPF(cpf);
    }

    // endpoint 7: Cadastrar novo uso de contrato

    // endpoint 8: Calcular valor total de um contrato
    @GetMapping("/financeiro/consultatotalcontrato")
    public double calculaValorContrato(@RequestParam int id) {
        return calculaValorContratoUseCase.executar(id);
    }

    // endpoint 9: Calcular cobrança total de um cliente
    @GetMapping("/financeiro/consultatotalcliente")
    public double consultaCobrancaPorCpf(@RequestParam String cpf) {
        return consultaCobrancaClienteUseCase.executar(cpf);
    }

    // endpoint 10: Alterar situação de um jogo
    @PutMapping("/cadastro/atualizajogo/{codigo}/situacao/{status}")
    public ResponseEntity<Jogo> alteraSituacaoJogo(@PathVariable int codigo, @PathVariable String status) {
       return atualizaSituacaoJogoUseCase.executar(codigo.status);
    }

    // endpoint 11: Cancelar contrato logicamente

}

