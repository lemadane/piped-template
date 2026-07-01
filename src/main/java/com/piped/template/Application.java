package com.piped.template;

public class Application {
   private Application() {
   }

   public static void main(String[] args) {
      var engine = new TemplateEngine();
      var template = """
            <h1>Hello Piped Template Engine</h1>
            <p>This is our first runnable demo.</p>
            """;
      var html = engine.render(template, Map.of());
      System.out.println(html);
   }
}
