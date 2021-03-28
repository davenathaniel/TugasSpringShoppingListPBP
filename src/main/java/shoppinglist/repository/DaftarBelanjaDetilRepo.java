/*
 * DaftarBelanjaRepoDetil.java
 *
 * Created on Mar 26, 2021, 05.43
 */
package shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppinglist.entity.DaftarBelanjaDetil;

import javax.transaction.Transactional;


/**
 * @author dave
 */
public interface DaftarBelanjaDetilRepo extends JpaRepository<DaftarBelanjaDetil, Long>
{
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM public.daftarbelanjadetil WHERE daftarbelanja_id = :id", nativeQuery = true)
    void deleteByDaftarBelanja_id(@Param("id") long daftarbelanja_id);
}