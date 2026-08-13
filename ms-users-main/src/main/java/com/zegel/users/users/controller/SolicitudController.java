package com.zegel.users.users.controller;

import com.zegel.users.users.model.Solicitud;
import com.zegel.users.users.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    // ADMIN: Obtener todas las solicitudes
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Solicitud>> obtenerTodas() {
        return ResponseEntity.ok(solicitudService.listarTodas());
    }

    // CLIENTE: Obtener sus propias solicitudes
    @GetMapping("/mis-solicitudes")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<List<Solicitud>> misSolicitudes() {
        return ResponseEntity.ok(solicitudService.listarMisSolicitudes());
    }

    // TECNICO: Obtener solicitudes asignadas
    @GetMapping("/mis-asignaciones")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<List<Solicitud>> misAsignaciones() {
        return ResponseEntity.ok(solicitudService.listarMisAsignaciones());
    }

    // Obtener solicitud por ID
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerPorId(@PathVariable Long id) {
        return solicitudService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CLIENTE / ADMIN: Crear nueva solicitud
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<Solicitud> crearSolicitud(@RequestBody Solicitud solicitud) {
        return ResponseEntity.ok(solicitudService.crearSolicitud(solicitud));
    }

    // CLIENTE / TECNICO / ADMIN: Actualizar solicitud
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizarSolicitud(@PathVariable Long id, @RequestBody Solicitud solicitud) {
        return ResponseEntity.ok(solicitudService.actualizarSolicitud(id, solicitud));
    }

    // ADMIN: Asignar técnico
    @PutMapping("/{id}/asignar-tecnico/{tecnicoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Solicitud> asignarTecnico(@PathVariable Long id, @PathVariable Long tecnicoId) {
        return ResponseEntity.ok(solicitudService.asignarTecnico(id, tecnicoId));
    }

    // ADMIN: Eliminar solicitud
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}