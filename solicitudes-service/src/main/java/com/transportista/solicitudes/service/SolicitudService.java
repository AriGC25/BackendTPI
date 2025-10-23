package com.transportista.solicitudes.service;

import com.transportista.solicitudes.dto.SolicitudRequestDTO;
import com.transportista.solicitudes.dto.SolicitudResponseDTO;
import com.transportista.solicitudes.entity.Contenedor;
import com.transportista.solicitudes.entity.Solicitud;
import com.transportista.solicitudes.repository.ContenedorRepository;
import com.transportista.solicitudes.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private ContenedorRepository contenedorRepository;

    public SolicitudResponseDTO crearSolicitud(SolicitudRequestDTO dto) {
        // Crear contenedor
        Contenedor contenedor = new Contenedor();
        contenedor.setPeso(dto.getPesoContenedor());
        contenedor.setVolumen(dto.getVolumenContenedor());
        contenedor.setDescripcion(dto.getDescripcionContenedor());
        contenedor.setClienteId(dto.getClienteId());
        contenedor.setEstado("PENDIENTE");
        contenedor = contenedorRepository.save(contenedor);

        // Crear solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setContenedor(contenedor);
        solicitud.setClienteId(dto.getClienteId());
        solicitud.setDireccionOrigen(dto.getDireccionOrigen());
        solicitud.setLatitudOrigen(dto.getLatitudOrigen());
        solicitud.setLongitudOrigen(dto.getLongitudOrigen());
        solicitud.setDireccionDestino(dto.getDireccionDestino());
        solicitud.setLatitudDestino(dto.getLatitudDestino());
        solicitud.setLongitudDestino(dto.getLongitudDestino());
        solicitud.setEstado("PENDIENTE");

        solicitud = solicitudRepository.save(solicitud);
        return toResponseDTO(solicitud);
    }

    public SolicitudResponseDTO obtenerSolicitud(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        return toResponseDTO(solicitud);
    }

    public SolicitudResponseDTO obtenerSolicitudPorNumero(String numeroSolicitud) {
        Solicitud solicitud = solicitudRepository.findByNumeroSolicitud(numeroSolicitud)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        return toResponseDTO(solicitud);
    }

    public List<SolicitudResponseDTO> listarSolicitudes() {
        return solicitudRepository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudResponseDTO> listarSolicitudesPorCliente(Long clienteId) {
        return solicitudRepository.findByClienteId(clienteId).stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudResponseDTO> listarSolicitudesPorEstado(String estado) {
        return solicitudRepository.findByEstado(estado).stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    private SolicitudResponseDTO toResponseDTO(Solicitud solicitud) {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setId(solicitud.getId());
        dto.setNumeroSolicitud(solicitud.getNumeroSolicitud());
        dto.setContenedorId(solicitud.getContenedor().getId());
        dto.setClienteId(solicitud.getClienteId());
        dto.setDireccionOrigen(solicitud.getDireccionOrigen());
        dto.setDireccionDestino(solicitud.getDireccionDestino());
        dto.setCostoTotal(solicitud.getCostoTotal());
        dto.setTiempoEstimadoHoras(solicitud.getTiempoEstimadoHoras());
        dto.setEstado(solicitud.getEstado());
        dto.setFechaSolicitud(solicitud.getFechaSolicitud());
        dto.setFechaEstimadaEntrega(solicitud.getFechaEstimadaEntrega());
        return dto;
    }
}
