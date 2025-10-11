package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.mapper.ObjectMapper;
import br.com.jzbreno.model.DTO.PcDTO;
import br.com.jzbreno.model.Pc;
import br.com.jzbreno.repository.PcRepository;
import io.micrometer.common.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PcServices {

    private final PcRepository pcRepository;

    public PcServices(PcRepository pcRepository) {
        log.info("PcServices constructor");
        this.pcRepository = pcRepository;
    }

    public PcDTO findById(String id){
        log.info("Finding Pc by id : " + id);
        return ObjectMapper.parseObject(pcRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)), PcDTO.class);
    }

    public List<PcDTO> findAll(){
        log.info("Finding all Pc");
        return ObjectMapper.parseObjectList(pcRepository.findAll(), PcDTO.class);
    }

    public PcDTO create(@NonNull PcDTO pcDto){
        log.info("Creating Pc : " + pcDto.toString());
        pcRepository.save(ObjectMapper.parseObject(pcDto, Pc.class));
        return pcDto;
    }

    public PcDTO updating(PcDTO pcDto){
        log.info("Updating Pc : " + pcDto.toString() );
        PcDTO computerUpdate = findById(pcDto.getId().toString());
        computerUpdate.setId(pcDto.getId());
        computerUpdate.setVideoCard(pcDto.getVideoCard());
        computerUpdate.setProcessor(pcDto.getProcessor());
        computerUpdate.setRamMemory(pcDto.getRamMemory());
        computerUpdate.setStorageUnit(pcDto.getStorageUnit());
        Pc pc = ObjectMapper.parseObject(computerUpdate, Pc.class);
        pcRepository.save(pc);
        return computerUpdate;
    }

    public void deleteById(String id){
        log.info("Deleting Pc : " + id);
        pcRepository.deleteById(Long.parseLong(id));
    }


}
