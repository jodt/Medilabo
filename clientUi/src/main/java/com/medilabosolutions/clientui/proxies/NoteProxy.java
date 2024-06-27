package com.medilabosolutions.clientui.proxies;

import com.medilabosolutions.clientui.beans.NoteBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "gateway", url = "${gateway.uri}", contextId = "notes-ms")
public interface NoteProxy {

    @GetMapping("v1/api/notes/findByPatientId/{id}")
    public List<NoteBean> findNotesByPatientId(@PathVariable Integer id);

    @GetMapping("v1/api/notes/add")
    public ResponseEntity<NoteBean> addPatientNote(@RequestBody NoteBean note);

}
