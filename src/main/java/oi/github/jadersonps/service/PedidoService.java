package oi.github.jadersonps.service;


import oi.github.jadersonps.domain.entity.Pedido;
import oi.github.jadersonps.domain.enums.StatusPedido;
import oi.github.jadersonps.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
