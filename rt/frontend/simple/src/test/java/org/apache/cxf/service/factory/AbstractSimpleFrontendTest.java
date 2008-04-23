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
name|service
operator|.
name|factory
package|;
end_package

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
name|binding
operator|.
name|soap
operator|.
name|SoapBindingFactory
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
name|SoapTransportFactory
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
name|test
operator|.
name|AbstractCXFTest
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
name|ConduitInitiatorManager
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
name|transport
operator|.
name|local
operator|.
name|LocalTransportFactory
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
name|AbstractSimpleFrontendTest
extends|extends
name|AbstractCXFTest
block|{
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
name|setUpBus
argument_list|()
expr_stmt|;
name|Bus
name|bus
init|=
name|getBus
argument_list|()
decl_stmt|;
name|SoapBindingFactory
name|bindingFactory
init|=
operator|new
name|SoapBindingFactory
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
operator|.
name|registerBindingFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
name|bindingFactory
argument_list|)
expr_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|SoapTransportFactory
name|soapTF
init|=
operator|new
name|SoapTransportFactory
argument_list|()
decl_stmt|;
name|soapTF
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
name|soapTF
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
name|soapTF
argument_list|)
expr_stmt|;
name|LocalTransportFactory
name|localTransport
init|=
operator|new
name|LocalTransportFactory
argument_list|()
decl_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|ConduitInitiatorManager
name|extension
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|extension
operator|.
name|registerConduitInitiator
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|extension
operator|.
name|registerConduitInitiator
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|extension
operator|.
name|registerConduitInitiator
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|extension
operator|.
name|registerConduitInitiator
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
name|soapTF
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

