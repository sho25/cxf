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
name|transport
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|net
operator|.
name|URL
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|ExchangeImpl
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
name|message
operator|.
name|Message
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
name|Service
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
name|EndpointInfo
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
name|Conduit
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
name|MessageObserver
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
name|wsdl11
operator|.
name|WSDLServiceFactory
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
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|Before
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJMSTester
extends|extends
name|Assert
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|MESSAGE_CONTENT
init|=
literal|"HelloWorld"
decl_stmt|;
specifier|private
specifier|static
name|JMSBrokerSetup
name|broker
decl_stmt|;
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|protected
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|protected
name|EndpointReferenceType
name|target
decl_stmt|;
specifier|protected
name|MessageObserver
name|observer
decl_stmt|;
specifier|protected
name|Message
name|inMessage
decl_stmt|;
specifier|public
specifier|static
name|void
name|startBroker
parameter_list|(
name|JMSBrokerSetup
name|b
parameter_list|)
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|broker
operator|=
name|b
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stopBroker
parameter_list|()
throws|throws
name|Exception
block|{
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
name|broker
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|BusFactory
name|bf
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"cxf.config.file"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"cxf.config.file"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setupServiceInfo
parameter_list|(
name|String
name|ns
parameter_list|,
name|String
name|wsdl
parameter_list|,
name|String
name|serviceName
parameter_list|,
name|String
name|portName
parameter_list|)
block|{
name|URL
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|wsdl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdlUrl
argument_list|)
expr_stmt|;
name|WSDLServiceFactory
name|factory
init|=
operator|new
name|WSDLServiceFactory
argument_list|(
name|bus
argument_list|,
name|wsdlUrl
argument_list|,
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|serviceName
argument_list|)
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|endpointInfo
operator|=
name|service
operator|.
name|getEndpointInfo
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|portName
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|sendoutMessage
parameter_list|(
name|Conduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|,
name|Boolean
name|isOneWay
parameter_list|)
throws|throws
name|IOException
block|{
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setOneWay
argument_list|(
name|isOneWay
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setSynchronous
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
try|try
block|{
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"JMSConduit can't perpare to send out message"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The OutputStream should not be null "
argument_list|,
name|os
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|MESSAGE_CONTENT
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO encoding
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|JMSConduit
name|setupJMSConduit
parameter_list|(
name|boolean
name|send
parameter_list|,
name|boolean
name|decoupled
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|decoupled
condition|)
block|{
comment|// setup the reference type
block|}
else|else
block|{
name|target
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|EndpointReferenceType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|JMSConfiguration
name|jmsConfig
init|=
operator|new
name|JMSOldConfigHolder
argument_list|()
operator|.
name|createJMSConfigurationFromEndpointInfo
argument_list|(
name|bus
argument_list|,
name|endpointInfo
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|JMSConduit
name|jmsConduit
init|=
operator|new
name|JMSConduit
argument_list|(
name|endpointInfo
argument_list|,
name|target
argument_list|,
name|jmsConfig
argument_list|,
name|bus
argument_list|)
decl_stmt|;
if|if
condition|(
name|send
condition|)
block|{
comment|// setMessageObserver
name|observer
operator|=
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|inMessage
operator|=
name|m
expr_stmt|;
block|}
block|}
expr_stmt|;
name|jmsConduit
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
block|}
return|return
name|jmsConduit
return|;
block|}
block|}
end_class

end_unit

