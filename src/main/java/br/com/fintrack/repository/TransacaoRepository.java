package br.com.fintrack.repository;

import br.com.fintrack.enums.TipoTransacao;
import br.com.fintrack.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByTipo(TipoTransacao tipo);

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.tipo = :tipo")
    BigDecimal somaPorTipo(@Param("tipo") TipoTransacao tipo);



    @Query("""
    SELECT COALESCE(SUM(t.valor), 0)
    FROM Transacao t
    WHERE t.tipo = :tipo
      AND t.data BETWEEN :inicio AND :fim
    """)
    BigDecimal somaPorTipoNoPeriodo(
            @Param("tipo") TipoTransacao tipo,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("""
    SELECT t.categoria, SUM(t.valor)
    FROM Transacao t
    WHERE t.tipo = :tipo
    GROUP BY t.categoria
    ORDER BY t.categoria
    """)
    List<Object[]> somarPorCategoria(TipoTransacao tipo);



    List<Transacao> findByDataBetween(
            LocalDate inicio,
            LocalDate fim
    );

    List<Transacao> findAllByOrderByDataDesc();

    Page<Transacao> findAll(Pageable pageable);
}
