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
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|JAXBException
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
name|common
operator|.
name|jaxb
operator|.
name|JAXBUtils
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|jsse
operator|.
name|TLSServerParametersConfig
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
name|http_undertow
operator|.
name|CXFUndertowHttpHandler
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
name|transports
operator|.
name|http_undertow
operator|.
name|configuration
operator|.
name|TLSServerParametersIdentifiedType
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
name|transports
operator|.
name|http_undertow
operator|.
name|configuration
operator|.
name|ThreadingParametersIdentifiedType
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
name|transports
operator|.
name|http_undertow
operator|.
name|configuration
operator|.
name|ThreadingParametersType
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
name|transports
operator|.
name|http_undertow
operator|.
name|configuration
operator|.
name|UndertowHTTPServerEngineConfigType
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
name|transports
operator|.
name|http_undertow
operator|.
name|configuration
operator|.
name|UndertowHTTPServerEngineFactoryConfigType
import|;
end_import

begin_class
specifier|public
class|class
name|UndertowHTTPServerEngineFactoryHolder
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
name|UndertowHTTPServerEngineFactoryHolder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|parsedElement
decl_stmt|;
specifier|private
name|UndertowHTTPServerEngineFactory
name|factory
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CXFUndertowHttpHandler
argument_list|>
argument_list|>
name|handlersMap
decl_stmt|;
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|jaxbClasses
decl_stmt|;
specifier|public
name|UndertowHTTPServerEngineFactoryHolder
parameter_list|()
block|{     }
specifier|public
name|void
name|init
parameter_list|()
block|{
try|try
block|{
name|Element
name|element
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|StringReader
argument_list|(
name|parsedElement
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|UndertowHTTPServerEngineFactoryConfigType
name|config
init|=
name|getJaxbObject
argument_list|(
name|element
argument_list|,
name|UndertowHTTPServerEngineFactoryConfigType
operator|.
name|class
argument_list|)
decl_stmt|;
name|factory
operator|=
operator|new
name|UndertowHTTPServerEngineFactory
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ThreadingParameters
argument_list|>
name|threadingParametersMap
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|ThreadingParameters
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getIdentifiedThreadingParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ThreadingParametersIdentifiedType
name|threads
range|:
name|config
operator|.
name|getIdentifiedThreadingParameters
argument_list|()
control|)
block|{
name|ThreadingParameters
name|rThreads
init|=
operator|new
name|ThreadingParameters
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|threads
operator|.
name|getId
argument_list|()
decl_stmt|;
name|rThreads
operator|.
name|setMaxThreads
argument_list|(
name|threads
operator|.
name|getThreadingParameters
argument_list|()
operator|.
name|getMaxThreads
argument_list|()
argument_list|)
expr_stmt|;
name|rThreads
operator|.
name|setMinThreads
argument_list|(
name|threads
operator|.
name|getThreadingParameters
argument_list|()
operator|.
name|getMinThreads
argument_list|()
argument_list|)
expr_stmt|;
name|rThreads
operator|.
name|setWorkerIOThreads
argument_list|(
name|threads
operator|.
name|getThreadingParameters
argument_list|()
operator|.
name|getWorkerIOThreads
argument_list|()
argument_list|)
expr_stmt|;
name|threadingParametersMap
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|rThreads
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|setThreadingParametersMap
argument_list|(
name|threadingParametersMap
argument_list|)
expr_stmt|;
block|}
comment|//SSL
name|Map
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
name|sslMap
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getIdentifiedTLSServerParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|TLSServerParametersIdentifiedType
name|t
range|:
name|config
operator|.
name|getIdentifiedTLSServerParameters
argument_list|()
control|)
block|{
try|try
block|{
name|TLSServerParameters
name|parameter
init|=
operator|new
name|TLSServerParametersConfig
argument_list|(
name|t
operator|.
name|getTlsServerParameters
argument_list|()
argument_list|)
decl_stmt|;
name|sslMap
operator|.
name|put
argument_list|(
name|t
operator|.
name|getId
argument_list|()
argument_list|,
name|parameter
argument_list|)
expr_stmt|;
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
literal|"Could not configure TLS for id "
operator|+
name|t
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|factory
operator|.
name|setTlsServerParametersMap
argument_list|(
name|sslMap
argument_list|)
expr_stmt|;
block|}
comment|//Engines
name|List
argument_list|<
name|UndertowHTTPServerEngine
argument_list|>
name|engineList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|UndertowHTTPServerEngineConfigType
name|engine
range|:
name|config
operator|.
name|getEngine
argument_list|()
control|)
block|{
name|UndertowHTTPServerEngine
name|eng
init|=
operator|new
name|UndertowHTTPServerEngine
argument_list|()
decl_stmt|;
if|if
condition|(
name|engine
operator|.
name|getHandlers
argument_list|()
operator|!=
literal|null
operator|&&
name|handlersMap
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|CXFUndertowHttpHandler
argument_list|>
name|handlers
init|=
name|handlersMap
operator|.
name|get
argument_list|(
name|engine
operator|.
name|getPort
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|handlers
operator|!=
literal|null
condition|)
block|{
name|eng
operator|.
name|setHandlers
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not find the handlers instance for engine with port"
operator|+
name|engine
operator|.
name|getPort
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|engine
operator|.
name|isContinuationsEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|eng
operator|.
name|setContinuationsEnabled
argument_list|(
name|engine
operator|.
name|isContinuationsEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|engine
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|engine
operator|.
name|getHost
argument_list|()
argument_list|)
condition|)
block|{
name|eng
operator|.
name|setHost
argument_list|(
name|engine
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|engine
operator|.
name|getMaxIdleTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|eng
operator|.
name|setMaxIdleTime
argument_list|(
name|engine
operator|.
name|getMaxIdleTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|engine
operator|.
name|getPort
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|eng
operator|.
name|setPort
argument_list|(
name|engine
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|engine
operator|.
name|getThreadingParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ThreadingParametersType
name|threads
init|=
name|engine
operator|.
name|getThreadingParameters
argument_list|()
decl_stmt|;
name|ThreadingParameters
name|rThreads
init|=
operator|new
name|ThreadingParameters
argument_list|()
decl_stmt|;
name|rThreads
operator|.
name|setMaxThreads
argument_list|(
name|threads
operator|.
name|getMaxThreads
argument_list|()
argument_list|)
expr_stmt|;
name|rThreads
operator|.
name|setMinThreads
argument_list|(
name|threads
operator|.
name|getMinThreads
argument_list|()
argument_list|)
expr_stmt|;
name|rThreads
operator|.
name|setWorkerIOThreads
argument_list|(
name|threads
operator|.
name|getWorkerIOThreads
argument_list|()
argument_list|)
expr_stmt|;
name|eng
operator|.
name|setThreadingParameters
argument_list|(
name|rThreads
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|engine
operator|.
name|getTlsServerParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|TLSServerParameters
name|parameter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|parameter
operator|=
operator|new
name|TLSServerParametersConfig
argument_list|(
name|engine
operator|.
name|getTlsServerParameters
argument_list|()
argument_list|)
expr_stmt|;
name|eng
operator|.
name|setTlsServerParameters
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
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
literal|"Could not configure TLS for engine on  "
operator|+
name|eng
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|eng
operator|.
name|getPort
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|eng
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
name|engineList
operator|.
name|add
argument_list|(
name|eng
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|setEnginesList
argument_list|(
name|engineList
argument_list|)
expr_stmt|;
comment|//Unravel this completely.
name|factory
operator|.
name|initComplete
argument_list|()
expr_stmt|;
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
literal|"Could not process configuration."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
comment|// need to release the reference of the jaxb Classes
name|factory
operator|.
name|postShutdown
argument_list|()
expr_stmt|;
name|jaxbClasses
operator|.
name|clear
argument_list|()
expr_stmt|;
name|jaxbContext
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|String
name|getParsedElement
parameter_list|()
block|{
return|return
name|parsedElement
return|;
block|}
specifier|public
name|void
name|setParsedElement
parameter_list|(
name|String
name|parsedElement
parameter_list|)
block|{
name|this
operator|.
name|parsedElement
operator|=
name|parsedElement
expr_stmt|;
block|}
specifier|public
name|void
name|setHandlersMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CXFUndertowHttpHandler
argument_list|>
argument_list|>
name|handlersMap
parameter_list|)
block|{
name|this
operator|.
name|handlersMap
operator|=
name|handlersMap
expr_stmt|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getJaxbObject
parameter_list|(
name|Element
name|parent
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|c
parameter_list|)
block|{
try|try
block|{
name|JAXBElement
argument_list|<
name|T
argument_list|>
name|ele
init|=
name|JAXBUtils
operator|.
name|unmarshall
argument_list|(
name|getContext
argument_list|(
name|c
argument_list|)
argument_list|,
name|parent
argument_list|,
name|c
argument_list|)
decl_stmt|;
return|return
name|ele
operator|.
name|getValue
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unable to parse property due to "
operator|+
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|protected
specifier|synchronized
name|JAXBContext
name|getContext
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
name|jaxbContext
operator|==
literal|null
operator|||
name|jaxbClasses
operator|==
literal|null
operator|||
operator|!
name|jaxbClasses
operator|.
name|contains
argument_list|(
name|cls
argument_list|)
condition|)
block|{
try|try
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|tmp
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
if|if
condition|(
name|jaxbClasses
operator|!=
literal|null
condition|)
block|{
name|tmp
operator|.
name|addAll
argument_list|(
name|jaxbClasses
argument_list|)
expr_stmt|;
block|}
name|JAXBContextCache
operator|.
name|addPackage
argument_list|(
name|tmp
argument_list|,
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|cls
argument_list|)
argument_list|,
name|cls
operator|==
literal|null
condition|?
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
else|:
name|cls
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
name|boolean
name|hasOf
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|tmp
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getPackage
argument_list|()
operator|==
name|cls
operator|.
name|getPackage
argument_list|()
operator|&&
literal|"ObjectFactory"
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getSimpleName
argument_list|()
argument_list|)
condition|)
block|{
name|hasOf
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|hasOf
condition|)
block|{
name|tmp
operator|.
name|add
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
name|JAXBContextCache
operator|.
name|scanPackages
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|JAXBContextCache
operator|.
name|CachedContextAndSchemas
name|ccs
init|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|tmp
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|jaxbClasses
operator|=
name|ccs
operator|.
name|getClasses
argument_list|()
expr_stmt|;
name|jaxbContext
operator|=
name|ccs
operator|.
name|getContext
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
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
block|}
return|return
name|jaxbContext
return|;
block|}
block|}
end_class

end_unit

