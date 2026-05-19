# Microservicio de Demostración: Resilience4j con Spring Boot

Este proyecto es una aplicación de ejemplo que demuestra la implementación de patrones de tolerancia a fallos utilizando **Resilience4j** en un entorno de **Spring Boot 3**.

## Requisitos

*   **Java:** 21
*   **Framework:** Spring Boot
*   **Librerías principales:** Resilience4j (Circuit Breaker, Rate Limiter, Bulkhead, Retry)

## Patrones Implementados

El microservicio expone varios endpoints a través de `ResilientAppController` para probar diferentes estrategias de resiliencia:

### 1. Circuit Breaker (Disyuntor)
*   **Nombre:** `CircuitBreakerApi`
*   **Endpoint:** `GET /test-circuit`
*   **Comportamiento:** Simula una falla externa arrojando una excepción. El disyuntor cambiará de estado (CLOSED -> OPEN) basándose en una tasa de fallo del 50% tras un mínimo de 5 llamadas.
*   **Fallback:** Retorna un mensaje indicando que el servicio no está disponible temporalmente.

### 2. Bulkhead (Compartimentación)
*   **Nombre:** `bulkheadApi`
*   **Endpoint:** `GET /bulkhead`
*   **Configuración:** 
    *   `maxConcurrentCalls`: 2 (Máximo 2 peticiones simultáneas).
    *   `maxWaitDuration`: 0ms (Rechazo inmediato si está lleno).
*   **Fallback:** Retorna un error indicando que el bulkhead está lleno.

### 3. Rate Limiter (Limitador de Tasa)
*   **Nombre:** `rateLimiterApi`
*   **Endpoint:** `GET /rate-limiter`
*   **Configuración:**
    *   `limitForPeriod`: 5 permisos.
    *   `limitRefreshPeriod`: 10 segundos.
*   **Fallback:** Retorna un mensaje indicando que se ha excedido el límite de peticiones.

### 4. Retry (Reintento)
*   **Nombre:** `retryApi`
*   **Endpoint:** `GET /retryApi`
*   **Configuración:**
    *   `maxAttempts`: 3 intentos totales.
    *   `waitDuration`: 2 segundos entre intentos.
*   **Fallback:** Se activa después de agotar los reintentos si la excepción persiste.

## Configuración Técnica

La configuración de Resilience4j se encuentra centralizada en el archivo `src/main/resources/application.yml`. 

```yaml
resilience4j:
  circuitbreaker:
    instances:
      CircuitBreakerApi:
        slidingWindowSize: 10
        failureRateThreshold: 50
        # ...
```

## Monitoreo y Salud (Actuator)

El proyecto tiene habilitado **Spring Boot Actuator** para monitorear el estado de los componentes de resiliencia:

*   **Health Check:** `http://localhost:8080/actuator/health`
*   Se ha configurado `show-details: always` para visualizar el estado detallado de los Circuit Breakers.

## Estructura del Código

*   `ResilientAppController.java`: Define los puntos de entrada REST y las anotaciones de Resilience4j.
*   `ExternalAPICaller.java`: Servicio que simula las llamadas a APIs externas o lógica de negocio sensible.
*   `ExternalApiCallerConfig.java`: Configuración de beans como `RestTemplate`.

## Cómo Probar

1.  Inicie la aplicación Spring Boot.
2.  Para probar el **Circuit Breaker**: Acceda repetidamente a `/test-circuit` hasta que el estado cambie a abierto.
3.  Para probar el **Bulkhead**: Utilice una herramienta de carga (como JMeter o Apache Benchmark) para enviar más de 2 peticiones concurrentes a `/bulkhead`.
4.  Para probar el **Rate Limiter**: Refresque el endpoint `/rate-limiter` más de 5 veces en menos de 10 segundos.
5.  Para probar el **Retry**: El endpoint llamará al servicio y reintentará automáticamente según la configuración si ocurre una `RuntimeException`.

---
*Desarrollado como ejemplo de arquitectura resiliente.*