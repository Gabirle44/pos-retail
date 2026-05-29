package com.posretail.dto;

import java.util.ArrayList;
import java.util.List;

public class VentaDTO {
    private Integer idCliente;
    private String tipoPago;
    private String metodoPago;
    private List<LineaDTO> lineas = new ArrayList<>();

    public VentaDTO() {}

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public List<LineaDTO> getLineas() { return lineas; }
    public void setLineas(List<LineaDTO> lineas) { this.lineas = lineas; }
}
