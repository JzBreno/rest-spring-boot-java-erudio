package br.com.jzbreno.controllers.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@Tag(name = "Jasper Reports", description = "Exportação de relatórios gerados com JasperReports")
public interface PersonJasperReportControllerDoc {

    @Operation(
            summary = "Relatório de pessoas (PDF via Jasper)",
            description = "Gera PDF usando o template Jasper `Relatorio_Pessoas.jrxml`, com os dados da página solicitada.",
            tags = {"Jasper Reports", "People"})
    ResponseEntity<Resource> exportPeoplePdf(
            Integer page,
            Integer size,
            String direction,
            String sort) throws Exception;

    @Operation(
            summary = "Relatório de pessoas (Excel via Jasper)",
            description = "Gera planilha XLSX a partir do mesmo relatório Jasper preenchido com os dados da página solicitada.",
            tags = {"Jasper Reports", "People"})
    ResponseEntity<Resource> exportPeopleXlsx(
            Integer page,
            Integer size,
            String direction,
            String sort) throws Exception;
}
