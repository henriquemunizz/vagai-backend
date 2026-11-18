package br.com.fiap.vagai.bo;

import br.com.fiap.vagai.dao.EstacionamentoDao;
import br.com.fiap.vagai.model.Estacionamento;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;

@Component
public class EstacionamentoOsmBo {
    private final EstacionamentoDao locais;

    public EstacionamentoOsmBo(EstacionamentoDao locais) {
        this.locais = locais;
    }

    @Transactional(rollbackFor = SQLException.class)
    public void salvar(Estacionamento importado) throws SQLException {
        var existentes = locais.porOsm(importado.getIdOsm());
        if (existentes.isEmpty()) {
            locais.cadastrar(importado);
            return;
        }
        Estacionamento atual = existentes.getFirst();
        if (atual.getIdUsuarioCriador() == null && "OSM".equals(atual.getFonte())) {
            importado.setId(atual.getId());
            locais.atualizar(importado);
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public void suspender(String chave) throws SQLException {
        for (Estacionamento atual : locais.porOsm(chave)) {
            if (atual.getIdUsuarioCriador() == null && "OSM".equals(atual.getFonte())) {
                atual.setAtivo(0);
                locais.atualizar(atual);
            }
        }
    }
}
