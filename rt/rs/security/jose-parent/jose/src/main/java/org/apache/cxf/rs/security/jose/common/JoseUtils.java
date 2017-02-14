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
name|rs
operator|.
name|security
operator|.
name|jose
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
name|io
operator|.
name|InputStream
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|Properties
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
name|logging
operator|.
name|Logger
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
name|jaxrs
operator|.
name|json
operator|.
name|basic
operator|.
name|JsonMapObjectReaderWriter
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
name|message
operator|.
name|MessageUtils
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
name|resource
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwe
operator|.
name|JweHeaders
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jws
operator|.
name|JwsHeaders
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
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JoseUtils
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
name|JoseUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLASSPATH_PREFIX
init|=
literal|"classpath:"
decl_stmt|;
specifier|private
name|JoseUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|String
index|[]
name|getCompactParts
parameter_list|(
name|String
name|compactContent
parameter_list|)
block|{
if|if
condition|(
name|compactContent
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|&&
name|compactContent
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|compactContent
operator|=
name|compactContent
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|compactContent
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|StringUtils
operator|.
name|split
argument_list|(
name|compactContent
argument_list|,
literal|"\\."
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|setJoseContextProperty
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|String
name|context
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|JoseConstants
operator|.
name|JOSE_CONTEXT_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|JOSE_CONTEXT_PROPERTY
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|setJoseMessageContextProperty
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|headers
operator|.
name|setHeader
argument_list|(
name|JoseConstants
operator|.
name|JOSE_CONTEXT_PROPERTY
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|JOSE_CONTEXT_PROPERTY
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|setMessageContextProperty
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
name|String
name|context
init|=
operator|(
name|String
operator|)
name|headers
operator|.
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|JOSE_CONTEXT_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|JOSE_CONTEXT_PROPERTY
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|validateRequestContextProperty
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|Object
name|requestContext
init|=
name|message
operator|.
name|get
argument_list|(
name|JoseConstants
operator|.
name|JOSE_CONTEXT_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|headerContext
init|=
name|headers
operator|.
name|getHeader
argument_list|(
name|JoseConstants
operator|.
name|JOSE_CONTEXT_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestContext
operator|==
literal|null
operator|&&
name|headerContext
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|requestContext
operator|==
literal|null
operator|&&
name|headerContext
operator|!=
literal|null
operator|||
name|requestContext
operator|!=
literal|null
operator|&&
name|headerContext
operator|==
literal|null
operator|||
operator|!
name|requestContext
operator|.
name|equals
argument_list|(
name|headerContext
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Invalid JOSE context property"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|()
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|checkContentType
parameter_list|(
name|String
name|contentType
parameter_list|,
name|String
name|defaultType
parameter_list|)
block|{
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|int
name|paramIndex
init|=
name|contentType
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
decl_stmt|;
name|String
name|typeWithoutParams
init|=
name|paramIndex
operator|==
operator|-
literal|1
condition|?
name|contentType
else|:
name|contentType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|paramIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeWithoutParams
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|contentType
operator|=
literal|"application/"
operator|+
name|contentType
expr_stmt|;
block|}
block|}
else|else
block|{
name|contentType
operator|=
name|defaultType
expr_stmt|;
block|}
return|return
name|contentType
return|;
block|}
specifier|public
specifier|static
name|String
name|expandContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|int
name|paramIndex
init|=
name|contentType
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
decl_stmt|;
name|String
name|typeWithoutParams
init|=
name|paramIndex
operator|==
operator|-
literal|1
condition|?
name|contentType
else|:
name|contentType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|paramIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeWithoutParams
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|contentType
operator|=
literal|"application/"
operator|+
name|contentType
expr_stmt|;
block|}
return|return
name|contentType
return|;
block|}
specifier|public
specifier|static
name|String
name|decodeToString
parameter_list|(
name|String
name|encoded
parameter_list|)
block|{
return|return
operator|new
name|String
argument_list|(
name|decode
argument_list|(
name|encoded
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|decode
parameter_list|(
name|String
name|encoded
parameter_list|)
block|{
return|return
name|CryptoUtils
operator|.
name|decodeSequence
argument_list|(
name|encoded
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|validateCriticalHeaders
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|critical
init|=
name|headers
operator|.
name|getCritical
argument_list|()
decl_stmt|;
if|if
condition|(
name|critical
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// The "crit" value MUST NOT be empty "[]" or contain either duplicate values or "crit"
if|if
condition|(
name|critical
operator|.
name|isEmpty
argument_list|()
operator|||
name|detectDoubleEntry
argument_list|(
name|critical
argument_list|)
operator|||
name|critical
operator|.
name|contains
argument_list|(
name|JoseConstants
operator|.
name|HEADER_CRITICAL
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Check that the headers contain these critical headers
return|return
name|headers
operator|.
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|containsAll
argument_list|(
name|critical
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|boolean
name|detectDoubleEntry
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|list
parameter_list|)
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|inputSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|list
argument_list|)
decl_stmt|;
return|return
name|list
operator|.
name|size
argument_list|()
operator|>
name|inputSet
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|traceHeaders
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
name|Message
name|m
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|m
argument_list|,
name|JoseConstants
operator|.
name|JOSE_DEBUG
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|JsonMapObjectReaderWriter
name|writer
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|String
name|thePrefix
init|=
name|headers
operator|instanceof
name|JwsHeaders
condition|?
literal|"JWS"
else|:
name|headers
operator|instanceof
name|JweHeaders
condition|?
literal|"JWE"
else|:
literal|"JOSE"
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|thePrefix
operator|+
literal|" Headers: \r\n"
operator|+
name|writer
operator|.
name|toJson
argument_list|(
name|headers
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|//
comment|//<Start> Copied from JAX-RS RT FRONTEND ResourceUtils
comment|//
specifier|public
specifier|static
name|InputStream
name|getResourceStream
parameter_list|(
name|String
name|loc
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getResourceURL
argument_list|(
name|loc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
return|return
name|url
operator|==
literal|null
condition|?
literal|null
else|:
name|url
operator|.
name|openStream
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|URL
name|getResourceURL
parameter_list|(
name|String
name|loc
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|loc
operator|.
name|startsWith
argument_list|(
name|CLASSPATH_PREFIX
argument_list|)
condition|)
block|{
name|String
name|path
init|=
name|loc
operator|.
name|substring
argument_list|(
name|CLASSPATH_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|url
operator|=
name|JoseUtils
operator|.
name|getClasspathResourceURL
argument_list|(
name|path
argument_list|,
name|JoseUtils
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|loc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// it can be either a classpath or file resource without a scheme
name|url
operator|=
name|JoseUtils
operator|.
name|getClasspathResourceURL
argument_list|(
name|loc
argument_list|,
name|JoseUtils
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|loc
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|url
operator|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No resource "
operator|+
name|loc
operator|+
literal|" is available"
argument_list|)
expr_stmt|;
block|}
return|return
name|url
return|;
block|}
specifier|public
specifier|static
name|URL
name|getClasspathResourceURL
parameter_list|(
name|String
name|path
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|URL
name|url
init|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|path
argument_list|,
name|callingClass
argument_list|)
decl_stmt|;
return|return
name|url
operator|==
literal|null
condition|?
name|getResource
argument_list|(
name|path
argument_list|,
name|URL
operator|.
name|class
argument_list|,
name|bus
argument_list|)
else|:
name|url
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getResource
parameter_list|(
name|String
name|path
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resourceClass
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|ResourceManager
name|rm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rm
operator|!=
literal|null
condition|)
block|{
return|return
name|rm
operator|.
name|resolveResource
argument_list|(
name|path
argument_list|,
name|resourceClass
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Properties
name|loadProperties
parameter_list|(
name|String
name|propertiesLocation
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|getResourceStream
argument_list|(
name|propertiesLocation
argument_list|,
name|bus
argument_list|)
init|)
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
return|return
name|props
return|;
block|}
comment|//
comment|//<End> Copied from JAX-RS RT FRONTEND ResourceUtils
comment|//
block|}
end_class

end_unit

