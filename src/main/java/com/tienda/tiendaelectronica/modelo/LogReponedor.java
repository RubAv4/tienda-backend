package com.tienda.tiendaelectronica.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "reponedor_logs")
@Getter
@Setter
@NoArgsConstructor
public class LogReponedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // usuario_reponedor_id (FK a usuarios.id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_reponedor_id")
    @NotFound(action = NotFoundAction.IGNORE) // si el usuario ya no existe, lo pone en null
    @JsonIgnoreProperties({"password", "roles", "pedidos", "carritos"})
    private Usuario usuarioReponedor;

    // producto_id (FK a productos.id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    @NotFound(action = NotFoundAction.IGNORE) // si el producto ya no existe, lo pone en null
    @JsonIgnoreProperties({"categoria", "itemsPedido"})
    private Producto producto;

    @Column(name = "cantidad_agregada")
    private Integer cantidadAgregada;

    private String mensaje;

    private LocalDateTime fecha;
}
