package br.com.jzbreno.file.exporter.factory;

import br.com.jzbreno.file.exporter.contract.FileExporter;
import br.com.jzbreno.file.importer.contract.FileImporter;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

//utilizaremos essa anotacao para que possamos injetar essa classe onde se necessita ser utilizada
@Component
public class FileExporterFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);
    //injetando dependencias das nossas classes anotadas por component para que o spring as reconheca, elas devem implementar a interface FileImport para serem encontradas
    private final List<FileExporter> fileExporters;
    @Autowired
    private ApplicationContext applicationContext;

    public FileExporterFactory(List<FileExporter> fileExporters) {
        logger.debug("Iniciando constructor de FileImporterFactory");
        this.fileExporters = fileExporters;
        logger.debug("Fim de constructor de FileImporterFactory");
        logger.debug(this.fileExporters.toString());
    }


    public FileExporter getFileExporter(String tipo) throws BadRequestException {
        logger.debug("Iniciando getFileImporterEnd de FileImporterFactory");
        return fileExporters.stream()
                .filter(importer -> importer.canHandle(tipo))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Format not supported"));
    }

}
