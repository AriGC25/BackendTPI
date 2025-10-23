package com.transportista.solicitudes.repository;

import com.transportista.solicitudes.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    
    Optional<Solicitud> findByNumeroSolicitud(String numeroSolicitud);
    
    List<Solicitud> findByClienteId(Long clienteId);
    
    List<Solicitud> findByEstado(String estado);
}
