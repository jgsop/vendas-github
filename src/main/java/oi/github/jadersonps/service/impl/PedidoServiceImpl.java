package oi.github.jadersonps.service.impl;

import lombok.RequiredArgsConstructor;
import oi.github.jadersonps.domain.entity.Cliente;
import oi.github.jadersonps.domain.entity.ItemPedido;
import oi.github.jadersonps.domain.entity.Pedido;
import oi.github.jadersonps.domain.entity.Produto;
import oi.github.jadersonps.domain.enums.StatusPedido;
import oi.github.jadersonps.domain.repository.Clientes;
import oi.github.jadersonps.domain.repository.ItemsPedido;
import oi.github.jadersonps.domain.repository.Pedidos;
import oi.github.jadersonps.domain.repository.Produtos;
import oi.github.jadersonps.exception.PedidoNaoEncontradoException;
import oi.github.jadersonps.exception.RegraNegociosExeption;
import oi.github.jadersonps.rest.dto.ItemPedidoDTO;
import oi.github.jadersonps.rest.dto.PedidoDTO;
import oi.github.jadersonps.service.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemsPedido itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(()-> new RegraNegociosExeption("Código de Cliente inválido."));


        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus( Integer id, StatusPedido statusPedido ) {
        repository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow(()-> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegociosExeption("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(()->
                                    new RegraNegociosExeption("Código de Produto inválido: " + idProduto));
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setQuantidade(dto.getQuantidade());
                itemPedido.setPedido(pedido);
                itemPedido.setProduto(produto);
                return itemPedido;
        }).collect(Collectors.toList());
    }
}
