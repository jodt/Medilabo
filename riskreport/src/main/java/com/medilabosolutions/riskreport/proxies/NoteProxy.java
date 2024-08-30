package com.medilabosolutions.riskreport.proxies;

import com.medilabosolutions.riskreport.beans.NoteBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "gateway", url = "${gateway.uri}", contextId = "notes-ms")
public interface NoteProxy {

    @GetMapping("v1/api/notes/findByPatientId/{id}")
    public List<NoteBean> findNotesByPatientId(@PathVariable Integer id);

}
