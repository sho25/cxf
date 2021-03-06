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
name|binding
operator|.
name|soap
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLReader
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
name|stream
operator|.
name|XMLStreamReader
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
name|XMLStreamWriter
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
name|binding
operator|.
name|BindingFactoryManager
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
name|phase
operator|.
name|Phase
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|service
operator|.
name|model
operator|.
name|BindingInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|staxutils
operator|.
name|StaxUtils
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
name|transport
operator|.
name|DestinationFactoryManager
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
name|wsdl11
operator|.
name|WSDLServiceBuilder
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
name|easymock
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|TestBase
block|{
specifier|protected
name|PhaseInterceptorChain
name|chain
decl_stmt|;
specifier|protected
name|SoapMessage
name|soapMessage
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
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|phases
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Phase
name|phase1
init|=
operator|new
name|Phase
argument_list|(
literal|"phase1"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Phase
name|phase2
init|=
operator|new
name|Phase
argument_list|(
literal|"phase2"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Phase
name|phase3
init|=
operator|new
name|Phase
argument_list|(
literal|"phase3"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|Phase
name|phase4
init|=
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|WRITE_ENDING
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|phases
operator|.
name|add
argument_list|(
name|phase1
argument_list|)
expr_stmt|;
name|phases
operator|.
name|add
argument_list|(
name|phase2
argument_list|)
expr_stmt|;
name|phases
operator|.
name|add
argument_list|(
name|phase3
argument_list|)
expr_stmt|;
name|phases
operator|.
name|add
argument_list|(
name|phase4
argument_list|)
expr_stmt|;
name|phases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|POST_LOGICAL
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|phases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|INVOKE
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|chain
operator|=
operator|new
name|PhaseInterceptorChain
argument_list|(
name|phases
argument_list|)
expr_stmt|;
name|soapMessage
operator|=
name|TestUtil
operator|.
name|createEmptySoapMessage
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|,
name|chain
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{     }
specifier|public
name|InputStream
name|getTestStream
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|String
name|file
parameter_list|)
block|{
return|return
name|clz
operator|.
name|getResourceAsStream
argument_list|(
name|file
argument_list|)
return|;
block|}
specifier|public
name|XMLStreamReader
name|getXMLStreamReader
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
return|return
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
return|;
block|}
specifier|public
name|XMLStreamWriter
name|getXMLStreamWriter
parameter_list|(
name|OutputStream
name|os
parameter_list|)
block|{
return|return
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|)
return|;
block|}
specifier|public
name|Method
name|getTestMethod
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|sei
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|Method
index|[]
name|iMethods
init|=
name|sei
operator|.
name|getMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|iMethods
control|)
block|{
if|if
condition|(
name|methodName
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|m
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|BindingInfo
name|getTestService
parameter_list|(
name|String
name|wsdlUrl
parameter_list|,
name|String
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceInfo
name|service
init|=
name|getMockedServiceModel
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|wsdlUrl
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|BindingInfo
name|binding
init|=
name|service
operator|.
name|getEndpoint
argument_list|(
operator|new
name|QName
argument_list|(
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|port
argument_list|)
argument_list|)
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|binding
argument_list|)
expr_stmt|;
return|return
name|binding
return|;
block|}
specifier|protected
name|ServiceInfo
name|getMockedServiceModel
parameter_list|(
name|String
name|wsdlUrl
parameter_list|)
throws|throws
name|Exception
block|{
name|WSDLReader
name|wsdlReader
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|wsdlReader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Definition
name|def
init|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|wsdlUrl
argument_list|)
decl_stmt|;
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingFactoryManager
name|bindingFactoryManager
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|WSDLServiceBuilder
name|wsdlServiceBuilder
init|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|def
operator|.
name|getServices
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Service
condition|)
block|{
name|service
operator|=
operator|(
name|Service
operator|)
name|obj
expr_stmt|;
break|break;
block|}
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bindingFactoryManager
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|dfm
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|wsdlServiceBuilder
operator|.
name|buildServices
argument_list|(
name|def
argument_list|,
name|service
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|serviceInfo
operator|.
name|setProperty
argument_list|(
name|WSDLServiceBuilder
operator|.
name|WSDL_DEFINITION
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|serviceInfo
operator|.
name|setProperty
argument_list|(
name|WSDLServiceBuilder
operator|.
name|WSDL_SERVICE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|serviceInfo
return|;
block|}
block|}
end_class

end_unit

