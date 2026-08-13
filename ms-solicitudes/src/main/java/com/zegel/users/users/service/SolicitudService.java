package com.zegel.users.users.service;

import com.zegel.users.users.model.Solicitud;
import com.zegel.users.users.model.User;
import com.zegel.users.users.repository.SolicitudRepository;
import com.zegel.users.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UserRepository userRepository;

    // Obtener el usuario conectado desde el token JWT
    private User getUsuarioAutenticado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en sesión"));
    }

    public List<Solicitud> listarTodas() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud> listarMisSolicitudes() {
        User cliente = getUsuarioAutenticado();
        return solicitudRepository.findByCliente(cliente);
    }

    public List<Solicitud> listarMisAsignaciones() {
        User tecnico = getUsuarioAutenticado();
        return solicitudRepository.findByTecnico(tecnico);
    }

    public Optional<Solicitud> obtenerPorId(Long id) {
        return solicitudRepository.findById(id);
    }

    public Solicitud crearSolicitud(Solicitud solicitud) {
        User usuario = getUsuarioAutenticado();
        solicitud.setCliente(usuario);
        solicitud.setEstado("PENDIENTE");
        return solicitudRepository.save(solicitud);
    }

    public Solicitud actualizarSolicitud(Long id, Solicitud solicitudDetalles) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));

        if (solicitudDetalles.getTitulo() != null) solicitud.setTitulo(solicitudDetalles.getTitulo());
        if (solicitudDetalles.getDescripcion() != null) solicitud.setDescripcion(solicitudDetalles.getDescripcion());
        if (solicitudDetalles.getEstado() != null) solicitud.setEstado(solicitudDetalles.getEstado());
        if (solicitudDetalles.getObservaciones() != null) solicitud.setObservaciones(solicitudDetalles.getObservaciones());

        return solicitudRepository.save(solicitud);
    }

    public Solicitud asignarTecnico(Long solicitudId, Long tecnicoId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + solicitudId));

        User tecnico = userRepository.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado con ID: " + tecnicoId));

        solicitud.setTecnico(tecnico);
        solicitud.setEstado("EN_PROCESO");
        return solicitudRepository.save(solicitud);
    }

    public void eliminarSolicitud(Long id) {
        solicitudRepository.deleteById(id);
    }
}