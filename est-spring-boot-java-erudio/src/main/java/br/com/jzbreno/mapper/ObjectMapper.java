package br.com.jzbreno.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapper {
//    esse cara ira mapear entidade -> dto e dto -> entidade
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

//    nosso metodo <origem, destino> tipo de retorno(Destino - D)
    public static <O, D> D parseObject(O origin, Class<D> destinationClass) {
        return mapper.map(origin, destinationClass);
    }
    //    nosso metodo <origem, destino> tipo de retorno Lista(Destino - D)
    public static <O, D> List<D> parseObjectList(List<O> origin, Class<D> destination) {
        List<D> destinationObject = new ArrayList<>();
        origin.forEach( obj -> {
            destinationObject.add(mapper.map(obj, destination));
        });
        return destinationObject;
    }

}
