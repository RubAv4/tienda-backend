package com.tienda.tiendaelectronica.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reponedor_logs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReponedorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_reponedor_id")
    private Usuario usuarioReponedor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "cantidad_agregada", nullable = false)
    private Integer cantidadAgregada;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 255)
    private String mensaje;
}
