package com.alliander.osgp.adapter.domain.smartmetering.application.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.alliander.osgp.domain.core.valueobjects.smartmetering.OsgpUnit;
import com.alliander.osgp.dto.valueobjects.smartmetering.DlmsUnitDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ScalerUnitDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ScalerUnitResponseDto;

public class StandardUnitCalulatorTest {

    @Test
    public void testCalculate() {
        final StandardUnitConverter calculator = new StandardUnitConverter();
        ScalerUnitResponseDto response = new ScalerUnitResponseDto() {

            @Override
            public ScalerUnitDto getScalerUnit() {
                return new ScalerUnitDto(DlmsUnitDto.WH, 0);
            }
        };
        assertEquals(Double.valueOf(123.456d), Double.valueOf(calculator.calculateStandardizedValue(123456l, response)));
        response = new ScalerUnitResponseDto() {

            @Override
            public ScalerUnitDto getScalerUnit() {
                return new ScalerUnitDto(DlmsUnitDto.WH, -2);
            }
        };
        assertEquals(Double.valueOf(1.235d), Double.valueOf(calculator.calculateStandardizedValue(123456l, response)));
        response = new ScalerUnitResponseDto() {

            @Override
            public ScalerUnitDto getScalerUnit() {
                return new ScalerUnitDto(DlmsUnitDto.M3, 2);
            }
        };
        assertEquals(Double.valueOf(12345600d),
                Double.valueOf(calculator.calculateStandardizedValue(123456l, response)));
        response = new ScalerUnitResponseDto() {

            @Override
            public ScalerUnitDto getScalerUnit() {
                return new ScalerUnitDto(DlmsUnitDto.M3COR, -2);
            }
        };
        assertEquals(Double.valueOf(1234.56d), Double.valueOf(calculator.calculateStandardizedValue(123456l, response)));
        response = new ScalerUnitResponseDto() {

            @Override
            public ScalerUnitDto getScalerUnit() {
                return new ScalerUnitDto(DlmsUnitDto.A, 2);
            }
        };
        try {
            calculator.calculateStandardizedValue(123456l, response);
            fail("dlms unit A not supported, expected exception");
        } catch (final IllegalArgumentException ex) {

        }
    }

    @Test
    public void testUnit() {
        final StandardUnitConverter calculator = new StandardUnitConverter();
        ScalerUnitResponseDto response = new ScalerUnitResponseDto() {

            @Override
            public ScalerUnitDto getScalerUnit() {
                return new ScalerUnitDto(DlmsUnitDto.WH, 0);
            }
        };
        assertEquals(OsgpUnit.KWH, calculator.toStandardUnit(response));
        response = new ScalerUnitResponseDto() {

            @Override
            public ScalerUnitDto getScalerUnit() {
                return new ScalerUnitDto(DlmsUnitDto.M3, 2);
            }
        };
        assertEquals(OsgpUnit.M3, calculator.toStandardUnit(response));
        response = new ScalerUnitResponseDto() {

            @Override
            public ScalerUnitDto getScalerUnit() {
                return new ScalerUnitDto(DlmsUnitDto.M3COR, 2);
            }
        };
        assertEquals(OsgpUnit.M3, calculator.toStandardUnit(response));
        response = new ScalerUnitResponseDto() {

            @Override
            public ScalerUnitDto getScalerUnit() {
                return new ScalerUnitDto(DlmsUnitDto.A, 2);
            }
        };
        try {
            calculator.toStandardUnit(response);
            fail("dlms unit A not supported, expected exception");
        } catch (final IllegalArgumentException ex) {

        }
    }
}
