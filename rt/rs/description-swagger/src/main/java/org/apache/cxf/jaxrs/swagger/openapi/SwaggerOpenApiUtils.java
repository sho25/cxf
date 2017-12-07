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
operator|.
name|openapi
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|helpers
operator|.
name|CastUtils
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
name|helpers
operator|.
name|IOUtils
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
name|JsonMapObject
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SwaggerOpenApiUtils
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
name|SwaggerOpenApiUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|SIMPLE_TYPE_RELATED_PROPS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"format"
argument_list|,
literal|"minimum"
argument_list|,
literal|"maximum"
argument_list|,
literal|"default"
argument_list|)
decl_stmt|;
specifier|private
name|SwaggerOpenApiUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|String
name|getOpenApiFromSwaggerLoc
parameter_list|(
name|String
name|loc
parameter_list|)
block|{
return|return
name|getOpenApiFromSwaggerLoc
argument_list|(
name|loc
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getOpenApiFromSwaggerLoc
parameter_list|(
name|String
name|loc
parameter_list|,
name|OpenApiConfiguration
name|cfg
parameter_list|)
block|{
return|return
name|getOpenApiFromSwaggerLoc
argument_list|(
name|loc
argument_list|,
name|cfg
argument_list|,
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getOpenApiFromSwaggerLoc
parameter_list|(
name|String
name|loc
parameter_list|,
name|OpenApiConfiguration
name|cfg
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
try|try
block|{
name|InputStream
name|is
init|=
name|ResourceUtils
operator|.
name|getResourceStream
argument_list|(
name|loc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getOpenApiFromSwaggerStream
argument_list|(
name|is
argument_list|,
name|cfg
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Problem with processing a user model at "
operator|+
name|loc
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|getOpenApiFromSwaggerStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getOpenApiFromSwaggerStream
argument_list|(
name|is
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getOpenApiFromSwaggerStream
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|OpenApiConfiguration
name|cfg
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getOpenApiFromSwaggerJson
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
argument_list|,
name|cfg
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getOpenApiFromSwaggerJson
parameter_list|(
name|String
name|json
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getOpenApiFromSwaggerJson
argument_list|(
name|json
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getOpenApiFromSwaggerJson
parameter_list|(
name|String
name|json
parameter_list|,
name|OpenApiConfiguration
name|cfg
parameter_list|)
throws|throws
name|IOException
block|{
name|JsonMapObjectReaderWriter
name|readerWriter
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|JsonMapObject
name|sw2
init|=
name|readerWriter
operator|.
name|fromJsonToJsonObject
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|JsonMapObject
name|sw3
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
comment|// "openapi"
name|sw3
operator|.
name|setProperty
argument_list|(
literal|"openapi"
argument_list|,
literal|"3.0.0"
argument_list|)
expr_stmt|;
comment|// "servers"
name|setServersProperty
argument_list|(
name|sw2
argument_list|,
name|sw3
argument_list|)
expr_stmt|;
comment|// "info"
name|JsonMapObject
name|infoObject
init|=
name|sw2
operator|.
name|getJsonMapProperty
argument_list|(
literal|"info"
argument_list|)
decl_stmt|;
if|if
condition|(
name|infoObject
operator|!=
literal|null
condition|)
block|{
name|sw3
operator|.
name|setProperty
argument_list|(
literal|"info"
argument_list|,
name|infoObject
argument_list|)
expr_stmt|;
block|}
comment|// "tags"
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|tagsObject
init|=
name|sw2
operator|.
name|getListMapProperty
argument_list|(
literal|"tags"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tagsObject
operator|!=
literal|null
condition|)
block|{
name|sw3
operator|.
name|setProperty
argument_list|(
literal|"tags"
argument_list|,
name|tagsObject
argument_list|)
expr_stmt|;
block|}
comment|// paths
name|setPathsProperty
argument_list|(
name|sw2
argument_list|,
name|sw3
argument_list|)
expr_stmt|;
comment|// components
name|setComponentsProperty
argument_list|(
name|sw2
argument_list|,
name|sw3
argument_list|)
expr_stmt|;
comment|// externalDocs
name|Object
name|externalDocsObject
init|=
name|sw2
operator|.
name|getProperty
argument_list|(
literal|"externalDocs"
argument_list|)
decl_stmt|;
if|if
condition|(
name|externalDocsObject
operator|!=
literal|null
condition|)
block|{
name|sw3
operator|.
name|setProperty
argument_list|(
literal|"externalDocs"
argument_list|,
name|externalDocsObject
argument_list|)
expr_stmt|;
block|}
return|return
name|readerWriter
operator|.
name|toJson
argument_list|(
name|sw3
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|setComponentsProperty
parameter_list|(
name|JsonMapObject
name|sw2
parameter_list|,
name|JsonMapObject
name|sw3
parameter_list|)
block|{
name|JsonMapObject
name|comps
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
name|comps
operator|.
name|setProperty
argument_list|(
literal|"requestBodies"
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|s2Defs
init|=
name|sw2
operator|.
name|getProperty
argument_list|(
literal|"definitions"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s2Defs
operator|!=
literal|null
condition|)
block|{
name|comps
operator|.
name|setProperty
argument_list|(
literal|"schemas"
argument_list|,
name|s2Defs
argument_list|)
expr_stmt|;
block|}
name|Object
name|s2SecurityDefs
init|=
name|sw2
operator|.
name|getProperty
argument_list|(
literal|"securityDefinitions"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s2SecurityDefs
operator|!=
literal|null
condition|)
block|{
name|comps
operator|.
name|setProperty
argument_list|(
literal|"securitySchemes"
argument_list|,
name|s2SecurityDefs
argument_list|)
expr_stmt|;
block|}
name|sw3
operator|.
name|setProperty
argument_list|(
literal|"components"
argument_list|,
name|comps
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|setPathsProperty
parameter_list|(
name|JsonMapObject
name|sw2
parameter_list|,
name|JsonMapObject
name|sw3
parameter_list|)
block|{
name|JsonMapObject
name|sw2Paths
init|=
name|sw2
operator|.
name|getJsonMapProperty
argument_list|(
literal|"paths"
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|sw2PathEntries
range|:
name|sw2Paths
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|JsonMapObject
name|sw2PathVerbs
init|=
operator|new
name|JsonMapObject
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|sw2PathEntries
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|sw2PathVerbEntries
range|:
name|sw2PathVerbs
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|JsonMapObject
name|sw2PathVerbProps
init|=
operator|new
name|JsonMapObject
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|sw2PathVerbEntries
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|prepareRequestBody
argument_list|(
name|sw2PathVerbProps
argument_list|)
expr_stmt|;
name|prepareResponses
argument_list|(
name|sw2PathVerbProps
argument_list|)
expr_stmt|;
block|}
block|}
name|sw3
operator|.
name|setProperty
argument_list|(
literal|"paths"
argument_list|,
name|sw2Paths
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|prepareResponses
parameter_list|(
name|JsonMapObject
name|sw2PathVerbProps
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|sw2PathVerbProduces
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|sw2PathVerbProps
operator|.
name|removeProperty
argument_list|(
literal|"produces"
argument_list|)
argument_list|)
decl_stmt|;
name|JsonMapObject
name|sw3PathVerbResps
init|=
literal|null
decl_stmt|;
name|JsonMapObject
name|sw2PathVerbResps
init|=
name|sw2PathVerbProps
operator|.
name|getJsonMapProperty
argument_list|(
literal|"responses"
argument_list|)
decl_stmt|;
if|if
condition|(
name|sw2PathVerbResps
operator|!=
literal|null
condition|)
block|{
name|sw3PathVerbResps
operator|=
operator|new
name|JsonMapObject
argument_list|()
expr_stmt|;
name|JsonMapObject
name|okResp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|sw2PathVerbResps
operator|.
name|containsProperty
argument_list|(
literal|"200"
argument_list|)
condition|)
block|{
name|okResp
operator|=
operator|new
name|JsonMapObject
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|sw2PathVerbResps
operator|.
name|removeProperty
argument_list|(
literal|"200"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|JsonMapObject
name|newOkResp
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
name|String
name|description
init|=
name|okResp
operator|.
name|getStringProperty
argument_list|(
literal|"description"
argument_list|)
decl_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|newOkResp
operator|.
name|setProperty
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
name|JsonMapObject
name|schema
init|=
name|okResp
operator|.
name|getJsonMapProperty
argument_list|(
literal|"schema"
argument_list|)
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|JsonMapObject
name|content
init|=
name|prepareContentFromSchema
argument_list|(
name|schema
argument_list|,
name|sw2PathVerbProduces
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
name|newOkResp
operator|.
name|setProperty
argument_list|(
literal|"content"
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
name|JsonMapObject
name|headers
init|=
name|okResp
operator|.
name|getJsonMapProperty
argument_list|(
literal|"headers"
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|newOkResp
operator|.
name|setProperty
argument_list|(
literal|"headers"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
name|sw3PathVerbResps
operator|.
name|setProperty
argument_list|(
literal|"200"
argument_list|,
name|newOkResp
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|sw2PathVerbResps
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sw3PathVerbResps
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sw2PathVerbProps
operator|.
name|setProperty
argument_list|(
literal|"responses"
argument_list|,
name|sw3PathVerbResps
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|prepareRequestBody
parameter_list|(
name|JsonMapObject
name|sw2PathVerbProps
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|sw2PathVerbConsumes
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|sw2PathVerbProps
operator|.
name|removeProperty
argument_list|(
literal|"consumes"
argument_list|)
argument_list|)
decl_stmt|;
name|JsonMapObject
name|sw3RequestBody
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|JsonMapObject
argument_list|>
name|sw3formBody
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|sw2PathVerbParamsList
init|=
name|sw2PathVerbProps
operator|.
name|getListMapProperty
argument_list|(
literal|"parameters"
argument_list|)
decl_stmt|;
if|if
condition|(
name|sw2PathVerbParamsList
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|it
init|=
name|sw2PathVerbParamsList
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|JsonMapObject
name|sw2PathVerbParamMap
init|=
operator|new
name|JsonMapObject
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"body"
operator|.
name|equals
argument_list|(
name|sw2PathVerbParamMap
operator|.
name|getStringProperty
argument_list|(
literal|"in"
argument_list|)
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
name|sw3RequestBody
operator|=
operator|new
name|JsonMapObject
argument_list|()
expr_stmt|;
name|String
name|description
init|=
name|sw2PathVerbParamMap
operator|.
name|getStringProperty
argument_list|(
literal|"description"
argument_list|)
decl_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|sw3RequestBody
operator|.
name|setProperty
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
name|Boolean
name|required
init|=
name|sw2PathVerbParamMap
operator|.
name|getBooleanProperty
argument_list|(
literal|"required"
argument_list|)
decl_stmt|;
if|if
condition|(
name|required
operator|!=
literal|null
condition|)
block|{
name|sw3RequestBody
operator|.
name|setProperty
argument_list|(
literal|"required"
argument_list|,
name|required
argument_list|)
expr_stmt|;
block|}
name|JsonMapObject
name|schema
init|=
name|sw2PathVerbParamMap
operator|.
name|getJsonMapProperty
argument_list|(
literal|"schema"
argument_list|)
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|JsonMapObject
name|content
init|=
name|prepareContentFromSchema
argument_list|(
name|schema
argument_list|,
name|sw2PathVerbConsumes
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
name|sw3RequestBody
operator|.
name|setProperty
argument_list|(
literal|"content"
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
literal|"formData"
operator|.
name|equals
argument_list|(
name|sw2PathVerbParamMap
operator|.
name|getStringProperty
argument_list|(
literal|"in"
argument_list|)
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
if|if
condition|(
name|sw3formBody
operator|==
literal|null
condition|)
block|{
name|sw3formBody
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
name|sw3RequestBody
operator|=
operator|new
name|JsonMapObject
argument_list|()
expr_stmt|;
block|}
name|sw2PathVerbParamMap
operator|.
name|removeProperty
argument_list|(
literal|"in"
argument_list|)
expr_stmt|;
name|sw2PathVerbParamMap
operator|.
name|removeProperty
argument_list|(
literal|"required"
argument_list|)
expr_stmt|;
name|sw3formBody
operator|.
name|add
argument_list|(
name|sw2PathVerbParamMap
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"array"
operator|.
name|equals
argument_list|(
name|sw2PathVerbParamMap
operator|.
name|getStringProperty
argument_list|(
literal|"type"
argument_list|)
argument_list|)
condition|)
block|{
name|sw2PathVerbParamMap
operator|.
name|removeProperty
argument_list|(
literal|"type"
argument_list|)
expr_stmt|;
name|sw2PathVerbParamMap
operator|.
name|removeProperty
argument_list|(
literal|"collectionFormat"
argument_list|)
expr_stmt|;
name|sw2PathVerbParamMap
operator|.
name|setProperty
argument_list|(
literal|"explode"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|JsonMapObject
name|items
init|=
name|sw2PathVerbParamMap
operator|.
name|getJsonMapProperty
argument_list|(
literal|"items"
argument_list|)
decl_stmt|;
name|sw2PathVerbParamMap
operator|.
name|removeProperty
argument_list|(
literal|"items"
argument_list|)
expr_stmt|;
name|JsonMapObject
name|schema
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
name|schema
operator|.
name|setProperty
argument_list|(
literal|"type"
argument_list|,
literal|"array"
argument_list|)
expr_stmt|;
name|schema
operator|.
name|setProperty
argument_list|(
literal|"items"
argument_list|,
name|items
argument_list|)
expr_stmt|;
name|sw2PathVerbParamMap
operator|.
name|setProperty
argument_list|(
literal|"schema"
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|type
init|=
operator|(
name|String
operator|)
name|sw2PathVerbParamMap
operator|.
name|removeProperty
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|JsonMapObject
name|schema
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
name|schema
operator|.
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|prop
range|:
name|SIMPLE_TYPE_RELATED_PROPS
control|)
block|{
name|Object
name|value
init|=
name|sw2PathVerbParamMap
operator|.
name|removeProperty
argument_list|(
name|prop
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|schema
operator|.
name|setProperty
argument_list|(
name|prop
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|"password"
operator|.
name|equals
argument_list|(
name|sw2PathVerbParamMap
operator|.
name|getProperty
argument_list|(
literal|"name"
argument_list|)
argument_list|)
condition|)
block|{
name|schema
operator|.
name|setProperty
argument_list|(
literal|"format"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
block|}
name|sw2PathVerbParamMap
operator|.
name|setProperty
argument_list|(
literal|"schema"
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|sw2PathVerbParamsList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sw2PathVerbProps
operator|.
name|removeProperty
argument_list|(
literal|"parameters"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sw3formBody
operator|!=
literal|null
condition|)
block|{
name|sw3RequestBody
operator|.
name|setProperty
argument_list|(
literal|"content"
argument_list|,
name|prepareFormContent
argument_list|(
name|sw3formBody
argument_list|,
name|sw2PathVerbConsumes
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sw3RequestBody
operator|!=
literal|null
condition|)
block|{
comment|// Inline for now, or the map of requestBodies can be created instead
comment|// and added to the /components
name|sw2PathVerbProps
operator|.
name|setProperty
argument_list|(
literal|"requestBody"
argument_list|,
name|sw3RequestBody
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|JsonMapObject
name|prepareFormContent
parameter_list|(
name|List
argument_list|<
name|JsonMapObject
argument_list|>
name|formList
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|mediaTypes
parameter_list|)
block|{
name|String
name|mediaType
init|=
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|mediaTypes
argument_list|)
condition|?
literal|"application/x-www-form-urlencoded"
else|:
name|mediaTypes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|JsonMapObject
name|content
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
name|JsonMapObject
name|formType
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
name|JsonMapObject
name|schema
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
name|schema
operator|.
name|setProperty
argument_list|(
literal|"type"
argument_list|,
literal|"object"
argument_list|)
expr_stmt|;
name|JsonMapObject
name|props
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
for|for
control|(
name|JsonMapObject
name|prop
range|:
name|formList
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|prop
operator|.
name|removeProperty
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|prop
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|prop
operator|.
name|getProperty
argument_list|(
literal|"type"
argument_list|)
argument_list|)
condition|)
block|{
name|prop
operator|.
name|setProperty
argument_list|(
literal|"type"
argument_list|,
literal|"string"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|prop
operator|.
name|containsProperty
argument_list|(
literal|"format"
argument_list|)
condition|)
block|{
name|prop
operator|.
name|setProperty
argument_list|(
literal|"format"
argument_list|,
literal|"binary"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|schema
operator|.
name|setProperty
argument_list|(
literal|"properties"
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|formType
operator|.
name|setProperty
argument_list|(
literal|"schema"
argument_list|,
name|schema
argument_list|)
expr_stmt|;
name|content
operator|.
name|setProperty
argument_list|(
name|mediaType
argument_list|,
name|formType
argument_list|)
expr_stmt|;
return|return
name|content
return|;
block|}
specifier|private
specifier|static
name|JsonMapObject
name|prepareContentFromSchema
parameter_list|(
name|JsonMapObject
name|schema
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|mediaTypes
parameter_list|)
block|{
name|String
name|type
init|=
name|schema
operator|.
name|getStringProperty
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"object"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|||
operator|!
literal|"string"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|String
name|ref
init|=
literal|null
decl_stmt|;
name|JsonMapObject
name|items
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"array"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|items
operator|=
name|schema
operator|.
name|getJsonMapProperty
argument_list|(
literal|"items"
argument_list|)
expr_stmt|;
name|ref
operator|=
operator|(
name|String
operator|)
name|items
operator|.
name|getProperty
argument_list|(
literal|"$ref"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ref
operator|=
name|schema
operator|.
name|getStringProperty
argument_list|(
literal|"$ref"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|int
name|index
init|=
name|ref
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|modelName
init|=
name|ref
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|items
operator|==
literal|null
condition|)
block|{
name|schema
operator|.
name|setProperty
argument_list|(
literal|"$ref"
argument_list|,
literal|"#components/schemas/"
operator|+
name|modelName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|items
operator|.
name|setProperty
argument_list|(
literal|"$ref"
argument_list|,
literal|"#components/schemas/"
operator|+
name|modelName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|JsonMapObject
name|content
init|=
operator|new
name|JsonMapObject
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|mediaTypesList
init|=
name|mediaTypes
operator|==
literal|null
condition|?
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"application/json"
argument_list|)
else|:
name|mediaTypes
decl_stmt|;
for|for
control|(
name|String
name|mediaType
range|:
name|mediaTypesList
control|)
block|{
name|content
operator|.
name|setProperty
argument_list|(
name|mediaType
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"schema"
argument_list|,
name|schema
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|content
return|;
block|}
specifier|private
specifier|static
name|void
name|setServersProperty
parameter_list|(
name|JsonMapObject
name|sw2
parameter_list|,
name|JsonMapObject
name|sw3
parameter_list|)
block|{
name|String
name|sw2Host
init|=
name|sw2
operator|.
name|getStringProperty
argument_list|(
literal|"host"
argument_list|)
decl_stmt|;
name|String
name|sw2BasePath
init|=
name|sw2
operator|.
name|getStringProperty
argument_list|(
literal|"basePath"
argument_list|)
decl_stmt|;
name|String
name|sw2Scheme
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|sw2Schemes
init|=
name|sw2
operator|.
name|getListStringProperty
argument_list|(
literal|"schemes"
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|sw2Schemes
argument_list|)
condition|)
block|{
name|sw2Scheme
operator|=
literal|"https"
expr_stmt|;
block|}
else|else
block|{
name|sw2Scheme
operator|=
name|sw2Schemes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|String
name|sw3ServerUrl
init|=
name|sw2Scheme
operator|+
literal|"://"
operator|+
name|sw2Host
operator|+
name|sw2BasePath
decl_stmt|;
name|sw3
operator|.
name|setProperty
argument_list|(
literal|"servers"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"url"
argument_list|,
name|sw3ServerUrl
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

