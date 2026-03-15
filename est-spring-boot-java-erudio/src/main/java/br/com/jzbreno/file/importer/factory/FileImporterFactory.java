package br.com.jzbreno.file.importer.factory;

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
public class FileImporterFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);
    //injetando dependencias das nossas classes anotadas por component para que o spring as reconheca, elas devem implementar a interface FileImport para serem encontradas
    private final List<FileImporter> fileImporters;
    @Autowired
    private ApplicationContext applicationContext;

    public FileImporterFactory(List<FileImporter> fileImporters) {
        logger.debug("Iniciando constructor de FileImporterFactory");
        this.fileImporters = fileImporters;
        logger.debug("Fim de constructor de FileImporterFactory");
        logger.debug(this.fileImporters.toString());
    }


    public FileImporter getFileImporter(String fileName) throws BadRequestException {
        logger.debug("Iniciando getFileImporterEnd de FileImporterFactory");
        return fileImporters.stream()
                .filter(importer -> importer.canHandle(fileName))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Format not supported"));
    }

}
