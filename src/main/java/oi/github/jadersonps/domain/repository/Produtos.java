package oi.github.jadersonps.domain.repository;

import oi.github.jadersonps.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {

}
