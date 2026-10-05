package org.springframework.samples.petclinic.rest.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Pins the REST API contract (status, Location header and exact JSON body, including property order)
 * against golden files in src/test/resources/contract, using the full application context and H2 sample data.
 * <p>
 * To re-record the golden files after an intentional contract change, run with {@code -Dcontract.update=true}
 * and review the diff.
 */
@SpringBootTest
@TestPropertySource(properties = "petclinic.security.enable=false")
@Transactional
class ApiContractSnapshotTests {

    private static final Path CONTRACT_DIR = Path.of("src/test/resources/contract");

    private static final boolean UPDATE = Boolean.getBoolean("contract.update");

    private static final JsonMapper JSON = JsonMapper.builder().build();

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void getEndpoints() throws Exception {
        Map<String, String> requests = Map.ofEntries(
            Map.entry("get-owners", "/api/owners"),
            Map.entry("get-owner-6", "/api/owners/6"),
            Map.entry("get-owners-by-last-name", "/api/owners?lastName=Davis"),
            Map.entry("get-owner-6-pet-7", "/api/owners/6/pets/7"),
            Map.entry("get-pets", "/api/pets"),
            Map.entry("get-pet-7", "/api/pets/7"),
            Map.entry("get-pettypes", "/api/pettypes"),
            Map.entry("get-pettype-1", "/api/pettypes/1"),
            Map.entry("get-specialties", "/api/specialties"),
            Map.entry("get-vets", "/api/vets"),
            Map.entry("get-vet-3", "/api/vets/3"),
            Map.entry("get-visits", "/api/visits"),
            Map.entry("get-visit-1", "/api/visits/1"),
            Map.entry("get-v2-owners", "/api/v2/owners?page=0&size=3"),
            Map.entry("get-v2-owners-by-last-name", "/api/v2/owners?lastName=Davis"),
            Map.entry("get-v2-pets", "/api/v2/pets?page=1&size=5"),
            Map.entry("get-owner-not-found", "/api/owners/999"));
        for (Map.Entry<String, String> request : requests.entrySet()) {
            assertSnapshot(request.getKey(), get(request.getValue()));
        }
    }

    @Test
    void createEndpoints() throws Exception {
        assertSnapshot("post-owner", json(post("/api/owners"),
            "{\"firstName\":\"Ivan\",\"lastName\":\"Franko\",\"address\":\"Lychakivska 1\",\"city\":\"Lviv\",\"telephone\":\"0123456789\"}"));
        assertSnapshot("post-owner-pet", json(post("/api/owners/1/pets"),
            "{\"name\":\"Rex\",\"birthDate\":\"2020-01-02\",\"type\":{\"id\":2,\"name\":\"dog\"}}"));
        assertSnapshot("post-owner-pet-visit", json(post("/api/owners/6/pets/7/visits"),
            "{\"date\":\"2024-03-04\",\"description\":\"checkup\"}"));
        assertSnapshot("post-visit", json(post("/api/visits"),
            "{\"date\":\"2024-03-04\",\"description\":\"vaccination\",\"petId\":7}"));
        assertSnapshot("post-pettype", json(post("/api/pettypes"), "{\"name\":\"parrot\"}"));
        assertSnapshot("post-specialty", json(post("/api/specialties"), "{\"name\":\"cardiology\"}"));
        assertSnapshot("post-vet", json(post("/api/vets"),
            "{\"firstName\":\"Anna\",\"lastName\":\"Koval\",\"specialties\":[{\"id\":1,\"name\":\"radiology\"}]}"));

        assertSnapshot("post-user", json(post("/api/users"),
            "{\"username\":\"student\",\"password\":\"secret\",\"enabled\":true,\"roles\":[{\"name\":\"OWNER_ADMIN\"}]}"));
    }

    @Test
    void errorAndEdgeCases() throws Exception {
        assertSnapshot("post-owner-invalid", json(post("/api/owners"),
            "{\"firstName\":\"\",\"lastName\":\"Franko\",\"address\":\"Lychakivska 1\",\"city\":\"Lviv\",\"telephone\":\"phone\"}"));
        assertSnapshot("post-owner-pet-invalid", json(post("/api/owners/1/pets"),
            "{\"name\":\"Rex\",\"birthDate\":\"2020-01-02\",\"type\":{\"name\":\"\"}}"));
        assertSnapshot("post-vet-without-specialties", json(post("/api/vets"),
            "{\"firstName\":\"Anna\",\"lastName\":\"Koval\"}"));
        assertSnapshot("post-owner-text-plain", post("/api/owners")
            .contentType(MediaType.TEXT_PLAIN).content("hello"));
        assertSnapshot("get-owner-negative-id", get("/api/owners/-1"));
        assertSnapshot("get-v2-owners-size-0", get("/api/v2/owners?size=0"));
        assertSnapshot("get-v2-owners-size-101", get("/api/v2/owners?size=101"));
        assertSnapshot("get-v2-pets-page-negative", get("/api/v2/pets?page=-1"));
        assertSnapshot("get-owner-id-not-a-number", get("/api/owners/abc"));
        assertSnapshot("post-owner-malformed-json", json(post("/api/owners"), "{\"firstName\":"));
        assertSnapshot("get-unknown-path", get("/api/unknown"));
        assertSnapshot("patch-owners-not-allowed", patch("/api/owners"));
    }

    @Test
    void documentedOperations() throws Exception {
        MvcResult result = mockMvc.perform(get("/v3/api-docs")).andReturn();
        JsonNode paths = JSON.readTree(result.getResponse().getContentAsString()).get("paths");
        List<String> operations = new ArrayList<>();
        for (Map.Entry<String, JsonNode> path : paths.properties()) {
            for (Map.Entry<String, JsonNode> operation : path.getValue().properties()) {
                operations.add(operation.getKey().toUpperCase() + " " + path.getKey());
            }
        }
        operations.sort(null);
        compareWithGolden("operations.txt", String.join("\n", operations) + "\n");
    }

    private RequestBuilder json(org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder request,
                                String body) {
        return request.contentType(MediaType.APPLICATION_JSON).content(body);
    }

    private void assertSnapshot(String name, RequestBuilder request) throws Exception {
        MvcResult result = mockMvc.perform(request).andReturn();
        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8)
            // generated ids depend on test order, timestamps on the clock
            .replaceAll("\"timestamp\":\"[^\"]*\"", "\"timestamp\":\"<timestamp>\"");
        if (name.startsWith("post-") && location != null) {
            String id = location.substring(location.lastIndexOf('/') + 1);
            body = body.replace("\"id\":" + id + ",", "\"id\":<id>,").replace("\"id\":" + id + "}", "\"id\":<id>}");
            location = location.substring(0, location.lastIndexOf('/') + 1) + "<id>";
        }
        if (body.contains("\"schemaValidationErrors\":[{")) {
            body = sortValidationErrors(body);
        }
        String snapshot = "status: " + result.getResponse().getStatus() + "\n"
            + "location: " + location + "\n"
            + "body: " + body + "\n";
        compareWithGolden(name + ".txt", snapshot);
    }

    /**
     * Bean Validation reports violations in no particular order, so sort them to keep the snapshot stable.
     */
    private String sortValidationErrors(String body) {
        JsonNode root = JSON.readTree(body);
        ArrayNode errors = (ArrayNode) root.get("schemaValidationErrors");
        List<JsonNode> sorted = new ArrayList<>();
        errors.forEach(sorted::add);
        sorted.sort(Comparator.comparing(JsonNode::toString));
        errors.removeAll();
        errors.addAll(sorted);
        return JSON.writeValueAsString(root);
    }

    private void compareWithGolden(String fileName, String actual) throws IOException {
        Path file = CONTRACT_DIR.resolve(fileName);
        if (UPDATE) {
            Files.createDirectories(CONTRACT_DIR);
            Files.writeString(file, actual);
            return;
        }
        assertThat(file).as("golden file %s (record with -Dcontract.update=true)", file).exists();
        assertThat(actual).as(fileName).isEqualTo(Files.readString(file));
    }
}
