package org.bobpark.article_read.domain.article.event.handler;

import org.bobpark.common.event.Event;
import org.bobpark.common.event.EventPayload;

public interface EventHandler<T extends EventPayload> {

    void handle(Event<T> event);

    boolean supports(Event<T> event);

}
