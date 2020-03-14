package org.owasp.dependencycheck.data.nvdcve;

import java.util.stream.Collectors;

import org.owasp.dependencycheck.data.nvd.json.DefCveItem;
import org.owasp.dependencycheck.dependency.VulnerableSoftware;

/**
 * 
 * Utility for processing {@linkplain DefCveItem} in order to extract key values like textual description and ecosystem type. 
 *
 */

public class CveItemOperator {
    
    public String extractDescription(DefCveItem cve) {
        return cve.getCve().getDescription().getDescriptionData().stream().filter((desc)
                -> "en".equals(desc.getLang())).map(d
                -> d.getValue()).collect(Collectors.joining(" "));
    }
    
    /**
     * Attempts to determine the ecosystem based on the vendor, product and
     * targetSw.
     *
     * @param baseEcosystem the base ecosystem
     * @param vendor the vendor
     * @param product the product
     * @param targetSw the target software
     * @return the ecosystem if one is identified
     */
    private String extractEcosystem(String baseEcosystem, String vendor, String product, String targetSw) {
        if ("ibm".equals(vendor) && "java".equals(product)) {
            return "c/c++";
        }
        if ("oracle".equals(vendor) && "vm".equals(product)) {
            return "c/c++";
        }
        if ("*".equals(targetSw) || baseEcosystem != null) {
            return baseEcosystem;
        }
        return targetSw;
    }

    public String extractEcosystem(String baseEcosystem, VulnerableSoftware parsedCpe) {
        return extractEcosystem(baseEcosystem, parsedCpe.getVendor(), parsedCpe.getProduct(), parsedCpe.getTargetSw());
    }
    
    public boolean isRejected(String description) {
        return description.startsWith("** REJECT **");
    }

}
