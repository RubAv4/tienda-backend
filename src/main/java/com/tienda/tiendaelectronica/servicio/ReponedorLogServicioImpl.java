package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.modelo.Producto;
import com.tienda.tiendaelectronica.modelo.ReponedorLog;
import com.tienda.tiendaelectronica.modelo.Usuario;
import com.tienda.tiendaelectronica.repositorio.ReponedorLogRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReponedorLogServicioImpl implements ReponedorLogServicio {

    private final ReponedorLogRepositorio reponedorLogRepositorio;

    @Override
    public void registrarMovimiento(Usuario reponedor, Producto producto, int cantidad, String mensaje) {

        ReponedorLog log = ReponedorLog.builder()
                .usuarioReponedor(reponedor)
                .producto(producto)
                .cantidadAgregada(cantidad)
                .fecha(LocalDateTime.now())
                .mensaje(mensaje)
                .build();

        reponedorLogRepositorio.save(log);
    }

    @Override
    public List<ReponedorLog> listarTodos() {
        return reponedorLogRepositorio.findAllByOrderByFechaDesc();
    }
}
