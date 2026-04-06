# Breve informe con observaciones sobre palíndromos y puntuación.
Un palíndromo es una palabra o frase que se lee igual de izquierda a derecha que de derecha a izquierda.
Al trabajar con frases reales, es necesario ignorar mayúsculas, espacios y signos de puntuación.
Para lograrlo, se transforma el texto a minúsculas y se eliminan caracteres no alfabéticos.
Se observó que frases como "Anita, lava la tina!" siguen siendo palíndromos al limpiarlas, mientras que otras como "Hola mundo" no lo son.
Además, se comprobó que los métodos de String no modifican el contenido original debido a su inmutabilidad, sino que generan nuevas cadenas.

# Por que String es inmutable?

En Java, un String es inmutable, lo que significa que no se puede modificar una vez creado.

Por ejemplo:
String texto = "Hola";
texto.toUpperCase();

Esto NO cambia el contenido original. En realidad, crea un nuevo String:
String nuevo = texto.toUpperCase();

# ¿Cómo influye en la solución del palíndromo?
Cada vez que usamos toLowerCase() o replaceAll(), se crean nuevos Strings.
Por eso hacemos:

String limpio = texto.toLowerCase().replaceAll(...);

y no modificamos el original.

# Ejemplos de métodos de Strings

Ya están en el código, pero resumidos:

- substring(ini, fin) -> parte del string
- split(",") -> separa en partes
- subSequence() -> similar a substring
- trim() -> elimina espacios extremos
- toLowerCase() / toUpperCase() -> cambia mayúsculas/minúsculas
- indexOf() -> primera aparición
- lastIndexOf() -> última aparición
- contains() -> contiene o no
- replace() -> reemplazo simple
- replaceAll() -> con regex
- replaceFirst() -> solo la primera coincidencia


