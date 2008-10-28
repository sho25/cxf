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
name|configuration
operator|.
name|spring
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
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverterInterface
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|HexBinaryAdapter
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
name|bus
operator|.
name|spring
operator|.
name|BusApplicationContext
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
name|configuration
operator|.
name|Configurable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|ConfigurerImplTest
extends|extends
name|Assert
block|{
static|static
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
try|try
block|{
try|try
block|{
name|cls
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.sun.xml.bind.DatatypeConverterImpl"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|cls
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.sun.xml.internal.bind.DatatypeConverterImpl"
argument_list|)
expr_stmt|;
block|}
name|DatatypeConverterInterface
name|convert
init|=
operator|(
name|DatatypeConverterInterface
operator|)
name|cls
operator|.
name|getField
argument_list|(
literal|"theInstance"
argument_list|)
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|DatatypeConverter
operator|.
name|setDatatypeConverter
argument_list|(
name|convert
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConfigureSimpleNoMatchingBean
parameter_list|()
block|{
name|SimpleBean
name|sb
init|=
operator|new
name|SimpleBean
argument_list|(
literal|"unknown"
argument_list|)
decl_stmt|;
name|BusApplicationContext
name|ac
init|=
operator|new
name|BusApplicationContext
argument_list|(
literal|"/org/apache/cxf/configuration/spring/test-beans.xml"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ConfigurerImpl
name|configurer
init|=
operator|new
name|ConfigurerImpl
argument_list|(
name|ac
argument_list|)
decl_stmt|;
name|configurer
operator|.
name|configureBean
argument_list|(
name|sb
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute stringAttr"
argument_list|,
literal|"hello"
argument_list|,
name|sb
operator|.
name|getStringAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected value for attribute booleanAttr"
argument_list|,
name|sb
operator|.
name|getBooleanAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute integerAttr"
argument_list|,
name|BigInteger
operator|.
name|ONE
argument_list|,
name|sb
operator|.
name|getIntegerAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute intAttr"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|,
name|sb
operator|.
name|getIntAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute longAttr"
argument_list|,
operator|new
name|Long
argument_list|(
literal|3L
argument_list|)
argument_list|,
name|sb
operator|.
name|getLongAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute shortAttr"
argument_list|,
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|4
argument_list|)
argument_list|,
name|sb
operator|.
name|getShortAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute decimalAttr"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|"5"
argument_list|)
argument_list|,
name|sb
operator|.
name|getDecimalAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute floatAttr"
argument_list|,
operator|new
name|Float
argument_list|(
literal|6F
argument_list|)
argument_list|,
name|sb
operator|.
name|getFloatAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute doubleAttr"
argument_list|,
operator|new
name|Double
argument_list|(
literal|7D
argument_list|)
argument_list|,
name|sb
operator|.
name|getDoubleAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute byteAttr"
argument_list|,
operator|new
name|Byte
argument_list|(
operator|(
name|byte
operator|)
literal|8
argument_list|)
argument_list|,
name|sb
operator|.
name|getByteAttr
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|qn
init|=
name|sb
operator|.
name|getQnameAttr
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute qnameAttrNoDefault"
argument_list|,
literal|"schema"
argument_list|,
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute qnameAttrNoDefault"
argument_list|,
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|,
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|expected
init|=
name|DatatypeConverter
operator|.
name|parseBase64Binary
argument_list|(
literal|"abcd"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|val
init|=
name|sb
operator|.
name|getBase64BinaryAttr
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute base64BinaryAttrNoDefault"
argument_list|,
name|expected
operator|.
name|length
argument_list|,
name|val
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
name|expected
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute base64BinaryAttrNoDefault"
argument_list|,
name|expected
index|[
name|i
index|]
argument_list|,
name|val
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|expected
operator|=
operator|new
name|HexBinaryAdapter
argument_list|()
operator|.
name|unmarshal
argument_list|(
literal|"aaaa"
argument_list|)
expr_stmt|;
name|val
operator|=
name|sb
operator|.
name|getHexBinaryAttr
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute hexBinaryAttrNoDefault"
argument_list|,
name|expected
operator|.
name|length
argument_list|,
name|val
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
name|expected
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute hexBinaryAttrNoDefault"
argument_list|,
name|expected
index|[
name|i
index|]
argument_list|,
name|val
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute unsignedIntAttrNoDefault"
argument_list|,
operator|new
name|Long
argument_list|(
literal|9L
argument_list|)
argument_list|,
name|sb
operator|.
name|getUnsignedIntAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute unsignedShortAttrNoDefault"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|10
argument_list|)
argument_list|,
name|sb
operator|.
name|getUnsignedShortAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute unsignedByteAttrNoDefault"
argument_list|,
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|11
argument_list|)
argument_list|,
name|sb
operator|.
name|getUnsignedByteAttr
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConfigureSimple
parameter_list|()
block|{
name|SimpleBean
name|sb
init|=
operator|new
name|SimpleBean
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
name|BusApplicationContext
name|ac
init|=
operator|new
name|BusApplicationContext
argument_list|(
literal|"/org/apache/cxf/configuration/spring/test-beans.xml"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ConfigurerImpl
name|configurer
init|=
operator|new
name|ConfigurerImpl
argument_list|()
decl_stmt|;
name|configurer
operator|.
name|setApplicationContext
argument_list|(
name|ac
argument_list|)
expr_stmt|;
name|configurer
operator|.
name|configureBean
argument_list|(
name|sb
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute stringAttr"
argument_list|,
literal|"hallo"
argument_list|,
name|sb
operator|.
name|getStringAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected value for attribute booleanAttr"
argument_list|,
operator|!
name|sb
operator|.
name|getBooleanAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute integerAttr"
argument_list|,
name|BigInteger
operator|.
name|TEN
argument_list|,
name|sb
operator|.
name|getIntegerAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute intAttr"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|12
argument_list|)
argument_list|,
name|sb
operator|.
name|getIntAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute longAttr"
argument_list|,
operator|new
name|Long
argument_list|(
literal|13L
argument_list|)
argument_list|,
name|sb
operator|.
name|getLongAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute shortAttr"
argument_list|,
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|14
argument_list|)
argument_list|,
name|sb
operator|.
name|getShortAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute decimalAttr"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|"15"
argument_list|)
argument_list|,
name|sb
operator|.
name|getDecimalAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute floatAttr"
argument_list|,
operator|new
name|Float
argument_list|(
literal|16F
argument_list|)
argument_list|,
name|sb
operator|.
name|getFloatAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute doubleAttr"
argument_list|,
operator|new
name|Double
argument_list|(
literal|17D
argument_list|)
argument_list|,
name|sb
operator|.
name|getDoubleAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute byteAttr"
argument_list|,
operator|new
name|Byte
argument_list|(
operator|(
name|byte
operator|)
literal|18
argument_list|)
argument_list|,
name|sb
operator|.
name|getByteAttr
argument_list|()
argument_list|)
expr_stmt|;
comment|/*         QName qn = sb.getQnameAttr();         assertEquals("Unexpected value for attribute qnameAttrNoDefault",                       "string", qn.getLocalPart());         assertEquals("Unexpected value for attribute qnameAttrNoDefault",                      "http://www.w3.org/2001/XMLSchema", qn.getNamespaceURI());         */
comment|/*         byte[] expected = DatatypeConverter.parseBase64Binary("wxyz");         byte[] val = sb.getBase64BinaryAttr();          assertEquals("Unexpected value for attribute base64BinaryAttrNoDefault", expected.length, val.length);         for (int i = 0; i< expected.length; i++) {             assertEquals("Unexpected value for attribute base64BinaryAttrNoDefault", expected[i], val[i]);         }          expected = new HexBinaryAdapter().unmarshal("bbbb");         val = sb.getHexBinaryAttr();         assertEquals("Unexpected value for attribute hexBinaryAttrNoDefault", expected.length, val.length);         for (int i = 0; i< expected.length; i++) {             assertEquals("Unexpected value for attribute hexBinaryAttrNoDefault", expected[i], val[i]);         }         */
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute unsignedIntAttrNoDefault"
argument_list|,
operator|new
name|Long
argument_list|(
literal|19L
argument_list|)
argument_list|,
name|sb
operator|.
name|getUnsignedIntAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute unsignedShortAttrNoDefault"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|20
argument_list|)
argument_list|,
name|sb
operator|.
name|getUnsignedShortAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute unsignedByteAttrNoDefault"
argument_list|,
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|21
argument_list|)
argument_list|,
name|sb
operator|.
name|getUnsignedByteAttr
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConfigureSimpleMatchingStarBeanId
parameter_list|()
block|{
name|SimpleBean
name|sb
init|=
operator|new
name|SimpleBean
argument_list|(
literal|"simple2"
argument_list|)
decl_stmt|;
name|BusApplicationContext
name|ac
init|=
operator|new
name|BusApplicationContext
argument_list|(
literal|"/org/apache/cxf/configuration/spring/test-beans.xml"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ConfigurerImpl
name|configurer
init|=
operator|new
name|ConfigurerImpl
argument_list|()
decl_stmt|;
name|configurer
operator|.
name|setApplicationContext
argument_list|(
name|ac
argument_list|)
expr_stmt|;
name|configurer
operator|.
name|configureBean
argument_list|(
name|sb
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected value for attribute booleanAttr"
argument_list|,
operator|!
name|sb
operator|.
name|getBooleanAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute integerAttr"
argument_list|,
name|BigInteger
operator|.
name|TEN
argument_list|,
name|sb
operator|.
name|getIntegerAttr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for attribute stringAttr"
argument_list|,
literal|"StarHallo"
argument_list|,
name|sb
operator|.
name|getStringAttr
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBeanName
parameter_list|()
block|{
name|ConfigurerImpl
name|configurer
init|=
operator|new
name|ConfigurerImpl
argument_list|()
decl_stmt|;
name|Object
name|beanInstance
init|=
operator|new
name|Configurable
argument_list|()
block|{
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
literal|"a"
return|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|configurer
operator|.
name|getBeanName
argument_list|(
name|beanInstance
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
class|class
name|NamedBean
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
literal|"b"
return|;
block|}
block|}
name|beanInstance
operator|=
operator|new
name|NamedBean
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|configurer
operator|.
name|getBeanName
argument_list|(
name|beanInstance
argument_list|)
argument_list|)
expr_stmt|;
name|beanInstance
operator|=
name|this
expr_stmt|;
name|assertNull
argument_list|(
name|configurer
operator|.
name|getBeanName
argument_list|(
name|beanInstance
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
class|class
name|SimpleBean
implements|implements
name|Configurable
block|{
specifier|private
name|String
name|beanName
decl_stmt|;
specifier|private
name|String
name|stringAttr
init|=
literal|"hello"
decl_stmt|;
specifier|private
name|Boolean
name|booleanAttr
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
specifier|private
name|BigInteger
name|integerAttr
init|=
name|BigInteger
operator|.
name|ONE
decl_stmt|;
specifier|private
name|Integer
name|intAttr
init|=
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|private
name|Long
name|longAttr
init|=
operator|new
name|Long
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|private
name|Short
name|shortAttr
init|=
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|4
argument_list|)
decl_stmt|;
specifier|private
name|BigDecimal
name|decimalAttr
init|=
operator|new
name|BigDecimal
argument_list|(
literal|"5"
argument_list|)
decl_stmt|;
specifier|private
name|Float
name|floatAttr
init|=
operator|new
name|Float
argument_list|(
literal|6F
argument_list|)
decl_stmt|;
specifier|private
name|Double
name|doubleAttr
init|=
operator|new
name|Double
argument_list|(
literal|7D
argument_list|)
decl_stmt|;
specifier|private
name|Byte
name|byteAttr
init|=
operator|new
name|Byte
argument_list|(
operator|(
name|byte
operator|)
literal|8
argument_list|)
decl_stmt|;
specifier|private
name|QName
name|qnameAttr
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|,
literal|"schema"
argument_list|,
literal|"xs"
argument_list|)
decl_stmt|;
specifier|private
name|byte
index|[]
name|base64BinaryAttr
init|=
name|DatatypeConverter
operator|.
name|parseBase64Binary
argument_list|(
literal|"abcd"
argument_list|)
decl_stmt|;
specifier|private
name|byte
index|[]
name|hexBinaryAttr
init|=
operator|new
name|HexBinaryAdapter
argument_list|()
operator|.
name|unmarshal
argument_list|(
literal|"aaaa"
argument_list|)
decl_stmt|;
specifier|private
name|Long
name|unsignedIntAttr
init|=
operator|new
name|Long
argument_list|(
literal|9
argument_list|)
decl_stmt|;
specifier|private
name|Integer
name|unsignedShortAttr
init|=
operator|new
name|Integer
argument_list|(
literal|10
argument_list|)
decl_stmt|;
specifier|private
name|Short
name|unsignedByteAttr
init|=
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|11
argument_list|)
decl_stmt|;
specifier|public
name|SimpleBean
parameter_list|(
name|String
name|bn
parameter_list|)
block|{
name|beanName
operator|=
name|bn
expr_stmt|;
block|}
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|beanName
return|;
block|}
specifier|public
name|byte
index|[]
name|getBase64BinaryAttr
parameter_list|()
block|{
return|return
name|base64BinaryAttr
return|;
block|}
specifier|public
name|void
name|setBase64BinaryAttr
parameter_list|(
name|byte
index|[]
name|base64BinaryAttr
parameter_list|)
block|{
name|this
operator|.
name|base64BinaryAttr
operator|=
name|base64BinaryAttr
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getBooleanAttr
parameter_list|()
block|{
return|return
name|booleanAttr
return|;
block|}
specifier|public
name|void
name|setBooleanAttr
parameter_list|(
name|Boolean
name|booleanAttr
parameter_list|)
block|{
name|this
operator|.
name|booleanAttr
operator|=
name|booleanAttr
expr_stmt|;
block|}
specifier|public
name|Byte
name|getByteAttr
parameter_list|()
block|{
return|return
name|byteAttr
return|;
block|}
specifier|public
name|void
name|setByteAttr
parameter_list|(
name|Byte
name|byteAttr
parameter_list|)
block|{
name|this
operator|.
name|byteAttr
operator|=
name|byteAttr
expr_stmt|;
block|}
specifier|public
name|BigDecimal
name|getDecimalAttr
parameter_list|()
block|{
return|return
name|decimalAttr
return|;
block|}
specifier|public
name|void
name|setDecimalAttr
parameter_list|(
name|BigDecimal
name|decimalAttr
parameter_list|)
block|{
name|this
operator|.
name|decimalAttr
operator|=
name|decimalAttr
expr_stmt|;
block|}
specifier|public
name|Double
name|getDoubleAttr
parameter_list|()
block|{
return|return
name|doubleAttr
return|;
block|}
specifier|public
name|void
name|setDoubleAttr
parameter_list|(
name|Double
name|doubleAttr
parameter_list|)
block|{
name|this
operator|.
name|doubleAttr
operator|=
name|doubleAttr
expr_stmt|;
block|}
specifier|public
name|Float
name|getFloatAttr
parameter_list|()
block|{
return|return
name|floatAttr
return|;
block|}
specifier|public
name|void
name|setFloatAttr
parameter_list|(
name|Float
name|floatAttr
parameter_list|)
block|{
name|this
operator|.
name|floatAttr
operator|=
name|floatAttr
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getHexBinaryAttr
parameter_list|()
block|{
return|return
name|hexBinaryAttr
return|;
block|}
specifier|public
name|void
name|setHexBinaryAttr
parameter_list|(
name|byte
index|[]
name|hexBinaryAttr
parameter_list|)
block|{
name|this
operator|.
name|hexBinaryAttr
operator|=
name|hexBinaryAttr
expr_stmt|;
block|}
specifier|public
name|Integer
name|getIntAttr
parameter_list|()
block|{
return|return
name|intAttr
return|;
block|}
specifier|public
name|void
name|setIntAttr
parameter_list|(
name|Integer
name|intAttr
parameter_list|)
block|{
name|this
operator|.
name|intAttr
operator|=
name|intAttr
expr_stmt|;
block|}
specifier|public
name|BigInteger
name|getIntegerAttr
parameter_list|()
block|{
return|return
name|integerAttr
return|;
block|}
specifier|public
name|void
name|setIntegerAttr
parameter_list|(
name|BigInteger
name|integerAttr
parameter_list|)
block|{
name|this
operator|.
name|integerAttr
operator|=
name|integerAttr
expr_stmt|;
block|}
specifier|public
name|Long
name|getLongAttr
parameter_list|()
block|{
return|return
name|longAttr
return|;
block|}
specifier|public
name|void
name|setLongAttr
parameter_list|(
name|Long
name|longAttr
parameter_list|)
block|{
name|this
operator|.
name|longAttr
operator|=
name|longAttr
expr_stmt|;
block|}
specifier|public
name|QName
name|getQnameAttr
parameter_list|()
block|{
return|return
name|qnameAttr
return|;
block|}
specifier|public
name|void
name|setQnameAttr
parameter_list|(
name|QName
name|qnameAttr
parameter_list|)
block|{
name|this
operator|.
name|qnameAttr
operator|=
name|qnameAttr
expr_stmt|;
block|}
specifier|public
name|Short
name|getShortAttr
parameter_list|()
block|{
return|return
name|shortAttr
return|;
block|}
specifier|public
name|void
name|setShortAttr
parameter_list|(
name|Short
name|shortAttr
parameter_list|)
block|{
name|this
operator|.
name|shortAttr
operator|=
name|shortAttr
expr_stmt|;
block|}
specifier|public
name|String
name|getStringAttr
parameter_list|()
block|{
return|return
name|stringAttr
return|;
block|}
specifier|public
name|void
name|setStringAttr
parameter_list|(
name|String
name|stringAttr
parameter_list|)
block|{
name|this
operator|.
name|stringAttr
operator|=
name|stringAttr
expr_stmt|;
block|}
specifier|public
name|Short
name|getUnsignedByteAttr
parameter_list|()
block|{
return|return
name|unsignedByteAttr
return|;
block|}
specifier|public
name|void
name|setUnsignedByteAttr
parameter_list|(
name|Short
name|unsignedByteAttr
parameter_list|)
block|{
name|this
operator|.
name|unsignedByteAttr
operator|=
name|unsignedByteAttr
expr_stmt|;
block|}
specifier|public
name|Long
name|getUnsignedIntAttr
parameter_list|()
block|{
return|return
name|unsignedIntAttr
return|;
block|}
specifier|public
name|void
name|setUnsignedIntAttr
parameter_list|(
name|Long
name|unsignedIntAttr
parameter_list|)
block|{
name|this
operator|.
name|unsignedIntAttr
operator|=
name|unsignedIntAttr
expr_stmt|;
block|}
specifier|public
name|Integer
name|getUnsignedShortAttr
parameter_list|()
block|{
return|return
name|unsignedShortAttr
return|;
block|}
specifier|public
name|void
name|setUnsignedShortAttr
parameter_list|(
name|Integer
name|unsignedShortAttr
parameter_list|)
block|{
name|this
operator|.
name|unsignedShortAttr
operator|=
name|unsignedShortAttr
expr_stmt|;
block|}
specifier|public
name|void
name|setBeanName
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
name|this
operator|.
name|beanName
operator|=
name|beanName
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

