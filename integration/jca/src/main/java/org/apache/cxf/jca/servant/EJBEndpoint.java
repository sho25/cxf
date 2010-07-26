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
name|jca
operator|.
name|servant
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|EJBHome
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|work
operator|.
name|WorkManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|rmi
operator|.
name|PortableRemoteObject
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|PackageUtils
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|JaxWsServerFactoryBean
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
name|jca
operator|.
name|cxf
operator|.
name|WorkManagerThreadPool
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
name|http_jetty
operator|.
name|JettyHTTPServerEngine
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
name|http_jetty
operator|.
name|JettyHTTPServerEngineFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|AbstractConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|nio
operator|.
name|SelectChannelConnector
import|;
end_import

begin_class
specifier|public
class|class
name|EJBEndpoint
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|EJBEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_HTTP_PORT
init|=
literal|80
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HTTPS_PREFIX
init|=
literal|"https"
decl_stmt|;
specifier|private
name|EJBServantConfig
name|config
decl_stmt|;
specifier|private
name|Context
name|jndiContext
decl_stmt|;
specifier|private
name|EJBHome
name|ejbHome
decl_stmt|;
specifier|private
name|String
name|ejbServantBaseURL
decl_stmt|;
specifier|private
name|WorkManager
name|workManager
decl_stmt|;
specifier|public
name|EJBEndpoint
parameter_list|(
name|EJBServantConfig
name|ejbConfig
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|ejbConfig
expr_stmt|;
block|}
specifier|public
name|Server
name|publish
parameter_list|()
throws|throws
name|Exception
block|{
name|jndiContext
operator|=
operator|new
name|InitialContext
argument_list|()
expr_stmt|;
name|Object
name|obj
init|=
name|jndiContext
operator|.
name|lookup
argument_list|(
name|config
operator|.
name|getJNDIName
argument_list|()
argument_list|)
decl_stmt|;
name|ejbHome
operator|=
operator|(
name|EJBHome
operator|)
name|PortableRemoteObject
operator|.
name|narrow
argument_list|(
name|obj
argument_list|,
name|EJBHome
operator|.
name|class
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|interfaceClass
init|=
name|Class
operator|.
name|forName
argument_list|(
name|getServiceClassName
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|isJaxws
init|=
name|isJaxWsServiceInterface
argument_list|(
name|interfaceClass
argument_list|)
decl_stmt|;
name|ServerFactoryBean
name|factory
init|=
name|isJaxws
condition|?
operator|new
name|JaxWsServerFactoryBean
argument_list|()
else|:
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|interfaceClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getWsdlURL
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWsdlURL
argument_list|(
name|config
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|setInvoker
argument_list|(
operator|new
name|EJBInvoker
argument_list|(
name|ejbHome
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|baseAddress
init|=
name|isNotNull
argument_list|(
name|getEjbServantBaseURL
argument_list|()
argument_list|)
condition|?
name|getEjbServantBaseURL
argument_list|()
else|:
name|getDefaultEJBServantBaseURL
argument_list|()
decl_stmt|;
name|String
name|address
init|=
name|baseAddress
operator|+
literal|"/"
operator|+
name|config
operator|.
name|getJNDIName
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
if|if
condition|(
name|address
operator|.
name|length
argument_list|()
operator|>=
literal|5
operator|&&
name|HTTPS_PREFIX
operator|.
name|equalsIgnoreCase
argument_list|(
name|address
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"EJBEndpoint creation by https protocol is unsupported"
argument_list|)
throw|;
block|}
if|if
condition|(
name|getWorkManager
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setWorkManagerThreadPoolToJetty
argument_list|(
name|factory
operator|.
name|getBus
argument_list|()
argument_list|,
name|baseAddress
argument_list|)
expr_stmt|;
block|}
name|Server
name|server
init|=
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Published EJB Endpoint of ["
operator|+
name|config
operator|.
name|getJNDIName
argument_list|()
operator|+
literal|"] at ["
operator|+
name|address
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|server
return|;
block|}
specifier|protected
name|void
name|setWorkManagerThreadPoolToJetty
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|baseAddress
parameter_list|)
block|{
name|JettyHTTPServerEngineFactory
name|engineFactory
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|JettyHTTPServerEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|getAddressPort
argument_list|(
name|baseAddress
argument_list|)
decl_stmt|;
if|if
condition|(
name|engineFactory
operator|.
name|retrieveJettyHTTPServerEngine
argument_list|(
name|port
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|JettyHTTPServerEngine
name|engine
init|=
operator|new
name|JettyHTTPServerEngine
argument_list|()
decl_stmt|;
name|AbstractConnector
name|connector
init|=
operator|new
name|SelectChannelConnector
argument_list|()
decl_stmt|;
name|connector
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setThreadPool
argument_list|(
operator|new
name|WorkManagerThreadPool
argument_list|(
name|getWorkManager
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JettyHTTPServerEngine
argument_list|>
name|engineList
init|=
operator|new
name|ArrayList
argument_list|<
name|JettyHTTPServerEngine
argument_list|>
argument_list|()
decl_stmt|;
name|engineList
operator|.
name|add
argument_list|(
name|engine
argument_list|)
expr_stmt|;
name|engineFactory
operator|.
name|setEnginesList
argument_list|(
name|engineList
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getServiceClassName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|packageName
init|=
name|PackageUtils
operator|.
name|parsePackageName
argument_list|(
name|config
operator|.
name|getServiceName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|interfaceName
init|=
name|packageName
operator|+
literal|"."
operator|+
name|config
operator|.
name|getJNDIName
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|config
operator|.
name|getJNDIName
argument_list|()
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
decl_stmt|;
return|return
name|interfaceName
return|;
block|}
specifier|public
name|String
name|getDefaultEJBServantBaseURL
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|hostName
init|=
literal|""
decl_stmt|;
try|try
block|{
name|InetAddress
name|addr
init|=
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
decl_stmt|;
name|hostName
operator|=
name|addr
operator|.
name|getCanonicalHostName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|e
parameter_list|)
block|{
name|hostName
operator|=
literal|"localhost"
expr_stmt|;
block|}
return|return
literal|"http://"
operator|+
name|hostName
operator|+
literal|":9999"
return|;
block|}
specifier|public
name|int
name|getAddressPort
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|int
name|index
init|=
name|address
operator|.
name|lastIndexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|int
name|end
init|=
name|address
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
literal|4
condition|)
block|{
return|return
name|DEFAULT_HTTP_PORT
return|;
block|}
if|if
condition|(
name|end
operator|<
name|index
condition|)
block|{
return|return
operator|new
name|Integer
argument_list|(
name|address
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
argument_list|)
operator|.
name|intValue
argument_list|()
return|;
block|}
return|return
operator|new
name|Integer
argument_list|(
name|address
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|end
argument_list|)
argument_list|)
operator|.
name|intValue
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isJaxWsServiceInterface
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
literal|null
operator|!=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|WebService
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|getEjbServantBaseURL
parameter_list|()
block|{
return|return
name|ejbServantBaseURL
return|;
block|}
specifier|public
name|void
name|setEjbServantBaseURL
parameter_list|(
name|String
name|ejbServantBaseURL
parameter_list|)
block|{
name|this
operator|.
name|ejbServantBaseURL
operator|=
name|ejbServantBaseURL
expr_stmt|;
block|}
specifier|private
specifier|static
name|boolean
name|isNotNull
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|value
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|WorkManager
name|getWorkManager
parameter_list|()
block|{
return|return
name|workManager
return|;
block|}
specifier|public
name|void
name|setWorkManager
parameter_list|(
name|WorkManager
name|workManager
parameter_list|)
block|{
name|this
operator|.
name|workManager
operator|=
name|workManager
expr_stmt|;
block|}
block|}
end_class

end_unit

