// src/main/java/com/tienda/tiendaelectronica/dto/ResumenVentaUsuarioDto.java
package com.tienda.tiendaelectronica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenVentaUsuarioDto {

    private Long usuarioId;
    private String username;
    private long pedidos;   // cantidad de pedidos
    private double total;   // total vendido
}
