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
name|http_undertow
operator|.
name|osgi
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
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
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
name|bus
operator|.
name|blueprint
operator|.
name|BlueprintNameSpaceHandlerFactory
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
name|bus
operator|.
name|blueprint
operator|.
name|NamespaceHandlerRegisterer
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSParameterJaxBUtils
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSServerParameters
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
name|configuration
operator|.
name|security
operator|.
name|CertStoreType
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
name|configuration
operator|.
name|security
operator|.
name|CertificateConstraintsType
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
name|configuration
operator|.
name|security
operator|.
name|ClientAuthentication
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
name|configuration
operator|.
name|security
operator|.
name|CombinatorType
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
name|configuration
operator|.
name|security
operator|.
name|DNConstraintsType
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
name|configuration
operator|.
name|security
operator|.
name|FiltersType
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
name|configuration
operator|.
name|security
operator|.
name|KeyManagersType
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
name|configuration
operator|.
name|security
operator|.
name|KeyStoreType
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
name|configuration
operator|.
name|security
operator|.
name|SecureRandomParameters
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
name|configuration
operator|.
name|security
operator|.
name|TrustManagersType
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
name|http_undertow
operator|.
name|ThreadingParameters
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
name|http_undertow
operator|.
name|UndertowHTTPServerEngine
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
name|http_undertow
operator|.
name|UndertowHTTPServerEngineFactory
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
name|http_undertow
operator|.
name|blueprint
operator|.
name|HTTPUndertowTransportNamespaceHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceRegistration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ManagedServiceFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_class
specifier|public
class|class
name|HTTPUndertowTransportActivator
implements|implements
name|BundleActivator
implements|,
name|ManagedServiceFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FACTORY_PID
init|=
literal|"org.apache.cxf.http.undertow"
decl_stmt|;
name|BundleContext
name|context
decl_stmt|;
name|MBeanServer
name|mbeans
decl_stmt|;
name|ServiceTracker
name|mbeanServerTracker
decl_stmt|;
name|ServiceRegistration
name|reg
decl_stmt|;
name|UndertowHTTPServerEngineFactory
name|factory
init|=
operator|new
name|UndertowHTTPServerEngineFactory
argument_list|()
block|{
specifier|public
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
return|return
operator|(
name|MBeanServer
operator|)
name|mbeanServerTracker
operator|.
name|getService
argument_list|()
return|;
block|}
block|}
decl_stmt|;
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|context
operator|=
name|ctx
expr_stmt|;
name|Properties
name|servProps
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|servProps
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|,
name|FACTORY_PID
argument_list|)
expr_stmt|;
name|reg
operator|=
name|context
operator|.
name|registerService
argument_list|(
name|ManagedServiceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|this
argument_list|,
name|servProps
argument_list|)
expr_stmt|;
name|mbeanServerTracker
operator|=
operator|new
name|ServiceTracker
argument_list|(
name|ctx
argument_list|,
name|MBeanServer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|BlueprintNameSpaceHandlerFactory
name|nsHandlerFactory
init|=
operator|new
name|BlueprintNameSpaceHandlerFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|createNamespaceHandler
parameter_list|()
block|{
return|return
operator|new
name|HTTPUndertowTransportNamespaceHandler
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|NamespaceHandlerRegisterer
operator|.
name|register
argument_list|(
name|context
argument_list|,
name|nsHandlerFactory
argument_list|,
literal|"http://cxf.apache.org/transports/http-undertow/configuration"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
comment|// Blueprint not available, ignore
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|mbeanServerTracker
operator|.
name|close
argument_list|()
expr_stmt|;
name|reg
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|FACTORY_PID
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|updated
parameter_list|(
name|String
name|pid
parameter_list|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|Dictionary
name|properties
parameter_list|)
throws|throws
name|ConfigurationException
block|{
if|if
condition|(
name|pid
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|int
name|port
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"port"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|host
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"host"
argument_list|)
decl_stmt|;
try|try
block|{
name|TLSServerParameters
name|tls
init|=
name|createTlsServerParameters
argument_list|(
name|properties
argument_list|)
decl_stmt|;
if|if
condition|(
name|tls
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setTLSServerParametersForPort
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|tls
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
literal|"http"
argument_list|)
expr_stmt|;
block|}
name|UndertowHTTPServerEngine
name|e
init|=
name|factory
operator|.
name|retrieveUndertowHTTPServerEngine
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|configure
argument_list|(
name|e
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|configure
parameter_list|(
name|UndertowHTTPServerEngine
name|e
parameter_list|,
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|ThreadingParameters
name|threading
init|=
name|createThreadingParameters
argument_list|(
name|properties
argument_list|)
decl_stmt|;
if|if
condition|(
name|threading
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|setThreadingParameters
argument_list|(
name|threading
argument_list|)
expr_stmt|;
block|}
name|Enumeration
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|properties
operator|.
name|keys
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|k
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"continuationsEnabled"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|e
operator|.
name|setContinuationsEnabled
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|properties
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"maxIdleTime"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|e
operator|.
name|setMaxIdleTime
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|properties
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|deleted
parameter_list|(
name|String
name|pid
parameter_list|)
block|{     }
specifier|private
name|ThreadingParameters
name|createThreadingParameters
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|d
parameter_list|)
block|{
name|Enumeration
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|d
operator|.
name|keys
argument_list|()
decl_stmt|;
name|ThreadingParameters
name|p
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|k
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"threadingParameters."
argument_list|)
condition|)
block|{
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|p
operator|=
operator|new
name|ThreadingParameters
argument_list|()
expr_stmt|;
block|}
name|String
name|v
init|=
name|d
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
name|k
operator|=
name|k
operator|.
name|substring
argument_list|(
literal|"threadingParameters."
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"minThreads"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|p
operator|.
name|setMinThreads
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"maxThreads"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|p
operator|.
name|setMaxThreads
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"workerIOThreads"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|p
operator|.
name|setWorkerIOThreads
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|p
return|;
block|}
specifier|private
name|TLSServerParameters
name|createTlsServerParameters
parameter_list|(
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|d
parameter_list|)
block|{
name|Enumeration
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|d
operator|.
name|keys
argument_list|()
decl_stmt|;
name|TLSServerParameters
name|p
init|=
literal|null
decl_stmt|;
name|SecureRandomParameters
name|srp
init|=
literal|null
decl_stmt|;
name|KeyManagersType
name|kmt
init|=
literal|null
decl_stmt|;
name|TrustManagersType
name|tmt
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|k
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"tlsServerParameters."
argument_list|)
condition|)
block|{
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|p
operator|=
operator|new
name|TLSServerParameters
argument_list|()
expr_stmt|;
block|}
name|String
name|v
init|=
name|d
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
name|k
operator|=
name|k
operator|.
name|substring
argument_list|(
literal|"tlsServerParameters."
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"secureSocketProtocol"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|p
operator|.
name|setSecureSocketProtocol
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"jsseProvider"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|p
operator|.
name|setJsseProvider
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"certAlias"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|p
operator|.
name|setCertAlias
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"clientAuthentication.want"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
if|if
condition|(
name|p
operator|.
name|getClientAuthentication
argument_list|()
operator|==
literal|null
condition|)
block|{
name|p
operator|.
name|setClientAuthentication
argument_list|(
operator|new
name|ClientAuthentication
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|getClientAuthentication
argument_list|()
operator|.
name|setWant
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"clientAuthentication.required"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
if|if
condition|(
name|p
operator|.
name|getClientAuthentication
argument_list|()
operator|==
literal|null
condition|)
block|{
name|p
operator|.
name|setClientAuthentication
argument_list|(
operator|new
name|ClientAuthentication
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|getClientAuthentication
argument_list|()
operator|.
name|setRequired
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"certConstraints."
argument_list|)
condition|)
block|{
name|configureCertConstraints
argument_list|(
name|p
argument_list|,
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"secureRandomParameters."
argument_list|)
condition|)
block|{
name|srp
operator|=
name|configureSecureRandom
argument_list|(
name|srp
argument_list|,
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"cipherSuitesFilter."
argument_list|)
condition|)
block|{
name|configureCipherSuitesFilter
argument_list|(
name|p
argument_list|,
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"cipherSuites"
argument_list|)
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|v
argument_list|,
literal|","
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|p
operator|.
name|getCipherSuites
argument_list|()
operator|.
name|add
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"excludeProtocols"
argument_list|)
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|v
argument_list|,
literal|","
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|p
operator|.
name|getExcludeProtocols
argument_list|()
operator|.
name|add
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"trustManagers."
argument_list|)
condition|)
block|{
name|tmt
operator|=
name|getTrustManagers
argument_list|(
name|tmt
argument_list|,
name|k
operator|.
name|substring
argument_list|(
literal|"trustManagers."
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"keyManagers."
argument_list|)
condition|)
block|{
name|kmt
operator|=
name|getKeyManagers
argument_list|(
name|kmt
argument_list|,
name|k
operator|.
name|substring
argument_list|(
literal|"keyManagers."
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
try|try
block|{
if|if
condition|(
name|srp
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setSecureRandom
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getSecureRandom
argument_list|(
name|srp
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|kmt
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setKeyManagers
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getKeyManagers
argument_list|(
name|kmt
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tmt
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|setTrustManagers
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getTrustManagers
argument_list|(
name|tmt
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
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
return|return
name|p
return|;
block|}
specifier|private
name|void
name|configureCipherSuitesFilter
parameter_list|(
name|TLSServerParameters
name|p
parameter_list|,
name|String
name|k
parameter_list|,
name|String
name|v
parameter_list|)
block|{
name|k
operator|=
name|k
operator|.
name|substring
argument_list|(
literal|"cipherSuitesFilter."
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|v
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|FiltersType
name|ft
init|=
name|p
operator|.
name|getCipherSuitesFilter
argument_list|()
decl_stmt|;
if|if
condition|(
name|ft
operator|==
literal|null
condition|)
block|{
name|ft
operator|=
operator|new
name|FiltersType
argument_list|()
expr_stmt|;
name|p
operator|.
name|setCipherSuitesFilter
argument_list|(
name|ft
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|lst
init|=
literal|"include"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|?
name|ft
operator|.
name|getInclude
argument_list|()
else|:
name|ft
operator|.
name|getExclude
argument_list|()
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|lst
operator|.
name|add
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|SecureRandomParameters
name|configureSecureRandom
parameter_list|(
name|SecureRandomParameters
name|srp
parameter_list|,
name|String
name|k
parameter_list|,
name|String
name|v
parameter_list|)
block|{
name|k
operator|=
name|k
operator|.
name|substring
argument_list|(
literal|"secureRandomParameters."
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|srp
operator|==
literal|null
condition|)
block|{
name|srp
operator|=
operator|new
name|SecureRandomParameters
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|"algorithm"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|srp
operator|.
name|setAlgorithm
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"provider"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|srp
operator|.
name|setProvider
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
return|return
name|srp
return|;
block|}
specifier|private
name|void
name|configureCertConstraints
parameter_list|(
name|TLSServerParameters
name|p
parameter_list|,
name|String
name|k
parameter_list|,
name|String
name|v
parameter_list|)
block|{
name|k
operator|=
name|k
operator|.
name|substring
argument_list|(
literal|"certConstraints."
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|CertificateConstraintsType
name|cct
init|=
name|p
operator|.
name|getCertConstraints
argument_list|()
decl_stmt|;
if|if
condition|(
name|cct
operator|==
literal|null
condition|)
block|{
name|cct
operator|=
operator|new
name|CertificateConstraintsType
argument_list|()
expr_stmt|;
name|p
operator|.
name|setCertConstraints
argument_list|(
name|cct
argument_list|)
expr_stmt|;
block|}
name|DNConstraintsType
name|dnct
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"SubjectDNConstraints."
argument_list|)
condition|)
block|{
name|dnct
operator|=
name|cct
operator|.
name|getSubjectDNConstraints
argument_list|()
expr_stmt|;
if|if
condition|(
name|dnct
operator|==
literal|null
condition|)
block|{
name|dnct
operator|=
operator|new
name|DNConstraintsType
argument_list|()
expr_stmt|;
name|cct
operator|.
name|setSubjectDNConstraints
argument_list|(
name|dnct
argument_list|)
expr_stmt|;
block|}
name|k
operator|=
name|k
operator|.
name|substring
argument_list|(
literal|"SubjectDNConstraints."
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"IssuerDNConstraints."
argument_list|)
condition|)
block|{
name|dnct
operator|=
name|cct
operator|.
name|getIssuerDNConstraints
argument_list|()
expr_stmt|;
if|if
condition|(
name|dnct
operator|==
literal|null
condition|)
block|{
name|dnct
operator|=
operator|new
name|DNConstraintsType
argument_list|()
expr_stmt|;
name|cct
operator|.
name|setIssuerDNConstraints
argument_list|(
name|dnct
argument_list|)
expr_stmt|;
block|}
name|k
operator|=
name|k
operator|.
name|substring
argument_list|(
literal|"IssuerDNConstraints."
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dnct
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"combinator"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|dnct
operator|.
name|setCombinator
argument_list|(
name|CombinatorType
operator|.
name|fromValue
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"RegularExpression"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|dnct
operator|.
name|getRegularExpression
argument_list|()
operator|.
name|add
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|KeyManagersType
name|getKeyManagers
parameter_list|(
name|KeyManagersType
name|keyManagers
parameter_list|,
name|String
name|k
parameter_list|,
name|String
name|v
parameter_list|)
block|{
if|if
condition|(
name|keyManagers
operator|==
literal|null
condition|)
block|{
name|keyManagers
operator|=
operator|new
name|KeyManagersType
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|"factoryAlgorithm"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|keyManagers
operator|.
name|setFactoryAlgorithm
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"provider"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|keyManagers
operator|.
name|setProvider
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"keyPassword"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|keyManagers
operator|.
name|setKeyPassword
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"keyStore."
argument_list|)
condition|)
block|{
name|keyManagers
operator|.
name|setKeyStore
argument_list|(
name|getKeyStore
argument_list|(
name|keyManagers
operator|.
name|getKeyStore
argument_list|()
argument_list|,
name|k
operator|.
name|substring
argument_list|(
literal|"keyStore."
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|keyManagers
return|;
block|}
specifier|private
name|KeyStoreType
name|getKeyStore
parameter_list|(
name|KeyStoreType
name|ks
parameter_list|,
name|String
name|k
parameter_list|,
name|String
name|v
parameter_list|)
block|{
if|if
condition|(
name|ks
operator|==
literal|null
condition|)
block|{
name|ks
operator|=
operator|new
name|KeyStoreType
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|"type"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|ks
operator|.
name|setType
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"password"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|ks
operator|.
name|setPassword
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"provider"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|ks
operator|.
name|setProvider
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"url"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|ks
operator|.
name|setUrl
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|ks
operator|.
name|setFile
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"resource"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|ks
operator|.
name|setResource
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
return|return
name|ks
return|;
block|}
specifier|private
name|TrustManagersType
name|getTrustManagers
parameter_list|(
name|TrustManagersType
name|tmt
parameter_list|,
name|String
name|k
parameter_list|,
name|String
name|v
parameter_list|)
block|{
if|if
condition|(
name|tmt
operator|==
literal|null
condition|)
block|{
name|tmt
operator|=
operator|new
name|TrustManagersType
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|"provider"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|tmt
operator|.
name|setProvider
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"factoryAlgorithm"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|tmt
operator|.
name|setFactoryAlgorithm
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"keyStore."
argument_list|)
condition|)
block|{
name|tmt
operator|.
name|setKeyStore
argument_list|(
name|getKeyStore
argument_list|(
name|tmt
operator|.
name|getKeyStore
argument_list|()
argument_list|,
name|k
operator|.
name|substring
argument_list|(
literal|"keyStore."
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|k
operator|.
name|startsWith
argument_list|(
literal|"certStore"
argument_list|)
condition|)
block|{
name|tmt
operator|.
name|setCertStore
argument_list|(
name|getCertStore
argument_list|(
name|tmt
operator|.
name|getCertStore
argument_list|()
argument_list|,
name|k
operator|.
name|substring
argument_list|(
literal|"certStore."
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|tmt
return|;
block|}
specifier|private
name|CertStoreType
name|getCertStore
parameter_list|(
name|CertStoreType
name|cs
parameter_list|,
name|String
name|k
parameter_list|,
name|String
name|v
parameter_list|)
block|{
if|if
condition|(
name|cs
operator|==
literal|null
condition|)
block|{
name|cs
operator|=
operator|new
name|CertStoreType
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|cs
operator|.
name|setFile
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"url"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|cs
operator|.
name|setUrl
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"resource"
operator|.
name|equals
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|cs
operator|.
name|setResource
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
return|return
name|cs
return|;
block|}
block|}
end_class

end_unit

