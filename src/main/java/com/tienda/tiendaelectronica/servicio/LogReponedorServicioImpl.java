/* LogReponedorServicioImpl.java */
package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.modelo.LogReponedor;
import com.tienda.tiendaelectronica.repositorio.LogReponedorRepositorio;

import com.tienda.tiendaelectronica.repositorio.ProductoRepositorio;

import com.tienda.tiendaelectronica.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LogReponedorServicioImpl implements LogReponedorServicio {

    private final LogReponedorRepositorio logRepo;
    private final UsuarioRepositorio usuarioRepositorio;
    private final ProductoRepositorio productoRepositorio;

    @Override
    @Transactional(readOnly = true)
    public List<LogReponedor> listarTodos() {
        return logRepo.findAllByOrderByFechaDesc();
    }

    @Override
    public void registrarAccion(String username,
                                Long productoId,
                                Integer cantidadAgregada,
                                String mensaje) {

        LogReponedor log = new LogReponedor();
        log.setFecha(LocalDateTime.now());
        log.setCantidadAgregada(cantidadAgregada);
        log.setMensaje(mensaje);

        // Usuario reponedor (si no se encuentra, lo deja null)
        usuarioRepositorio.findByUsername(username)
                .ifPresent(log::setUsuarioReponedor);

        // Producto (si no se encuentra, lo deja null)
        if (productoId != null) {
            productoRepositorio.findById(productoId)
                    .ifPresent(log::setProducto);
        }

        logRepo.save(log);
    }
}
