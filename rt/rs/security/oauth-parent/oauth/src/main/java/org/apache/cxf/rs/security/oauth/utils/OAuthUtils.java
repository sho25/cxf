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
name|oauth
operator|.
name|utils
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
name|ArrayList
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
name|Map
operator|.
name|Entry
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
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|core
operator|.
name|MediaType
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
name|core
operator|.
name|MultivaluedMap
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
name|core
operator|.
name|Response
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
name|core
operator|.
name|Response
operator|.
name|ResponseBuilder
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuth
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthAccessor
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthConsumer
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthMessage
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthProblemException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthValidator
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|server
operator|.
name|OAuthServlet
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
name|util
operator|.
name|PropertyUtils
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
name|ext
operator|.
name|MessageContext
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
name|impl
operator|.
name|MetadataMap
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
name|model
operator|.
name|URITemplate
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
name|utils
operator|.
name|FormUtils
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
name|rs
operator|.
name|security
operator|.
name|oauth
operator|.
name|data
operator|.
name|Client
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
name|oauth
operator|.
name|data
operator|.
name|RequestToken
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
name|oauth
operator|.
name|data
operator|.
name|Token
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
name|oauth
operator|.
name|provider
operator|.
name|DefaultOAuthValidator
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
name|oauth
operator|.
name|provider
operator|.
name|OAuthDataProvider
import|;
end_import

begin_comment
comment|/**  * Various utility methods  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OAuthUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|REPORT_FAILURE_DETAILS
init|=
literal|"report.failure.details"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPORT_FAILURE_DETAILS_AS_HEADER
init|=
literal|"report.failure.details.as.header"
decl_stmt|;
specifier|private
name|OAuthUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|boolean
name|checkRequestURI
parameter_list|(
name|String
name|servletPath
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|boolean
name|wildcard
init|=
name|uri
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
decl_stmt|;
name|String
name|theURI
init|=
name|wildcard
condition|?
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uri
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
else|:
name|uri
decl_stmt|;
try|try
block|{
name|URITemplate
name|template
init|=
operator|new
name|URITemplate
argument_list|(
name|theURI
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|template
operator|.
name|match
argument_list|(
name|servletPath
argument_list|,
name|map
argument_list|)
condition|)
block|{
name|String
name|finalGroup
init|=
name|map
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
if|if
condition|(
name|wildcard
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|finalGroup
argument_list|)
operator|||
literal|"/"
operator|.
name|equals
argument_list|(
name|finalGroup
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|void
name|validateMessage
parameter_list|(
name|OAuthMessage
name|oAuthMessage
parameter_list|,
name|Client
name|client
parameter_list|,
name|Token
name|token
parameter_list|,
name|OAuthDataProvider
name|provider
parameter_list|,
name|OAuthValidator
name|validator
parameter_list|)
throws|throws
name|Exception
block|{
name|OAuthConsumer
name|consumer
init|=
operator|new
name|OAuthConsumer
argument_list|(
literal|null
argument_list|,
name|client
operator|.
name|getConsumerKey
argument_list|()
argument_list|,
name|client
operator|.
name|getSecretKey
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|OAuthAccessor
name|accessor
init|=
operator|new
name|OAuthAccessor
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|RequestToken
condition|)
block|{
name|accessor
operator|.
name|requestToken
operator|=
name|token
operator|.
name|getTokenKey
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|accessor
operator|.
name|accessToken
operator|=
name|token
operator|.
name|getTokenKey
argument_list|()
expr_stmt|;
block|}
name|accessor
operator|.
name|tokenSecret
operator|=
name|token
operator|.
name|getTokenSecret
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|validator
operator|.
name|validateMessage
argument_list|(
name|oAuthMessage
argument_list|,
name|accessor
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
name|provider
operator|.
name|removeToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
throw|throw
name|ex
throw|;
block|}
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
name|validator
operator|instanceof
name|DefaultOAuthValidator
condition|)
block|{
operator|(
operator|(
name|DefaultOAuthValidator
operator|)
name|validator
operator|)
operator|.
name|validateToken
argument_list|(
name|token
argument_list|,
name|provider
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|OAuthMessage
name|getOAuthMessage
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|String
index|[]
name|requiredParams
parameter_list|)
throws|throws
name|Exception
block|{
name|OAuthMessage
name|oAuthMessage
init|=
name|OAuthServlet
operator|.
name|getMessage
argument_list|(
name|request
argument_list|,
name|request
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|OAuthUtils
operator|.
name|addParametersIfNeeded
argument_list|(
name|mc
argument_list|,
name|request
argument_list|,
name|oAuthMessage
argument_list|)
expr_stmt|;
name|oAuthMessage
operator|.
name|requireParameters
argument_list|(
name|requiredParams
argument_list|)
expr_stmt|;
return|return
name|oAuthMessage
return|;
block|}
specifier|public
specifier|static
name|void
name|addParametersIfNeeded
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|OAuthMessage
name|oAuthMessage
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|params
init|=
name|oAuthMessage
operator|.
name|getParameters
argument_list|()
decl_stmt|;
name|String
name|enc
init|=
name|oAuthMessage
operator|.
name|getBodyEncoding
argument_list|()
decl_stmt|;
name|enc
operator|=
name|enc
operator|==
literal|null
condition|?
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
else|:
name|enc
expr_stmt|;
if|if
condition|(
name|params
operator|.
name|isEmpty
argument_list|()
operator|&&
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED_TYPE
operator|.
name|isCompatible
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
name|oAuthMessage
operator|.
name|getBodyType
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|InputStream
name|stream
init|=
name|mc
operator|!=
literal|null
condition|?
name|mc
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
else|:
name|oAuthMessage
operator|.
name|getBodyAsStream
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|FormUtils
operator|.
name|readBody
argument_list|(
name|stream
argument_list|,
name|enc
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|FormUtils
operator|.
name|populateMapFromString
argument_list|(
name|map
argument_list|,
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
name|body
argument_list|,
name|enc
argument_list|,
literal|true
argument_list|,
name|request
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|oAuthMessage
operator|.
name|addParameter
argument_list|(
name|key
argument_list|,
name|map
operator|.
name|getFirst
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// This path will most likely work only for the AuthorizationRequestService
comment|// when processing a user confirmation with only 3 parameters expected
name|String
name|ct
init|=
name|request
operator|.
name|getContentType
argument_list|()
decl_stmt|;
if|if
condition|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
operator|.
name|equals
argument_list|(
name|ct
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
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
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|param
range|:
name|params
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|param
operator|.
name|getKey
argument_list|()
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|param
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|FormUtils
operator|.
name|logRequestParametersIfNeeded
argument_list|(
name|map
argument_list|,
name|enc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|Response
name|handleException
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|Exception
name|e
parameter_list|,
name|int
name|status
parameter_list|)
block|{
name|ResponseBuilder
name|builder
init|=
name|Response
operator|.
name|status
argument_list|(
name|status
argument_list|)
decl_stmt|;
if|if
condition|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|mc
operator|.
name|getContextualProperty
argument_list|(
name|REPORT_FAILURE_DETAILS
argument_list|)
argument_list|)
condition|)
block|{
name|boolean
name|asHeader
init|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|mc
operator|.
name|getContextualProperty
argument_list|(
name|REPORT_FAILURE_DETAILS_AS_HEADER
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|text
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|OAuthProblemException
condition|)
block|{
name|OAuthProblemException
name|problem
init|=
operator|(
name|OAuthProblemException
operator|)
name|e
decl_stmt|;
if|if
condition|(
name|asHeader
operator|&&
name|problem
operator|.
name|getProblem
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|text
operator|=
name|problem
operator|.
name|getProblem
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
name|text
operator|=
name|e
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|asHeader
condition|)
block|{
name|builder
operator|.
name|header
argument_list|(
literal|"oauth_problem"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|entity
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|parseParamValue
parameter_list|(
name|String
name|paramValue
parameter_list|,
name|String
name|defaultValue
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|scopeList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|paramValue
argument_list|)
condition|)
block|{
name|StringTokenizer
name|tokenizer
init|=
operator|new
name|StringTokenizer
argument_list|(
name|paramValue
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
while|while
condition|(
name|tokenizer
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|token
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|scopeList
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|defaultValue
operator|!=
literal|null
operator|&&
operator|!
name|scopeList
operator|.
name|contains
argument_list|(
name|defaultValue
argument_list|)
condition|)
block|{
name|scopeList
operator|.
name|add
argument_list|(
name|defaultValue
argument_list|)
expr_stmt|;
block|}
return|return
name|scopeList
return|;
block|}
specifier|public
specifier|static
name|RequestToken
name|handleTokenRejectedException
parameter_list|()
throws|throws
name|OAuthProblemException
block|{
name|OAuthProblemException
name|problemEx
init|=
operator|new
name|OAuthProblemException
argument_list|(
name|OAuth
operator|.
name|Problems
operator|.
name|TOKEN_REJECTED
argument_list|)
decl_stmt|;
name|problemEx
operator|.
name|setParameter
argument_list|(
name|OAuthProblemException
operator|.
name|HTTP_STATUS_CODE
argument_list|,
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
throw|throw
name|problemEx
throw|;
block|}
specifier|public
specifier|static
name|Object
name|instantiateClass
parameter_list|(
name|String
name|className
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|className
argument_list|,
name|OAuthUtils
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|clazz
operator|.
name|newInstance
argument_list|()
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|OAuthDataProvider
name|getOAuthDataProvider
parameter_list|(
name|OAuthDataProvider
name|provider
parameter_list|,
name|ServletContext
name|servletContext
parameter_list|)
block|{
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
return|return
name|provider
return|;
block|}
return|return
name|getOAuthDataProvider
argument_list|(
name|servletContext
argument_list|)
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|OAuthDataProvider
name|getOAuthDataProvider
parameter_list|(
name|ServletContext
name|servletContext
parameter_list|)
block|{
name|OAuthDataProvider
name|dataProvider
init|=
operator|(
name|OAuthDataProvider
operator|)
name|servletContext
operator|.
name|getAttribute
argument_list|(
name|OAuthConstants
operator|.
name|OAUTH_DATA_PROVIDER_INSTANCE_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataProvider
operator|==
literal|null
condition|)
block|{
name|String
name|dataProviderClassName
init|=
name|servletContext
operator|.
name|getInitParameter
argument_list|(
name|OAuthConstants
operator|.
name|OAUTH_DATA_PROVIDER_CLASS
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|dataProviderClassName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"There should be provided [ "
operator|+
name|OAuthConstants
operator|.
name|OAUTH_DATA_PROVIDER_CLASS
operator|+
literal|" ] context init param in web.xml"
argument_list|)
throw|;
block|}
try|try
block|{
name|dataProvider
operator|=
operator|(
name|OAuthDataProvider
operator|)
name|OAuthUtils
operator|.
name|instantiateClass
argument_list|(
name|dataProviderClassName
argument_list|)
expr_stmt|;
name|servletContext
operator|.
name|setAttribute
argument_list|(
name|OAuthConstants
operator|.
name|OAUTH_DATA_PROVIDER_INSTANCE_KEY
argument_list|,
name|dataProvider
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
literal|"Cannot instantiate OAuth Data Provider class: "
operator|+
name|dataProviderClassName
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|dataProvider
return|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|OAuthValidator
name|getOAuthValidator
parameter_list|(
name|ServletContext
name|servletContext
parameter_list|)
block|{
name|OAuthValidator
name|dataProvider
init|=
operator|(
name|OAuthValidator
operator|)
name|servletContext
operator|.
name|getAttribute
argument_list|(
name|OAuthConstants
operator|.
name|OAUTH_VALIDATOR_INSTANCE_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataProvider
operator|==
literal|null
condition|)
block|{
name|String
name|dataProviderClassName
init|=
name|servletContext
operator|.
name|getInitParameter
argument_list|(
name|OAuthConstants
operator|.
name|OAUTH_VALIDATOR_CLASS
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|dataProviderClassName
argument_list|)
condition|)
block|{
try|try
block|{
name|dataProvider
operator|=
operator|(
name|OAuthValidator
operator|)
name|OAuthUtils
operator|.
name|instantiateClass
argument_list|(
name|dataProviderClassName
argument_list|)
expr_stmt|;
name|servletContext
operator|.
name|setAttribute
argument_list|(
name|OAuthConstants
operator|.
name|OAUTH_VALIDATOR_INSTANCE_KEY
argument_list|,
name|dataProvider
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
literal|"Cannot instantiate OAuthValidator class: "
operator|+
name|dataProviderClassName
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|dataProvider
operator|==
literal|null
condition|?
operator|new
name|DefaultOAuthValidator
argument_list|()
else|:
name|dataProvider
return|;
block|}
block|}
end_class

end_unit

