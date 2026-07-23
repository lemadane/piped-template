package io.lemadane.piped.template.engine.spring;

import io.lemadane.piped.template.engine.TemplateEngine;
import io.lemadane.piped.template.engine.compiler.CompiledTemplate;
import io.lemadane.piped.template.engine.expression.TemplateContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.AbstractTemplateView;

import java.util.HashMap;
import java.util.Map;

public class PipedTemplateView extends AbstractTemplateView {

    private TemplateEngine templateEngine;

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    protected void renderMergedTemplateModel(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String viewName = getUrl();
        CompiledTemplate compiled = templateEngine.compileTemplate(viewName);
        Map<String, Object> metadata = compiled.getMetadata();

        // Prevent conflicts: only add "page" if not already present
        if (!model.containsKey("page")) {
            model.put("page", new PipedPageContext(request));
        }

        // Prevent conflicts: only add "title" from page metadata if not already present in the model
        if (metadata.containsKey("title") && !model.containsKey("title")) {
            model.put("title", metadata.get("title"));
        }

        // Call the templateEngine render method directly to support layouts and section yields
        String html = templateEngine.render(viewName, model);

        response.setContentType(getContentType());
        response.getWriter().write(html);
    }
}
