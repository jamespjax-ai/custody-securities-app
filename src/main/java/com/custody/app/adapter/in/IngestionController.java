package com.custody.app.adapter.in;

import com.custody.app.domain.service.InstructionWorkflowService;
import com.custody.app.iso20022.model.Sese023Document;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringReader;

@RestController
@RequestMapping("/api/ingest")
public class IngestionController {

    private final InstructionWorkflowService workflowService;

    public IngestionController(InstructionWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> ingestInstruction(@RequestBody String xmlPayload) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Sese023Document.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xmlPayload);
            Sese023Document document = (Sese023Document) unmarshaller.unmarshal(reader);

            if (document == null || document.getInstruction() == null) {
                return ResponseEntity.badRequest().body("Failed to parse ISO 20022 instruction from XML.");
            }

            String result = workflowService.processDepositoryInstruction(document);

            if (result.contains("REJECTED")) {
                return ResponseEntity.badRequest().body(result);
            }

            return ResponseEntity.ok(result);
        } catch (JAXBException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid XML Format: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Processing Error: " + e.getMessage());
        }
    }
}
