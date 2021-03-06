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
name|testutil
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|Field
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
name|Port
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
name|extensions
operator|.
name|soap
operator|.
name|SOAPAddress
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|broker
operator|.
name|BrokerService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|store
operator|.
name|memory
operator|.
name|MemoryPersistenceAdapter
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
name|util
operator|.
name|ReflectionUtil
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
name|WSDLManager
import|;
end_import

begin_class
specifier|public
class|class
name|EmbeddedJMSBrokerLauncher
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|EmbeddedJMSBrokerLauncher
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|brokerUrl1
decl_stmt|;
name|BrokerService
name|broker
decl_stmt|;
name|String
name|brokerName
decl_stmt|;
specifier|public
name|EmbeddedJMSBrokerLauncher
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EmbeddedJMSBrokerLauncher
parameter_list|(
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
literal|"tcp://localhost:"
operator|+
name|PORT
expr_stmt|;
block|}
name|brokerUrl1
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|void
name|setBrokerName
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|brokerName
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|String
name|getBrokerURL
parameter_list|()
block|{
return|return
name|brokerUrl1
return|;
block|}
specifier|public
name|String
name|getEncodedBrokerURL
parameter_list|()
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|(
name|brokerUrl1
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|brokerUrl1
operator|.
name|length
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|char
name|c
init|=
name|brokerUrl1
operator|.
name|charAt
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'?'
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"%3F"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|updateWsdl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|URL
name|wsdlLocation
parameter_list|)
block|{
name|updateWsdl
argument_list|(
name|b
argument_list|,
name|wsdlLocation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|updateWsdl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|String
name|wsdlLocation
parameter_list|)
block|{
name|updateWsdlExtensors
argument_list|(
name|b
argument_list|,
name|wsdlLocation
argument_list|,
name|brokerUrl1
argument_list|,
name|getEncodedBrokerURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|updateWsdlExtensors
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|wsdlLocation
parameter_list|)
block|{
name|updateWsdlExtensors
argument_list|(
name|bus
argument_list|,
name|wsdlLocation
argument_list|,
literal|"tcp://localhost:"
operator|+
name|PORT
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|updateWsdlExtensors
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|wsdlLocation
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|encodedUrl
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|encodedUrl
operator|==
literal|null
condition|)
block|{
name|encodedUrl
operator|=
name|url
expr_stmt|;
block|}
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
expr_stmt|;
block|}
name|Definition
name|def
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getDefinition
argument_list|(
name|wsdlLocation
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|def
operator|.
name|getAllServices
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|o
decl_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|ports
init|=
name|service
operator|.
name|getPorts
argument_list|()
decl_stmt|;
name|adjustExtensibilityElements
argument_list|(
name|service
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|,
name|url
argument_list|,
name|encodedUrl
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|p
range|:
name|ports
operator|.
name|values
argument_list|()
control|)
block|{
name|Port
name|port
init|=
operator|(
name|Port
operator|)
name|p
decl_stmt|;
name|adjustExtensibilityElements
argument_list|(
name|port
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|,
name|url
argument_list|,
name|encodedUrl
argument_list|)
expr_stmt|;
name|adjustExtensibilityElements
argument_list|(
name|port
operator|.
name|getBinding
argument_list|()
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|,
name|url
argument_list|,
name|encodedUrl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|adjustExtensibilityElements
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|l
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|encodedUrl
parameter_list|)
block|{
for|for
control|(
name|Object
name|e
range|:
name|l
control|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|SOAPAddress
condition|)
block|{
name|String
name|add
init|=
operator|(
operator|(
name|SOAPAddress
operator|)
name|e
operator|)
operator|.
name|getLocationURI
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|add
operator|.
name|indexOf
argument_list|(
literal|"jndiURL="
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|int
name|idx2
init|=
name|add
operator|.
name|indexOf
argument_list|(
literal|'&'
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|add
operator|=
name|add
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
operator|+
literal|"jndiURL="
operator|+
name|encodedUrl
operator|+
operator|(
name|idx2
operator|==
operator|-
literal|1
condition|?
literal|""
else|:
name|add
operator|.
name|substring
argument_list|(
name|idx2
argument_list|)
operator|)
expr_stmt|;
operator|(
operator|(
name|SOAPAddress
operator|)
name|e
operator|)
operator|.
name|setLocationURI
argument_list|(
name|add
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"JndiURLType"
argument_list|)
condition|)
block|{
try|try
block|{
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setValue"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|invoke
argument_list|(
name|e
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
else|else
block|{
try|try
block|{
name|Field
name|f
init|=
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"jmsNamingProperty"
argument_list|)
decl_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|props
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|f
operator|.
name|get
argument_list|(
name|e
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|prop
range|:
name|props
control|)
block|{
name|f
operator|=
name|prop
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"java.naming.provider.url"
operator|.
name|equals
argument_list|(
name|f
operator|.
name|get
argument_list|(
name|prop
argument_list|)
argument_list|)
condition|)
block|{
name|f
operator|=
name|prop
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"value"
argument_list|)
expr_stmt|;
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|f
operator|.
name|get
argument_list|(
name|prop
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|||
operator|!
name|value
operator|.
name|startsWith
argument_list|(
literal|"classpath"
argument_list|)
condition|)
block|{
name|f
operator|.
name|set
argument_list|(
name|prop
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|broker
operator|!=
literal|null
condition|)
block|{
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|//START SNIPPET: broker
specifier|public
specifier|final
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|broker
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|broker
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setPersistenceAdapter
argument_list|(
operator|new
name|MemoryPersistenceAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setTmpDataDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"./target"
argument_list|)
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setUseJmx
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|brokerName
operator|!=
literal|null
condition|)
block|{
name|broker
operator|.
name|setBrokerName
argument_list|(
name|brokerName
argument_list|)
expr_stmt|;
block|}
name|broker
operator|.
name|addConnector
argument_list|(
name|brokerUrl1
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|//END SNIPPET: broker
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|String
name|url
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|url
operator|=
name|args
index|[
literal|0
index|]
expr_stmt|;
block|}
name|EmbeddedJMSBrokerLauncher
name|s
init|=
operator|new
name|EmbeddedJMSBrokerLauncher
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

