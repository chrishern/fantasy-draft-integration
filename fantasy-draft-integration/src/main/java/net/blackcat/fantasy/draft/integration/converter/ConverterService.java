/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Service;

/**
 * Service used for handling custom type conversions (e.g, from domain to DTO) used within the application.
 * 
 */
@Service
public class ConverterService extends GenericConversionService {

    @SuppressWarnings("rawtypes")
    @Autowired
    @IntegrationConverter
    private List<Converter> converters;

    @SuppressWarnings("rawtypes")
    @PostConstruct
    public void initialiseConverters() {

        for (final Converter converter : converters) {
            this.addConverter(converter);
        }
    }
}
