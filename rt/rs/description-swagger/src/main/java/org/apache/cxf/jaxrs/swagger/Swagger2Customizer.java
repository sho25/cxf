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
name|swagger
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|ImmutablePair
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
name|io
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|config
operator|.
name|BeanConfig
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
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
name|models
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Swagger
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Tag
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|parameters
operator|.
name|Parameter
import|;
end_import

begin_class
specifier|public
class|class
name|Swagger2Customizer
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
name|boolean
name|applyDefaultVersion
init|=
literal|true
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
name|BeanConfig
name|beanConfig
decl_stmt|;
specifier|public
name|Swagger
name|customize
parameter_list|(
name|Swagger
name|data
parameter_list|)
block|{
if|if
condition|(
name|dynamicBasePath
condition|)
block|{
name|MessageContext
name|ctx
init|=
name|createMessageContext
argument_list|()
decl_stmt|;
name|String
name|currentBasePath
init|=
name|StringUtils
operator|.
name|substringBeforeLast
argument_list|(
name|ctx
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|getRequestURI
argument_list|()
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|data
operator|.
name|setBasePath
argument_list|(
name|currentBasePath
argument_list|)
expr_stmt|;
if|if
condition|(
name|data
operator|.
name|getHost
argument_list|()
operator|==
literal|null
condition|)
block|{
name|data
operator|.
name|setHost
argument_list|(
name|beanConfig
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|data
operator|.
name|getInfo
argument_list|()
operator|==
literal|null
condition|)
block|{
name|data
operator|.
name|setInfo
argument_list|(
name|beanConfig
operator|.
name|getInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|beanConfig
operator|.
name|getSwagger
argument_list|()
operator|!=
literal|null
operator|&&
name|beanConfig
operator|.
name|getSwagger
argument_list|()
operator|.
name|getSecurityDefinitions
argument_list|()
operator|!=
literal|null
operator|&&
name|data
operator|.
name|getSecurityDefinitions
argument_list|()
operator|==
literal|null
condition|)
block|{
name|data
operator|.
name|setSecurityDefinitions
argument_list|(
name|beanConfig
operator|.
name|getSwagger
argument_list|()
operator|.
name|getSecurityDefinitions
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|cris
control|)
block|{
for|for
control|(
name|OperationResourceInfo
name|ori
range|:
name|cri
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|getOperationResourceInfos
argument_list|()
control|)
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
name|ImmutablePair
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
block|}
if|if
condition|(
name|replaceTags
operator|&&
name|data
operator|.
name|getTags
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|getTags
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Path
argument_list|>
name|entry
range|:
name|data
operator|.
name|getPaths
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Tag
name|tag
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|replaceTags
operator|&&
name|operations
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
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
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|tag
operator|=
operator|new
name|Tag
argument_list|()
expr_stmt|;
name|tag
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
name|data
operator|.
name|addTag
argument_list|(
name|tag
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|HttpMethod
argument_list|,
name|Operation
argument_list|>
name|subentry
range|:
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getOperationMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|replaceTags
operator|&&
name|tag
operator|!=
literal|null
condition|)
block|{
name|subentry
operator|.
name|getValue
argument_list|()
operator|.
name|setTags
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|tag
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
name|ImmutablePair
operator|.
name|of
argument_list|(
name|subentry
operator|.
name|getKey
argument_list|()
operator|.
name|name
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
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
name|subentry
operator|.
name|getValue
argument_list|()
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|subentry
operator|.
name|getValue
argument_list|()
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
name|subentry
operator|.
name|getValue
argument_list|()
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
name|addParameters
argument_list|(
name|subentry
operator|.
name|getValue
argument_list|()
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|subentry
operator|.
name|getValue
argument_list|()
operator|.
name|getResponses
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|subentry
operator|.
name|getValue
argument_list|()
operator|.
name|getResponses
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|subentry
operator|.
name|getValue
argument_list|()
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
block|}
block|}
if|if
condition|(
name|replaceTags
operator|&&
name|data
operator|.
name|getTags
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|data
operator|.
name|getTags
argument_list|()
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Tag
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|Tag
name|tag1
parameter_list|,
specifier|final
name|Tag
name|tag2
parameter_list|)
block|{
return|return
name|tag1
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|tag2
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|applyDefaultVersion
argument_list|(
name|data
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
specifier|private
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
specifier|protected
name|void
name|applyDefaultVersion
parameter_list|(
name|Swagger
name|data
parameter_list|)
block|{
if|if
condition|(
name|applyDefaultVersion
operator|&&
name|data
operator|.
name|getInfo
argument_list|()
operator|!=
literal|null
operator|&&
name|data
operator|.
name|getInfo
argument_list|()
operator|.
name|getVersion
argument_list|()
operator|==
literal|null
operator|&&
name|beanConfig
operator|!=
literal|null
operator|&&
name|beanConfig
operator|.
name|getResourcePackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Package
name|resourcePackage
init|=
name|Package
operator|.
name|getPackage
argument_list|(
name|beanConfig
operator|.
name|getResourcePackage
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourcePackage
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|getInfo
argument_list|()
operator|.
name|setVersion
argument_list|(
name|resourcePackage
operator|.
name|getImplementationVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Allows to add parameters to the list, related to an {@link Operation} instance; the method is invoked      * for all instances available.      *      * @param parameters list of parameters defined for an {@link Operation}      * @see io.swagger.models.parameters.HeaderParameter      * @see io.swagger.models.parameters.CookieParameter      * @see io.swagger.models.parameters.PathParameter      * @see io.swagger.models.parameters.BodyParameter      * @see io.swagger.models.parameters.QueryParameter      * @see io.swagger.models.parameters.RefParameter      */
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
name|setBeanConfig
parameter_list|(
name|BeanConfig
name|beanConfig
parameter_list|)
block|{
name|this
operator|.
name|beanConfig
operator|=
name|beanConfig
expr_stmt|;
block|}
specifier|public
name|void
name|setApplyDefaultVersion
parameter_list|(
name|boolean
name|applyDefaultVersion
parameter_list|)
block|{
name|this
operator|.
name|applyDefaultVersion
operator|=
name|applyDefaultVersion
expr_stmt|;
block|}
block|}
end_class

end_unit

