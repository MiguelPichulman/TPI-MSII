# TPI-MSII
Repositorio creado para TPI de Metodologia de Sistemas II
# Sistema de Gestión de Solicitudes Académicas (SGSA)

Trabajo Práctico Integrador (TPI) - **Metodología de Sistemas II**  
*Tecnicatura Universitaria en Programación - Universidad Tecnológica Nacional*

---

## Índice General

1. [Descripción General y Objetivos Académicos](#1-descripción-general-y-objetivos-académicos)
2. [Arquitectura y Estructura del Proyecto](#2-arquitectura-y-estructura-del-proyecto)
3. [Catálogo de Funcionalidades del Sistema](#3-catálogo-de-funcionalidades-del-sistema)
4. [Diagnóstico de Deuda Técnica y Code Smells](#4-diagnóstico-de-deuda-técnica-y-code-smells)
5. [Estrategia de Refactorización y Patrones de Diseño](#5-estrategia-de-refactorización-y-patrones-de-diseño)
6. [Tecnologías y Entorno de Ejecución](#6-tecnologías-y-entorno-de-ejecución)
7. [Instalación, Compilación y Ejecución](#7-instalación-compilación-y-ejecución)
8. [Estrategia de Pruebas Automatizadas (Testing Suite)](#8-estrategia-de-pruebas-automatizadas-testing-suite)
9. [Proceso de Code Review y Criterios de Aceptación Técnica (TAC)](#9-proceso-de-code-review-y-criterios-de-aceptación-técnica-tac)
10. [Conclusiones, Límites y Trabajo Futuro](#10-conclusiones-límites-y-trabajo-futuro)
11. [Gobernanza del Repositorio y Trazabilidad Git](#11-gobernanza-del-repositorio-y-trazabilidad-git)

---

## 1. Descripción General y Objetivos Académicos

### 1.1 Contexto del Dominio
El presente proyecto aborda la problemática operativa del procesamiento, auditoría y resolución de trámites estudiantiles en el ámbito universitario (solicitudes de certificados de alumno regular, homologación y equivalencia de asignaturas, reinscripciones fuera de término y notas generales a mesa de entradas). 

El sistema original ("Sistema Base") representaba una solución operativa inicial construida con foco exclusivo en la **calidad externa** (eficacia funcional aparente): el software resolvía los casos de uso básicos pero presentaba una severa degradación en su **calidad interna** (arquitectura monolítica, ausencia de encapsulamiento, clases sobredimensionadas, dependencias acopladas a librerías de infraestructura y silenciamiento de errores en tiempo de ejecución).

### 1.2 Objetivo General
Ejecutar un proceso riguroso de ingeniería inversa, diagnóstico de deuda técnica y **refactorización segura** sobre el núcleo del sistema, elevando sus atributos de calidad interna (mantenibilidad, testabilidad, modularidad y legibilidad) conforme a los estándares de **Clean Code** y los principios **SOLID** postulados por Robert C. Martin, garantizando matemáticamente la **ausencia total de regresiones funcionales** en el comportamiento observable del software.

### 1.3 Objetivos Específicos
* Diagnosticar, clasificar y documentar al menos 8 debilidades de diseño (*code smells*) presentes en la versión heredada.
* Descomponer la clase coordinadora monolítica (`SolicitudManager`) aplicando el Principio de Responsabilidad Única (**SRP**).
* Erradicar la lógica condicional encadenada dependiente de literales de texto mediante la implementación del patrón de comportamiento **Strategy** (**OCP**).
* Encapsular el ciclo de vida del trámite implementando una máquina de estados orientada a objetos con el patrón **State**.
* Aislar la biblioteca de notificaciones externas legacy mediante el patrón estructural **Adapter** y el principio de Inversión de Dependencias (**DIP**).
* Construir una red de seguridad automatizada mediante pruebas unitarias e integración en memoria con **JUnit Jupiter**, patrón **AAA** y dobles de prueba (**Mocks**).
* Formalizar una instancia de auditoría técnica colaborativa (*Peer Code Review*) con listas de control objetivas, Criterios de Aceptación Técnica (**TAC**) y comunicación asertiva.

---

## 2. Arquitectura y Estructura del Proyecto

El código fuente se organiza siguiendo el estándar de empaquetado canónico de Java bajo la raíz `ar.edu.utn.ms2.tpi`, garantizando alta cohesión funcional y bajo acoplamiento por capas:

```text
ms2.tpi/
├── build.gradle                              # Script de configuración y dependencias de Gradle
├── settings.gradle                           # Definición del proyecto raíz
├── gradlew / gradlew.bat                     # Gradle Wrappers multiplataforma
├── README.md                                 # Documentación integral del proyecto
├── docs/                                     # Artefactos y matrices documentales
│   ├── diagnostico-deuda-tecnica.md
│   └── code-review.md
└── src/
    ├── main/
    │   └── java/
    │       └── ar/edu/utn/frd/ms2/tpi/
    │           ├── Main.java                 # Punto de entrada y caso de uso demostrativo
    │           ├── model/                    # Entidades centrales de dominio y Enums fuertemente tipados
    │           │   ├── Area.java
    │           │   ├── Estudiante.java
    │           │   ├── NivelPrioridad.java
    │           │   └── Solicitud.java
    │           ├── dto/                      # Parameter Objects y objetos de transferencia
    │           │   ├── ClasificacionResultado.java
    │           │   └── RegistroSolicitudComando.java
    │           ├── validation/               # Reglas de validación aisladas (Extract Class)
    │           │   └── SolicitudValidator.java
    │           ├── state/                    # Jerarquía polimórfica del ciclo de vida (Patrón State)
    │           │   ├── EstadoSolicitud.java
    │           │   ├── EstadoPendiente.java
    │           │   ├── EstadoEnProceso.java
    │           │   ├── EstadoAprobada.java
    │           │   └── EstadoRechazada.java
    │           ├── strategy/                 # Clasificación polimórfica de trámites (Patrón Strategy)
    │           │   ├── ClasificadorSolicitudStrategy.java
    │           │   ├── ClasificadorCertificadoStrategy.java
    │           │   ├── ClasificadorEquivalenciaStrategy.java
    │           │   ├── ClasificadorReinscripcionStrategy.java
    │           │   └── ClasificadorGeneralStrategy.java
    │           ├── notification/             # Adaptación e Inversión de Dependencias (Patrón Adapter)
    │           │   ├── INotificador.java
    │           │   ├── NotificacionLegacyAdapter.java
    │           │   └── ServicioNotificacionLegacy.java
    │           ├── service/                  # Coordinación de casos de uso (Capa de Aplicación)
    │           │   └── SolicitudManager.java
    │           └── exception/                # Excepciones de dominio tipadas (Fail-Fast)
    │               ├── ValidacionSolicitudException.java
    │               ├── TransicionEstadoInvalidaException.java
    │               └── NotificacionException.java
    └── test/
        └── java/
            └── ar/edu/utn/frd/ms2/tpi/
                ├── mock/                     # Dobles de prueba para aislamiento de infraestructura
                │   └── NotificadorMock.java
                └── service/                  # Suite automatizada de pruebas unitarias y de regresión
                    └── SolicitudManagerTest.java
```

---

## 3. Catálogo de Funcionalidades del Sistema

El sistema procesa y audita el ciclo de vida de los trámites garantizando integridad transaccional:

1. **Ingreso y Validación de Solicitudes:**
   * Recepción encapsulada de parámetros mediante `RegistroSolicitudComando`.
   * Validación sintáctica y de negocio con excepciones descriptivas: legajo obligatorio de longitud mínima 4, formato de correo válido con estructura canónica, y descripción explicativa con al menos 10 caracteres.
2. **Clasificación Automática y Determinación de Prioridad:**
   * `CERTIFICADO`: Derivación al Departamento de Alumnos (`ALUMNOS`) con prioridad `BAJA`.
   * `EQUIVALENCIA`: Derivación a Secretaría Académica (`ACADEMICA`) con prioridad condicionada al nivel de urgencia (`ALTA` si urgencia $\ge 2$, caso contrario `MEDIA`).
   * `REINSCRIPCION`: Derivación al Departamento de Sistemas (`SISTEMAS`) con prioridad `MEDIA`.
   * Trámites especiales/no catalogados: Derivación por defecto a Mesa de Entradas (`MESA_GENERAL`) con prioridad `BAJA`.
3. **Gobierno Estricto del Ciclo de Vida:**
   * La solicitud nace en estado inmutable `PENDIENTE`.
   * Transición controlada obligatoria a `EN_PROCESO` antes de cualquier resolución.
   * Dictamen de cierre definitivo hacia `APROBADA` o `RECHAZADA` con persistencia de dictamen/resolución.
   * Bloqueo determinista ante saltos ilegales (intento de aprobar o rechazar directamente desde `PENDIENTE`).
4. **Reasignación de Áreas Destino:**
   * Modificación de la dependencia responsable mediante objetos de valor cohesivos (`Area`), erradicando primitivos sueltos.
5. **Filtrado y Reportes Multicriterio:**
   * Consultas combinadas por estado y área resolutiva para emisión de reportes operativos de gestión.
6. **Despacho Auditado de Notificaciones:**
   * Envío de alertas al estudiante en cada evento clave a través de interfaces desacopladas.

---

## 4. Diagnóstico de Deuda Técnica y Code Smells

El análisis de la versión base identificó los siguientes 8 defectos estructurales conforme al catálogo de Martin Fowler:

| N° | Code Smell / Debilidad | Ubicación Original | Principio Afectado | Impacto en Mantenibilidad |
|:--:|:-----------------------|:-------------------|:-------------------|:--------------------------|
| **1** | **Large Class / God Class** | `SolicitudManager.java` | Single Responsibility (SRP) | Monopolio de responsabilidades: almacenamiento, validación, negocio y notificaciones en una única clase. |
| **2** | **Long Method** | `registrarSolicitud` | Clean Code / Funciones Pequeñas | Método de más de 50 líneas mezclando múltiples niveles de abstracción y lógica dispersa. |
| **3** | **Long Parameter List** | Firmas de `registrar` y `reasignar` | Clean Code / Parameter Object | Métodos que requerían hasta 6 parámetros primitivos contiguos propensos a errores de invocación. |
| **4** | **Primitive Obsession** | Campos `tipologia` y `prioridad` | Typesafety / Modelado Orientado a Objetos | Manejo de cadenas e enteros libres (`1, 2, 3`) sin validación intrínseca de dominio. |
| **5** | **Falta de Encapsulamiento** | Atributos públicos en entidades | Encapsulamiento / OOD | Campos declarados `public`, permitiendo mutaciones de estado externas sin control de invariantes. |
| **6** | **Lógica Condicional Compleja** | Bifurcaciones `if-else` en registro | Open/Closed Principle (OCP) | Imposibilidad de extender nuevos trámites sin modificar y recompilar el archivo central. |
| **7** | **Máquina de Estados Primitiva** | `actualizarEstado` con `equals` | Patrón State / Alta Cohesión | Transiciones gobernadas desde afuera mediante cadenas de texto; riesgo de estados inconsistentes. |
| **8** | **Silencio de Excepciones / DIP** | Instanciación con `new` y `catch` vacío | Dependency Inversion (DIP) y Clean Code | Dependencia rígida a infraestructura legacy y ocultamiento deliberado de errores de ejecución. |

---

## 5. Estrategia de Refactorización y Patrones de Diseño



### 5.1 Refactorización 1: Introduce Parameter Object
* **Problema:** `registrarSolicitud` requería 6 argumentos (`legajo`, `nombre`, `email`, `tipologia`, `descripcion`, `urgencia`), mientras que `reasignarArea` recibía 3 strings sueltos (`codigo`, `nombre`, `email`).
* **Solución:** Se diseñó el objeto de transferencia `RegistroSolicitudComando` para agrupar los datos de entrada del trámite, y se reestructuró la firma de reasignación para recibir la entidad cohesiva `Area`.
* **Beneficio:** Reducción drástica de la firma a un único parámetro tipado y eliminación del smell *Data Clumps*.

### 5.2 Refactorización 2: Extract Method y Extract Class (Validaciones)
* **Problema:** `SolicitudManager` ejecutaba validaciones sintácticas de bajo nivel mediante bloques `if` en cascada y devolvía valores nulos (`return null`) ante fallos.
* **Solución:** Se extrajo la responsabilidad hacia la clase `SolicitudValidator`, subdividiéndola mediante *Extract Method* en rutinas atómicas (`validarLegajo`, `validarEmail`, `validarDescripcion`). Los fallos disparan la excepción de negocio no chequeada `ValidacionSolicitudException`.
* **Beneficio:** Cumplimiento de SRP, método principal conciso y adopción del principio *Fail-Fast*.

### 5.3 Refactorización 3: Move Method e Implementación del Patrón State
* **Problema:** El ciclo de vida de la solicitud era controlado de forma externa por el manager mediante comparaciones de texto plano (`codigoNuevoEstado.equals("APROBADA")`), exponiendo los campos de la entidad a modificaciones arbitrarias.
* **Solución:** Se trasladó la lógica de cambio de estado a la entidad `Solicitud` (*Move Method*), convirtiéndola en contexto del patrón **State**. Se definió la interfaz `EstadoSolicitud` y las clases concretas `EstadoPendiente`, `EstadoEnProceso`, `EstadoAprobada` y `EstadoRechazada`.
* **Beneficio:** Encapsulamiento total de las reglas de transición. Los saltos indebidos de estado son interceptados polimórficamente arrojando `TransicionEstadoInvalidaException`.

### 5.4 Refactorización 4: Replace Conditional with Polymorphism (Strategy) y Adapter (DIP)
* **Problema:** Las reglas de asignación residían en un árbol condicional `if-else` rígido. Paralelamente, el manager instanciaba de forma directa a `ServicioNotificacionLegacy`, atrapando fallos en un bloque `catch` vacío.
* **Solución:**
  1. **Patrón Strategy:** Se declaró la interfaz `ClasificadorSolicitudStrategy` y sus clases derivadas por trámite. El manager delega la resolución mediante un mapa de estrategias polimórfico.
  2. **Patrón Adapter:** Se definió la abstracción `INotificador`. La clase `NotificacionLegacyAdapter` envuelve al componente externo de infraestructura adaptando su firma y traduciendo fallos a `NotificacionException`.
  3. **Inyección de Dependencias:** El manager recibe `INotificador` vía constructor, desvinculándose de implementaciones concretas.
* **Beneficio:** Cumplimiento simultáneo de los principios OCP y DIP, posibilitando extender el sistema con nuevos trámites o reemplazar el proveedor de correo sin alterar el código de dominio.

---

## 6. Tecnologías y Entorno de Ejecución

* **Lenguaje de Programación:** Java SE 17+ (LTS).
* **Herramienta de Construcción (Build Tool):** Gradle 8.x / 9.x con Gradle Wrapper integrado.
* **Framework de Testing:** JUnit Jupiter 5.10.x.
* **Control de Versiones:** Git 2.4x y GitHub.
* **Entorno de Desarrollo Integrado:** IntelliJ IDEA 2024.x / Eclipse / VS Code con Extension Pack for Java.

---

## 7. Instalación, Compilación y Ejecución

### 7.1 Clonar el Repositorio
```bash
git clone https://github.com/MiguelPichulman/TPI-MSII.git
```

### 7.2 Compilación del Proyecto
El proyecto utiliza Gradle Wrapper, por lo que no requiere una instalación previa de Gradle en el sistema operativo:

* **En entornos Linux / macOS:**
  ```bash
  ./gradlew build -x test
  ```
* **En entornos Windows (PowerShell / Command Prompt):**
  ```powershell
  .\gradlew.bat build -x test
  ```

### 7.3 Ejecución del Caso de Uso Principal
Para ejecutar la clase demostrativa `Main.java` desde la consola:

* **En entornos Linux / macOS:**
  ```bash
  ./gradlew run
  ```
* **En entornos Windows (PowerShell / Command Prompt):**
  ```powershell
  .\gradlew.bat run
  ```

---

## 8. Estrategia de Pruebas Automatizadas (Testing Suite)

### 8.1 Metodología de Verificación
La suite de pruebas fue diseñada para proporcionar una red de seguridad inquebrantable, adoptando:
1. **Patrón AAA (Arrange - Act - Assert):** Estructura formal en cada prueba, separando preparación de contexto, ejecución y aserciones.
2. **Aislamiento mediante Test Double (`NotificadorMock`):** Sustituye la infraestructura de red por un doble de prueba en memoria, permitiendo auditar destinatarios, mensajes y niveles de prioridad despachados sin efectos colaterales.
3. **Validación de Casos de Borde (Edge Cases):** Pruebas sistemáticas sobre cadenas vacías, longitudes insuficientes, formatos inválidos y saltos ilegales de ciclo de vida.

### 8.2 Comando de Ejecución de Pruebas
Para compilar y correr la totalidad de los tests unitarios automatizados:

* **En entornos Linux / macOS:**
  ```bash
  ./gradlew test
  ```
* **En entornos Windows (PowerShell):**
  ```powershell
  .\gradlew.bat test
  ```

La salida esperada en consola debe certificar una ejecución limpia:
```text
BUILD SUCCESSFUL in 2s
3 actionable tasks: 2 executed, 1 up-to-date
```

### 8.3 Reporte HTML de Cobertura y Resultados
Tras la ejecución, Gradle genera un reporte detallado en formato web auditable. Para visualizarlo en el navegador:
* **Ruta relativa del reporte:** `build/reports/tests/test/index.html`

### 8.4 Matriz de Casos de Prueba Implementados

| Identificador | Caso de Prueba / Escenario | Entrada (*Arrange*) | Acción (*Act*) | Aserción Verificada (*Assert*) | Veredicto |
|:---:|:---|:---|:---|:---|:---:|
| **CP-01** | Registro de Solicitud de Certificado | Trámite `"CERTIFICADO"`, urgencia `1` | `registrarSolicitud(cmd)` | Área `"ALUMNOS"`, `NivelPrioridad.BAJA`, 1 notificación mock | **PASSED** |
| **CP-02** | Registro de Equivalencia Urgente | Trámite `"EQUIVALENCIA"`, urgencia `2` | `registrarSolicitud(cmd)` | Área `"ACADEMICA"`, `NivelPrioridad.ALTA`, 1 notificación mock | **PASSED** |
| **CP-03** | Tipología Desconocida (*Fallback*) | Trámite no catalogado `"PASANTIA"` | `registrarSolicitud(cmd)` | Estrategia general: área `"MESA_GENERAL"`, prioridad `BAJA` | **PASSED** |
| **CP-04** | Validación de Legajo Inválido | Legajo `"12"` (< 4 caracteres) | `registrarSolicitud(cmd)` | Lanza `ValidacionSolicitudException`, 0 notificaciones | **PASSED** |
| **CP-05** | Validación de Formato de Email | Correo `"correo_invalido.com"` | `registrarSolicitud(cmd)` | Lanza `ValidacionSolicitudException` descriptiva | **PASSED** |
| **CP-06** | Validación de Descripción Insuficiente | Descripción `"Corta"` (< 10 caracteres) | `registrarSolicitud(cmd)` | Lanza `ValidacionSolicitudException` descriptiva | **PASSED** |
| **CP-07** | Ciclo de Vida Secuencial Válido | Solicitud nacida en `PENDIENTE` | Avanzar a `EN_PROCESO` y `APROBADA` | Ambas retornan `true`; estado final `APROBADA` con dictamen | **PASSED** |
| **CP-08** | Bloqueo de Transición Ilegal | Intento de dictamen desde `PENDIENTE` | `actualizarEstado(id, "APROBADA")` | Retorna `false`; captura `TransicionEstadoInvalidaException` | **PASSED** |
| **CP-09** | Reasignación Estructurada de Área | Solicitud activa y nueva entidad `Area` | `reasignarArea(id, nuevaArea)` | Retorna `true`; campos de área actualizados integralmente | **PASSED** |
| **CP-10** | Consulta y Filtrado Multicriterio | Múltiples solicitudes persistidas | `obtenerSolicitudesPorEstadoYArea` | Coincidencia unívoca de colecciones por estado y área | **PASSED** |

---

## 9. Proceso de Code Review y Criterios de Aceptación Técnica (TAC)

### 9.1 Criterios de Aceptación Técnica (TAC) Innegociables
Para asegurar que ningún incremento degrade la calidad del repositorio, se formalizaron 5 TAC obligatorios:
* **TAC-01 (Compilación Limpia):** Ejecución determinista de tareas de construcción en Gradle sin advertencias críticas (`BUILD SUCCESSFUL`).
* **TAC-02 (Tests Automatizados al 100%):** Cobertura total de caminos críticos y casos de borde en verde mediante JUnit.
* **TAC-03 (Adherencia SOLID):** Erradicación de clases sobrecargadas, cumplimiento estricto de OCP en reglas y DIP en servicios de infraestructura.
* **TAC-04 (Manejo de Errores Limpio):** Erradicación total de retornos de referencias nulas (`null`) y de bloques `catch` vacíos; adopción del modelo *Fail-Fast*.
* **TAC-05 (Orden Modular de Paquetes):** Aislamiento de entidades, servicios, estados, estrategias y adaptadores en paquetes cohesivos independientes.

### 9.2 Checklist Técnico y Dictamen de Aceptación
La auditoría técnica por pares verificó la conformidad del 100% de los ítems de control de arquitectura, legibilidad, diseño orientado a objetos y testing, emitiendo el dictamen formal: **APROBADO SIN RESERVAS (*Approved*)**.

---

## 10. Conclusiones, Límites y Trabajo Futuro

### 10.1 Balance de Calidad Externa vs. Calidad Interna
El desarrollo evidenció que un software que "funciona" a los ojos del usuario final puede ocultar una arquitectura frágil y costosa de mantener. El sistema refactorizado ofrece un comportamiento externo idéntico al sistema base original, pero reduce el costo de cambio a niveles mínimos: agregar un nuevo trámite ya no requiere tocar clases existentes, y el ciclo de vida de los trámites se encuentra protegido contra mutaciones no autorizadas.

### 10.2 Límites Actuales del Sistema
* **Persistencia Volátil:** El almacenamiento opera en memoria estructurada (`List<Solicitud>`), reiniciándose al terminar el proceso.
* **Canal de Notificaciones Simulado:** La infraestructura real imprime en consola estándar sin gestionar reintentos asíncronos ante fallos de red.
* **Punto de Entrada en Consola:** La interacción se encuentra coordinada vía `Main.java`, careciendo de interfaces gráficas desacopladas o APIs web.

### 10.3 Hoja de Ruta y Mejoras Pendientes
* **Persistencia Relacional:** Implementar el patrón Repository con soporte JPA/Hibernate sobre PostgreSQL.
* **Capa de Exposición REST:** Exponer los casos de uso mediante controladores en un framework web (Spring Boot / Jakarta EE).
* **Contenedor de Inyección de Dependencias (IoC):** Gestionar el ciclo de vida de estrategias y servicios dinámicamente mediante anotaciones.
* **Mensajería Asíncrona:** Integrar un broker de mensajería (RabbitMQ / Apache Kafka) dentro del adaptador de notificaciones para despachos no bloqueantes.

---

## 11. Gobernanza del Repositorio y Trazabilidad Git

La evolución del proyecto se encuentra auditada mediante una convención rigurosa de commits semánticos (*Conventional Commits*) y el uso de **Git Tags anotados** que certifican el cumplimiento de cada hito de entrega:

* `estado-inicial`: Estado original del sistema base con la presencia de deuda técnica.
* `etapa-diagnostico`: Diagnóstico formal de los 8 code smells y matriz de deuda técnica.
* `etapa-refactoring`: Aplicación sistemática de las 4 refactorizaciones (State, Strategy, Adapter y Parameter Object).
* `etapa-pruebas`: Construcción de la suite de pruebas unitarias 100% en verde respaldada por Gradle.
* `estadorefactorizado`: Hito de cierre formal y consolidación definitiva del proyecto.

### Comandos de Inspección de Tags
```bash
# Listar los tags anotados del repositorio local
git tag -n

# Auditar los metadatos y el commit vinculado al cierre
git show estadorefactorizado
```