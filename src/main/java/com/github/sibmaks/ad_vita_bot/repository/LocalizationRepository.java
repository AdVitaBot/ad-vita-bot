package com.github.sibmaks.ad_vita_bot.repository;

import com.github.sibmaks.ad_vita_bot.entity.LocalizationEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface LocalizationRepository extends CrudRepository<LocalizationEntity, String> {
}
