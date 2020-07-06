package br.com.compasso.uol.cliente.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.compasso.uol.cliente.model.entity.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>{

	/**
	 * Query para pesquisar cidade pelo nome, @query não precisava ser usado nesse caso, pois não é uma query complexa.
	 * @param String nome, String estado
	 * @return Lista do objeto Cidade
	 */
	@Query("select c from Cidade c where c.nome = :nome and c.estado = :estado")
	public List<Cidade> findByNomeAndEstado(@Param("nome") String nome, @Param("estado") String estado);
	
	/**
	 * Buscar cidade pelo campo Estado 
	 * @param String estado
	 * @return Lista do objeto Cidade
	 */
	public List<Cidade> findByEstado(String Estado);
	
	/**
	 * Buscar cidade pelo campo Nome 
	 * @param String estado
	 * @return Lista do objeto Cidade
	 */
	public List<Cidade> findByNome(String nome);
}
