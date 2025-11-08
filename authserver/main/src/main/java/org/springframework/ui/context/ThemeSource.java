package org.springframework.ui.context;


import org.springframework.lang.Nullable;

@Deprecated(since = "6.0")
public interface ThemeSource {
    @Nullable
    Theme getTheme(String paramString);
}
