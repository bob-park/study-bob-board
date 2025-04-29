package org.bobpark.hot_article.domain.hotarticle.service.event;

import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventPayload;

public interface EventHandler<T extends EventPayload> {

    void handle(Event<T> event);

    boolean supports(Event<T> event);

    Long findArticleId(Event<T> event);

}
