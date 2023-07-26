### Arcane

A blazing fast [Molang](https://learn.microsoft.com/en-us/minecraft/creator/reference/content/molangreference/examples/molangconcepts/molangintroduction) library for Java, built by a person who has written more Molang than they would like to admit.
Arcane will chug through millions of Molang evaluations each tick without impacting performance<sup>1</sup>.
Faster than every other available library<sup>2</sup>, bundled at less than 25Kb, and licensed with our fellow developers in mind.

&nbsp;&nbsp;&nbsp;Not sure what Molang is? [Scroll down]() to find out more.

---

### Disclaimer

Arcane is in active development. API *will* break in the future.

---

### Index

- Dependency Details
- About Molang
- Performance<sup>1, 2</sup>
- Usage Examples
  - API Walkthrough 
  - Tips & Tricks
  - Production Examples
- Feature Status

---

### ⚙ Dependency Info

*Repository*
```groovy
maven {
    name = "Draylar Maven"
    url = "maven.draylar.dev/releases"
}
```

*Dependency*
```groovy
modImplementation('dev.omega:arcane:1.0.0')
```

---

### 📜 About Molang

Molang is an expression language [built by Microsoft & Mojang for the Bedrock Engine](https://learn.microsoft.com/en-us/minecraft/creator/reference/content/molangreference/examples/molangconcepts/molangintroduction). 
It takes an input like `5.0 + 5.0` and spits out `10.0` in return. Bedrock developers rely heavily on Molang due to its prevalence in
addon mechanics. A good Molang evaluator is essential when porting over Bedrock mechanics (particles, animations) to Java edition.

---

### ⚡ Performance

Arcane is the fastest Molang evaluator available for public use. 
It can evaluate a complex nested expression with multiple query references __100,000__ times in ~__15ms__, or **50ms** without expression re-use.
This is 3-10x faster than leading competitor projects based on my personal testing. Coupled with thread-safe expression parsing & evaluation, 
Arcane will handle anything you throw at it with speed and reliability.

---

### Usage Examples

##### Modes: Direct vs. Re-use

You can use this library in 2 ways: direct or expression re-use.

In direct mode, you call the `Molang` class with your input String. 
This option is best for processing input which is only evaluated one time. 
The parsing is cached, but the expression *cannot* use variables or queries. 

```java
// If an internal Exception is thrown, you get an RuntimeException.
Molang.evaluateUnchecked("5.0 + 10.0");


// OR: Use the unchecked version to handle lexer/parser errors...
try {
    MolangParser.parse(input).evaluate();
} catch (MolangLexException | MolangParseException exception) {
    // ...
}
```

The alternative and recommended option is expression re-use. 
Rather than asking Molang for a float, you compile and store a `MolangExpression` for future re-use.

```java
MolangExpression expression = MolangParser.parse("1.0 + 1.0");

// Weeee!
for(int i = 0; i < ∞; i++){
    expression.evaluate();
}
```

By storing the expression, you can avoid doing a parser cache lookup, *and* you can supply additional context 
data to the expression for queries and variables. 

For reference - evaluating `5.0 + 10.0 + ((3.0 / 3.0) - 7.0) / 3.5 - 9.9 + (1.3 - 35.5)` 10 million times takes ~`30ms` in direct mode,
and `<=2ms` in expression re-use mode. This makes the latter option a desirable option for expressions that are evaluated each tick
or render frame.

##### Passing Fast Variable & Query Context

Molang has a concept of queries and variables, but the role of *defining* those types is left up to the projects that depend on Arcane.
As an example, take an expression which exposes `query.age` for an `Entity` class:

```java
Entity entity = new Entity();

// Show the expression how to resolve query.age -> entity.getAge()
ExpressionBindingContext context = new ExpressionBindingContext();
context.registerReferenceResolver(
        ReferenceType.QUERY, // query.age
        "age",
        Entity.class,
        entity -> new ObjectAwareExpression<>(entity) {
            @Override
            public float evaluate() {
                return value.getAge();
            }
        }
    );
}

// Compile your expression (store this for later!)
MolangExpression expression = MolangParser.parse("query.age * 5.0");

// Bind the entity to the expression. This operation returns a new expression instance.
expression = expression.bind(context, entity);

// Our expression now knows to resolve query.age -> entity.getAge(). 
// This becomes an extremely fast expression after the initial .bind() call.
entity.age = 50;
float returned = expression.evaluate(); // -> 250.0
```

Rather than updating a variable or query each tick, Arcane forces you to show the expression how to obtain the value when it needs it,
which results in simple & fast expression execution. You can bind also bind an expression to multiple objects!

Directly set variable/queries for expensive operations isn't supported yet, but it is on the roadmap.

---

### ✔ Feature Status

*Visit [the Molang Introduction docs](https://learn.microsoft.com/en-us/minecraft/creator/reference/content/molangreference/examples/molangconcepts/molangintroduction) for a list of language features.*

- Numerical Constants (`1.23`)
- Logical Operators (`! && < <= >= > == !=`)
- Math Operators (`* / + -`)
- Parentheses scoping (`( )`)
- Ternary operators (`<test> ? <if true> : <if false>`)
- Query Properties (`query.function_name`)

Geometry, Material, Texture, and Context reference types aren't needed in the context of Minecraft Java edition, so they are currently not supported in Arcane.