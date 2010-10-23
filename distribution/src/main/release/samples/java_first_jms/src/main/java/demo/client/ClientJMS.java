begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|client
package|;
end_package

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
name|ws
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
name|transport
operator|.
name|jms
operator|.
name|spec
operator|.
name|JMSSpecConstants
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|service
operator|.
name|HelloWorld
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ClientJMS
block|{
specifier|private
specifier|static
specifier|final
name|String
name|JMS_ENDPOINT_URI
init|=
literal|"jms:queue:test.cxf.jmstransport.queue?timeToLive=1000"
operator|+
literal|"&jndiConnectionFactoryName=ConnectionFactory"
operator|+
literal|"&jndiInitialContextFactory"
operator|+
literal|"=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://impl.service.demo/"
argument_list|,
literal|"HelloWorldImplService"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://service.demo/"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
specifier|private
name|ClientJMS
parameter_list|()
block|{
comment|//
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|jaxws
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|arg
range|:
name|args
control|)
block|{
if|if
condition|(
literal|"-jaxws"
operator|.
name|equals
argument_list|(
name|arg
argument_list|)
condition|)
block|{
name|jaxws
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Invalid argument "
operator|+
name|arg
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|HelloWorld
name|client
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|jaxws
condition|)
block|{
name|client
operator|=
name|createClientJaxWs
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|client
operator|=
name|createClientCxf
argument_list|()
expr_stmt|;
block|}
name|String
name|reply
init|=
name|client
operator|.
name|sayHi
argument_list|(
literal|"HI"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|HelloWorld
name|createClientJaxWs
parameter_list|()
block|{
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
comment|// Add a port to the Service
name|service
operator|.
name|addPort
argument_list|(
name|PORT_QNAME
argument_list|,
name|JMSSpecConstants
operator|.
name|SOAP_JMS_SPECIFICATION_TRANSPORTID
argument_list|,
name|JMS_ENDPOINT_URI
argument_list|)
expr_stmt|;
return|return
name|service
operator|.
name|getPort
argument_list|(
name|HelloWorld
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|HelloWorld
name|createClientCxf
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
name|setTransportId
argument_list|(
name|JMSSpecConstants
operator|.
name|SOAP_JMS_SPECIFICATION_TRANSPORTID
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|JMS_ENDPOINT_URI
argument_list|)
expr_stmt|;
name|HelloWorld
name|client
init|=
name|factory
operator|.
name|create
argument_list|(
name|HelloWorld
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

