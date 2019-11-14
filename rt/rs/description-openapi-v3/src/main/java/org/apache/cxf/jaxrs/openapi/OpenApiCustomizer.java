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
name|jaxrs
operator|.
name|openapi
package|;
end_package

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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Optional
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
name|ApplicationPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
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
name|commons
operator|.
name|lang3
operator|.
name|tuple
operator|.
name|Pair
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
name|model
operator|.
name|ApplicationInfo
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
name|ClassResourceInfo
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
name|OperationResourceInfo
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
name|doc
operator|.
name|DocumentationProvider
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
name|doc
operator|.
name|JavaDocProvider
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
name|JAXRSUtils
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
name|ResourceUtils
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|jaxrs2
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|integration
operator|.
name|api
operator|.
name|OpenAPIConfiguration
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|models
operator|.
name|OpenAPI
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|models
operator|.
name|Operation
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|models
operator|.
name|parameters
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|models
operator|.
name|responses
operator|.
name|ApiResponse
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|models
operator|.
name|servers
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|models
operator|.
name|tags
operator|.
name|Tag
import|;
end_import

begin_class
specifier|public
class|class
name|OpenApiCustomizer
block|{
specifier|protected
name|boolean
name|dynamicBasePath
decl_stmt|;
specifier|protected
name|boolean
name|replaceTags
decl_stmt|;
specifier|protected
name|DocumentationProvider
name|javadocProvider
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|cris
decl_stmt|;
specifier|protected
name|String
name|applicationPath
decl_stmt|;
specifier|public
name|OpenAPIConfiguration
name|customize
parameter_list|(
specifier|final
name|OpenAPIConfiguration
name|configuration
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
return|return
name|configuration
return|;
block|}
if|if
condition|(
name|dynamicBasePath
condition|)
block|{
specifier|final
name|MessageContext
name|ctx
init|=
name|createMessageContext
argument_list|()
decl_stmt|;
comment|// If the JAX-RS application with custom path is defined, it might be present twice, in the
comment|// request URI as well as in each resource operation URI. To properly represent server URL,
comment|// the application path should be removed from it.
specifier|final
name|String
name|url
init|=
name|StringUtils
operator|.
name|removeEnd
argument_list|(
name|StringUtils
operator|.
name|substringBeforeLast
argument_list|(
name|ctx
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"/"
argument_list|)
argument_list|,
name|applicationPath
argument_list|)
decl_stmt|;
specifier|final
name|Collection
argument_list|<
name|Server
argument_list|>
name|servers
init|=
name|configuration
operator|.
name|getOpenAPI
argument_list|()
operator|.
name|getServers
argument_list|()
decl_stmt|;
if|if
condition|(
name|servers
operator|==
literal|null
operator|||
name|servers
operator|.
name|stream
argument_list|()
operator|.
name|noneMatch
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getUrl
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|url
argument_list|)
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|getOpenAPI
argument_list|()
operator|.
name|setServers
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|Server
argument_list|()
operator|.
name|url
argument_list|(
name|url
argument_list|)
argument_list|)
argument_list|)
block|;             }
block|}
return|return
name|configuration
return|;
block|}
specifier|public
name|void
name|customize
parameter_list|(
specifier|final
name|OpenAPI
name|oas
parameter_list|)
block|{
if|if
condition|(
name|replaceTags
operator|||
name|javadocProvider
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ClassResourceInfo
argument_list|>
name|operations
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Pair
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|,
name|OperationResourceInfo
argument_list|>
name|methods
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|cris
operator|.
name|forEach
argument_list|(
name|cri
lambda|->
block|{
name|cri
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|getOperationResourceInfos
argument_list|()
operator|.
name|forEach
argument_list|(
name|ori
lambda|->
block|{
name|String
name|normalizedPath
init|=
name|getNormalizedPath
argument_list|(
name|cri
operator|.
name|getURITemplate
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|ori
operator|.
name|getURITemplate
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|operations
operator|.
name|put
argument_list|(
name|normalizedPath
argument_list|,
name|cri
argument_list|)
expr_stmt|;
name|methods
operator|.
name|put
argument_list|(
name|Pair
operator|.
name|of
argument_list|(
name|ori
operator|.
name|getHttpMethod
argument_list|()
argument_list|,
name|normalizedPath
argument_list|)
argument_list|,
name|ori
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Tag
argument_list|>
name|tags
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|oas
operator|.
name|getPaths
argument_list|()
operator|.
name|forEach
argument_list|(
parameter_list|(
name|pathKey
parameter_list|,
name|pathItem
parameter_list|)
lambda|->
block|{
name|Optional
argument_list|<
name|Tag
argument_list|>
name|tag
decl_stmt|;
if|if
condition|(
name|replaceTags
operator|&&
name|operations
operator|.
name|containsKey
argument_list|(
name|pathKey
argument_list|)
condition|)
block|{
name|ClassResourceInfo
name|cri
init|=
name|operations
operator|.
name|get
argument_list|(
name|pathKey
argument_list|)
decl_stmt|;
name|tag
operator|=
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|Tag
argument_list|()
argument_list|)
expr_stmt|;
name|tag
operator|.
name|get
argument_list|()
operator|.
name|setName
argument_list|(
name|cri
operator|.
name|getURITemplate
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"/"
argument_list|,
literal|"_"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|javadocProvider
operator|!=
literal|null
condition|)
block|{
name|tag
operator|.
name|get
argument_list|()
operator|.
name|setDescription
argument_list|(
name|javadocProvider
operator|.
name|getClassDoc
argument_list|(
name|cri
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|tags
operator|.
name|contains
argument_list|(
name|tag
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
name|tags
operator|.
name|add
argument_list|(
name|tag
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|tag
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
block|}
name|pathItem
operator|.
name|readOperationsMap
argument_list|()
operator|.
name|forEach
argument_list|(
parameter_list|(
name|method
parameter_list|,
name|operation
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|replaceTags
operator|&&
name|tag
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|operation
operator|.
name|setTags
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|tag
operator|.
name|get
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Pair
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|key
init|=
name|Pair
operator|.
name|of
argument_list|(
name|method
operator|.
name|name
argument_list|()
argument_list|,
name|pathKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|methods
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
name|javadocProvider
operator|!=
literal|null
condition|)
block|{
name|OperationResourceInfo
name|ori
init|=
name|methods
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|operation
operator|.
name|getSummary
argument_list|()
argument_list|)
condition|)
block|{
name|operation
operator|.
name|setSummary
argument_list|(
name|javadocProvider
operator|.
name|getMethodDoc
argument_list|(
name|ori
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|operation
operator|.
name|getParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|Parameter
argument_list|>
name|parameters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|addParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|operation
operator|.
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operation
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
operator|==
name|ori
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|operation
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|operation
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|)
condition|)
block|{
name|operation
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|setDescription
argument_list|(
name|javadocProvider
operator|.
name|getMethodParameterDoc
argument_list|(
name|ori
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|addParameters
argument_list|(
name|operation
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|customizeResponses
argument_list|(
name|operation
argument_list|,
name|ori
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|replaceTags
operator|&&
name|oas
operator|.
name|getTags
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|oas
operator|.
name|setTags
argument_list|(
name|tags
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|String
name|getNormalizedPath
parameter_list|(
name|String
name|classResourcePath
parameter_list|,
name|String
name|operationResourcePath
parameter_list|)
block|{
name|StringBuilder
name|normalizedPath
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
index|[]
name|segments
init|=
operator|(
name|classResourcePath
operator|+
name|operationResourcePath
operator|)
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|segment
range|:
name|segments
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|segment
argument_list|)
condition|)
block|{
name|normalizedPath
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|segment
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Adapt to Swagger's path expression
if|if
condition|(
name|normalizedPath
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|":.*}"
argument_list|)
condition|)
block|{
name|normalizedPath
operator|.
name|setLength
argument_list|(
name|normalizedPath
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
name|normalizedPath
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
return|return
name|StringUtils
operator|.
name|EMPTY
operator|.
name|equals
argument_list|(
name|normalizedPath
operator|.
name|toString
argument_list|()
argument_list|)
condition|?
literal|"/"
else|:
name|normalizedPath
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Allows to add parameters to the list, related to an {@link Operation} instance; the method is invoked      * for all instances available.      *      * @param parameters list of parameters defined for an {@link Operation}      * @see io.swagger.v3.oas.models.parameters.HeaderParameter      * @see io.swagger.v3.oas.models.parameters.CookieParameter      * @see io.swagger.v3.oas.models.parameters.PathParameter      * @see io.swagger.v3.oas.models.parameters.QueryParameter      */
specifier|protected
name|void
name|addParameters
parameter_list|(
specifier|final
name|List
argument_list|<
name|Parameter
argument_list|>
name|parameters
parameter_list|)
block|{
comment|// does nothing by default
block|}
comment|/**      * Allows to customize the responses of the given {@link Operation} instance; the method is invoked      * for all instances available.      *      * @param operation operation instance      * @param ori CXF data about the given operation instance      */
specifier|protected
name|void
name|customizeResponses
parameter_list|(
specifier|final
name|Operation
name|operation
parameter_list|,
specifier|final
name|OperationResourceInfo
name|ori
parameter_list|)
block|{
if|if
condition|(
name|operation
operator|.
name|getResponses
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|operation
operator|.
name|getResponses
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ApiResponse
name|response
init|=
name|operation
operator|.
name|getResponses
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|response
operator|.
name|getDescription
argument_list|()
argument_list|)
operator|||
operator|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|javadocProvider
operator|.
name|getMethodResponseDoc
argument_list|(
name|ori
argument_list|)
argument_list|)
operator|&&
name|Reader
operator|.
name|DEFAULT_DESCRIPTION
operator|.
name|equals
argument_list|(
name|response
operator|.
name|getDescription
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|response
operator|.
name|setDescription
argument_list|(
name|javadocProvider
operator|.
name|getMethodResponseDoc
argument_list|(
name|ori
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setDynamicBasePath
parameter_list|(
specifier|final
name|boolean
name|dynamicBasePath
parameter_list|)
block|{
name|this
operator|.
name|dynamicBasePath
operator|=
name|dynamicBasePath
expr_stmt|;
block|}
specifier|public
name|void
name|setReplaceTags
parameter_list|(
specifier|final
name|boolean
name|replaceTags
parameter_list|)
block|{
name|this
operator|.
name|replaceTags
operator|=
name|replaceTags
expr_stmt|;
block|}
specifier|public
name|void
name|setJavadocProvider
parameter_list|(
specifier|final
name|DocumentationProvider
name|javadocProvider
parameter_list|)
block|{
name|this
operator|.
name|javadocProvider
operator|=
name|javadocProvider
expr_stmt|;
block|}
specifier|public
name|void
name|setClassResourceInfos
parameter_list|(
specifier|final
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|classResourceInfos
parameter_list|)
block|{
name|this
operator|.
name|cris
operator|=
name|classResourceInfos
expr_stmt|;
block|}
specifier|public
name|void
name|setJavaDocPath
parameter_list|(
specifier|final
name|String
name|javaDocPath
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|javadocProvider
operator|=
operator|new
name|JavaDocProvider
argument_list|(
name|javaDocPath
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setJavaDocPaths
parameter_list|(
specifier|final
name|String
modifier|...
name|javaDocPaths
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|javadocProvider
operator|=
operator|new
name|JavaDocProvider
argument_list|(
name|javaDocPaths
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setJavaDocURLs
parameter_list|(
specifier|final
name|URL
index|[]
name|javaDocURLs
parameter_list|)
block|{
name|this
operator|.
name|javadocProvider
operator|=
operator|new
name|JavaDocProvider
argument_list|(
name|javaDocURLs
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setApplicationInfo
parameter_list|(
name|ApplicationInfo
name|application
parameter_list|)
block|{
if|if
condition|(
name|application
operator|!=
literal|null
operator|&&
name|application
operator|.
name|getProvider
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|application
operator|.
name|getProvider
argument_list|()
operator|.
name|getClass
argument_list|()
decl_stmt|;
specifier|final
name|ApplicationPath
name|path
init|=
name|ResourceUtils
operator|.
name|locateApplicationPath
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|applicationPath
operator|=
name|path
operator|.
name|value
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|applicationPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|applicationPath
operator|=
literal|"/"
operator|+
name|applicationPath
expr_stmt|;
block|}
if|if
condition|(
name|applicationPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|applicationPath
operator|=
name|applicationPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|applicationPath
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|MessageContext
name|createMessageContext
parameter_list|()
block|{
return|return
name|JAXRSUtils
operator|.
name|createContextValue
argument_list|(
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
literal|null
argument_list|,
name|MessageContext
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

