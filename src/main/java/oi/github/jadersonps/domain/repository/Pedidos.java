package oi.github.jadersonps.domain.repository;

import oi.github.jadersonps.domain.entity.Cliente;
import oi.github.jadersonps.domain.entity.Pedido;
import oi.github.jadersonps.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface Pedidos extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByCliente(Cliente cliente);

    @Query(" select p from Pedido p left join fetch p.itens where p.id = :id ")
    Optional<Pedido> findByIdFetchItens(@PathVariable("id") Integer id);
}
