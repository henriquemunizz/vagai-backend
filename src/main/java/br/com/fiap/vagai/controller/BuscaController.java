package br.com.fiap.vagai.controller;

import br.com.fiap.vagai.dto.ProximoSaida;
import br.com.fiap.vagai.service.BuscaGeografica;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
public class BuscaController {
    private final BuscaGeografica busca;

    public BuscaController(BuscaGeografica busca) {
        this.busca = busca;
    }

    @GetMapping("/estacionamentos/proximos")
    public List<ProximoSaida> proximos(@RequestParam double latitude, @RequestParam double longitude,
            @RequestParam double raioKm, @RequestParam(required = false) String tipo) throws SQLException {
        return busca.buscar(latitude, longitude, raioKm, tipo);
    }
}
