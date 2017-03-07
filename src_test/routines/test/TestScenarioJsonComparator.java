package routines.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
		File testFile = new File("/var/testdata/json/cdh.json");
		de.cimt.talendcomp.json.JsonDocument doc = new de.cimt.talendcomp.json.JsonDocument(testFile);
		globalMap.put("tJSONDocOpen_1", doc);
	}
	
	@Test
	public void calculateConflicts() throws Exception {
		JsonComparator comp = new JsonComparator();
		JsonDocument doc = (JsonDocument) globalMap.get("tJSONDocOpen_1");
		ObjectNode root = (ObjectNode) doc.getRootNode();
		// prepare the result node
		ObjectNode resultNode = doc.createObjectNode("result");
		globalMap.put("result_node", resultNode);
		ArrayNode conflictsArrayNode = resultNode.withArray("conflicts");
		globalMap.put("conflicts_node", conflictsArrayNode);
		ArrayNode arrayNodeNewROStatusNavi = resultNode.withArray("new_rightsownership_process_status_navi");
		ArrayNode arrayNodeNewROStatusCore = resultNode.withArray("new_rightsownership_process_status_core");
		ArrayNode arrayNodeNewProductODStatusNavi = resultNode.withArray("new_product_operational_data_status_navi");
		ArrayNode arrayNodeNewProductODStatusCore = resultNode.withArray("new_product_operational_data_status_core");
		ArrayNode node_product_array = (ArrayNode) doc.getNode(root, "$.products");
		// iterate over all products
		List<JsonNode> listAllRO = new ArrayList<JsonNode>();
		List<JsonNode> listROInConflicts = new ArrayList<JsonNode>();
		List<JsonNode> listAllProducts = new ArrayList<JsonNode>();
		List<JsonNode> listProductsInConflict = new ArrayList<JsonNode>();
		for (int i1 = 0, n = node_product_array.size(); i1 < n; i1++) {
			JsonNode nodeProduct1 = node_product_array.get(i1);
			if (listAllProducts.contains(nodeProduct1) == false) {
				listAllProducts.add(nodeProduct1);
			}
			// there is a reference product
			ArrayNode nodeProduct1ROArray = (ArrayNode) doc.getNode(nodeProduct1, "rightsownerships");
			// iterate over the rightsownerships
			for (JsonNode nodeProduct1RO : nodeProduct1ROArray) {
				if (listAllRO.contains(nodeProduct1RO) == false) {
					listAllRO.add(nodeProduct1RO);
				}
				String ro1ProcessStatus = nodeProduct1RO.path("process_status").asText();
				if ("rights_ownership_deactivated".equals(ro1ProcessStatus) || "rights_ownership_revoked".equals(ro1ProcessStatus)) {
					continue; // skip over this ro
				}
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
					if (listAllProducts.contains(nodeProduct2) == false) {
						listAllProducts.add(nodeProduct2);
					}
					// iterate over the products to compare
					ArrayNode nodeProduct2ROArray = (ArrayNode) doc.getNode(nodeProduct2, "rightsownerships");
					if (nodeProduct2ROArray != null) {
						// iterate over the rightsownerships
						for (JsonNode nodeProduct2RO : nodeProduct2ROArray) {
							if (listAllRO.contains(nodeProduct2RO) == false) {
								listAllRO.add(nodeProduct2RO);
							}
							String ro2ProcessStatus = nodeProduct2RO.path("process_status").textValue();
							if ("rights_ownership_deactivated".equals(ro2ProcessStatus)) {
								continue; // skip over this ro
							}
							// now check the valid date ranges
							String compValidFrom = nodeProduct2RO.path("valid_from").textValue();
							String compValidTo = nodeProduct2RO.path("valid_to").textValue();
							if (StringUtil.isEmpty(compValidFrom) || StringUtil.isEmpty(compValidTo)) {
								continue;
							}
							boolean inConflict = false;
							if (TimestampUtil.isOverlapping(refValidFrom, refValidTo, compValidFrom, compValidTo)) {
								// ok the valid date ranges are overlapping
								// lets check the regions and usages
								ArrayNode nodeProduct2RO_region_include = (ArrayNode) doc
										.getNode(nodeProduct2RO, "region_include");
								ArrayNode nodeProduct2RO_usage_include = (ArrayNode) doc
										.getNode(nodeProduct2RO, "usage_include");
								inConflict = true;
								JsonNode nodeInterSectRegions = null;
								JsonNode nodeInterSectUsages = null;
								if (nodeProduct1RO_region_include != null && nodeProduct2RO_region_include != null) {
									nodeInterSectRegions = comp.intersect(nodeProduct1RO_region_include, nodeProduct2RO_region_include);
									if (nodeInterSectRegions.size() == 0) {
										inConflict = false;
									}
								}
								if (nodeProduct1RO_usage_include != null && nodeProduct2RO_usage_include != null) {
									nodeInterSectUsages = comp.intersect(nodeProduct1RO_usage_include, nodeProduct2RO_usage_include);
									if (nodeInterSectUsages.size() == 0) {
										inConflict = false;
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
									conflictNode.set("usage_include", nodeInterSectUsages);
									ObjectNode party1Node = doc.createEmptyNode();
									party1Node.set("product_id", nodeProduct1.get("product_id"));
									party1Node.set("source", nodeProduct1.get("source"));
									party1Node.set("rights_ownership_id", nodeProduct1RO.get("rights_ownership_id"));
									party1Node.put("share_percent", 100);
									conflictNode.withArray("conflict_parties").add(party1Node);
									ObjectNode party2Node = doc.createEmptyNode();
									party2Node.set("product_id", nodeProduct2.get("product_id"));
									party2Node.set("source", nodeProduct2.get("source"));
									party2Node.set("rights_ownership_id", nodeProduct2RO.get("rights_ownership_id"));
									party2Node.put("share_percent", 100);
									conflictNode.withArray("conflict_parties").add(party2Node);
									// set product status
									if (listProductsInConflict.contains(nodeProduct1) == false) {
										listProductsInConflict.add(nodeProduct1);
									}
									if (listProductsInConflict.contains(nodeProduct2) == false) {
										listProductsInConflict.add(nodeProduct2);
									}
									if (listROInConflicts.contains(nodeProduct1RO) == false) {
										listROInConflicts.add(nodeProduct1RO);
									}
									if (listROInConflicts.contains(nodeProduct2RO) == false) {
										listROInConflicts.add(nodeProduct2RO);
									}
								}
							}
						}
					}
				}
			}
		}
		// now set the status for all ROs not in conflict
		for (JsonNode ro : listAllRO) {
			if (listROInConflicts.contains(ro)) {
				ObjectNode status = doc.createEmptyNode();
				status.set("rights_ownership_id", ro.get("rights_ownership_id"));
				status.put("new_process_status", "rights_ownership_in_conflict");
				status.set("prev_process_status", ro.get("process_status"));
				status.set("source", ro.get("source"));
				String source = ro.path("source").textValue();
				if ("navi".equals(source)) {
					arrayNodeNewROStatusNavi.add(status);
				} else {
					arrayNodeNewROStatusCore.add(status);
				}
			} else {
				// this is a RO without any conflicts
				String processStatus = ro.path("process_status").textValue();
				if ("rights_ownership_in_conflict".equals(processStatus)) {
					ObjectNode status = doc.createEmptyNode();
					status.set("rights_ownership_id", ro.get("rights_ownership_id"));
					status.put("new_process_status", "rights_ownership_conflict_winner");
					status.set("prev_process_status", ro.get("process_status"));
					status.set("source", ro.get("source"));
					String source = ro.path("source").textValue();
					if ("navi".equals(source)) {
						arrayNodeNewROStatusNavi.add(status);
					} else {
						arrayNodeNewROStatusCore.add(status);
					}
				} else {
					ObjectNode status = doc.createEmptyNode();
					status.set("rights_ownership_id", ro.get("rights_ownership_id"));
					status.put("new_process_status", "rights_ownership_not_in_conflict");
					status.set("prev_process_status", ro.get("process_status"));
					status.set("source", ro.get("source"));
					String source = ro.path("source").textValue();
					if ("navi".equals(source)) {
						arrayNodeNewROStatusNavi.add(status);
					} else {
						arrayNodeNewROStatusCore.add(status);
					}
				}
			}
		}
		for (JsonNode product : listAllProducts) {
			ObjectNode status = doc.createEmptyNode();
			status.set("product_id", product.get("product_id"));
			if (listProductsInConflict.contains(product)) {
				status.put("navi_process_status", "cdh_in_dispute");
			} else {
				status.put("navi_process_status", "navi_ready_for_merge");
			}
			String source = product.path("source").textValue();
			if ("navi".equals(source)) {
				arrayNodeNewProductODStatusNavi.add(status);
			} else {
				arrayNodeNewProductODStatusCore.add(status);
			}
		}
		System.out.println(doc.getJsonString(true, false));
		assertEquals(5, conflictsArrayNode.size());
		assertEquals(7, arrayNodeNewROStatusNavi.size());
		assertEquals(1, arrayNodeNewROStatusCore.size());
		assertEquals(3, arrayNodeNewProductODStatusNavi.size());
		assertEquals(1, arrayNodeNewProductODStatusCore.size());
	}

}
