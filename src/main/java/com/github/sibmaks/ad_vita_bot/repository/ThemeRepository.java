package com.github.sibmaks.ad_vita_bot.repository;

import com.github.sibmaks.ad_vita_bot.entity.Theme;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ThemeRepository extends CrudRepository<Theme, Long> {

    List<Theme> getAllBy();

}
