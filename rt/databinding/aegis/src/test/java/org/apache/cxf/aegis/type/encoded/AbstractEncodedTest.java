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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|AbstractAegisTest
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
name|AegisContext
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
name|DatabindingException
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
name|DefaultTypeMapping
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
name|Type
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
name|TypeMapping
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
name|MessageReader
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
name|MessageWriter
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
name|ElementWriter
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
name|binding
operator|.
name|soap
operator|.
name|Soap11
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
name|common
operator|.
name|util
operator|.
name|SOAPConstants
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
name|helpers
operator|.
name|MapNamespaceContext
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
name|helpers
operator|.
name|XMLUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractEncodedTest
extends|extends
name|AbstractAegisTest
block|{
specifier|protected
name|DefaultTypeMapping
name|mapping
decl_stmt|;
specifier|protected
name|TrailingBlocks
name|trailingBlocks
decl_stmt|;
annotation|@
name|Before
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
name|addNamespace
argument_list|(
literal|"b"
argument_list|,
literal|"urn:Bean"
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"a"
argument_list|,
literal|"urn:anotherns"
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"xsi"
argument_list|,
name|SOAPConstants
operator|.
name|XSI_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"soapenc"
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getSoapEncodingStyle
argument_list|()
argument_list|)
expr_stmt|;
name|AegisContext
name|context
init|=
operator|new
name|AegisContext
argument_list|()
decl_stmt|;
comment|// create a different mapping than the context creates.
name|TypeMapping
name|baseMapping
init|=
name|DefaultTypeMapping
operator|.
name|createSoap11TypeMapping
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|mapping
operator|=
operator|new
name|DefaultTypeMapping
argument_list|(
name|SOAPConstants
operator|.
name|XSD
argument_list|,
name|baseMapping
argument_list|)
expr_stmt|;
name|mapping
operator|.
name|setTypeCreator
argument_list|(
name|context
operator|.
name|createTypeCreator
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|context
operator|.
name|initialize
argument_list|()
expr_stmt|;
comment|// serialization root type
name|trailingBlocks
operator|=
operator|new
name|TrailingBlocks
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|Context
name|getContext
parameter_list|()
block|{
name|AegisContext
name|globalContext
init|=
operator|new
name|AegisContext
argument_list|()
decl_stmt|;
name|globalContext
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|globalContext
operator|.
name|setTypeMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
return|return
operator|new
name|Context
argument_list|(
name|globalContext
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|readWriteReadRef
parameter_list|(
name|String
name|file
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|typeClass
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
name|Type
name|type
init|=
name|mapping
operator|.
name|getType
argument_list|(
name|typeClass
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no type found for "
operator|+
name|typeClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// read file
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
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|T
name|value
init|=
name|typeClass
operator|.
name|cast
argument_list|(
name|type
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
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
comment|// write value to element
name|Element
name|element
init|=
name|writeRef
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// reread value from element
name|value
operator|=
name|typeClass
operator|.
name|cast
argument_list|(
name|readRef
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
specifier|public
name|Object
name|readRef
parameter_list|(
name|String
name|file
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|ElementReader
name|root
init|=
operator|new
name|ElementReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|readRef
argument_list|(
name|root
argument_list|)
return|;
block|}
specifier|public
name|Object
name|readRef
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|xml
init|=
name|XMLUtils
operator|.
name|toString
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|ElementReader
name|root
decl_stmt|;
try|try
block|{
name|root
operator|=
operator|new
name|ElementReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|xml
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|readRef
argument_list|(
name|root
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Object
name|readRef
parameter_list|(
name|ElementReader
name|root
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// get Type based on the element qname
name|MessageReader
name|reader
init|=
name|root
operator|.
name|getNextElementReader
argument_list|()
decl_stmt|;
name|Type
name|type
init|=
name|this
operator|.
name|mapping
operator|.
name|getType
argument_list|(
name|reader
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"type is null"
argument_list|,
name|type
argument_list|)
expr_stmt|;
comment|// read ref
name|SoapRefType
name|soapRefType
init|=
operator|new
name|SoapRefType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|SoapRef
name|ref
init|=
operator|(
name|SoapRef
operator|)
name|soapRefType
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
name|readToEnd
argument_list|()
expr_stmt|;
comment|// read the trailing blocks (referenced objects)
name|List
argument_list|<
name|Object
argument_list|>
name|roots
init|=
name|trailingBlocks
operator|.
name|readBlocks
argument_list|(
name|root
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|roots
argument_list|)
expr_stmt|;
comment|// close the input stream
name|root
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// return the ref
return|return
name|ref
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
name|Element
name|writeRef
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|Type
name|type
init|=
name|mapping
operator|.
name|getType
argument_list|(
name|instance
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no type found for "
operator|+
name|instance
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// create the document
name|Element
name|element
init|=
name|createElement
argument_list|(
literal|"urn:Bean"
argument_list|,
literal|"root"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|MapNamespaceContext
name|namespaces
init|=
operator|new
name|MapNamespaceContext
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|getNamespaces
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|namespaces
operator|.
name|addNamespace
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ElementWriter
name|rootWriter
init|=
name|getElementWriter
argument_list|(
name|element
argument_list|,
name|namespaces
argument_list|)
decl_stmt|;
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
comment|// get Type based on the object instance
name|assertNotNull
argument_list|(
literal|"type is null"
argument_list|,
name|type
argument_list|)
expr_stmt|;
comment|// write the ref
name|SoapRefType
name|soapRefType
init|=
operator|new
name|SoapRefType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|MessageWriter
name|cwriter
init|=
name|rootWriter
operator|.
name|getElementWriter
argument_list|(
name|soapRefType
operator|.
name|getSchemaType
argument_list|()
argument_list|)
decl_stmt|;
name|soapRefType
operator|.
name|writeObject
argument_list|(
name|instance
argument_list|,
name|cwriter
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|cwriter
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// write the trailing blocks (referenced objects)
name|trailingBlocks
operator|.
name|writeBlocks
argument_list|(
name|rootWriter
argument_list|,
name|context
argument_list|)
expr_stmt|;
comment|// log xml for debugging
comment|// XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
comment|// System.out.println(xmlOutputter.outputString(element)) ;
return|return
name|element
return|;
block|}
specifier|public
name|void
name|verifyInvalid
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|expectedType
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|Type
name|type
init|=
name|mapping
operator|.
name|getType
argument_list|(
name|expectedType
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"type is null"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|Context
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
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
name|resourceName
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|type
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected DatabindingException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DatabindingException
name|expected
parameter_list|)
block|{
comment|// expected
block|}
finally|finally
block|{
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|validateShippingAddress
parameter_list|(
name|Address
name|address
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1234 Riverside Drive"
argument_list|,
name|address
operator|.
name|getStreet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Gainesville"
argument_list|,
name|address
operator|.
name|getCity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FL"
argument_list|,
name|address
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"30506"
argument_list|,
name|address
operator|.
name|getZip
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|validateBillingAddress
parameter_list|(
name|Address
name|billing
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"billing is null"
argument_list|,
name|billing
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1234 Fake Street"
argument_list|,
name|billing
operator|.
name|getStreet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Las Vegas"
argument_list|,
name|billing
operator|.
name|getCity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NV"
argument_list|,
name|billing
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"89102"
argument_list|,
name|billing
operator|.
name|getZip
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

