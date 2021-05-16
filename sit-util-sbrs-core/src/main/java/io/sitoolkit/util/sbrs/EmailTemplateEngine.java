package io.sitoolkit.util.sbrs;

import java.util.Locale;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplateEngine {
  private static TemplateEngine templateEngine;

  static {
    templateEngine = new TemplateEngine();
    templateEngine.addTemplateResolver(generateTemplateResolver());
  }

  private static ITemplateResolver generateTemplateResolver() {

    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("/");
    templateResolver.setCharacterEncoding("utf-8");
    templateResolver.setCacheable(false);

    return templateResolver;
  }

  public static String generate(String template, Map<String, Object> variables) {
    Context ctx = new Context(Locale.getDefault(), variables);
    return templateEngine.process(template, ctx);
  }
}
