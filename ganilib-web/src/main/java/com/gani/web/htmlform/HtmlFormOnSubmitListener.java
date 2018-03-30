package com.gani.web.htmlform;

import java.io.Serializable;

public interface HtmlFormOnSubmitListener extends Serializable {
    void onSubmit(HtmlForm form);
    void afterBuild(HtmlForm form);
}
