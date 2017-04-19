package com.company.test.model.xml;

import com.company.model.xml.JavaObjectXmlConverter;
import com.company.test.model.xjc.generated.SomeModel;
import com.company.test.model.xjc.generated.SomeModels;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Di on 16.04.2017.
 */
public class JavaObjectXmlConverterTest
{

    @Test
    public void writeReadXMLTest()
    {
        int intVal = 3;
        double doubleVal = 3.52;
        boolean boolVal = true;
        String strVal = "someWord";

        SomeModel someModel = new SomeModel();

        someModel.setIntVal(BigInteger.valueOf(intVal));
        someModel.setDoubleVal(BigDecimal.valueOf(doubleVal));
        someModel.setBoolVal(boolVal);
        someModel.setStringVal(strVal);

        SomeModels someModelsTrue = new SomeModels();
        someModelsTrue.getSomeModels().add(someModel);

        String path = "./src/com/company/test/model/xml/someModel";

        JavaObjectXmlConverter.writeToXML(someModelsTrue,
                SomeModels.class, path);

        SomeModels someModels = JavaObjectXmlConverter.readFromXML(SomeModels.class, path);

        assertTrue(someModel.getIntVal().intValue() ==
                someModels.getSomeModels().get(0).getIntVal().intValue());
        assertTrue(someModel.getDoubleVal().doubleValue() ==
                someModels.getSomeModels().get(0).getDoubleVal().doubleValue());
        assertTrue(someModel.isBoolVal() ==
                someModels.getSomeModels().get(0).isBoolVal());
        assertTrue(someModel.getStringVal()
                .equals(someModels.getSomeModels().get(0).getStringVal()));
    }






}
