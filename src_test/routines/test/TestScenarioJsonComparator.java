package routines.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.cimt.talendcomp.json.JsonComparator;
import de.cimt.talendcomp.json.JsonDocument;
import de.cimt.talendcomp.test.TalendFakeJob;
import routines.StringUtil;
import routines.TalendDate;
import routines.TimestampUtil;

public class TestScenarioJsonComparator extends TalendFakeJob {
	
	@Before
	public void setupInput() throws Exception {
		String input = "{\n"
			    + "  \"merge_source_product_id\" : 137106627,\n"
			    + "  \"products\" : [ {\n"
			    + "    \"product_id\" : 137106629,\n"
			    + "    \"source\" : \"navi\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 137107911,\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } ]\n"
			    + "  }, {\n"
			    + "    \"product_id\" : 137106628,\n"
			    + "    \"source\" : \"navi\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 137107909,\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } ]\n"
			    + "  }, {\n"
			    + "    \"product_id\" : 137106627,\n"
			    + "    \"source\" : \"navi\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 137107907,\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } ]\n"
			    + "  } ]\n"
			    + "}";
		de.cimt.talendcomp.json.JsonDocument doc = new de.cimt.talendcomp.json.JsonDocument(input);
		globalMap.put("tJSONDocOpen_1", doc);
	}
	
	@Test
	public void calculateConflicts() throws Exception {
		JsonComparator comp = new JsonComparator();
		JsonDocument doc = (JsonDocument) globalMap.get("tJSONDocOpen_1");
		ObjectNode root = (ObjectNode) doc.getRootNode();
		// prepare the result node
		ArrayNode conflictsArrayNode = doc.createArrayNode("conflicts");
		ArrayNode node_product_array = (ArrayNode) doc.getNode(root, "$.products");
		// iterate over all products
		for (int i1 = 0, n = node_product_array.size(); i1 < n; i1++) {
			JsonNode nodeProduct1 = node_product_array.get(i1);
			// there is a reference product
			ArrayNode nodeProduct1ROArray = (ArrayNode) doc.getNode(nodeProduct1, "rightsownerships");
			// iterate over the rightsownerships
			for (JsonNode nodeProduct1RO : nodeProduct1ROArray) {
				String refValidFrom = nodeProduct1RO.path("valid_from").textValue();
				String refValidTo = nodeProduct1RO.path("valid_to").textValue();
				if (StringUtil.isEmpty(refValidFrom) || StringUtil.isEmpty(refValidTo)) {
					continue;
				}
				ArrayNode nodeProduct1RO_region_include = (ArrayNode) doc.getNode(nodeProduct1RO, "region_include");
				ArrayNode nodeProduct1RO_usage_include = (ArrayNode) doc.getNode(nodeProduct1RO, "usage_include");
				// iterate over the all products except the products we had already met in the first list
				// this way we get unique product combinations regardless in which order the products are in a pair
				for (int i2 = i1 + 1; i2 < n; i2++) {
					JsonNode nodeProduct2 = node_product_array.get(i2);
					// iterate over the products to compare
					ArrayNode nodeProduct2ROArray = (ArrayNode) doc.getNode(nodeProduct2, "rightsownerships");
					if (nodeProduct2ROArray != null) {
						// iterate over the rightsownerships
						for (JsonNode nodeProduct2RO : nodeProduct2ROArray) {
							// now check the valid date ranges
							String compValidFrom = nodeProduct2RO.path("valid_from").textValue();
							String compValidTo = nodeProduct2RO.path("valid_to").textValue();
							if (StringUtil.isEmpty(compValidFrom) || StringUtil.isEmpty(compValidTo)) {
								continue;
							}
							if (TimestampUtil.isOverlapping(refValidFrom, refValidTo, compValidFrom, compValidTo)) {
								// ok the valid date ranges are overlapping
								// lets check the regions and usages
								ArrayNode nodeProduct2RO_region_include = (ArrayNode) doc
										.getNode(nodeProduct2RO, "region_include");
								ArrayNode nodeProduct2RO_usage_include = (ArrayNode) doc
										.getNode(nodeProduct2RO, "usage_include");
								boolean inConflict = false;
								JsonNode nodeInterSectRegions = null;
								JsonNode nodeInterSectUsages = null;
								if (nodeProduct1RO_region_include != null && nodeProduct2RO_region_include != null) {
									nodeInterSectRegions = comp.intersect(nodeProduct1RO_region_include, nodeProduct2RO_region_include);
									if (nodeInterSectRegions.size() > 0) {
										inConflict = true;
									}
								}
								if (nodeProduct1RO_usage_include != null && nodeProduct2RO_usage_include != null) {
									nodeInterSectUsages = comp.intersect(nodeProduct1RO_usage_include, nodeProduct2RO_usage_include);
									if (nodeInterSectUsages.size() > 0) {
										inConflict = true;
									}
								}
								if (inConflict) {
									java.util.Date[] dateOverlap = TimestampUtil.getOverlappingRange(refValidFrom, refValidTo, compValidFrom, compValidTo);
									ObjectNode conflictNode = doc.createEmptyNode();
									conflictsArrayNode.add(conflictNode);
									conflictNode.set("merge_source_product_id", root.get("merge_source_product_id"));
									conflictNode.put("simple_conflict", true);
									conflictNode.put("conflict_from", TalendDate.formatDate("yyyy-MM-dd", dateOverlap[0]));
									conflictNode.put("conflict_to", TalendDate.formatDate("yyyy-MM-dd", dateOverlap[1]));
									conflictNode.set("region_include", nodeInterSectRegions);
									conflictNode.set("region_include", nodeInterSectUsages);
									ObjectNode party1Node = doc.createEmptyNode();
									party1Node.set("product_id", nodeProduct1.get("product_id"));
									party1Node.set("rightsownership_id", nodeProduct1RO.get("rights_ownership_id"));
									party1Node.put("share_percent", 100);
									conflictNode.withArray("conflict_parties").add(party1Node);
									ObjectNode party2Node = doc.createEmptyNode();
									party2Node.set("product_id", nodeProduct2.get("product_id"));
									party2Node.set("rightsownership_id", nodeProduct2RO.get("rights_ownership_id"));
									party2Node.put("share_percent", 100);
									conflictNode.withArray("conflict_parties").add(party2Node);
								}
							}
						}
					}
				}
			}
		}

		System.out.println(doc.getJsonString(true, false));
		assertEquals(3, conflictsArrayNode.size());
	}

}
