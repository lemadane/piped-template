# Piped Template Engine (PTE)

[![JitPack](https://jitpack.io/v/lemadane/piped-template-engine.svg)](https://jitpack.io/#lemadane/piped-template-engine)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Java 17+](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)

**Piped Template Engine (PTE)** is a modern, light-weight, and ultra-high-performance server-side HTML template engine for Java and Spring Boot. It uses a unique pipe-based syntax (`|var|`) designed to keep HTML code readable, natural, and free of clutter.

```html
<h1>|title|</h1>
<p>Hello, |user?.profile?.displayName ?? 'Guest'|</p>
```

---

## ⚡ Why Use Piped Template Engine?

1. **🚀 Native Bytecode Performance**: PTE transpiles template AST trees into Java source and compiles them **in-memory** to live `.class` JVM bytecode. It runs at native JVM speed, matching **JTE** and executing **3x–8x faster than Thymeleaf** with zero disk I/O.
2. **📁 SvelteKit-Style Routing**: Stop writing boilerplate Java `@Controller` mappings just to load static pages. PTE registers routes automatically from your directory structure.
3. **🔌 Built for HTMX**: Render specific page zones dynamically using inline **Fragments**, compile clean **Target DOM IDs** using the slug filter, and perform out-of-band updates with zero friction.
4. **🛡️ Secure by Default**: Automatically escapes all variables to defend against Cross-Site Scripting (XSS) attacks.

---

## 🍃 How to Use PTE in Spring Boot

### 1. Add Dependencies
Configure your `build.gradle` to fetch PTE from **JitPack**:

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Spring Boot Starter (auto-configures view mapping)
    implementation 'com.github.lemadane.piped-template-engine:piped-template-engine-spring-boot-starter:v0.1.1'
}
```

### 2. Configure Properties
Add your template location rules in `src/main/resources/application.properties`:

```properties
# Custom prefix folder (Defaults to pte-templates)
piped.template.prefix=src/main/resources/pte-templates
piped.template.suffix=.pte
```

### 3. File-Based Routing vs. Controllers
PTE lets you use both standard Spring MVC controllers and automatic file-based routes:

* **Option A: SvelteKit-Style File-Based Routing (Zero Java Code)**
  Create `src/main/resources/pte-routes/about/+page.pte`. Navigating to `/about` will render it instantly.
* **Option B: Spring MVC Controllers**
  ```java
  @Controller
  public class WebController {
      @GetMapping("/")
      public String home(Model model) {
          model.addAttribute("title", "Dashboard");
          return "pages/home"; // Resolves to pte-templates/pages/home.pte
      }
  }
  ```

---

## 📖 Feature Guide & Code Samples

Here is a complete list of all template features supported in PTE since day one:

---

### 1. Output & Escaping Modes
PTE provides 5 context-aware escaping modes to prevent injection attacks.

```html
<!-- HTML Escaped (Default) -->
<p>|user.bio|</p> <!-- '<b>Hi</b>' -> '&lt;b&gt;Hi&lt;/b&gt;' -->

<!-- Raw / Trusted HTML -->
<div>|html user.signature|</div> <!-- Renders raw HTML markup safely -->

<!-- HTML Attribute Escaped -->
<input value="|attr user.name|"> <!-- Prevents breaking out of input attributes -->

<!-- JSON Escaped -->
<script>var config = |json user.settings|;</script>

<!-- URL Escaped -->
<a href="/search?q=|url search_query|">Search</a>
```

---

### 2. Optional Chaining & Null Safety
Safely navigate deeply nested object properties without throwing `NullPointerException`.

```html
<!-- Safe optional navigation with ?? fallback value -->
<p>Welcome back, |user?.profile?.nickname ?? 'Guest'|</p>
```

---

### 3. Ternary Conditional Operator
Clean inline branching syntax directly inside expressions and attribute values.

```html
<!-- Dynamic status class assignment -->
<div class="|task.completed ? 'is-complete' : 'is-pending'|">
    |task.title|
</div>
```

---

### 4. Conditional Attribute Shorthand
Cleanly attach boolean attributes like `checked`, `disabled`, or `selected` without printing empty properties.

```html
<!-- Renders checked only if true; otherwise prints nothing -->
<input type="checkbox" |attr checked if task.completed|>
```

---

### 5. If / Else-If / Else Conditionals
Standard blocks for rendering structural template changes.

```html
|if user.role == 'ADMIN'|
    <span class="badge admin">Administrator</span>
|else-if user.role == 'MANAGER'|
    <span class="badge manager">Manager</span>
|else|
    <span class="badge user">User</span>
|/if|
```

---

### 6. Switch Blocks
Efficient multi-branch switch statements. Supports explicit `fallthrough`.

```html
|switch task.priority|
    |case 'HIGH'|
        <div class="priority-red">Urgent</div>
    |case 'MEDIUM'|
        <div class="priority-yellow">Important</div>
    |default|
        <div class="priority-green">Standard</div>
|/switch|
```

---

### 7. Loops (`|each|`) & Loop Metadata
Loop over Collections, Sets, Maps, and Arrays. Provides a fallback `|else|` if empty, and local metadata variables.

```html
<ul>
|each item in taskList|
    <li class="|each.first ? 'first-item' : ''|">
        |each.count|: |item.title|
    </li>
|else|
    <li>No tasks found.</li>
|/each|
</ul>
```

---

### 8. Loop Separators
Render delimiters (like commas, dots, or HTML spans) between list elements automatically.

```html
<!-- Prints: Java, Spring Boot, PTE -->
<p>Tech Stack: 
|each tech in techStack|
    <strong>|tech|</strong>|separator|, |/separator|
|/each|
</p>
```

---

### 9. Layouts & Yield Sections
Wrap pages inside master templates to reuse headers, sidebars, and scripts.

**Layout File (`layouts/main.pte`)**:
```html
<html>
<head>
    <title>|yield title|</title>
</head>
<body>
    <nav>Navigation Banner</nav>
    <main>|yield content|</main>
</body>
</html>
```

**Page File (`pages/home.pte`)**:
```html
|layout layouts/main|
|section title| Dashboard Page |/section|
|section content|
    <h1>Welcome User</h1>
|/section|
```

---

### 10. Components & Named Slots
Define highly reusable interface widgets and pass them rich nested markup slots.

**Component File (`components/card.pte`)**:
```html
<div class="card">
    <div class="card-header">|slot header|</div>
    <div class="card-body">|slot body|</div>
</div>
```

**Usage Page (`pages/dashboard.pte`)**:
```html
|component components/card|
    |slot header|
        <h3>System Stats</h3>
    |/slot|
    |slot body|
        <p>All servers online.</p>
    |/slot|
|/component|
```

---

### 11. Includes
Include simple partial template files directly. Supports passing sub-models using the `with` statement.

```html
<!-- Include header and pass navigation list object -->
|include partials/navbar with navItems|
```

---

### 12. Template-Defined Macros
Define reusable markup function helpers directly inside your templates or utility files.

```html
<!-- Define Macro -->
|macro action_button(label, color)|
    <button style="background-color: |color|; border-radius: 4px;">|label|</button>
|/macro|

<!-- Call Macro -->
|call action_button('Delete Item', '#ff3860')|
```

---

### 13. Inline Template Fragments
Target and render specific subsections of a template. Excellent for returning lightweight HTML payloads for HTMX updates.

**Template File (`pages/tasks.pte`)**:
```html
<div>
    <h1>Tasks</h1>
    |fragment list-zone|
        <ul id="task-list">
            <li>Buy milk</li>
        </ul>
    |/fragment|
</div>
```

**Java Controller Invocation**:
```java
// Renders only the <ul> block, skipping the surrounding headers!
String html = templateEngine.renderFragment("pages/tasks", "list-zone", model);
```

---

### 14. Strongly Typed Models
Explicitly declare your page model type at the top of templates.

```html
|model com.example.model.TaskPageModel|

<h1>|model.pageTitle|</h1>
<p>Due Date: |model.dueDate|</p>
```

---

### 15. built-in Pipe Filters
Clean filters to modify, capitalize, format, or slugify output values.

```html
|title, upper|             <!-- 'hello' -> 'HELLO' -->
|name, lower, capitalize|  <!-- 'JOHN' -> 'John' -->
|title, slug|              <!-- 'Hello World & welcome!' -> 'hello-world-welcome' -->
|price, currency 'USD'|    <!-- 9.99 -> '$9.99' -->
|createdDate, date 'yyyy'| <!-- 2026-07-22 -> '2026' -->
```