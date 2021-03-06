package com.dknapik.flowershop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class RestResponsePage<T> extends PageImpl<T> implements DTO {
    private static final long serialVersionUID = 2396954967833463444L;


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestResponsePage(@JsonProperty("content") List<T> content,
                            @JsonProperty("number") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") long totalElements,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("last") boolean last,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("sort") JsonNode sort,
                            @JsonProperty("first") boolean first,
                            @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    /**
     * Create RestResponsePage with the same properties but new content.
     * Useful for mapping Entities to Dto.
     *
     * @param content          - List with content
     * @param restResponsePage - Already existing RestResponsePage object
     */
    public <Y> RestResponsePage(List<T> content, RestResponsePage<Y> restResponsePage) {
        super(content, restResponsePage.getPageable(), restResponsePage.getTotalElements());
    }

    public RestResponsePage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }


    public RestResponsePage(List<T> content) {
        super(content);
    }

    public RestResponsePage(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    public RestResponsePage() {
        super(new ArrayList<>());
    }
}
