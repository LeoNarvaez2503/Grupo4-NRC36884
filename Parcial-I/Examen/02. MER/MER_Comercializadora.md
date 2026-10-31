# Ítem 4: Diagrama Entidad - Relación del Sistema de la Comercializadora de Teléfonos (Puntaje: 1.0 / 1.0)

Según las especificaciones del examen práctico:
> *"El sistema de facturación de la comercializadora NO deberá contar con inventario, basta con solo tener una entidad donde se encuentren los electrodomésticos disponibles para la venta. Formas de pago: Efectivo (con 42% de descuento) y Crédito Directo (con BanQuito)."*

```mermaid
erDiagram
    TELEFONO_CELULAR ||--o{ FACTURA : "es facturado en"

    TELEFONO_CELULAR {
        int COD_TELEFONO PK "Clave Primaria Autoincremental"
        varchar MARCA "Marca del equipo móvil"
        varchar MODELO "Modelo comercial"
        numeric PRECIO "Precio de venta al público"
        text CARACTERISTICAS "Memoria, RAM, pantalla, cámara"
        varchar IMAGEN_URL "URL fotográfica del terminal"
        varchar ESTADO "DISPONIBLE o INACTIVO"
    }

    FACTURA {
        int NUM_FACTURA PK "Clave Primaria Autoincremental"
        varchar NUMERO_COMPROBANTE UK "Código único (FAC-EFEC o FAC-CRED)"
        datetime FECHA "Fecha y hora de emisión"
        int COD_CLIENTE_BANCO "Código del cliente en BanQuito (si aplica)"
        varchar CEDULA_CLIENTE "Cédula de identidad del comprador"
        varchar NOMBRE_CLIENTE "Nombre completo del comprador"
        int COD_TELEFONO FK "Referencia al teléfono adquirido"
        varchar MODELO_TELEFONO "Denominación del equipo al facturar"
        numeric PRECIO_UNITARIO "Precio de lista original"
        varchar FORMA_PAGO "EFECTIVO o CREDITO_DIRECTO"
        numeric PORCENTAJE_DESCUENTO "42.00 para Efectivo, 0.00 para Crédito"
        numeric VALOR_DESCUENTO "Monto descontado en USD"
        numeric TOTAL_PAGAR "Monto final liquidado"
        int COD_CREDITO_BANCO "Número de crédito generado en BanQuito"
        int NUM_CUOTAS "Plazo del crédito (3 a 18 meses)"
        numeric VALOR_CUOTA "Valor de cuota fija mensual al 16.5% anual"
        varchar OBSERVACION "Notas de auditoría comercial"
    }
```

---

## Modelo Entidad - Relación del Sistema Core Bancario BanQuito (Módulo Crédito)

```mermaid
erDiagram
    CLIENTE ||--o{ CUENTA : "posee (1..N)"
    CUENTA ||--o{ MOVIMIENTO : "registra (1..N)"
    CLIENTE ||--o{ CREDITO : "adquiere (0..N)"
    CREDITO ||--|{ TABLA_AMORTIZACION : "contiene (1..N cuotas)"

    CLIENTE {
        int COD_CLIENTE PK
        varchar CEDULA UK
        varchar NOMBRE
        varchar GENERO "M / F"
        date FECHA_NACIMIENTO
    }

    CUENTA {
        varchar NUM_CUENTA PK
        int COD_CLIENTE FK
        numeric SALDO
    }

    MOVIMIENTO {
        int COD_MOVIMIENTO PK
        varchar NUM_CUENTA FK
        varchar TIPO "DEP / RET"
        numeric VALOR
        date FECHA
    }

    CREDITO {
        int COD_CREDITO PK
        int COD_CLIENTE FK
        numeric MONTO
        int NUM_CUOTAS
        numeric TASA_INTERES_ANUAL "16.50%"
        numeric VALOR_CUOTA
        datetime FECHA_CREACION
        varchar ESTADO "ACTIVO / PAGADO"
    }

    TABLA_AMORTIZACION {
        int COD_AMORTIZACION PK
        int COD_CREDITO FK
        int NUM_CUOTA
        numeric VALOR_CUOTA
        numeric INTERES_PAGADO
        numeric CAPITAL_PAGADO
        numeric SALDO
    }
```
