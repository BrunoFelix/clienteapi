package br.com.compasso.uol.cliente.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.compasso.uol.cliente.model.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	/**
	 * Buscar clientes pelo campo Nome, @query não precisava ser usado nesse caso, pois não é uma query complexa. 
	 * @param String nome
	 * @return Lista do objeto Cliente
	 */
	@Query("select c from Cliente c where c.nome = :nome")
	public List<Cliente> findByNome(@Param("nome") String nome);
}
