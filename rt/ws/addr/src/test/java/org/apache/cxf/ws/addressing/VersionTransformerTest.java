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
name|ws
operator|.
name|addressing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
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
name|JAXBException
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
name|Unmarshaller
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|EndpointReference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
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
name|Document
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
name|helpers
operator|.
name|XMLUtils
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
name|wsdl
operator|.
name|EndpointReferenceUtils
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
name|VersionTransformerTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testConvertToInternal
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/hello_world_soap_http_infoset.xml"
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|DOMSource
name|erXML
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|EndpointReference
name|endpointReference
init|=
name|readEndpointReference
argument_list|(
name|erXML
argument_list|)
decl_stmt|;
name|EndpointReferenceType
name|ert
init|=
name|VersionTransformer
operator|.
name|convertToInternal
argument_list|(
name|endpointReference
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ert
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:8080/test"
argument_list|,
name|ert
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|,
name|EndpointReferenceUtils
operator|.
name|getServiceName
argument_list|(
name|ert
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
comment|// VersionTransformer.convertToInternal produces metadata extensions of type
comment|// DOM Element hence we're testing the relevant EndpointReferenceUtils.setPortName
comment|// code path here
name|List
argument_list|<
name|Object
argument_list|>
name|metadata
init|=
name|ert
operator|.
name|getMetadata
argument_list|()
operator|.
name|getAny
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Single metadata extension expected"
argument_list|,
literal|1
argument_list|,
name|metadata
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Metadata extension of type DOM Element expected"
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Element
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"No portName has been set yet"
argument_list|,
name|EndpointReferenceUtils
operator|.
name|getPortName
argument_list|(
name|ert
argument_list|)
argument_list|)
expr_stmt|;
name|EndpointReferenceUtils
operator|.
name|setPortName
argument_list|(
name|ert
argument_list|,
literal|"Greeter"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No portName has been set"
argument_list|,
literal|"Greeter"
argument_list|,
name|EndpointReferenceUtils
operator|.
name|getPortName
argument_list|(
name|ert
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|EndpointReference
name|readEndpointReference
parameter_list|(
name|Source
name|eprInfoset
parameter_list|)
block|{
try|try
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|W3CEndpointReference
operator|.
name|class
argument_list|)
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
return|return
operator|(
name|EndpointReference
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|eprInfoset
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

