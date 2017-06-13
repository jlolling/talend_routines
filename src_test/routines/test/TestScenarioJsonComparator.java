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
import routines.NumberUtil;
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
		resultNode.set("merge_source_product_id", root.get("merge_source_product_id"));
		globalMap.put("result_node", resultNode);
		ArrayNode conflictsArrayNode = resultNode.withArray("conflicts");
		globalMap.put("conflicts_node", conflictsArrayNode);
		ArrayNode arrayNodeNewROStatusNavi = resultNode.withArray("new_rightsownership_process_status_navi");
		ArrayNode arrayNodeNewROStatusCore = resultNode.withArray("new_rightsownership_process_status_core");
		ArrayNode arrayNodeNewProductODStatusNavi = resultNode.withArray("new_product_operational_data_status_navi");
		ArrayNode arrayNodeNewProductODStatusCore = resultNode.withArray("new_product_operational_data_status_core");
		ArrayNode node_product_array = (ArrayNode) doc.getNode(root, "$.products");
		if (node_product_array == null) {
			throw new Exception("No baseproducts found with the identifier: " + root.get("merge_source_product_id"));
		}
		// iterate over all products
		List<JsonNode> listAllRO = new ArrayList<JsonNode>();
		List<JsonNode> listROInConflicts = new ArrayList<JsonNode>();
		List<JsonNode> listAllProducts = new ArrayList<JsonNode>();
		List<JsonNode> listProductsInConflict = new ArrayList<JsonNode>();
		Integer cdh_score = 0;
		for (int i1 = 0, n = node_product_array.size(); i1 < n; i1++) {
			JsonNode nodeProduct1 = node_product_array.get(i1);
			if (listAllProducts.contains(nodeProduct1) == false) {
				listAllProducts.add(nodeProduct1);
			}
			// is the current product the merg_source product?
			if (NumberUtil.getNullSaveLong(nodeProduct1.get("product_id")) == NumberUtil
					.getNullSaveLong(root.get("merge_source_product_id"))) {
				cdh_score = NumberUtil.getNullSaveInt(nodeProduct1.get("cdh_score"));
			}
			// there is a reference product
			ArrayNode nodeProduct1ROArray = (ArrayNode) doc.getNode(nodeProduct1, "rightsownerships");
			// iterate over the rightsownerships
			if (nodeProduct1ROArray != null) {
				for (JsonNode nodeProduct1RO : nodeProduct1ROArray) {
					String ro1ProcessStatus = nodeProduct1RO.path("process_status").asText();
					if ("rights_ownership_not_valid".equals(ro1ProcessStatus)
							|| "rights_ownership_revoked".equals(ro1ProcessStatus)
							|| "rights_ownership_not_personalised".equals(ro1ProcessStatus)) {
						continue; // skip over this ro
					}
					if (listAllRO.contains(nodeProduct1RO) == false) {
						listAllRO.add(nodeProduct1RO);
					}
					String refValidFrom = nodeProduct1RO.path("valid_from").textValue();
					String refValidTo = nodeProduct1RO.path("valid_to").textValue();
					if (StringUtil.isEmpty(refValidFrom) || StringUtil.isEmpty(refValidTo)) {
						continue;
					}
					ArrayNode nodeProduct1RO_region_include = (ArrayNode) doc.getNode(nodeProduct1RO, "region_include");
					ArrayNode nodeProduct1RO_usage_include = (ArrayNode) doc.getNode(nodeProduct1RO, "usage_include");
					// iterate over the all products except the products we had
					// already met in the first list
					// this way we get unique product combinations regardless in
					// which order the products are in a pair
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
								String ro2ProcessStatus = nodeProduct2RO.path("process_status").textValue();
								if ("rights_ownership_not_valid".equals(ro2ProcessStatus)
										|| "rights_ownership_revoked".equals(ro2ProcessStatus)
										|| "rights_ownership_not_personalised".equals(ro2ProcessStatus)) {
									continue; // skip over this ro
								}
								if (listAllRO.contains(nodeProduct2RO) == false) {
									listAllRO.add(nodeProduct2RO);
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
									ArrayNode nodeProduct2RO_region_include = (ArrayNode) doc.getNode(nodeProduct2RO,
											"region_include");
									ArrayNode nodeProduct2RO_usage_include = (ArrayNode) doc.getNode(nodeProduct2RO,
											"usage_include");
									inConflict = true;
									JsonNode nodeInterSectRegions = null;
									JsonNode nodeInterSectUsages = null;
									if (nodeProduct1RO_region_include != null
											&& nodeProduct2RO_region_include != null) {
										nodeInterSectRegions = comp.intersect(nodeProduct1RO_region_include,
												nodeProduct2RO_region_include);
										if (nodeInterSectRegions.size() == 0) {
											inConflict = false;
										}
									}
									if (nodeProduct1RO_usage_include != null && nodeProduct2RO_usage_include != null) {
										nodeInterSectUsages = comp.intersect(nodeProduct1RO_usage_include,
												nodeProduct2RO_usage_include);
										if (nodeInterSectUsages.size() == 0) {
											inConflict = false;
										}
									}
									if (inConflict) {
										java.util.Date[] dateOverlap = TimestampUtil.getOverlappingRange(refValidFrom,
												refValidTo, compValidFrom, compValidTo);
										ObjectNode conflictNode = doc.createEmptyNode();
										conflictsArrayNode.add(conflictNode);
										conflictNode.set("merge_source_product_id",
												root.get("merge_source_product_id"));
										conflictNode.put("simple_conflict", true);
										conflictNode.put("cdh_score", cdh_score);
										conflictNode.put("conflict_from",
												TalendDate.formatDate("yyyy-MM-dd", dateOverlap[0]));
										conflictNode.put("conflict_to",
												TalendDate.formatDate("yyyy-MM-dd", dateOverlap[1]));
										conflictNode.set("region_include", nodeInterSectRegions);
										conflictNode.set("usage_include", nodeInterSectUsages);
										ObjectNode party1Node = doc.createEmptyNode();
										party1Node.set("product_id", nodeProduct1.get("product_id"));
										party1Node.set("source", nodeProduct1.get("source"));
										party1Node.set("rights_ownership_id",
												nodeProduct1RO.get("rights_ownership_id"));
										party1Node.set("owner_id", nodeProduct1RO.get("owner_id"));
										party1Node.set("rights_ownership_creation_date",
												nodeProduct1RO.get("creation_date"));
										party1Node.set("share", nodeProduct1RO.get("share_percent"));
										party1Node.set("valid_to", nodeProduct1RO.get("valid_to"));
										party1Node.set("valid_from", nodeProduct1RO.get("valid_from"));
										party1Node.set("ro_usages", nodeProduct1RO.get("usage_include"));
										party1Node.set("ro_regions", nodeProduct1RO.get("region_include"));
										party1Node.set("assignee", nodeProduct1.get("assignee"));
										party1Node.set("isrc_list", nodeProduct1.get("isrc_list"));
										party1Node.set("track_id_label", nodeProduct1.get("track_id_label"));
										party1Node.set("title", nodeProduct1.get("title"));
										party1Node.set("titlesupplement", nodeProduct1.get("titlesupplement"));
										party1Node.set("mainartist", nodeProduct1.get("display_mainartist"));
										party1Node.set("genre", nodeProduct1.get("genre"));
										party1Node.set("recording_year", nodeProduct1.get("recording_year"));
										party1Node.set("recording_country", nodeProduct1.get("recording_country"));
										party1Node.set("publication_date", nodeProduct1.get("publication_date"));
										party1Node.set("productduration", nodeProduct1.get("productduration"));
										party1Node.set("composer", nodeProduct1.get("composer"));
										ObjectNode disputeParty1 = (ObjectNode) nodeProduct2RO.get("businesspartner")
												.deepCopy();
										disputeParty1.set("rights_ownership_id",
												nodeProduct2RO.get("rights_ownership_id"));
										disputeParty1.set("dispute_share", nodeProduct2RO.get("share_percent"));
										party1Node.withArray("dispute_parties").add(disputeParty1);
										conflictNode.withArray("conflict_parties").add(party1Node);
										ObjectNode party2Node = doc.createEmptyNode();
										party2Node.set("product_id", nodeProduct2.get("product_id"));
										party2Node.set("source", nodeProduct2.get("source"));
										party2Node.set("rights_ownership_id",
												nodeProduct2RO.get("rights_ownership_id"));
										party2Node.set("owner_id", nodeProduct2RO.get("owner_id"));
										party2Node.set("rights_ownership_creation_date",
												nodeProduct2RO.get("creation_date"));
										party2Node.set("share", nodeProduct2RO.get("share_percent"));
										party2Node.set("valid_to", nodeProduct2RO.get("valid_to"));
										party2Node.set("valid_from", nodeProduct2RO.get("valid_from"));
										party2Node.set("ro_usages", nodeProduct2RO.get("usage_include"));
										party2Node.set("ro_regions", nodeProduct2RO.get("region_include"));
										party2Node.set("assignee", nodeProduct2.get("assignee"));
										party2Node.set("isrc_list", nodeProduct2.get("isrc_list"));
										party2Node.set("track_id_label", nodeProduct2.get("track_id_label"));
										party2Node.set("title", nodeProduct2.get("title"));
										party2Node.set("titlesupplement", nodeProduct2.get("titlesupplement"));
										party2Node.set("mainartist", nodeProduct2.get("display_mainartist"));
										party2Node.set("genre", nodeProduct2.get("genre"));
										party2Node.set("recording_year", nodeProduct2.get("recording_year"));
										party2Node.set("recording_country", nodeProduct2.get("recording_country"));
										party2Node.set("publication_date", nodeProduct2.get("publication_date"));
										party2Node.set("productduration", nodeProduct2.get("productduration"));
										party2Node.set("composer", nodeProduct2.get("composer"));
										ObjectNode disputeParty2 = (ObjectNode) nodeProduct1RO.get("businesspartner")
												.deepCopy();
										disputeParty2.set("rights_ownership_id",
												nodeProduct1RO.get("rights_ownership_id"));
										disputeParty2.set("dispute_share", nodeProduct1RO.get("share_percent"));
										party2Node.withArray("dispute_parties").add(disputeParty2);
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
						} // if (nodeProduct2ROArray != null)
					} // for (int i2 = i1 + 1; i2 < n; i2++)
				} // for (JsonNode nodeProduct1RO : nodeProduct1ROArray)
			} // if (nodeProduct1ROArray != null)
		} // for (int i1 = 0, n = node_product_array.size(); i1 < n; i1++)
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
				// set the process_status regardless which former status it had
				ObjectNode status = doc.createEmptyNode();
				status.set("rights_ownership_id", ro.get("rights_ownership_id"));
				status.put("new_process_status", "rights_ownership_valid");
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
