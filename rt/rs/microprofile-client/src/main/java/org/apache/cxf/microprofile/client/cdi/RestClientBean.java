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
name|microprofile
operator|.
name|client
operator|.
name|cdi
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Constructor
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
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|concurrent
operator|.
name|TimeUnit
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
name|Level
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
name|annotation
operator|.
name|Priority
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|Dependent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|spi
operator|.
name|CreationalContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Default
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionPoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|PassivationCapable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|util
operator|.
name|AnnotationLiteral
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HostnameVerifier
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Priorities
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|microprofile
operator|.
name|client
operator|.
name|CxfTypeSafeClientBuilder
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
name|microprofile
operator|.
name|client
operator|.
name|config
operator|.
name|ConfigFacade
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|inject
operator|.
name|RegisterRestClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|inject
operator|.
name|RestClient
import|;
end_import

begin_class
specifier|public
class|class
name|RestClientBean
implements|implements
name|Bean
argument_list|<
name|Object
argument_list|>
implements|,
name|PassivationCapable
block|{
specifier|public
specifier|static
specifier|final
name|String
name|REST_URL_FORMAT
init|=
literal|"%s/mp-rest/url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_URI_FORMAT
init|=
literal|"%s/mp-rest/uri"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_SCOPE_FORMAT
init|=
literal|"%s/mp-rest/scope"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_PROVIDERS_FORMAT
init|=
literal|"%s/mp-rest/providers"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_CONN_TIMEOUT_FORMAT
init|=
literal|"%s/mp-rest/connectTimeout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_READ_TIMEOUT_FORMAT
init|=
literal|"%s/mp-rest/readTimeout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_PROVIDERS_PRIORITY_FORMAT
init|=
literal|"/mp-rest/providers/%s/priority"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_TRUST_STORE_FORMAT
init|=
literal|"%s/mp-rest/trustStore"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_TRUST_STORE_PASSWORD_FORMAT
init|=
literal|"%s/mp-rest/trustStorePassword"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_TRUST_STORE_TYPE_FORMAT
init|=
literal|"%s/mp-rest/trustStoreType"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_HOSTNAME_VERIFIER_FORMAT
init|=
literal|"%s/mp-rest/hostnameVerifier"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_KEY_STORE_FORMAT
init|=
literal|"%s/mp-rest/keyStore"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_KEY_STORE_PASSWORD_FORMAT
init|=
literal|"%s/mp-rest/keyStorePassword"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_KEY_STORE_TYPE_FORMAT
init|=
literal|"%s/mp-rest/keyStoreType"
decl_stmt|;
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
name|RestClientBean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Default
name|DEFAULT_LITERAL
init|=
operator|new
name|DefaultLiteral
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clientInterface
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|scope
decl_stmt|;
specifier|private
specifier|final
name|BeanManager
name|beanManager
decl_stmt|;
specifier|public
name|RestClientBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clientInterface
parameter_list|,
name|BeanManager
name|beanManager
parameter_list|)
block|{
name|this
operator|.
name|clientInterface
operator|=
name|clientInterface
expr_stmt|;
name|this
operator|.
name|beanManager
operator|=
name|beanManager
expr_stmt|;
name|this
operator|.
name|scope
operator|=
name|this
operator|.
name|readScope
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|clientInterface
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getBeanClass
parameter_list|()
block|{
return|return
name|clientInterface
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|InjectionPoint
argument_list|>
name|getInjectionPoints
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isNullable
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|create
parameter_list|(
name|CreationalContext
argument_list|<
name|Object
argument_list|>
name|creationalContext
parameter_list|)
block|{
name|CxfTypeSafeClientBuilder
name|builder
init|=
operator|new
name|CxfTypeSafeClientBuilder
argument_list|()
decl_stmt|;
name|String
name|baseUri
init|=
name|getBaseUri
argument_list|()
decl_stmt|;
name|builder
operator|=
operator|(
name|CxfTypeSafeClientBuilder
operator|)
name|builder
operator|.
name|baseUri
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|baseUri
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|providers
init|=
name|getConfiguredProviders
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|providerPriorities
init|=
name|getConfiguredProviderPriorities
argument_list|(
name|providers
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
range|:
name|providers
control|)
block|{
name|builder
operator|=
operator|(
name|CxfTypeSafeClientBuilder
operator|)
name|builder
operator|.
name|register
argument_list|(
name|providerClass
argument_list|,
name|providerPriorities
operator|.
name|getOrDefault
argument_list|(
name|providerClass
argument_list|,
name|Priorities
operator|.
name|USER
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setTimeouts
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|setSSLConfig
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|(
name|clientInterface
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroy
parameter_list|(
name|Object
name|instance
parameter_list|,
name|CreationalContext
argument_list|<
name|Object
argument_list|>
name|creationalContext
parameter_list|)
block|{      }
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Type
argument_list|>
name|getTypes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|clientInterface
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Annotation
argument_list|>
name|getQualifiers
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|DEFAULT_LITERAL
argument_list|,
name|RestClient
operator|.
name|RestClientLiteral
operator|.
name|LITERAL
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|clientInterface
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|getStereotypes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAlternative
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|private
name|String
name|getBaseUri
parameter_list|()
block|{
name|String
name|interfaceName
init|=
name|clientInterface
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|baseURI
init|=
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_URI_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElseGet
argument_list|(
parameter_list|()
lambda|->
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_URL_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseURI
operator|==
literal|null
condition|)
block|{
comment|// last, if baseUrl/Uri is not specified via MP Config, check the @RegisterRestClient annotation
name|RegisterRestClient
name|anno
init|=
name|clientInterface
operator|.
name|getAnnotation
argument_list|(
name|RegisterRestClient
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|anno
operator|!=
literal|null
condition|)
block|{
name|String
name|annoUri
init|=
name|anno
operator|.
name|baseUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|annoUri
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|anno
operator|.
name|baseUri
argument_list|()
argument_list|)
condition|)
block|{
name|baseURI
operator|=
name|annoUri
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|baseURI
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to determine base URI from configuration for "
operator|+
name|interfaceName
argument_list|)
throw|;
block|}
return|return
name|baseURI
return|;
block|}
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|readScope
parameter_list|()
block|{
comment|// first check to see if the value is set
name|String
name|configuredScope
init|=
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_SCOPE_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuredScope
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|configuredScope
argument_list|,
name|getClass
argument_list|()
argument_list|,
name|Annotation
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The scope "
operator|+
name|configuredScope
operator|+
literal|" is invalid"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|List
argument_list|<
name|Annotation
argument_list|>
name|possibleScopes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Annotation
index|[]
name|annotations
init|=
name|clientInterface
operator|.
name|getDeclaredAnnotations
argument_list|()
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|beanManager
operator|.
name|isScope
argument_list|(
name|annotation
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
name|possibleScopes
operator|.
name|add
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|possibleScopes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Dependent
operator|.
name|class
return|;
block|}
elseif|else
if|if
condition|(
name|possibleScopes
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|possibleScopes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|annotationType
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The client interface "
operator|+
name|clientInterface
operator|+
literal|" has multiple scopes defined "
operator|+
name|possibleScopes
argument_list|)
throw|;
block|}
block|}
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getConfiguredProviders
parameter_list|()
block|{
name|String
name|providersList
init|=
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_PROVIDERS_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|providersList
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|providerClassNames
init|=
name|providersList
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|providerClassNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|providers
operator|.
name|add
argument_list|(
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|providerClassNames
index|[
name|i
index|]
argument_list|,
name|RestClientBean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Could not load provider, {0}, configured for Rest Client interface, {1} "
argument_list|,
operator|new
name|Object
index|[]
block|{
name|providerClassNames
index|[
name|i
index|]
block|,
name|clientInterface
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|providers
return|;
block|}
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|getConfiguredProviderPriorities
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|providers
parameter_list|)
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
range|:
name|providers
control|)
block|{
name|String
name|propertyFormat
init|=
literal|"%s"
operator|+
name|String
operator|.
name|format
argument_list|(
name|REST_PROVIDERS_PRIORITY_FORMAT
argument_list|,
name|providerClass
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|priority
init|=
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|propertyFormat
argument_list|,
name|clientInterface
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
name|getPriorityFromClass
argument_list|(
name|providerClass
argument_list|,
name|Priorities
operator|.
name|USER
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|providerClass
argument_list|,
name|priority
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
specifier|private
specifier|static
name|int
name|getPriorityFromClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
name|Priority
name|p
init|=
name|providerClass
operator|.
name|getAnnotation
argument_list|(
name|Priority
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|p
operator|!=
literal|null
condition|?
name|p
operator|.
name|value
argument_list|()
else|:
name|defaultValue
return|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|DefaultLiteral
extends|extends
name|AnnotationLiteral
argument_list|<
name|Default
argument_list|>
implements|implements
name|Default
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|}
specifier|private
name|void
name|setTimeouts
parameter_list|(
name|CxfTypeSafeClientBuilder
name|builder
parameter_list|)
block|{
name|ConfigFacade
operator|.
name|getOptionalLong
argument_list|(
name|REST_CONN_TIMEOUT_FORMAT
argument_list|,
name|clientInterface
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|timeoutValue
lambda|->
block|{
name|builder
operator|.
name|connectTimeout
argument_list|(
name|timeoutValue
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"readTimeout set by MP Config: "
operator|+
name|timeoutValue
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ConfigFacade
operator|.
name|getOptionalLong
argument_list|(
name|REST_READ_TIMEOUT_FORMAT
argument_list|,
name|clientInterface
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|timeoutValue
lambda|->
block|{
name|builder
operator|.
name|readTimeout
argument_list|(
name|timeoutValue
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"readTimeout set by MP Config: "
operator|+
name|timeoutValue
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setSSLConfig
parameter_list|(
name|CxfTypeSafeClientBuilder
name|builder
parameter_list|)
block|{
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_HOSTNAME_VERIFIER_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|className
lambda|->
block|{
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|HostnameVerifier
argument_list|>
name|clazz
init|=
operator|(
name|Class
argument_list|<
name|HostnameVerifier
argument_list|>
operator|)
name|ClassLoaderUtils
operator|.
name|loadClassFromContextLoader
argument_list|(
name|className
argument_list|)
decl_stmt|;
name|Constructor
argument_list|<
name|HostnameVerifier
argument_list|>
name|ctor
init|=
name|ReflectionUtil
operator|.
name|getConstructor
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctor
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|hostnameVerifier
argument_list|(
name|ctor
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore - will log below
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"INVALID_HOSTNAME_VERIFIER_CONFIGURED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|className
operator|,
name|clientInterface
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|)
class|;
end_class

begin_expr_stmt
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_TRUST_STORE_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|trustStoreLoc
lambda|->
block|{
name|initTrustStore
argument_list|(
name|trustStoreLoc
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_KEY_STORE_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|keyStoreLoc
lambda|->
block|{
name|initKeyStore
argument_list|(
name|keyStoreLoc
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
end_expr_stmt

begin_function
unit|}      private
name|void
name|initTrustStore
parameter_list|(
name|String
name|trustStoreLoc
parameter_list|,
name|CxfTypeSafeClientBuilder
name|builder
parameter_list|)
block|{
name|String
name|password
init|=
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_TRUST_STORE_PASSWORD_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|String
name|storeType
init|=
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_TRUST_STORE_TYPE_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
try|try
block|{
name|KeyStore
name|trustStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|storeType
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|input
init|=
name|getInputStream
argument_list|(
name|trustStoreLoc
argument_list|)
init|)
block|{
name|trustStore
operator|.
name|load
argument_list|(
name|input
argument_list|,
name|password
operator|==
literal|null
condition|?
literal|null
else|:
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to initialize trust store from URL, "
operator|+
name|trustStoreLoc
argument_list|,
name|t
argument_list|)
throw|;
block|}
name|builder
operator|.
name|trustStore
argument_list|(
name|trustStore
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to initialize trust store from "
operator|+
name|trustStoreLoc
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
specifier|private
name|void
name|initKeyStore
parameter_list|(
name|String
name|keyStoreLoc
parameter_list|,
name|CxfTypeSafeClientBuilder
name|builder
parameter_list|)
block|{
name|String
name|password
init|=
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_KEY_STORE_PASSWORD_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|String
name|storeType
init|=
name|ConfigFacade
operator|.
name|getOptionalValue
argument_list|(
name|REST_KEY_STORE_TYPE_FORMAT
argument_list|,
name|clientInterface
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
try|try
block|{
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|storeType
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|input
init|=
name|getInputStream
argument_list|(
name|keyStoreLoc
argument_list|)
init|)
block|{
name|keyStore
operator|.
name|load
argument_list|(
name|input
argument_list|,
name|password
operator|==
literal|null
condition|?
literal|null
else|:
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to initialize key store from URL, "
operator|+
name|keyStoreLoc
argument_list|,
name|t
argument_list|)
throw|;
block|}
name|builder
operator|.
name|keyStore
argument_list|(
name|keyStore
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to initialize key store from "
operator|+
name|keyStoreLoc
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
name|InputStream
name|getInputStream
parameter_list|(
name|String
name|location
parameter_list|)
block|{
if|if
condition|(
name|location
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
name|location
operator|=
name|location
operator|.
name|substring
argument_list|(
literal|10
argument_list|)
expr_stmt|;
comment|// 10 == "classpath:".length()
comment|//TODO: replace getResources lookup with getResourceAsStream once
comment|// https://github.com/arquillian/arquillian-container-weld/issues/59 is resolved
comment|// in = ClassLoaderUtils.getResourceAsStream(location, clientInterface);
name|List
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|ClassLoaderUtils
operator|.
name|getResources
argument_list|(
name|location
argument_list|,
name|clientInterface
argument_list|)
decl_stmt|;
if|if
condition|(
name|urls
operator|!=
literal|null
operator|&&
operator|!
name|urls
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
return|return
name|urls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|openStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|LOG
operator|.
name|warning
argument_list|(
literal|"could not find classpath:"
operator|+
name|location
argument_list|)
expr_stmt|;
comment|//TODO: new message
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"could not find configured key/trust store: "
operator|+
name|location
argument_list|)
throw|;
block|}
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|location
argument_list|)
operator|.
name|openStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// try using a file URI
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
literal|"file:"
operator|+
name|location
argument_list|)
operator|.
name|openStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e2
parameter_list|)
block|{
comment|//ignore, rethrow original exception
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"could not find configured key/trust store URL: "
operator|+
name|location
argument_list|)
throw|;
block|}
block|}
end_function

unit|}
end_unit

