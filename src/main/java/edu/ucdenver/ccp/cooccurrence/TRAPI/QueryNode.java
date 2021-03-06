package edu.ucdenver.ccp.cooccurrence.TRAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryNode {
    private List<String> ids;
    private List<String> categories;
    private boolean isSet;
    private List<Object> constraints;

    public QueryNode() {
        ids = new ArrayList<>();
        categories = new ArrayList<>();
        constraints = new ArrayList<>();
        isSet = false;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public void addId(String id) {
        ids.add(id);
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public List<Object> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Object> constraints) {
        this.constraints = constraints;
    }

    public JsonNode toJSON() {
        ObjectMapper om = new ObjectMapper();
        ObjectNode nodeNode = om.createObjectNode();
        if (ids.size() > 0) {
            nodeNode.set("ids", om.convertValue(ids, ArrayNode.class));
        }
        if (categories.size() > 0) {
            nodeNode.set("categories", om.convertValue(categories, ArrayNode.class));
        }
        if (constraints.size() > 0) {
            nodeNode.set("constraints", om.convertValue(constraints, ArrayNode.class));
        }
        if (isSet) {
            nodeNode.put("is_set", true);
        }
        return nodeNode;
    }

    public static QueryNode parseJSON(JsonNode jsonQNode) {
        // Note: the YAML definition of QNode pluralizes "ids" and "categories" but the simple.json example in ReasonerAPI uses the singular.
        if (!(jsonQNode.hasNonNull("id") || jsonQNode.hasNonNull("ids")) &&
                !(jsonQNode.hasNonNull("category") || jsonQNode.hasNonNull("categories"))) {
            return null;
        }
        QueryNode node = new QueryNode();
        if (jsonQNode.hasNonNull("is_set")) {
            node.setSet(jsonQNode.get("is_set").asBoolean());
        }
        if (jsonQNode.hasNonNull("id") || jsonQNode.hasNonNull("ids")) {
            JsonNode idNode = jsonQNode.hasNonNull("id") ? jsonQNode.get("id") : jsonQNode.get("ids");
            if (idNode.isArray()) {
                Iterator<JsonNode> ids = idNode.elements();
                while (ids.hasNext()) {
                    JsonNode innerIdNode = ids.next();
                    node.addId(innerIdNode.asText());
                }
            } else if (idNode.isValueNode()) {
                node.addId(idNode.asText());
            } else {
                return null;
            }
        }
        if (jsonQNode.hasNonNull("category") || jsonQNode.hasNonNull("categories")) {
            JsonNode categoryNode = jsonQNode.hasNonNull("category") ? jsonQNode.get("category") : jsonQNode.get("categories");
            if (categoryNode.isArray()) {
                Iterator<JsonNode> cats = categoryNode.elements();
                while (cats.hasNext()) {
                    JsonNode innerCategoryNode = cats.next();
                    node.addCategory(innerCategoryNode.asText());
                }
            } else if (categoryNode.isValueNode()) {
                node.addCategory(categoryNode.asText());
            } else {
                return null;
            }
        }
        return node;
    }
}
