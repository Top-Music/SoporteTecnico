package com.zegel.users.users.repository;

import com.zegel.users.users.model.Solicitud;
import com.zegel.users.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    
    // Buscar solicitudes de un cliente
    List<Solicitud> findByCliente(User cliente);

    // Buscar solicitudes de un técnico
    List<Solicitud> findByTecnico(User tecnico);
}