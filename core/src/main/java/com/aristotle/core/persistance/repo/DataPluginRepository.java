package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.DataPlugin;

public interface DataPluginRepository extends JpaRepository<DataPlugin, Long> {

    @Query("select distinct dp from DataPlugin dp, UrlMapping up join up.urlMappingPlugins ump where  up.id=?1 and ump.dataPluginId=dp.id")
    List<DataPlugin> getDataPluginOfUrlMapping(Long urlMappingId);

    @Query("select dp from DataPlugin dp where dp.global=true")
    List<DataPlugin> getAllGlobalDataPlugins();

}
