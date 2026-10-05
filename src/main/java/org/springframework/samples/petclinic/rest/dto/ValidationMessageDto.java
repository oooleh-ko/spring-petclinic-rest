package org.springframework.samples.petclinic.rest.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

/**
 * Message describing a validation error, plus extra details such as the field name and the rejected value.
 */
public class ValidationMessageDto {

    private String message;

    private Map<String, Object> additionalProperties;

    public ValidationMessageDto() {
    }

    public ValidationMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Extra details are written as top-level JSON properties after {@code message}.
     */
    @JsonAnySetter
    public ValidationMessageDto putAdditionalProperty(String key, Object value) {
        if (this.additionalProperties == null) {
            this.additionalProperties = new HashMap<>();
        }
        this.additionalProperties.put(key, value);
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
}
