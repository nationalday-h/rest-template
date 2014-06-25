package com.rest.hgq.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.common.base.Throwables;
import com.rest.hgq.core.DBUtils;
import com.rest.hgq.core.JsonUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractResourceTest  {
    public static final String BASE_URI = "http://localhost:8089/rest/api/";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractResourceTest.class);
    @Inject
    private EntityManager entityManager;

    protected String toJson(Object object) {
        return JsonUtils.toJson(object);
    }

    protected <T> T fromJson(String json, Class<T> entityClass) {
        return JsonUtils.fromJson(json, entityClass);
    }

    protected <T> T persist(T object) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(object);

            entityTransaction.commit();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
            DBUtils.rollBackQuitely(entityTransaction);
            Throwables.propagate(e);
        }

        return object;
    }

    protected <T> T merge(T object) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            object = entityManager.merge(object);
            entityTransaction.commit();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
            DBUtils.rollBackQuitely(entityTransaction);
            Throwables.propagate(e);
        }
        return object;
    }

    public <T> void persistToDB(T... entities) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            for (T entity : entities) {
                entityManager.persist(entity);
            }

            entityTransaction.commit();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
            DBUtils.rollBackQuitely(entityTransaction);
            Throwables.propagate(e);
        }
    }

    protected <T> T load(Class<T> type, Object id) {
        entityManager.clear();
        return entityManager.find(type, id);
    }

    protected ClientResponse postRequest(Object entity, String url) {
        return createSpecificClient().resource(BASE_URI).path(url)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .entity(entity)
                .post(ClientResponse.class);
    }

    protected ClientResponse postRequest(String url) throws Exception {
        return createSpecificClient().resource(BASE_URI).path(url)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).post(ClientResponse.class);
    }

    protected ClientResponse putRequest(Object entity, String url) {
        return createSpecificClient().resource(BASE_URI).path(url)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .entity(entity)
                .put(ClientResponse.class);
    }

    protected ClientResponse postQueryRequest(Object entity, String url,MultivaluedMap<String,String> params) {
        return getPath(url)
                .queryParams(params)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .entity(entity)
                .post(ClientResponse.class);
    }

    protected ClientResponse deleteByIdRequest(String id, String url) {
        return createSpecificClient().resource(BASE_URI).path(url)
                .path(id)
                .delete(ClientResponse.class);
    }

    protected Client createSpecificClient() {
        //使用Jersey客户端时，需要单独配置Client端的信息。
        ClientConfig cc = new DefaultClientConfig();
//        cc.getClasses().add(JAXBContextResolver.class);
        cc.getClasses().add(JacksonJaxbJsonProvider.class);
        return Client.create(cc);
    }

    protected WebResource createSpecificClientForPath(String path) {
        return createSpecificClient().resource(BASE_URI).path(path);
    }

    protected ClientResponse getRequest(String url) {
        return createSpecificClient().resource(BASE_URI).path(url)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
    }

    protected ClientResponse getRequestWithParameter(String url, String parameterKey, String parameterValue) {
        return createSpecificClient().resource(BASE_URI).path(url)
                .queryParam(parameterKey, parameterValue)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
    }

    protected ClientResponse getRequestWithParameters(String url, MultivaluedMap<String, String> params) {
        return getPath(url)
                .queryParams(params)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
    }

    protected ParameterMapBuilder getRequestWithParameters(String url) {
        return new ParameterMapBuilder(url);
    }

    public class ParameterMapBuilder {
        private MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        private String url;
        public ParameterMapBuilder(String url) {
            this.url = url;
        }

        public ParameterMapBuilder add(String key, String value){
            map.add(key, value);
            return this;
        }

        public ClientResponse build() {
            return getPath(url).queryParams(map)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
        }
    }

    public WebResource getPath(String url) {
        return createSpecificClient().resource(BASE_URI).path(url);
    }

    protected JsonNode read(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(json);
    }

    protected boolean containsJsonNode(Iterable<JsonNode> jsonNodes, String name, Object value) {
        for (JsonNode node : jsonNodes) {
            if (getValue(node, name, value).equals(value)) {
                return true;
            }
        }
        return false;
    }

    protected JsonNode getJsonNodeBy(Iterable<JsonNode> jsonNodes, String name, Object value) {
        List<Object> list = new ArrayList<>();
        for (JsonNode node : jsonNodes) {
            Object actual = getValue(node, name, value);
            list.add(actual);
            if (actual.equals(value)) {
                return node;
            }
        }
        throw new RuntimeException(String.format("Cannot find %s in %s", value, list));
    }

    private Object getValue(JsonNode node, String name, Object value) {
        JsonNode jsonNode = node.get(name);
        if (value instanceof Long) {
            return jsonNode.asLong();
        }
        if (value instanceof Integer) {
            return jsonNode.asInt();
        }
        if (value instanceof Boolean) {
            return jsonNode.asBoolean();
        }
        if (value instanceof Double) {
            return jsonNode.asDouble();
        }
        return jsonNode.asText();
    }

}
