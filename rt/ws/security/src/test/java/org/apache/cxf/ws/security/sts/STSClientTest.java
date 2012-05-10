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
name|security
operator|.
name|sts
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|JAXBElement
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|BusFactory
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
name|jaxb
operator|.
name|JAXBContextCache
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|ws
operator|.
name|security
operator|.
name|trust
operator|.
name|STSClient
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
name|STSClientTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testConfigureViaEPR
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|addressingClasses
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|addressingClasses
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|wsdl
operator|.
name|ObjectFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|addressingClasses
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ObjectFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|JAXBContext
name|ctx
init|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|addressingClasses
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|Unmarshaller
name|um
init|=
name|ctx
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"epr.xml"
argument_list|)
decl_stmt|;
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|el
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|um
operator|.
name|unmarshal
argument_list|(
name|inStream
argument_list|)
decl_stmt|;
name|EndpointReferenceType
name|ref
init|=
operator|(
name|EndpointReferenceType
operator|)
name|el
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|STSClient
name|client
init|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|client
operator|.
name|configureViaEPR
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:8080/jaxws-samples-wsse-policy-trust-sts/SecurityTokenService?wsdl"
argument_list|,
name|client
operator|.
name|getWsdlLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/"
argument_list|,
literal|"SecurityTokenService"
argument_list|)
argument_list|,
name|client
operator|.
name|getServiceQName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/"
argument_list|,
literal|"UT_Port"
argument_list|)
argument_list|,
name|client
operator|.
name|getEndpointQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

