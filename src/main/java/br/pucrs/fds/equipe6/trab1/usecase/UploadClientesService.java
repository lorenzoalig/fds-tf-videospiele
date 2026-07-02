package br.pucrs.fds.equipe6.trab1.usecase;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import br.pucrs.fds.equipe6.trab1.Cliente;
import br.pucrs.fds.equipe6.trab1.repository.ClienteRepository;

@Service
public class UploadClientesService {

    private final ClienteRepository clienteRepository;

    public UploadClientesService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public boolean executar(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String line;
            boolean first = true;

            while ((line = reader.readLine()) != null) {

                if (first) { // pula header
                    first = false;
                    continue;
                }

                String[] data = line.split(";");

                Cliente cliente = new Cliente();
                cliente.setCod(Long.parseLong(data[0]));
                cliente.setNome(data[1]);
                cliente.setCPF(data[2]);

                clienteRepository.save(cliente);
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}