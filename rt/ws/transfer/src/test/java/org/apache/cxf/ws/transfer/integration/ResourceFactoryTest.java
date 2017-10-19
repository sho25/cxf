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
name|transfer
operator|.
name|integration
package|;
end_package

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
name|endpoint
operator|.
name|Server
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
name|DOMUtils
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|ReferenceParametersType
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
name|transfer
operator|.
name|Create
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
name|transfer
operator|.
name|CreateResponse
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
name|transfer
operator|.
name|Representation
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
name|transfer
operator|.
name|manager
operator|.
name|ResourceManager
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
name|transfer
operator|.
name|resourcefactory
operator|.
name|ResourceFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|ResourceFactoryTest
extends|extends
name|IntegrationBaseTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_UUID
init|=
literal|"123456"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REF_PARAM_NAMESPACE
init|=
literal|"org.apache.cxf.transfer/manager"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REF_PARAM_LOCAL_NAME
init|=
literal|"UUID"
decl_stmt|;
specifier|private
name|ReferenceParametersType
name|createReferenceParameters
parameter_list|()
block|{
name|ReferenceParametersType
name|refParam
init|=
operator|new
name|ReferenceParametersType
argument_list|()
decl_stmt|;
name|Element
name|uuidEl
init|=
name|DOMUtils
operator|.
name|getEmptyDocument
argument_list|()
operator|.
name|createElementNS
argument_list|(
name|REF_PARAM_NAMESPACE
argument_list|,
name|REF_PARAM_LOCAL_NAME
argument_list|)
decl_stmt|;
name|uuidEl
operator|.
name|setTextContent
argument_list|(
name|RESOURCE_UUID
argument_list|)
expr_stmt|;
name|refParam
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|uuidEl
argument_list|)
expr_stmt|;
return|return
name|refParam
return|;
block|}
specifier|private
name|Element
name|createXMLRepresentation
parameter_list|()
block|{
name|Element
name|root
init|=
name|document
operator|.
name|createElement
argument_list|(
literal|"root"
argument_list|)
decl_stmt|;
name|Element
name|child1
init|=
name|document
operator|.
name|createElement
argument_list|(
literal|"child1"
argument_list|)
decl_stmt|;
name|Element
name|child2
init|=
name|document
operator|.
name|createElement
argument_list|(
literal|"child2"
argument_list|)
decl_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|child1
argument_list|)
expr_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|child2
argument_list|)
expr_stmt|;
return|return
name|root
return|;
block|}
specifier|private
name|ResourceFactory
name|createClient
parameter_list|()
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|ResourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|RESOURCE_FACTORY_ADDRESS
argument_list|)
expr_stmt|;
return|return
operator|(
name|ResourceFactory
operator|)
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createLocalResourceTest
parameter_list|()
block|{
name|ReferenceParametersType
name|refParams
init|=
name|createReferenceParameters
argument_list|()
decl_stmt|;
name|ResourceManager
name|manager
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|manager
operator|.
name|create
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|Representation
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|refParams
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|once
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|Server
name|localResourceFactory
init|=
name|createLocalResourceFactory
argument_list|(
name|manager
argument_list|)
decl_stmt|;
name|ResourceFactory
name|client
init|=
name|createClient
argument_list|()
decl_stmt|;
name|Create
name|createRequest
init|=
operator|new
name|Create
argument_list|()
decl_stmt|;
name|Representation
name|representation
init|=
operator|new
name|Representation
argument_list|()
decl_stmt|;
name|representation
operator|.
name|setAny
argument_list|(
name|createXMLRepresentation
argument_list|()
argument_list|)
expr_stmt|;
name|createRequest
operator|.
name|setRepresentation
argument_list|(
name|representation
argument_list|)
expr_stmt|;
name|CreateResponse
name|response
init|=
name|client
operator|.
name|create
argument_list|(
name|createRequest
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"ResourceAddress is other than expected."
argument_list|,
name|RESOURCE_ADDRESS
argument_list|,
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|refParamEl
init|=
operator|(
name|Element
operator|)
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|getReferenceParameters
argument_list|()
operator|.
name|getAny
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|REF_PARAM_NAMESPACE
argument_list|,
name|refParamEl
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|REF_PARAM_LOCAL_NAME
argument_list|,
name|refParamEl
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|RESOURCE_UUID
argument_list|,
name|refParamEl
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
operator|(
operator|(
name|Element
operator|)
name|response
operator|.
name|getRepresentation
argument_list|()
operator|.
name|getAny
argument_list|()
operator|)
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Element
operator|)
name|response
operator|.
name|getRepresentation
argument_list|()
operator|.
name|getAny
argument_list|()
operator|)
operator|.
name|getChildNodes
argument_list|()
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|localResourceFactory
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createRemoteResourceTest
parameter_list|()
block|{
name|ReferenceParametersType
name|refParams
init|=
name|createReferenceParameters
argument_list|()
decl_stmt|;
name|ResourceManager
name|manager
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|manager
operator|.
name|create
argument_list|(
name|EasyMock
operator|.
name|isA
argument_list|(
name|Representation
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|refParams
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|once
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|Server
name|remoteResourceFactory
init|=
name|createRemoteResourceFactory
argument_list|()
decl_stmt|;
name|Server
name|remoteResource
init|=
name|createRemoteResource
argument_list|(
name|manager
argument_list|)
decl_stmt|;
name|ResourceFactory
name|client
init|=
name|createClient
argument_list|()
decl_stmt|;
name|Create
name|createRequest
init|=
operator|new
name|Create
argument_list|()
decl_stmt|;
name|Representation
name|representation
init|=
operator|new
name|Representation
argument_list|()
decl_stmt|;
name|representation
operator|.
name|setAny
argument_list|(
name|createXMLRepresentation
argument_list|()
argument_list|)
expr_stmt|;
name|createRequest
operator|.
name|setRepresentation
argument_list|(
name|representation
argument_list|)
expr_stmt|;
name|CreateResponse
name|response
init|=
name|client
operator|.
name|create
argument_list|(
name|createRequest
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"ResourceAddress is other than expected."
argument_list|,
name|RESOURCE_REMOTE_ADDRESS
argument_list|,
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|refParamEl
init|=
operator|(
name|Element
operator|)
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|getReferenceParameters
argument_list|()
operator|.
name|getAny
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|REF_PARAM_NAMESPACE
argument_list|,
name|refParamEl
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|REF_PARAM_LOCAL_NAME
argument_list|,
name|refParamEl
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|RESOURCE_UUID
argument_list|,
name|refParamEl
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
operator|(
operator|(
name|Element
operator|)
name|response
operator|.
name|getRepresentation
argument_list|()
operator|.
name|getAny
argument_list|()
operator|)
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Element
operator|)
name|response
operator|.
name|getRepresentation
argument_list|()
operator|.
name|getAny
argument_list|()
operator|)
operator|.
name|getChildNodes
argument_list|()
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|remoteResourceFactory
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|remoteResource
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

