package com.offves.stream;

import com.offves.stream.event.Event;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

public interface EventStream<T extends Event> {

    default boolean out(T event) {
        return false;
    }

    default void sink(T event) {

    }

    default Message<T> buildMessage(T event) {
        return MessageBuilder
                .withPayload(event)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
    }

}
