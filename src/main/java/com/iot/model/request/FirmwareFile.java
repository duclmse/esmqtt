package com.iot.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.codec.multipart.FilePart;

@Data
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class FirmwareFile extends FirmwareInfo {

    @JsonIgnore
    private FilePart firmwareFilePart;
}
