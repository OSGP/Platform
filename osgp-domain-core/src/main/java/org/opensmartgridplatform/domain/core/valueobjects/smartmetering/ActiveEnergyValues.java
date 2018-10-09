package org.opensmartgridplatform.domain.core.valueobjects.smartmetering;

public class ActiveEnergyValues {
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
