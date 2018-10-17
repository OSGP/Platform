package org.opensmartgridplatform.domain.core.valueobjects.smartmetering;

import java.io.Serializable;

public class ActiveEnergyValues implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 2837927976042182726L;
    private final OsgpMeterValue activeEnergyImport;
    private final OsgpMeterValue activeEnergyExport;
    private final OsgpMeterValue activeEnergyImportTariffOne;
    // may be null
    private final OsgpMeterValue activeEnergyImportTariffTwo;
    private final OsgpMeterValue activeEnergyExportTariffOne;
    // may be null
    private final OsgpMeterValue activeEnergyExportTariffTwo;

    public ActiveEnergyValues(OsgpMeterValue activeEnergyImport, OsgpMeterValue activeEnergyExport,
            OsgpMeterValue activeEnergyImportTariffOne, OsgpMeterValue activeEnergyImportTariffTwo,
            OsgpMeterValue activeEnergyExportTariffOne, OsgpMeterValue activeEnergyExportTariffTwo) {
        this.activeEnergyImport = activeEnergyImport;
        this.activeEnergyExport = activeEnergyExport;
        this.activeEnergyImportTariffOne = activeEnergyImportTariffOne;
        this.activeEnergyImportTariffTwo = activeEnergyImportTariffTwo;
        this.activeEnergyExportTariffOne = activeEnergyExportTariffOne;
        this.activeEnergyExportTariffTwo = activeEnergyExportTariffTwo;
    }

    public OsgpMeterValue getActiveEnergyImport() {
        return activeEnergyImport;
    }

    public OsgpMeterValue getActiveEnergyExport() {
        return activeEnergyExport;
    }

    public OsgpMeterValue getActiveEnergyImportTariffOne() {
        return activeEnergyImportTariffOne;
    }

    public OsgpMeterValue getActiveEnergyImportTariffTwo() {
        return activeEnergyImportTariffTwo;
    }

    public OsgpMeterValue getActiveEnergyExportTariffOne() {
        return activeEnergyExportTariffOne;
    }

    public OsgpMeterValue getActiveEnergyExportTariffTwo() {
        return activeEnergyExportTariffTwo;
    }
}
