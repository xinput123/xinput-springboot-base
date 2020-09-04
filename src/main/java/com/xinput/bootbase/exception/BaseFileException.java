package com.xinput.bootbase.exception;

import org.springframework.core.io.InputStreamResource;

/**
 * @author xinput
 * @date 2020-06-06 22:50
 */
public class BaseFileException extends RuntimeException {

    private static final long serialVersionUID = -570884929692512780L;

    private InputStreamResource resource;

    private Long size;

    public BaseFileException(InputStreamResource resource, Long size) {
        this.resource = resource;
        this.size = size;
    }

    public InputStreamResource getResource() {
        return resource;
    }

    public void setResource(InputStreamResource resource) {
        this.resource = resource;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
