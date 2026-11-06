| N° | Code Smell / Debilidad | Ubicación | Principio / Criterio Afectado | Impacto en el Sistema | Prioridad |
|---|---|---|---|---|---|
| 1 | **Large Class / God Class** | `SolicitudManager.java` | Single Responsibility (SRP) | Monopolio de responsabilidades y cuello de botella de mantenimiento. | Alta |
| 2 | **Long Method** | `registrarSolicitud` | Funciones pequeñas / Clean Code | Dificultad para lectura, depuración y testeo aislado. | Alta |
| 3 | **Long Parameter List** | `reasignarArea` y `registrarSolicitud` | Clean Code / Parameter Object | Signaturas frágiles, confusas y propensas a errores en llamadas. | Media |
| 4 | **Primitive Obsession** | `Solicitud.java` (`tipologia`, `prioridad`) | Modelado de Dominio / Typesafety | Carentes de validación intrínseca; propensos a estados anómalos. | Media |
| 5 | **Falta de Encapsulamiento** | Atributos `public` en todas las entidades | Encapsulamiento OOP / Cohesión | Mutación externa incontrolada y violación de invariantes. | Alta |
| 6 | **Lógica Condicional Compleja** | Evaluación de `tipologia` en `registrarSolicitud` | Open/Closed Principle (OCP) | Cada nuevo requerimiento exige reescribir y recompilar el core. | Alta |
| 7 | **Máquina de Estados Primitiva** | `actualizarEstado` con `equals` de Strings | Patrón State / Alta Cohesión | Reglas de transición dispersas, rígidas y fuera de la entidad. | Media |
| 8 | **Silencio de Excepciones y Dependencia Dura** | Bloque `catch` vacío e instanciación con `new` | Dependency Inversion (DIP) y Clean Code | Fallos ocultos en producción y bloqueo para pruebas unitarias. | Alta |