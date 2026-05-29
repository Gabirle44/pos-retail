package com.posretail.dto;

import java.util.ArrayList;
import java.util.List;

public class CompraDTO {
    private Integer idProveedor;
    private String tipoPago;
    private List<LineaDTO> lineas = new ArrayList<>();

    public CompraDTO() {}

    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }
    public List<LineaDTO> getLineas() { return lineas; }
    public void setLineas(List<LineaDTO> lineas) { this.lineas = lineas; }
}
