begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|type
operator|.
name|encoded
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|type
operator|.
name|AegisType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|type
operator|.
name|basic
operator|.
name|BeanTypeInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|xml
operator|.
name|stax
operator|.
name|ElementReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|SoapArrayTypeTest
extends|extends
name|AbstractEncodedTest
block|{
specifier|private
specifier|static
specifier|final
name|String
index|[]
index|[]
index|[]
name|ARRAY_2_3_4
init|=
operator|new
name|String
index|[]
index|[]
index|[]
block|{
operator|new
name|String
index|[]
index|[]
block|{
operator|new
name|String
index|[]
block|{
literal|"row1 col1 dep1"
block|,
literal|"row1 col1 dep2"
block|,
literal|"row1 col1 dep3"
block|,
literal|"row1 col1 dep4"
block|}
block|,
operator|new
name|String
index|[]
block|{
literal|"row1 col2 dep1"
block|,
literal|"row1 col2 dep2"
block|,
literal|"row1 col2 dep3"
block|,
literal|"row1 col2 dep4"
block|}
block|,
operator|new
name|String
index|[]
block|{
literal|"row1 col3 dep1"
block|,
literal|"row1 col3 dep2"
block|,
literal|"row1 col3 dep3"
block|,
literal|"row1 col3 dep4"
block|}
block|,         }
block|,
operator|new
name|String
index|[]
index|[]
block|{
operator|new
name|String
index|[]
block|{
literal|"row2 col1 dep1"
block|,
literal|"row2 col1 dep2"
block|,
literal|"row2 col1 dep3"
block|,
literal|"row2 col1 dep4"
block|}
block|,
operator|new
name|String
index|[]
block|{
literal|"row2 col2 dep1"
block|,
literal|"row2 col2 dep2"
block|,
literal|"row2 col2 dep3"
block|,
literal|"row2 col2 dep4"
block|}
block|,
operator|new
name|String
index|[]
block|{
literal|"row2 col3 dep1"
block|,
literal|"row2 col3 dep2"
block|,
literal|"row2 col3 dep3"
block|,
literal|"row2 col3 dep4"
block|}
block|,         }
block|,     }
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// address type
name|BeanTypeInfo
name|addressInfo
init|=
operator|new
name|BeanTypeInfo
argument_list|(
name|Address
operator|.
name|class
argument_list|,
literal|"urn:Bean"
argument_list|)
decl_stmt|;
name|addressInfo
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|StructType
name|addressType
init|=
operator|new
name|StructType
argument_list|(
name|addressInfo
argument_list|)
decl_stmt|;
name|addressType
operator|.
name|setTypeClass
argument_list|(
name|Address
operator|.
name|class
argument_list|)
expr_stmt|;
name|addressType
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"address"
argument_list|)
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|addressType
argument_list|)
expr_stmt|;
comment|// purchase order type
name|BeanTypeInfo
name|poInfo
init|=
operator|new
name|BeanTypeInfo
argument_list|(
name|PurchaseOrder
operator|.
name|class
argument_list|,
literal|"urn:Bean"
argument_list|)
decl_stmt|;
name|poInfo
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|StructType
name|purchaseOrderType
init|=
operator|new
name|StructType
argument_list|(
name|poInfo
argument_list|)
decl_stmt|;
name|purchaseOrderType
operator|.
name|setTypeClass
argument_list|(
name|PurchaseOrder
operator|.
name|class
argument_list|)
expr_stmt|;
name|purchaseOrderType
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|purchaseOrderType
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"po"
argument_list|)
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|purchaseOrderType
argument_list|)
expr_stmt|;
comment|// String[][][]
name|SoapArrayType
name|arrayOfString
init|=
name|createArrayType
argument_list|(
name|String
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"SOAPArrayOfString"
argument_list|)
argument_list|)
decl_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|arrayOfString
argument_list|)
expr_stmt|;
name|SoapArrayType
name|arrayOfArrayOfString
init|=
name|createArrayType
argument_list|(
name|String
index|[]
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"SOAPArrayOfArrayOfString"
argument_list|)
argument_list|)
decl_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|arrayOfArrayOfString
argument_list|)
expr_stmt|;
name|SoapArrayType
name|arrayOfArrayOfArrayOfString
init|=
name|createArrayType
argument_list|(
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"SOAPArrayOfArrayOfArrayOfString"
argument_list|)
argument_list|)
decl_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|arrayOfArrayOfArrayOfString
argument_list|)
expr_stmt|;
comment|// int[][]
name|SoapArrayType
name|arrayOfInt
init|=
name|createArrayType
argument_list|(
name|int
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"SOAPArrayOfInt"
argument_list|)
argument_list|)
decl_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|arrayOfInt
argument_list|)
expr_stmt|;
name|SoapArrayType
name|arrayOfArrayOfInt
init|=
name|createArrayType
argument_list|(
name|int
index|[]
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"SOAPArrayOfArrayOfInt"
argument_list|)
argument_list|)
decl_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|arrayOfArrayOfInt
argument_list|)
expr_stmt|;
comment|// Object[]
name|SoapArrayType
name|arrayOfAddress
init|=
name|createArrayType
argument_list|(
name|Address
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"SOAPArrayOfAddress"
argument_list|)
argument_list|)
decl_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|arrayOfAddress
argument_list|)
expr_stmt|;
name|SoapArrayType
name|arrayOfAny
init|=
name|createArrayType
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"SOAPArrayOfAny"
argument_list|)
argument_list|)
decl_stmt|;
name|mapping
operator|.
name|register
argument_list|(
name|arrayOfAny
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleArray
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// xsd:int[2]
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arraySimple.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|int
index|[]
name|numbers
init|=
operator|(
name|int
index|[]
operator|)
name|createArrayType
argument_list|(
name|int
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|numbers
argument_list|)
expr_stmt|;
comment|// round trip tests
name|numbers
operator|=
name|readWriteReadRef
argument_list|(
literal|"arraySimple.xml"
argument_list|,
name|int
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|,
name|numbers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUrTypeArray
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// ur-type[4] nested elements have xsi:type
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arrayUrType1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
index|[]
name|objects
init|=
operator|(
name|Object
index|[]
operator|)
name|createArrayType
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|42
block|,
operator|(
name|float
operator|)
literal|42.42
block|,
literal|"Forty Two"
block|}
argument_list|,
name|objects
argument_list|)
expr_stmt|;
comment|// ur-type[4] nested element name have a global schema type
name|reader
operator|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arrayUrType2.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|objects
operator|=
operator|(
name|Object
index|[]
operator|)
name|createArrayType
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|objects
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|42
block|,
operator|new
name|BigDecimal
argument_list|(
literal|"42.42"
argument_list|)
block|,
literal|"Forty Two"
block|}
argument_list|,
name|objects
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUrTypeArrayReadWriteRef1
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
index|[]
name|objects
decl_stmt|;
comment|// round trip tests
name|objects
operator|=
name|readWriteReadRef
argument_list|(
literal|"arrayUrType1.xml"
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|42
block|,
name|Float
operator|.
name|valueOf
argument_list|(
literal|42.42f
argument_list|)
block|,
literal|"Forty Two"
block|}
argument_list|,
name|objects
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUrTypeArrayReadWriteRef2
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
index|[]
name|objects
decl_stmt|;
comment|// round trip tests
name|objects
operator|=
name|readWriteReadRef
argument_list|(
literal|"arrayUrType2.xml"
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|42
block|,
operator|new
name|BigDecimal
argument_list|(
literal|"42.42"
argument_list|)
block|,
literal|"Forty Two"
block|}
argument_list|,
name|objects
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnyTypeArray
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// ur-type[4] nested elements have xsi:type
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arrayAnyType1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
index|[]
name|objects
init|=
operator|(
name|Object
index|[]
operator|)
name|createArrayType
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|42
block|,
operator|(
name|float
operator|)
literal|42.42
block|,
literal|"Forty Two"
block|}
argument_list|,
name|objects
argument_list|)
expr_stmt|;
comment|// ur-type[4] nested element name have a global schema type
name|reader
operator|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arrayAnyType2.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|objects
operator|=
operator|(
name|Object
index|[]
operator|)
name|createArrayType
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|42
block|,
operator|new
name|BigDecimal
argument_list|(
literal|"42.42"
argument_list|)
block|,
literal|"Forty Two"
block|}
argument_list|,
name|objects
argument_list|)
expr_stmt|;
comment|// round trip tests
name|objects
operator|=
name|readWriteReadRef
argument_list|(
literal|"arrayAnyType1.xml"
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|42
block|,
operator|(
name|float
operator|)
literal|42.42
block|,
literal|"Forty Two"
block|}
argument_list|,
name|objects
argument_list|)
expr_stmt|;
name|objects
operator|=
name|readWriteReadRef
argument_list|(
literal|"arrayAnyType2.xml"
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|42
block|,
operator|new
name|BigDecimal
argument_list|(
literal|"42.42"
argument_list|)
block|,
literal|"Forty Two"
block|}
argument_list|,
name|objects
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStructArray
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// b:address[2]
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arrayStructs.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Address
index|[]
name|addresses
init|=
operator|(
name|Address
index|[]
operator|)
name|createArrayType
argument_list|(
name|Address
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|AbstractEncodedTest
operator|.
name|validateShippingAddress
argument_list|(
name|addresses
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|AbstractEncodedTest
operator|.
name|validateBillingAddress
argument_list|(
name|addresses
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
comment|// round trip tests
name|addresses
operator|=
name|readWriteReadRef
argument_list|(
literal|"arrayStructs.xml"
argument_list|,
name|Address
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|AbstractEncodedTest
operator|.
name|validateShippingAddress
argument_list|(
name|addresses
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|AbstractEncodedTest
operator|.
name|validateBillingAddress
argument_list|(
name|addresses
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSquareArray
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// xsd:string[2,3,4]
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arraySquare.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|String
index|[]
index|[]
index|[]
name|strings
init|=
operator|(
name|String
index|[]
index|[]
index|[]
operator|)
name|createArrayType
argument_list|(
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|ARRAY_2_3_4
argument_list|,
name|strings
argument_list|)
expr_stmt|;
comment|// round trip tests
name|strings
operator|=
name|readWriteReadRef
argument_list|(
literal|"arraySquare.xml"
argument_list|,
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|ARRAY_2_3_4
argument_list|,
name|strings
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArrayOfArrays
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// xsd:string[,][2]
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arrayArrayOfArrays1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|String
index|[]
index|[]
index|[]
name|strings
init|=
operator|(
name|String
index|[]
index|[]
index|[]
operator|)
name|createArrayType
argument_list|(
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|ARRAY_2_3_4
argument_list|,
name|strings
argument_list|)
expr_stmt|;
comment|// round trip tests
name|strings
operator|=
name|readWriteReadRef
argument_list|(
literal|"arrayArrayOfArrays1.xml"
argument_list|,
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|ARRAY_2_3_4
argument_list|,
name|strings
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPartiallyTransmitted
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// xsd:int[5] offset="[2]"
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arrayPartiallyTransmitted.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|int
index|[]
name|numbers
init|=
operator|(
name|int
index|[]
operator|)
name|createArrayType
argument_list|(
name|int
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|3
block|,
literal|4
block|,
literal|0
block|}
argument_list|,
name|numbers
argument_list|)
expr_stmt|;
comment|// round trip tests
name|numbers
operator|=
name|readWriteReadRef
argument_list|(
literal|"arrayPartiallyTransmitted.xml"
argument_list|,
name|int
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|int
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|3
block|,
literal|4
block|,
literal|0
block|}
argument_list|,
name|numbers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSparseArray
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// xsd:string[2,3,4]
name|ElementReader
name|reader
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arraySparse1.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|String
index|[]
index|[]
index|[]
name|strings
init|=
operator|(
name|String
index|[]
index|[]
index|[]
operator|)
name|createArrayType
argument_list|(
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|verifySparseArray
argument_list|(
name|strings
argument_list|)
expr_stmt|;
comment|// xsd:string[,][4] -> xsd:string[3,4]
name|reader
operator|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arraySparse2.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|strings
operator|=
operator|(
name|String
index|[]
index|[]
index|[]
operator|)
name|createArrayType
argument_list|(
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|verifySparseArray
argument_list|(
name|strings
argument_list|)
expr_stmt|;
comment|// xsd:string[,][4] -> xsd:string[][3] -> xsd:string[4]
name|reader
operator|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"arraySparse3.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|strings
operator|=
operator|(
name|String
index|[]
index|[]
index|[]
operator|)
name|createArrayType
argument_list|(
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|verifySparseArray
argument_list|(
name|strings
argument_list|)
expr_stmt|;
comment|// round trip tests
name|strings
operator|=
name|readWriteReadRef
argument_list|(
literal|"arraySparse1.xml"
argument_list|,
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|verifySparseArray
argument_list|(
name|strings
argument_list|)
expr_stmt|;
name|strings
operator|=
name|readWriteReadRef
argument_list|(
literal|"arraySparse2.xml"
argument_list|,
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|verifySparseArray
argument_list|(
name|strings
argument_list|)
expr_stmt|;
name|strings
operator|=
name|readWriteReadRef
argument_list|(
literal|"arraySparse3.xml"
argument_list|,
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|verifySparseArray
argument_list|(
name|strings
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidArray
parameter_list|()
throws|throws
name|Exception
block|{
comment|// to many elements
name|verifyInvalid
argument_list|(
literal|"arrayInvalid1.xml"
argument_list|,
name|int
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// position out of bounds
name|verifyInvalid
argument_list|(
literal|"arrayInvalid2.xml"
argument_list|,
name|int
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// array dimensions mismatch
name|verifyInvalid
argument_list|(
literal|"arrayInvalid3.xml"
argument_list|,
name|int
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|verifyInvalid
argument_list|(
literal|"arrayInvalid4.xml"
argument_list|,
name|int
index|[]
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// array offset to large
name|verifyInvalid
argument_list|(
literal|"arrayInvalid5.xml"
argument_list|,
name|int
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|verifyInvalid
argument_list|(
literal|"arrayInvalid6.xml"
argument_list|,
name|int
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// duplicate entry in sparse array
name|verifyInvalid
argument_list|(
literal|"arrayInvalid7.xml"
argument_list|,
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// position doesn't have enough positions
name|verifyInvalid
argument_list|(
literal|"arrayInvalid8.xml"
argument_list|,
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// position has too many positions
name|verifyInvalid
argument_list|(
literal|"arrayInvalid9.xml"
argument_list|,
name|String
index|[]
index|[]
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifySparseArray
parameter_list|(
name|String
index|[]
index|[]
index|[]
name|strings
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"row1 col1 dep1"
argument_list|,
name|strings
index|[
literal|0
index|]
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|strings
index|[
literal|0
index|]
index|[
literal|0
index|]
index|[
literal|0
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|"row2 col1 dep1"
argument_list|,
name|strings
index|[
literal|1
index|]
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|strings
index|[
literal|1
index|]
index|[
literal|0
index|]
index|[
literal|0
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|"row1 col2 dep1"
argument_list|,
name|strings
index|[
literal|0
index|]
index|[
literal|1
index|]
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|strings
index|[
literal|0
index|]
index|[
literal|1
index|]
index|[
literal|0
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|"row1 col1 dep2"
argument_list|,
name|strings
index|[
literal|0
index|]
index|[
literal|0
index|]
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|strings
index|[
literal|0
index|]
index|[
literal|0
index|]
index|[
literal|1
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|"row1 col3 dep1"
argument_list|,
name|strings
index|[
literal|0
index|]
index|[
literal|2
index|]
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|strings
index|[
literal|0
index|]
index|[
literal|2
index|]
index|[
literal|0
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|"row1 col1 dep4"
argument_list|,
name|strings
index|[
literal|0
index|]
index|[
literal|0
index|]
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|strings
index|[
literal|0
index|]
index|[
literal|0
index|]
index|[
literal|3
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|"row2 col3 dep4"
argument_list|,
name|strings
index|[
literal|1
index|]
index|[
literal|2
index|]
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|strings
index|[
literal|1
index|]
index|[
literal|2
index|]
index|[
literal|3
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|"row2 col3 dep2"
argument_list|,
name|strings
index|[
literal|1
index|]
index|[
literal|2
index|]
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|strings
index|[
literal|1
index|]
index|[
literal|2
index|]
index|[
literal|1
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|"row1 col2 dep3"
argument_list|,
name|strings
index|[
literal|0
index|]
index|[
literal|1
index|]
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|strings
index|[
literal|0
index|]
index|[
literal|1
index|]
index|[
literal|2
index|]
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|strings
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|strings
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|strings
index|[
name|i
index|]
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|strings
index|[
name|i
index|]
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|strings
index|[
name|i
index|]
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|strings
index|[
name|i
index|]
index|[
name|j
index|]
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|strings
index|[
name|i
index|]
index|[
name|j
index|]
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|strings
index|[
name|i
index|]
index|[
name|j
index|]
operator|.
name|length
condition|;
name|k
operator|++
control|)
block|{
name|assertNull
argument_list|(
literal|"strings["
operator|+
name|i
operator|+
literal|"]["
operator|+
name|j
operator|+
literal|"]["
operator|+
name|k
operator|+
literal|"] is not null"
argument_list|,
name|strings
index|[
name|i
index|]
index|[
name|j
index|]
index|[
name|k
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
specifier|private
name|SoapArrayType
name|createArrayType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|)
block|{
return|return
name|createArrayType
argument_list|(
name|typeClass
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"stuff"
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|SoapArrayType
name|createArrayType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|,
name|QName
name|schemaType
parameter_list|)
block|{
name|AegisType
name|type
init|=
name|mapping
operator|.
name|getType
argument_list|(
name|typeClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|SoapArrayType
operator|)
name|type
return|;
block|}
name|SoapArrayType
name|arrayType
init|=
operator|new
name|SoapArrayType
argument_list|()
decl_stmt|;
name|arrayType
operator|.
name|setTypeClass
argument_list|(
name|typeClass
argument_list|)
expr_stmt|;
name|arrayType
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|arrayType
operator|.
name|setSchemaType
argument_list|(
name|schemaType
argument_list|)
expr_stmt|;
return|return
name|arrayType
return|;
block|}
block|}
end_class

end_unit

