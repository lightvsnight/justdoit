package org.springframework.ui.context;

import org.springframework.context.MessageSource;

@Deprecated(since = "6.0")
public interface Theme {
    String getName();

    MessageSource getMessageSource();
}

