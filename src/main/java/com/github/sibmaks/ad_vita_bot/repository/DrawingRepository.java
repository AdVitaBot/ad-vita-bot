package com.github.sibmaks.ad_vita_bot.repository;

import com.github.sibmaks.ad_vita_bot.entity.Drawing;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DrawingRepository extends CrudRepository<Drawing, Long> {

    @Query(value = "select d.* from {h-schema}\"drawing\" d " +
            "where d.theme_id = :themeId " +
            "order by (" +
            "select count(*) from {h-schema}\"donation\" dn " +
            "where dn.participant_id = :participantId and dn.drawing_id = d.id  and status = 'SUCCESSFUL'" +
            ") limit 1",
            nativeQuery = true
    )
    Drawing findLeastUsed(@Param("themeId") long themeId,
                          @Param("participantId") long participantId);

}
